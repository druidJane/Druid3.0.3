package com.xuanwu.mos.executor;

import com.xuanwu.mos.config.Config;
import com.xuanwu.mos.config.Platform;
import com.xuanwu.mos.domain.entity.FileTask;
import com.xuanwu.mos.domain.enums.TaskResult;
import com.xuanwu.mos.domain.enums.TaskState;
import com.xuanwu.mos.domain.enums.TaskType;
import com.xuanwu.mos.file.FileExporter;
import com.xuanwu.mos.file.FileImporter;
import com.xuanwu.mos.file.FileType;
import com.xuanwu.mos.file.FileUtil;
import com.xuanwu.mos.file.exporter.ExportInfo;
import com.xuanwu.mos.file.handler.FileHandler;
import com.xuanwu.mos.file.handler.FileHandlerFactory;
import com.xuanwu.mos.file.handler.ImportRowHandler;
import com.xuanwu.mos.file.handler.RowHandler;
import com.xuanwu.mos.mtclient.WebMessages;
import com.xuanwu.mos.rest.service.FileTaskService;
import com.xuanwu.mos.utils.DateUtil;
import com.xuanwu.mos.utils.ListUtil;
import com.xuanwu.mos.utils.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;

/**
 * Created by 林泽强 on 2016/8/25. 文件任务处理器
 */
@Component
public class FileTaskExecutor {

    private final static Logger logger = LoggerFactory.getLogger(FileTaskExecutor.class);
    private final static int MAX_POOL_TASK_SIZE = 50000; // 任务缓冲区大小
    private final static int FETCH_UNLOAD_TASK_PERIOD = 1 * 60 * 1000; // 加载类睡眠时间


    @Autowired
    private Config config; // 全局配置对象
    @Autowired
    @Qualifier("taskExecutor")
    private Executor executor;
    @Autowired
    private FileTaskService fileTaskService;
    @Autowired
    private FileImporter fileImporter;
    @Autowired
    private FileExporter fileExporter;
    @Value("${server.port}")
    private int serverPort; // 服务器启动端口

    private Map<Integer, FileTask> handlingTask; // 处理中的任务
    private BlockingQueue<FileTask> taskQueue; // 任务缓冲区
    private Limiter limiter;

    // 初始化
    @PostConstruct
    public void init() {
        taskQueue = new ArrayBlockingQueue<FileTask>(MAX_POOL_TASK_SIZE);
        handlingTask = new ConcurrentHashMap<Integer, FileTask>();
        limiter = new Limiter(config.getMaxPermitUsers(), config.getMaxPermitPerUserTasks());
        loadUnHandledTasks();

        executor.execute(new Boss());
        executor.execute(new Loader());
    }

    // 任务限制管理内部类
    class Limiter {
        private final int subLimit;
        private final Semaphore semaphore;
        private Map<Integer, Integer> limitMap;
        private ReentrantLock lock;

        public Limiter(int limit, int subLimit) {
            this.subLimit = subLimit;
            semaphore = new Semaphore(limit);
            limitMap = new HashMap<Integer, Integer>();
            lock = new ReentrantLock();
        }

        /**
         * 根据标识ID获取资源是否可用
         */
        public boolean acquire(Integer id) {
            lock.lock();
            try {
                Integer val = limitMap.get(id);
                if (val == null) {
                    semaphore.acquire();
                    limitMap.put(id, 1);
                    return true;
                }
                if (val != null && val.intValue() < subLimit) {
                    val += 1;
                    limitMap.put(id, val);
                    return true;
                }
            } catch (InterruptedException e) {
                // do nothing
            } finally {
                lock.unlock();
            }
            return false;
        }

        /**
         * 根据标志ID释放资源
         */
        public void release(Integer id) {
            lock.lock();
            try {
                Integer val = limitMap.get(id);
                if (val == null)
                    return;
                val--;
                if (val.intValue() <= 0) {
                    limitMap.remove(id);
                    semaphore.release();
                } else {
                    limitMap.put(id, val);
                }
            } finally {
                lock.unlock();
            }
        }
    }

    // 任务分发器线程内部类
    class Boss implements Runnable {

        @Override
        public void run() {
            for (; ; ) {
                FileTask task = null;
                try {
                    task = taskQueue.take();
                    if (limiter.acquire(task.getUserId())) {
                        executor.execute(new Worker(task));
                        continue;
                    }
                    putTask2Queue(task);
                    DateUtil.sleepWithoutInterrupte(5000);
                } catch (Exception e) {
                    if (e instanceof RejectedExecutionException || e.getCause() instanceof RejectedExecutionException) {
                        if (task != null)
                            limiter.release(task.getId());
                    }
                    logger.error("Get next file task failed,cause by:{}", e);
                }
            }
        }
    }

    // 任务执行器线程内部类
    class Worker implements Runnable {
        private FileTask task;

        public Worker(FileTask task) {
            this.task = task;
        }

        @Override
        public void run() {
            logger.info("Begin to execute file task.");
            try {
                // commit handling file task
                if (task.getState() == TaskState.Wait) {
                    task.setState(TaskState.Handle);
                    task.setHandleTime(new Date());
                    fileTaskService.commitHandlingFileTask(task);
                }

                FileType fileType = FileUtil.getFileType(task.getFileName());
                FileHandler fileHandler = FileHandlerFactory.getFileHandler(fileType);
                switch (task.getType()) {
                    case Import:
                        handleImport(fileType, task, fileHandler);
                        if (task.getResult() == TaskResult.Failed) {
                            task.setMessage(WebMessages.IMPORT_FAILED);
                            commitFileTask(task, TaskResult.Failed);
                        } else {
                            commitFileTask(task, TaskResult.Success);
                        }
                        break;
                    case Export:
                        handleExport(fileType, task, fileHandler);
                        commitFileTask(task, TaskResult.Success);
                        break;
                }
            } catch (Exception e) {
                // 异常情况下文件任务状态处理
                String msg = (task.getType() == TaskType.Import) ? WebMessages.IMPORT_FAILED
                        : WebMessages.EXPORT_FAILED;
                task.setMessage(msg);
                commitFileTask(task, TaskResult.Failed);
                logger.error("Execute file task failed,cause by:{}", e);
            } finally {
                // 释放资源
                limiter.release(task.getUserId());
            }
        }
    }

    // 数据库加载任务线程内部类
    class Loader implements Runnable {
        @Override
        public void run() {
            for (; ; ) {
                DateUtil.sleepWithoutInterrupte(FETCH_UNLOAD_TASK_PERIOD);
                removeAllHandledTask();
                try {
                    List<Integer> taskIDs = fileTaskService.fetchUnHandledTaskIDs(Platform.FRONTKIT.getIndex());
                    List<Integer> unLoadTaskIDs = getUnLoadTaskIDs(taskIDs);
                    if (ListUtil.isBlank(unLoadTaskIDs))
                        continue;
                    List<FileTask> tasks = fileTaskService
                            .fetchUnLoadTasks(unLoadTaskIDs);
                    for (FileTask task : tasks) {
                        if (task.getState() == TaskState.Wait)
                            putTask2Queue(task);
                        else
                            commitFileTask(task, TaskResult.Interrupt);
                    }
                } catch (Exception e) {
                    logger.error("Fetch un load task failed,cause by:{}", e);
                }
            }
        }
    }

    public void putTask2Queue(FileTask task) {
        boolean isOffered = taskQueue.offer(task);
        if (isOffered)
            handlingTask.put(task.getId(), task);
        else
            logger.warn("Put file task to queue failed, queue size:", taskQueue.size());
    }

    /**
     * 处理导入任务 边读取文件边导入数据
     */
    private ExportInfo handleImport(FileType fileType, final FileTask task, final FileHandler fileHandler)
            throws Exception {
        // long start = System.currentTimeMillis();
        final List<String[]> rowList = new ArrayList<>();
        final File importedFile = FileUtil.getImportedFile(task.getDataType(), task.getFileName(),
                config.getContextPath());

        final ExportInfo resultInfo = new ExportInfo(fileType, task.getDataType(), config.getContextPath(),
                task.getFileName(), config.getPerFileRecords(fileType));
        int lineCount = fileHandler.readLineCount(importedFile);
        task.setTotal(lineCount);

        // 定义方法内部类，行处理器
        RowHandler rowHandler = new ImportRowHandler(task, resultInfo) {
            @Override
            public boolean handleRow(String[] cells) {
                rowList.add(cells);
                // 如果行列表未满，继续塞数据
                if (rowList.size() < config.getMaxPerImportRecords()) {
                    return true;
                }
                try {
                    fileImporter.batchImport(getTask(), rowList, fileHandler, resultInfo);
                } catch (Exception e) {
                    logger.error("Import failed,cause by:{}", e.getMessage());
                }
                rowList.clear();
                return true;
            }

            @Override
            public boolean handleHead(String[] cells) {
                return true;
            }
        };
        String delimiter = task.getParameter("delimeter");
        fileHandler.readContent(importedFile, delimiter, rowHandler);
        if (ListUtil.isNotBlank(rowList)) {
            fileImporter.batchImport(task, clearXssString(rowList), fileHandler, resultInfo);//过滤xss字符攻击
            rowList.clear();
        }
        return resultInfo;
    }

    public static void main(String[] ar) {
        FileTaskExecutor e = new FileTaskExecutor();
        List<String[]> arr = new ArrayList<String[]>();
        String[] bb = {"测试", "<script>", "'ge'", "\"34\""};
        arr.add(bb);
        List<String[]> arr2 = e.clearXssString(arr);
        for (String[] itemArr : arr2) {
            for (String item : itemArr) {
                System.out.println(item);
            }
        }
    }

    /**
     * 过滤攻击字符
     */
    private List<String[]> clearXssString(List<String[]> arr) {
        List<String[]> newArr = new ArrayList<String[]>();
        for (String[] itemArr : arr) {
            int i = 0;
            String[] _n = new String[itemArr.length];
            for (String item : itemArr) {
                _n[i++] = StringUtil.xssEncode(item);
            }
            newArr.add(_n);
        }
        return newArr;
    }

    /**
     * 处理导出任务
     */
    private ExportInfo handleExport(FileType fileType, FileTask task, final FileHandler fileHandler) throws Exception {
        long start = System.currentTimeMillis();
        ExportInfo exportInfo = fileExporter.batchExport(task, fileType, fileHandler);

        logger.info(
                "End to handle export [rows=" + task.getHandledCount() + "] with params[dataType={}] eplased time:{}",
                task.getDataType(), (System.currentTimeMillis() - start));
        if (task.getHandledCount() == null) {
            task.setHandledCount(0);
        }
        task.setMessage("导出总记录数：" + task.getHandledCount());
        return exportInfo;
    }

    /**
     * 提交任务结果
     */
    private void commitFileTask(FileTask task, TaskResult result) {
        try {
            task.setResult(result);
            task.setState(TaskState.Over);
            task.setCommitTime(new Date());
            if (task.getHandledCount() == null) {
                task.setHandledCount(0);
            }
            if (task.getTotal() == null) {
                task.setTotal(0);
            }
            task.setPercent(100);
            fileTaskService.commitHandledFileTask(task);
        } catch (Exception e) {
            logger.error("Commit file task failed, cause:{}", e);
        }
    }

    /**
     * 清理处理完成的任务
     */
    private void removeAllHandledTask() {
        try {
            Map.Entry<Integer, FileTask> entry = null;
            Iterator<Map.Entry<Integer, FileTask>> iter = handlingTask.entrySet().iterator();
            while (iter.hasNext()) {
                entry = iter.next();
                if (entry.getValue().getState() == TaskState.Over)
                    iter.remove();
            }
        } catch (Exception e) {
            logger.error("Remove handled task error occur.", e);
        }
    }

    /**
     * 获取未加载的任务ID列表
     */
    private List<Integer> getUnLoadTaskIDs(List<Integer> taskIDs) {
        List<Integer> innerIDs = new ArrayList<Integer>();
        List<Integer> unLoadTaskIDs = new ArrayList<Integer>();
        for (FileTask task : handlingTask.values()) {
            innerIDs.add(task.getId());
        }
        for (Integer taskId : taskIDs) {
            if (!innerIDs.contains(taskId)) {
                unLoadTaskIDs.add(taskId);
            }
        }
        return unLoadTaskIDs;
    }

    /**
     * 获取未处理的任务列表，系统启动时需执行
     */
    private void loadUnHandledTasks() {
        try {
            List<FileTask> tasks = fileTaskService.fetchUnHandledTasks(config.getPlatformId());
            for (FileTask task : tasks) {
                if (task.getState() == TaskState.Wait)
                    putTask2Queue(task);
                else
                    commitFileTask(task, TaskResult.Interrupt);
            }
        } catch (Exception e) {
            logger.error("Load un handled file task failed,cause by:{}", e);
        }
    }
}

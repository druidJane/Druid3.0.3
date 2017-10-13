package com.xuanwu.mos.file.handler;

import com.xuanwu.mos.exception.BusinessException;
import com.xuanwu.mos.file.FileHead;
import com.xuanwu.mos.file.FileType;
import com.xuanwu.mos.file.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by 林泽强 on 2016/8/26.
 */
public class TextHandler extends AbstractFileHandler{
    private static final Logger logger = LoggerFactory
            .getLogger(TextHandler.class);
    private static final String NEW_LINE = "\r\n";

    @Override
    public FileHead readHead(String filePath, String delimiter) {
        File file = null;
        BufferedReader reader = null;
        Pattern pattern = null;
        String[] mark = {"+","&","*","?","^","\\","：",":","$"};
        boolean isContain = Arrays.toString(mark).contains(delimiter);
        if (isContain) {
            pattern = Pattern.compile("\\" + delimiter);
        } else {
            pattern = Pattern.compile(delimiter);
        }
        try {
            file = new File(filePath);
            reader = getBufferedReader(file);
            String line = reader.readLine();

            if (StringUtils.isBlank(line)) {
                return null;
            }

            FileHead fileHead = new FileHead();

            if(delimiter.equals("Single")){
                pattern = Pattern.compile("\\,");
            }
            String[] cells = pattern.split(line);
            for (int i = 0; i < cells.length; i++) {
                fileHead.putCell(i, cells[i]);
            }
            if (!delimiter.equals("Single") && !"\\|".equals(delimiter) && !"\\t".equals(delimiter)&&cells.length>1) {
                if (!line.contains(delimiter)) {
                    fileHead.setDelimterSucc(false);
                }
            }
            return fileHead;
        } catch (Exception e) {
            logger.error("Read file head failed,cause by:{}", e);
        } finally {
            FileUtil.closeReader(reader);
        }
        return null;
    }

    @Override
    public int readLineCount(File file) {
        int lineNumber = 0;
        LineNumberReader reader = null;
        try {
            reader = new LineNumberReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                line = reader.readLine();
                if(StringUtils.isEmpty(line))
                    continue;
                lineNumber++;
            }
        }catch (Exception e) {
            logger.error("Read file line count failed,cause by:{}", e);
        }finally {
            FileUtil.closeReader(reader);
        }
        return lineNumber;
    }

    @Override
    public void readContent(File file, String delimiter, RowHandler rowHandler) {
        BufferedReader reader = null;
        Pattern pattern = null;
        String[] mark = {"+","&","*","?","^","\\","：",":","$"};
        boolean isContain = Arrays.toString(mark).contains(delimiter);
        if (isContain) {
            pattern = Pattern.compile("\\" + delimiter);
        } else {
            pattern = Pattern.compile(delimiter);
        }
        try {
            reader = getBufferedReader(file);
            boolean first = true;
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (StringUtils.isBlank(line))
                    continue;
                String[] cells = pattern.split(line);
                if (first) {// 文件头
                    if (!rowHandler.handleHead(cells))
                        break;
                    first = false;
                    continue;
                }
                if (!rowHandler.handleRow(cells)) {
                    break;
                }
            }
        } catch (BusinessException e) {
            //捕获业务异常，不处理，往上级抛，由上级处理
            throw e;
        } catch (Exception e) {
            logger.error("Read file content failed,cause by:{}", e);
        } finally {
            FileUtil.closeReader(reader);
        }
    }

    @Override
    public void writeFile(File file, String delimiter, boolean isNew, List<String[]> cellsList, Charset charset) {
        BufferedWriter writer = null;
        try{
            boolean csv = file.getName().endsWith(FileType.Csv.getType());
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, true), charset));
            StringBuilder line = new StringBuilder();
            for(String[] cells :cellsList) {
                for(String str :cells) {
                    String s = str == null? "" :str;
                    if(csv) {
                        s = "\"" + s.replace("\"","＂") + "\"";
                    }
                    if (csv || ",".equals(delimiter)) {
                        s = s.replace(",", "，");
                    }
                    line.append(s);
                    line.append(delimiter);
                }
                if(line.length() > 0) {
                    line.deleteCharAt(line.toString().length() - 1);
                }
                writer.write(line.toString());
                line.delete(0, line.length());
                writer.write(NEW_LINE);
            }
        }catch (Exception e) {
            logger.error("Write text file failed,cause by:{}", e);
        }finally {
            FileUtil.closeWriter(writer);
        }
    }

    @Override
    public void writeFile(File file, String delimiter, boolean isNew, List<String[]> cellsList) {
        writeFile(file, delimiter, isNew, cellsList,Charset.defaultCharset());
    }


    /**
     * 根据文本编码生成BufferedReader
     *
     * @param file
     * @return
     * @throws IOException
     */
    private BufferedReader getBufferedReader(File file) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
                file));
        byte[] head = new byte[3];
        bis.mark(0);
        bis.read(head);
        bis.reset();
        String charset = FileUtil.getCharset(head);
        if ("UTF-8".equals(charset)) {
            bis.skip(3);
        } else if ("UTF-16LE".equals(charset) || "UTF-16BE".equals(charset)) {
            bis.skip(2);
        }
        return new BufferedReader(new InputStreamReader(bis, charset));
    }

    @Override
    public void readAll(String filePath, String delimiter, RowHandler rowHandler) {
        FileHead fileHead = readHead(filePath, delimiter);
        if (null != fileHead) {
            File file = new File(filePath);
            readContent(file, delimiter, rowHandler);
        }
    }
}

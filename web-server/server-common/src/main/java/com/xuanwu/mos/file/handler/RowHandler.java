package com.xuanwu.mos.file.handler;

/**
 * Created by 林泽强 on 2016/8/29.
 * 行处理器
 */
public interface RowHandler {
    /**
     * 处理第一行信息
     * @param cells
     * @return 是否继续读
     */
    public boolean handleHead(String[] cells);

    /**
     * 处理从第二行开始的信息
     * @param cells
     * @return 是否继续读
     */
    public boolean handleRow(String[] cells);

}

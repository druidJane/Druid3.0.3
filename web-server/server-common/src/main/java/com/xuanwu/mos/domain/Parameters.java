package com.xuanwu.mos.domain;

import com.xuanwu.mos.dto.QueryParameters;

import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;

import java.util.Date;
import java.util.Map;

/** 这个类设计用于在shardserver模块使用mybatis的拦截器拼接我们具体使用的表名，
 *  我们在相应的MsgPackMapper.xml 中使用 gsms_msg_pack,
 *  那么shardserver将会在通过msgType和queryTime两个变量名，拼接出相应的表名
 *  如：gsms_msg_pack_mms_0601 或者是 gsms_msg_pack_sms_0601,
 *  使用该类的时候，建议将所有的参数放在QueryParameters，虽然最后写起来会比较冗余
 *  但是这样子能够将各个变量的作用进行一个统一的规范。
 *  冗余形式: <if test="query.params.msgType!=null">msgType=#{query.params.msgType}</if>
 *
 * Created by 郭垚辉 on 2017/5/17.
 */
public class Parameters extends JpaRepositoriesAutoConfiguration {
    /**
     * 用于拼装mms或者是sms
     */
    private int msgType;

    /**
     * 用于拼装MMdd
     */
    private Date queryTime;

    /**
     * 用于封装QueryParameters
     * 建议跟数据库相关的数据封装在这个变量中
     */
    private QueryParameters query;

    /**
     * 添加其他的属性
     */
    private Map<Object,Object> other;

    public Parameters() {

    }

    public Parameters(QueryParameters query) {
        this.query = query;
    }

    public Parameters(QueryParameters query, int msgType, Date queryTime) {
        this.msgType = msgType;
        this.queryTime = queryTime;
        this.query = query;
    }

    public Parameters(int msgType) {
        this.msgType = msgType;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public Date getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(Date queryTime) {
        this.queryTime = queryTime;
    }

    public QueryParameters getQuery() {
        return query;
    }

    public void setQuery(QueryParameters query) {
        this.query = query;
    }

    public Map<Object, Object> getOther() {
        return other;
    }

    public void setOther(Map<Object, Object> other) {
        this.other = other;
    }
}

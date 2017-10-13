package com.xuanwu.mos.mtclient;

import com.xuanwu.mos.domain.entity.MTResp;
import com.xuanwu.msggate.common.sbi.entity.MsgPack;
import com.xuanwu.msggate.common.sbi.exception.CoreException;

import java.util.Date;
import java.util.Map;

/**
 *
 */
public interface FrontKitPackSender {


    /**
     * 专门为LoginResource中发送验证码开放的接口。
     */
    MTResp send(String username, String password, String remoteIp, MsgPack msgPack) throws CoreException;

    /**
     * 发送短彩信的接口
     * @param accountMap 用户名+密码+remoteIp
     * @param msgPack 要发送的信息包
     * @return
     * @throws CoreException
     */
    MTResp send(Map<String, String> accountMap, MsgPack msgPack) throws CoreException;

    /**
     * 审核短彩信接口
     * @param accountMap
     * @param packId   pack包的id
     * @param packUuid pack包的uuid
     * @param state    pack包的状态
     * @param msgType  消息类型
     * @param postTime 提交时间
     */
    MTResp auditing(Map<String, String> accountMap, String packId, String packUuid, int state, int msgType, Date postTime) throws CoreException;


    /**
     * 取消发送短彩信接口
     * @param accountMap
     * @param packId   批次id
     * @param packUuid 批次的uuid
     * @param state    状态
     * @param msgType  消息类型：彩信为2，短信为1
     * @param postTime 提交时间
     */
    MTResp cancel(Map<String, String> accountMap, String packId, String packUuid, int state, int msgType, Date postTime) throws CoreException;

}

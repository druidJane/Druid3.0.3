package com.xuanwu.mos.mtclient;

import com.xuanwu.mos.domain.entity.MTResp;
import com.xuanwu.msggate.common.sbi.entity.MsgPack;
import com.xuanwu.msggate.common.sbi.exception.CoreException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Service
public class RemotePackSenderManager implements FrontKitPackSender {
    @Autowired
    private MTClient mtClient;

    @Override
    public MTResp send(String username, String password, String remoteIp, MsgPack msgPack) throws CoreException {
        Map<String, String> accountMap = new HashMap<>();
        accountMap.put("username", username);
        accountMap.put("password", password);
        accountMap.put("remoteIp", remoteIp);
        return send(accountMap, msgPack);
    }

    @Override
    public MTResp send(Map<String, String> accountMap, MsgPack msgPack) throws CoreException {
        return mtClient.send(accountMap, msgPack);
    }

    @Override
    public MTResp auditing(Map<String, String> accountMap, String packId, String packUuid, int state, int msgType, Date postTime) throws CoreException {
        return mtClient.auditing(accountMap, packId, packUuid, state, msgType, postTime);
    }

    @Override
    public MTResp cancel(Map<String, String> accountMap, String packId, String packUuid, int state, int msgType, Date postTime) throws CoreException {
        return mtClient.cancel(accountMap, packId, packUuid, msgType, postTime);
    }
}

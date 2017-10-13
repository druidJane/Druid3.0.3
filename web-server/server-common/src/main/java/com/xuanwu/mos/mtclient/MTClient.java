package com.xuanwu.mos.mtclient;

import com.xuanwu.mos.config.Config;
import com.xuanwu.mos.domain.entity.MTResp;
import com.xuanwu.mos.domain.entity.MTResult;
import com.xuanwu.msggate.common.sbi.entity.MsgPack;
import com.xuanwu.msggate.common.sbi.exception.CoreException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;

/**
 * Created by Jiang.Ziyuan on 2017/3/27.
 */
@Component
public class MTClient {
    private static final Logger logger = LoggerFactory.getLogger(MTClient.class);

    private Config config;
    private int loginWaitTime;
    private int maxFrameLength;

    /**
     * 根据数据库中配置的网关端口和IP建立和网关之间的通信Socket
     * @return
     */
    private Socket connect() {
        Socket socket = null;
        try {
            socket = new Socket(config.getGatewayIPAddress(),
                    config.getGatewayPort());
            socket.setSoTimeout(loginWaitTime);
        } catch (UnknownHostException e) {
            logger.error("Connect to gateway UnknownHostException occur.");
        } catch (IOException e) {
            logger.error("Connect to gateway IOException occur, cause by:{}", e);
        }
        return socket;
    }

    /**
     * 将当前账号登录到网关服务
     * @param accountMap
     * @param client
     * @return
     * @throws Exception
     */
    private MTResp login(Map<String, String> accountMap, Socket client)
            throws Exception {
        byte[] recvBytes = transfer(client,
                MTSupport.tran2LoginBytes(accountMap, config));
        return MTSupport.buildResp(recvBytes);
    }

    /**
     * 发送短彩信
     * @param accountMap
     * @param pack
     * @return
     */
    public MTResp send(Map<String,String> accountMap, MsgPack pack) {
        Socket client = connect();
        if (client == null)
            return MTResp.build(MTResult.CONNECT_TO_SERVER_FAIL, WebMessages.CONNECT_TO_SERVER_FAIL);
        try {
            MTResp resp = login(accountMap, client);
            if (resp.getResult() == MTResult.SUCCESS) {
                byte[] recvBytes = transfer(client, MTSupport.tran2SendBytes(config, pack));
                return MTSupport.buildResp(recvBytes);
            }
            return resp;
        } catch (Exception e) {
            logger.error("Send msg pack by userName:{} failed,cause by:{}", accountMap.get("username"), e);
        } finally {
            try {
                if (client != null)
                    client.close();
            } catch (Exception e) {
                // Ignore it
            }
        }
        return MTResp.build(MTResult.INNER_ERROR, WebMessages.SYS_ERROR);
    }


    /**
     * 审核短彩信
     * @param accountMap
     * @param packId
     * @param packUuid
     * @param state
     * @param msgType
     * @param postTime
     * @return
     * @throws CoreException
     */
    public MTResp auditing(Map<String,String> accountMap, String packId, String packUuid,int state,int msgType,Date postTime) throws CoreException {
        return sendAuditReq(accountMap, packId,packUuid, state,msgType,postTime);
    }

    /**
     * 取消发送短彩信
     * @param accountMap
     * @param packId
     * @param packUuid
     * @param msgType
     * @param postTime
     * @return
     * @throws CoreException
     */
    public MTResp cancel(Map<String,String> accountMap, String packId,String packUuid,int msgType,Date postTime) throws CoreException {
        return sendAuditReq(accountMap, packId, packUuid,MsgPack.PackState.CANCEL.getIndex(),msgType,postTime);
    }


    private MTResp sendAuditReq(Map<String,String> accountMap, String packId, String packUuid, int state,int msgType,Date postTime){
        Socket client = connect();
        if (client == null)
            return MTResp.build(MTResult.CONNECT_TO_SERVER_FAIL, WebMessages.CONNECT_TO_SERVER_FAIL);
        MTResp resp = MTResp.build(MTResult.INNER_ERROR);
        try {
            resp = login(accountMap, client);
            if (resp.getResult() == MTResult.SUCCESS) {
                byte[] recvBytes = transfer(client, MTSupport.tran2AuditingBytes(packId,packUuid,state,msgType,postTime));
                resp = MTSupport.buildResp(recvBytes);
            }
        } catch (Exception e) {
            logger.error("Send audit request by userName:{} failed,cause by:{}", accountMap.get("username"), e);
        } finally {
            try {
                if (client != null)
                    client.close();
            } catch (Exception e) {
                // Ignore it
            }
        }
        return resp;
    }



    private byte[] transfer(Socket client, byte[] sendBytes) throws IOException {
        if (sendBytes == null) {
            throw new NullPointerException("Send bytes must be not null.");
        }
        try {
            InputStream in = client.getInputStream();
            OutputStream out = client.getOutputStream();
            writeInt(out, sendBytes.length);
            out.write(sendBytes);
            out.flush();

            // read bytes with header length
            int recvLength = readInt(in);
            if (recvLength < 0) {
                throw new IllegalArgumentException(
                        "recvLength must be a positive integer: " + recvLength);
            }
            if (recvLength > maxFrameLength) {
                throw new IllegalArgumentException("recvLength (" + recvLength
                        + ") " + "must be equal to or less than "
                        + "maxFrameLength (" + maxFrameLength + ").");
            }

            byte[] recvBytes = new byte[recvLength];
            in.read(recvBytes);

            return recvBytes;
        } catch (IOException ioe) {
            throw ioe;
        }
    }

    private void writeInt(OutputStream out, int v) throws IOException {
        out.write((v >>> 24) & 0xFF);
        out.write((v >>> 16) & 0xFF);
        out.write((v >>> 8) & 0xFF);
        out.write((v >>> 0) & 0xFF);
    }

    private int readInt(InputStream in) throws IOException {
        int ch1 = in.read();
        int ch2 = in.read();
        int ch3 = in.read();
        int ch4 = in.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public void setLoginWaitTime(int loginWaitTime) {
        this.loginWaitTime = loginWaitTime;
    }

    public void setMaxFrameLength(int maxFrameLength) {
        this.maxFrameLength = maxFrameLength;
    }
}


package com.xuanwu.mos.rest.repo;

import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.domain.Parameters;
import com.xuanwu.mos.domain.entity.*;
import com.xuanwu.mos.domain.repo.MsgPackRepo;
import com.xuanwu.mos.domain.repo.UserRepo;
import com.xuanwu.mos.dto.PageInfo;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.utils.DateUtil;
import com.xuanwu.mos.utils.ListUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jiang.Ziyuan on 2017/3/30.
 */
@Repository
public class MsgTicketRepo extends GsmsMybatisEntityRepository<MsgTicket> {
    private static final Logger logger = LoggerFactory.getLogger(MsgTicketRepo.class);
    @Override
    protected String namesapceForSqlId() {
        return "com.xuanwu.mos.mapper.MsgTicketMapper";
    }
    @Autowired
    private MsgPackRepo msgPackRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CarrierChannelRepo carrierChannelRepo;

    public List<PackStatInfo> findPackStatInfo(String packId, MsgContent.MsgType msgType,
                                               Date postTime) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Serializable> params = new HashMap<String, Serializable>();
            params.put("packId", packId);
            params.put("msgType", msgType.getIndex());
            params.put("postTime", postTime);
            return session.selectList(fullSqlId("findPackStatInfo"), params);
        }
    }

    public int findAllSendedTicketsCount(int entId, String phone, int userId,
                                         String deptPath, String batchName, String smsContent,
                                         MsgTicket.MsgStatus msgStatus, Date beginTime, Date endTime,
                                         String respResult, String reportResult, int channelId,
                                         MsgContent.MsgType msgType, boolean joinReport, MsgTicket.MsgSub msgSub, Long ticketId) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            String packId = null;
            List<String> packIds = Collections.emptyList();
            if (StringUtils.isNotBlank(batchName)) {
                packIds = msgPackRepo.findPackIdsByName(beginTime, endTime, batchName,
                        msgType.getIndex());
                if (ListUtil.isBlank(packIds)) {// // BatchName is not found!
                    return 0;
                }
            }
            if (packIds.size() == 1) {
                packId = packIds.get(0);
                packIds.clear();
            }
            boolean joinUser = (deptPath != null);
            boolean joinChannel = (channelId > 0);
            HashMap<String, Object> params = new HashMap<>();
            params.put("phone", phone);
            params.put("userId", userId);
            params.put("deptPath", deptPath);
            params.put("joinUser", joinUser);
            params.put("packId", packId);
            params.put("packIds", packIds);
            params.put("smsContent", smsContent);
            params.put("msgStatus", msgStatus);
            params.put("beginTime", beginTime);
            params.put("endTime", endTime);
            params.put("respResult", respResult);
            params.put("reportResult", reportResult);
            params.put("channelId", channelId);
            params.put("joinChannel", joinChannel);
            params.put("msgType", msgType.getIndex());
            params.put("joinReport", joinReport);
            params.put("msgSub", msgSub);
            params.put("ticketId", ticketId);
            int ret = session.selectOne(fullSqlId("findAllSendedTicketsCount"), params);
            return ret;
        } finally {
            session.close();
        }
    }

    public List<MsgTicket> findAllSendedTickets(int entId, String phone,
                                                int userId, String deptPath, String batchName, String smsContent,
                                                MsgTicket.MsgStatus msgStatus, Date beginTime, Date endTime,
                                                String respResult, String reportResult, int channelId,
                                                MsgContent.MsgType msgType, boolean joinReport, MsgTicket.MsgSub msgSub,
                                                PageInfo pageInfo, int id) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            String packId = null;
            List<String> packIds = Collections.emptyList();
            if (StringUtils.isNotBlank(batchName)) {
                packIds = msgPackRepo.findPackIdsByName(beginTime, endTime, batchName,
                        msgType.getIndex());
                if (ListUtil.isBlank(packIds)) {// BatchName is not found!
                    return Collections.emptyList();
                }
            }
            if (packIds.size() == 1) {
                packId = packIds.get(0);
                packIds.clear();
            }
            boolean joinUser = (deptPath != null);
            boolean joinChannel = (channelId > 0);

            String beginTimeStr = DateUtil.format(beginTime,
                    DateUtil.DateTimeType.DateTime);
            String endTimeStr = DateUtil.format(endTime, DateUtil.DateTimeType.DateTime);
            String date = DateUtil.format(endTime, DateUtil.DateTimeType.MonthDate);
            Map<String, Object> params = new HashMap<>();
            params.put("phone", phone);
            params.put("userId", userId);
            params.put("deptPath", deptPath);
            params.put("joinUser", joinUser);
            params.put("packId", packId);
            params.put("packIds", packIds);
            params.put("smsContent", smsContent);
            params.put("msgStatus", msgStatus);
            params.put("beginTime", beginTime);
            params.put("endTime", endTime);
            params.put("beginTimeStr", beginTimeStr);
            params.put("endTimeStr", endTimeStr);
            params.put("respResult", respResult);
            params.put("reportResult", reportResult);
            params.put("channelId", channelId);
            params.put("joinChannel", joinChannel);
            params.put("msgType", msgType.getIndex());
            params.put("msgSub", msgSub);
            params.put("pageInfo", pageInfo);
            params.put("id", id);
            params.put("date", date);
            List<MsgTicket> tickets = session.selectList(fullSqlId("findAllSendedTickets"), params);
            setTicketExt(beginTime, tickets, session);
            return tickets;
        } finally {
            session.close();
        }
    }

    private void setTicketExt(Date beginTime, List<MsgTicket> tickets,
                              SqlSession session) {
        if (tickets == null)
            return;
        Map<Integer, String> userNameMap = new HashMap<Integer, String>();
        Map<String, String> batchNameMap = new HashMap<String, String>();
        Map<String, String> linkManMap = new HashMap<String, String>();
        Map<Integer, CarrierChannel> channelMap = new HashMap<Integer, CarrierChannel>();
        for (MsgTicket t : tickets) {
            Integer key = Integer.valueOf(t.getUserId());
            String userName = userNameMap.get(key);
            if (userName == null) {
                userName = userRepo.findUserNameById(t.getUserId());
                userNameMap.put(key, userName);
            }

            String batchName = batchNameMap.get(t.getPackId());
            if (batchName == null) {
                /*batchName = session.getMapper(MsgPackMapper.class)
                        .findBatchNameById(t.getPackId(), t.getSmsType(),
                                beginTime);*/
                batchName = msgPackRepo.findBatchNameById(t.getPackId(), t.getSmsType(),
                        beginTime);
                batchNameMap.put(t.getPackId(), batchName);
            }
            key = Integer.valueOf(t.getChannelId());
            CarrierChannel channel = channelMap.get(key);
            if (channel == null) {
                /*channel = session.getMapper(CarrierChannelMapper.class)
                        .findChannelById(t.getChannelId());*/
                channel = carrierChannelRepo.findChannelById(t.getChannelId());
                channelMap.put(key, channel);
            }

            t.setUserName(userName);
            t.setBatchName(batchName);
            if (channel != null) {
                t.setChannelNum(channel.getChannelNum());
                t.setChannelName(channel.getName());
            }
        }
    }

    public MsgTicket findStateReport(MsgTicket msgTicket) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            String msgId = msgTicket.getMsgId();
            int channelId = msgTicket.getChannelId();
           /* return session.getMapper(MsgTicketMapper.class).findStateReport(
                    msgId, channelId, msgTicket.getSubmitRespTime());*/
            Map<String, Object> params = new HashMap<>();
            params.put("msgId", msgId);
            params.put("channelId", channelId);
            params.put("submitRespTime", msgTicket.getSubmitRespTime());
            return session.selectOne(fullSqlId("findStateReport"), params);
        } finally {
            session.close();
        }
    }

    public List<MsgTicket> findMsgTicketsById(long id, long parentId,
                                              int total, int msgType, Date postTime) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            /*return session.getMapper(MsgTicketMapper.class).findMsgTicketsById(
                    id, parentId, total, msgType, postTime);*/
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            params.put("parentId", parentId);;
            params.put("total", total);;
            params.put("msgType", msgType);;
            params.put("postTime", postTime);;
            return session.selectList(fullSqlId("findMsgTicketsById"), params);
        } finally {
            session.close();
        }
    }

    public List<MsgTicket> findParentMsgTicketsById(long id, int msgType,
                                                    Date postTime) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            /*return session.getMapper(MsgTicketMapper.class)
                    .findParentMsgTicketsById(id, msgType, postTime);*/
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            params.put("msgType", msgType);;
            params.put("postTime", postTime);;
            return session.selectList(fullSqlId("findParentMsgTicketsById"), params);
        } finally {
            session.close();
        }
    }

    public int findAbandonPackTicketsCount(String phone, String packId,
                                           MsgContent.MsgType msgType, MsgTicket.MsgSub msgSub, Date postTime) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            /*return session.getMapper(MsgTicketMapper.class)
                    .findAbandonPackTicketsCount(phone, packId,
                            msgType.getIndex(), msgSub, postTime);*/
            Map<String, Object> params = new HashMap<>();
            params.put("phone", phone);
            params.put("packId", packId);
            params.put("msgType", msgType.getIndex());
            params.put("msgSub", msgSub);
            params.put("postTime", postTime);
            return session.selectOne(fullSqlId("findParentMsgTicketsById"), params);
        } finally {
            session.close();
        }
    }

    public List<MsgTicket> findAbandonPackTickets(String phone, String packId, MsgContent.MsgType msgType, MsgTicket.MsgSub msgSub,
                                                  int offset, int reqNum, Date postTime) {
        SqlSession session = sqlSessionFactory.openSession(true);
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("phone", phone);
            params.put("packId", packId);
            params.put("msgType", msgType.getIndex());
            params.put("msgSub", msgSub);
            params.put("offset", offset);
            params.put("reqNum", reqNum);
            params.put("postTime", postTime);
            return session.selectList(fullSqlId("findAbandonPackTickets"), params);
        } finally {
            session.close();
        }
    }

    public int findSendedPackTicketsCount(String phone, String packId,
                                          MsgContent.MsgType msgType, MsgTicket.MsgStatus msgStatus, String respResult,
                                          String reportResult, boolean joinReport, MsgTicket.MsgSub msgSub,
                                          Date postTime) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            /*return session.getMapper(MsgTicketMapper.class)
                    .findSendedPackTicketsCount(phone, packId,
                            msgType.getIndex(), msgStatus, respResult,
                            reportResult, joinReport, msgSub, postTime);*/
            Map<String, Object> params = new HashMap<>();
            params.put("phone", phone);
            params.put("packId", packId);
            params.put("msgType", msgType.getIndex());
            params.put("msgStatus", msgStatus);
            params.put("respResult", respResult);
            params.put("reportResult", reportResult);
            params.put("joinReport", joinReport);
            params.put("msgSub", msgSub);
            params.put("postTime", postTime);
            return session.selectOne(fullSqlId("findSendedPackTicketsCount"), params);
        } finally {
            session.close();
        }
    }

    public List<MsgTicket> findSendedPackTickets(String phone, String packId,
                                                 MsgContent.MsgType msgType, MsgTicket.MsgStatus msgStatus, String respResult,
                                                 String reportResult, boolean joinReport, MsgTicket.MsgSub msgSub,
                                                 PageInfo pageInfo, Date postTime) {
        SqlSession session = sqlSessionFactory.openSession(true);
        try {
            /*return session.getMapper(MsgTicketMapper.class)
                    .findSendedPackTickets(phone, packId, msgType.getIndex(),
                            msgStatus, respResult, reportResult, joinReport,
                            msgSub, pageInfo, postTime);*/
            Map<String, Object> params = new HashMap<>();
            params.put("phone", phone);
            params.put("packId", packId);
            params.put("msgType", msgType.getIndex());
            params.put("msgStatus", msgStatus);
            params.put("respResult", respResult);
            params.put("reportResult", reportResult);
            params.put("joinReport", joinReport);
            params.put("msgSub", msgSub);
            params.put("pageInfo", pageInfo);
            params.put("postTime", postTime);
            return session.selectList(fullSqlId("findSendedPackTickets"), params);
        } finally {
            session.close();
        }
    }

     //号码记录总数统计
    public TicketCount findTicketCount(Date beginTime, Date endTime,
                                       TicketCount ticketCount) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            //return mapper.findTicketCount(beginTime, endTime, ticketCount);
            Map<String, Object> params = new HashMap<>();
            params.put("beginTime", beginTime);
            params.put("endTime", endTime);
            params.put("ticketCount", ticketCount);
            return session.selectOne(fullSqlId("findTicketCount"), params);
        } finally {
            session.close();
        }
    }

    public long findTicketCountMaxId(int msgType, Date time) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            //return mapper.findTicketCountMaxId(msgType, time);
            Map<String, Object> params = new HashMap<>();
            params.put("msgType", msgType);
            params.put("time", time);
            return session.selectOne(fullSqlId("findTicketCountMaxId"), params);
        } finally {
            session.close();
        }
    }

    public List<MsgTicket> findTickets(int msgType, Date postTime, long from,
                                       int size) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            //return mapper.findTickets(msgType, postTime, from, size);
            Map<String, Object> params = new HashMap<>();
            params.put("msgType", msgType);
            params.put("postTime", postTime);
            params.put("from", from);
            params.put("size", size);
            return session.selectList(fullSqlId("findTickets"), params);
        } finally {
            session.close();
        }
    }

    public boolean insertTicketCount(List<TicketCount> ticketCounts) {
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        boolean retValue = true;
        try {
            for (TicketCount ticketCount : ticketCounts) {
                //mapper.insertTicketCount(ticketCount);
                session.insert(fullSqlId("insertTicketCount"), ticketCount);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
            retValue = false;
            logger.error("Insert ticketCount error,cause by{}", e);
        } finally {
            session.close();
        }
        return retValue;
    }

    public List<FailTicket> findFailedMsgTickets(long pointId, int entId,
                                                 int userId, String path, String phone, Date beginTime, Date endTime,
                                                 MsgContent.MsgType msgType, int maxSize) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            /*return session.getMapper(MsgTicketMapper.class)
                    .findFailedMsgTickets(pointId, entId, userId,path, phone,
                            beginTime, endTime, msgType.getIndex(), maxSize);*/
            Map<String, Object> params = new HashMap<>();
            params.put("pointId", pointId);
            params.put("entId", entId);
            params.put("userId", userId);
            params.put("path", path);
            params.put("phone", phone);
            params.put("beginTime", beginTime);
            params.put("endTime", endTime);
            params.put("msgType", msgType.getIndex());
            params.put("maxSize", maxSize);
            return session.selectList(fullSqlId("findFailedMsgTickets"), params);
        } finally {
            session.close();
        }
    }

    public int findTicketChannelSended(int channelId, String msgType) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            String postTime = DateUtil.format(new Date(),
                    DateUtil.DateTimeType.MonthDate);
            String tableName = "gsms_msg_ticket_" + msgType + "_" + postTime;
            /*return session.getMapper(MsgTicketMapper.class)
                    .findTicketChannelSended(tableName, channelId);*/
            Map<String, Object> params = new HashMap<>();
            params.put("tableName", tableName);
            params.put("channelId", channelId);
            return session.selectOne(fullSqlId("findTicketChannelSended"), params);
        } finally {
            session.close();
        }
    }

    //----------------------------add by guoyaohui ------------------------------

    // 获取一个短信包中全部的短信的检核详情的总数
    public int findAllTicketsStateCount(String phone, String packId, MsgContent.MsgType msgType, MsgTicket.MsgSub msgSub, Date postTime) {
        SqlSession session = sqlSessionFactory.openSession(true);
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("phone", phone);
            params.put("packId", packId);
            params.put("msgType", msgType.getIndex());
            params.put("msgSub", msgSub);
            params.put("postTime", postTime);
            return session.selectOne(fullSqlId("findAllTicketsStateCount"), params);
        } finally {
            session.close();
        }
    }

    // 获取一个短信包中全部的短信的检核详情
    public List<MsgTicket> findAllTicketsState(String phone, String packId, MsgContent.MsgType msgType, MsgTicket.MsgSub msgSub, int offset, int reqNum, Date postTime) {
        SqlSession session = sqlSessionFactory.openSession(true);
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("phone", phone);
            params.put("packId", packId);
            params.put("msgType", msgType.getIndex());
            params.put("msgSub", msgSub);
            params.put("offset", offset);
            params.put("reqNum", reqNum);
            params.put("postTime", postTime);
            return session.selectList(fullSqlId("findAllTicketsState"), params);
        } finally {
            session.close();
        }
    }

    /**
     * 获取所有的号码的历史记录的总数
     * @param params
     * @return
     */
    public int findAllMmsNumberRecordCount(Parameters params) {
        try(SqlSession session = sqlSessionFactory.openSession()){
            return session.selectOne(fullSqlId("findAllMmsNumberRecordCount"), params);
        }
    }

    /**
     * 获取所有的号码的历史记录
     * @param params
     * @return
     */
    public List<MsgTicket> findAllMmsNumberRecord(Parameters params) {
        try(SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList(fullSqlId("findAllMmsNumberRecord"),params);
        }
    }

    public int checkAllMmsTicketsCount(Parameters params) {
        try(SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("checkAllMmsTicketsCount"),params);
        }
    }

    public List<MsgTicket> checkAllMmsTickets(Parameters params) {
        try(SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList(fullSqlId("checkAllMmsTickets"),params);
        }
    }

    public int findAbandonPackTicketsCount(Parameters params) {
        try(SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findAbandonPackTicketsCount"),params);
        }
    }

    public List<MsgTicket> findAbandonPackTickets(Parameters params) {
        try(SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList(fullSqlId("findAbandonPackTickets"),params);
        }
    }

    public List<MsgTicket> findTicketsByPackId(String packId, int msgType, Date postTime, MsgTicket.MsgSub msgSub, int fetchSize, String orderRule) {
        try(SqlSession session = sqlSessionFactory.openSession()) {
            Parameters parameters = new Parameters();
            parameters.setMsgType(MsgTicket.MsgType.SMS.getIndex());
            parameters.setQueryTime(postTime);
            QueryParameters params = new QueryParameters();
            params.addParam("packId", packId);
            params.addParam("msgType", msgType);
            params.addParam("postTime", postTime);
            params.addParam("fetchSize", fetchSize);
            params.addParam("msgSub", msgSub);
            params.addParam("orderRule", orderRule);
            parameters.setQuery(params);
            return session.selectList(fullSqlId("findTicketsByPackId"), parameters);
        }
    }
    public List<MsgTicket> findFailedTicketByPackId(Parameters params) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList(fullSqlId("findFailedTicketByPackId"), params);
        }
    }
}

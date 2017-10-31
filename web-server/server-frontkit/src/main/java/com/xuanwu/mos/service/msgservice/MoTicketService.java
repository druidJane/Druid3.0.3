package com.xuanwu.mos.service.msgservice;

import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.vo.MoReplyVo;
import com.xuanwu.mos.vo.MoTicketVo;

import java.util.List;

/**
 * 接收记录
 * @author <a href="mailto:jiangziyuan@wxchina.com">Ziyuan.Jiang</a>
 * @version 1.0.0
 * @date 2017/5/24.
 */
public interface MoTicketService {
    public int findMoTicketsCount(QueryParameters params);

    public List<MoTicketVo> findMoTickets(QueryParameters params);

    public int findMoReplyCountByMoTicketId(QueryParameters params);

    public List<MoReplyVo> findMoReplyByMoTicketId(QueryParameters params);

    public boolean addMoReply(MoReplyVo moReplyVo);

    void updateMoTicket(MoTicketVo update) throws RepositoryException;

    MoTicketVo findMoTicketById(Integer id);
}

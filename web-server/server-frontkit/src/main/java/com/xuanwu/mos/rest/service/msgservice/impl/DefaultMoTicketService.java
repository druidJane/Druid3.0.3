package com.xuanwu.mos.rest.service.msgservice.impl;

import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.rest.repo.MoTicketRepo;
import com.xuanwu.mos.rest.service.msgservice.MoTicketService;
import com.xuanwu.mos.vo.MoReplyVo;
import com.xuanwu.mos.vo.MoTicketVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:jiangziyuan@wxchina.com">Ziyuan.Jiang</a>
 * @version 1.0.0
 * @date 2017/5/24.
 */
@Service("moService")
public class DefaultMoTicketService implements MoTicketService {
    @Autowired
    private MoTicketRepo moTicketRepo;

    @Override
    public int findMoTicketsCount(QueryParameters params) {
        return moTicketRepo.findResultCount(params);
    }

    @Override
    public List<MoTicketVo> findMoTickets(QueryParameters params) {
        return moTicketRepo.findResults(params);
    }

    @Override
    public int findMoReplyCountByMoTicketId(QueryParameters params) {
        return moTicketRepo.findMoReplyCountByMoTicketId(params);
    }

    @Override
    public List<MoReplyVo> findMoReplyByMoTicketId(QueryParameters params) {
        return moTicketRepo.findMoReplyByMoTicketId(params);
    }

    @Override
    public boolean addMoReply(MoReplyVo moReplyVo) {
        try {
            moTicketRepo.addMoReply(moReplyVo);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void updateMoTicket(MoTicketVo update) throws RepositoryException {
        moTicketRepo.update(update);
    }

    @Override
    public MoTicketVo findMoTicketById(Integer id) {
        return moTicketRepo.getById(id,null);
    }
}

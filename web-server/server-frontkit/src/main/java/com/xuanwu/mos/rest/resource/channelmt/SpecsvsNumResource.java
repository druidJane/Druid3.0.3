package com.xuanwu.mos.rest.resource.channelmt;

import com.xuanwu.mos.config.Keys;
import com.xuanwu.mos.domain.entity.SimpleUser;
import com.xuanwu.mos.domain.entity.SpecsvsNumVo;
import com.xuanwu.mos.dto.*;
import com.xuanwu.mos.rest.service.SpecsvsNumService;
import com.xuanwu.mos.utils.SessionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**端口分配
 * Created by zhangz on 2017/3/23.
 */
@Component
@Path(Keys.CHANNELMGR_SPECSVSNUM)
public class SpecsvsNumResource {
    private static final Logger logger = LoggerFactory
            .getLogger(SpecsvsNumResource.class);
    @Autowired
    private SpecsvsNumService specsvsNumService;

    @POST
    @Path(Keys.CHANNELMGR_SPECSVSNUM_LIST)
    @Produces({ MediaType.APPLICATION_JSON })
    public JsonResp list(@Valid PageReqt req) {
        QueryParameters params = new QueryParameters(req);
        SimpleUser user = SessionUtil.getCurUser();
        params.addParam("enterpriseId",user.getEnterpriseId());
        int total = specsvsNumService.findSpecsvsNumCount(params);
        if (total == 0) {
            return PageResp.emptyResult();
        }
        PageInfo pageInfo = new PageInfo(req.getPage(), req.getCount(), total);
        params.setPage(pageInfo);
        List<SpecsvsNumVo> specsvsNumVolists = specsvsNumService.findSpecsvsNumLists(
                params);
        return PageResp.success(total, specsvsNumVolists);
    }
}

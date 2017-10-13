package com.xuanwu.mos.rest.resource.informationmt;

import com.xuanwu.mos.config.Keys;
import com.xuanwu.mos.domain.entity.CarrierTeleseg;
import com.xuanwu.mos.dto.*;
import com.xuanwu.mos.rest.service.CarrierTelesegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;

/**运营商号码段
 * Created by zhangz on 2017/3/23.
 */
@Component
@Path(Keys.INFO_CARRIERDNSEG)
public class CarrierTelesegResource {
    @Autowired
    private CarrierTelesegService carrierTelesegService;
    @POST
    @Path(Keys.INFO_CARRIERDNSEG_LIST)
    public JsonResp list(@Valid PageReqt req){
        QueryParameters params = new QueryParameters(req);
        int total = carrierTelesegService.findCarrierTelesegCount(params);
        if (total == 0) {
            return PageResp.emptyResult();
        }
        PageInfo pageInfo = new PageInfo(req.getPage(), req.getCount(), total);
        params.setPage(pageInfo);
        List<CarrierTeleseg> list = carrierTelesegService.findCarrier(params);
        return PageResp.success(total, list);
    }
}

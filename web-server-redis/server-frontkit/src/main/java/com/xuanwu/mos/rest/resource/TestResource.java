package com.xuanwu.mos.rest.resource;

import com.xuanwu.mos.dto.JsonResp;
import com.xuanwu.mos.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Component
@Path("login2")
public class TestResource {
	@Autowired
	private UserService userService;
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp login() {
		return null;
		//缺失方法，启动报错。暂时注释userService.getUser(1)
		//return JsonResp.success(userService.getUser(1));
	}
}

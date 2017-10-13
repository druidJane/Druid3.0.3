package com.xuanwu.mos.rest.resource.sysmgmt;

import com.xuanwu.mos.config.Keys;
import com.xuanwu.mos.domain.entity.FileTask;
import com.xuanwu.mos.domain.entity.SimpleUser;
import com.xuanwu.mos.dto.*;
import com.xuanwu.mos.rest.service.FileTaskService;
import com.xuanwu.mos.utils.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.activation.MimetypesFileTypeMap;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Collection;

/**
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Discription 文件任务管理
 * @Data 2017-3-24
 * @Version 1.0.0
 */
@Component
@Path(Keys.SYSTEMMGR_TASKMGR)
public class FileTaskResource {

	private static final Logger logger = LoggerFactory.
			getLogger(FileTaskResource.class);

	@Autowired
	private FileTaskService fileTaskService;

	private JsonResp list(PageReqt req) {
		QueryParameters params = new QueryParameters(req);
		SimpleUser user= SessionUtil.getCurUser();
		params.addParam("userId", user.getId());
		int total = fileTaskService.findTasksCount(params);
		if (total == 0) {
			return PageResp.emptyResult();
		}

		PageInfo pageInfo = new PageInfo(req.getPage(), req.getCount(), total);
		params.setPage(pageInfo);
		Collection<FileTask> tasks = fileTaskService.findTasks(params);
		return PageResp.success(total, tasks);
	}

	@POST
	@Path(Keys.SYSTEMMGR_TASKMGR_IMPORT)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp getImport(@Valid PageReqt req) {
		return list(req);
	}

	@POST
	@Path(Keys.SYSTEMMGR_TASKMGR_EXPORT)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp getExport(@Valid PageReqt req) {
		return list(req);
	}


	@GET
	@Path(Keys.SYSTEMMGR_TASKMGR_EXPORT)
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadFile(@QueryParam("fileName") String fileName,
								 @QueryParam("dataType") Integer dataType,
								 @QueryParam("type") Integer type) throws UnsupportedEncodingException {
		File file = new File(fileName);
		if (!file.exists()) {
			return Response.status(404).build();
		}
		String mt = new MimetypesFileTypeMap().getContentType(file);
		return Response.ok(file, mt).header("Content-disposition", "attachment;filename=" + new String(file.getName().getBytes("utf-8"), "ISO-8859-1"))
				.header("Cache-Control", "no-cache").build();
	}

}

package com.xuanwu.mos.security.filter;

import com.xuanwu.mos.utils.SessionUtil;
import com.xuanwu.mos.utils.WebConstants;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 授权验证访问过滤器
 *
 * @author <a href="mailto:jiji@javawind.com">XueFang.Xu</a>
 * @date 2016-08-24
 * @version 1.0.0
 */
public class ShiroAuthorizationFilter extends AuthorizationFilter {
	private Logger logger = LoggerFactory.getLogger(ShiroAuthorizationFilter.class);

	@Override
	protected boolean isAccessAllowed(ServletRequest req, ServletResponse resp, Object mappedValue) throws Exception {
		HttpServletRequest hreq = (HttpServletRequest) req;
		Subject subject = SecurityUtils.getSubject();

		boolean isPermitted = false;
		boolean isAuthenticated = subject.isAuthenticated();
		if (isAuthenticated) {
			String url = hreq.getPathInfo();
			if (StringUtils.isBlank(url)) {
				url = hreq.getServletPath();
			}
			if (StringUtils.isBlank(url)) {
				logger.error("url为空，权限校验失败!!!");
				return false;
			}
			hreq.setAttribute(WebConstants.KEY_DATA_SCOPE, SessionUtil.getDataSope(url));

			if (SessionUtil.isSessionTimeout()) {// session过期，超时登录，session退出，跳到login请求
				logger.error("登录超时，请重新登录!!!");
				SessionUtil.logout();
				return false;
			}

			//重新记时
			if (!url.equals("/common/notice/unReadNoticeCount") && !url.equals("/common/upAndLoadCount")) {// 前台定时任务的请求不计算session时间
				SessionUtil.newRequestTimer();
			}
            //验证权限
			isPermitted = subject.isPermitted(url);
		}
		return isPermitted;
	}

}

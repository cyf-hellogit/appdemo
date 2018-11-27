package com.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.entity.BackendUser;
import com.entity.DevUser;

public class SysInterceptor extends HandlerInterceptorAdapter{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		DevUser user = (DevUser)session.getAttribute(Constants.USER_SESSION);
		BackendUser bUser = (BackendUser)session.getAttribute(Constants.USER_SESSION);
		if(null==user) {
			response.sendRedirect(request.getContextPath()+"/dev/login");
			return false;
		}else if(null==bUser) {
			response.sendRedirect(request.getContextPath()+"/manager/login");
			return false;
		}
		return true;
	}
	
}

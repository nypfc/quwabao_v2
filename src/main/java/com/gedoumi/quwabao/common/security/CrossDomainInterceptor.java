package com.gedoumi.quwabao.common.security;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CrossDomainInterceptor implements HandlerInterceptor {


	//afterCompletion()方法在DispatcherServlet完全处理完请求后被调用
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse arg1, Object arg2, Exception arg3)throws Exception {
	}
	//postHandle()方法在业务处理器处理请求之后被调用
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object arg2, ModelAndView arg3) throws Exception {
//		response.addHeader("Access-Control-Allow-Origin","*");
//		response.addHeader("Access-Control-Allow-Methods","*");
//		response.addHeader("Access-Control-Max-Age","100");
//		response.addHeader("Access-Control-Allow-Headers", "auth-token,pfcsessionid,Content-Type,cookie,*");
//
//		response.addHeader("Access-Control-Allow-Credentials","false");

	}
	//preHandle()方法在业务处理器处理请求之前被调用
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
		if(request.getMethod().equals("OPTIONS")){
			response.setStatus(HttpServletResponse.SC_OK);
			return true;
		}
		response.addHeader("Access-Control-Allow-Origin","*");
		response.addHeader("Access-Control-Allow-Methods","*");
		response.addHeader("Access-Control-Max-Age","100");
		response.addHeader("Access-Control-Allow-Headers", "auth-token,pfcsessionid,Content-Type,cookie,*");

		response.addHeader("Access-Control-Allow-Credentials","false");
		return true;
	}
	

}

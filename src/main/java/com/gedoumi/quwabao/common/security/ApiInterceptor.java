package com.gedoumi.quwabao.common.security;

import com.gedoumi.quwabao.common.Constants;
import com.gedoumi.quwabao.common.enums.UserValidateStatus;
import com.gedoumi.quwabao.user.dataobj.entity.User;
import com.gedoumi.quwabao.user.service.UserService;
import com.gedoumi.quwabao.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApiInterceptor implements HandlerInterceptor {
	Logger logger = LoggerFactory.getLogger(ApiInterceptor.class);

	final static String[] VALIDATE_URIS = new String[]{
			"/v1/uasset/rent",
			"/v1/uasset/transfer",
			"/v1/uasset/withdraw"
	};

	@Resource
	private UserService userService;
	
	//afterCompletion()方法在DispatcherServlet完全处理完请求后被调用
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse arg1, Object arg2, Exception arg3)throws Exception {
	}
	//postHandle()方法在业务处理器处理请求之后被调用
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse arg1, Object arg2, ModelAndView arg3) throws Exception {
		//System.out.println("***请求执行之后***"+request.getRequestURI());
	}
	//preHandle()方法在业务处理器处理请求之前被调用
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
		logger.info("in preHandler : {}", request.getRequestURI());
		String context = request.getContextPath();
		String reqSessionId = SessionUtil.getSessionIdFromHead();
		String deviceId = SessionUtil.getDeviceFromHead();
		logger.info("in preHandler deviceId: {}", deviceId);
		String authToken = SessionUtil.getTokenFromHead();
		logger.info("in preHandler authToken: {}", authToken);
		if(SessionUtil.getSession().getAttribute(Constants.API_USER_KEY) == null){
			logger.info("in preHandler authToken={}", authToken);
			if(StringUtils.isNotEmpty(authToken)){
				User user = userService.getByToken(authToken);
				if(user == null){
					request.getRequestDispatcher(Constants.INDEX_PATH).forward(request, response);
					return false;
				}
				if(!StringUtils.equals(deviceId,user.getDeviceId())){
					logger.info("{}用户已经在其他设备上登录", user.getMobilePhone());
					request.getRequestDispatcher(Constants.DEVICE_ERROR_PATH).forward(request, response);
					return false;
				}
				SessionUtil.getSession().setAttribute(Constants.API_USER_KEY, user);
				if(!validateUser(user, request.getRequestURI(), context)){
					request.getRequestDispatcher(Constants.VALIDATE_ERROR_PATH).forward(request, response);
					return false;
				}
				return true;
			}

			request.getRequestDispatcher(Constants.INDEX_PATH).forward(request, response);
			return false;

		}

		User user  = userService.getByToken(authToken);
		if(user == null){
			request.getRequestDispatcher(Constants.INDEX_PATH).forward(request, response);
			return false;
		}
		if(!StringUtils.equals(deviceId,user.getDeviceId())){
			logger.info("{}用户已经在其他设备上登录", user.getMobilePhone());
			request.getRequestDispatcher(Constants.DEVICE_ERROR_PATH).forward(request, response);
			return false;
		}
		SessionUtil.getSession().setAttribute(Constants.API_USER_KEY, user);
		if(!validateUser(user, request.getRequestURI(), context)){
			request.getRequestDispatcher(Constants.VALIDATE_ERROR_PATH).forward(request, response);
			return false;
		}
		return true;
	}

	private boolean validateUser(User user, String uri, String context){
		for (String validateUri : VALIDATE_URIS) {
			//需要实名认证才能访问
			if(StringUtils.equals(context+validateUri, uri)){
				if(user.getValidateStatus() == null){
					logger.info("{}用户还未实名认证", user.getMobilePhone());
					return false;
				}
				if(user.getValidateStatus() != UserValidateStatus.Pass.getValue()){
					logger.info("{}用户还未实名认证", user.getMobilePhone());
					return false;
				}
			}
		}
		return true;
	}
	

}

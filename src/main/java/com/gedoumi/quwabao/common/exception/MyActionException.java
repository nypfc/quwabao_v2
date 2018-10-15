/**  
 * @Project: jxoa
 * @Title: MyActionException.java
 * @Package com.oa.commons.exception
 * @date 2013-4-2 下午3:39:49
 * @Copyright: 2013 
 */
package com.gedoumi.quwabao.common.exception;

import com.gedoumi.quwabao.common.Constants;
import com.gedoumi.quwabao.common.utils.WebUtil;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 
 * 类名：MyActionException
 * 功能：统一异常处理
 * 详细：统一处理所有请求异常
 * 作者：LiuJincheng
 * 版本：1.0
 * 日期：2013-4-2 下午3:39:49
 *
 */
public class MyActionException implements HandlerExceptionResolver {
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object obj, Exception e) {
		e.printStackTrace();
		String url=request.getRequestURI();//请求URL
		
		boolean isJson= WebUtil.isAjax(request);//是否需要返回json格式数据
		
		ModelAndView mav = new ModelAndView(Constants.INDEX_PATH);
		
		if(e instanceof org.apache.shiro.authz.UnauthorizedException){
			//没有访问权限
			org.apache.shiro.authz.UnauthorizedException ue=(org.apache.shiro.authz.UnauthorizedException)e;
			System.out.println("***没有访问权限:"+url+"  ***"+ue.getMessage());
			mav.addObject("message", "抱歉，您没有操作权限！");//没有操作权限
		}
		//最后返回错误提示信息
		
//		if(isJson){
//			e.printStackTrace();
			mav.addObject("statusCode","300" );
			mav.setViewName("jsonView");
			return mav;
//		}
//		else{
//			//不需要返回json格式，直接返回错误提示页面
////			mav.setViewName("main");
//			e.printStackTrace();
//			return mav;
//		}
	}

}

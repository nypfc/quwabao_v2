/**  
 * @Title UserAuthenticationFilter.java
 * @date 2013-12-4 上午10:46:12
 * @Copyright: 2013 
 */
package com.gedoumi.quwabao.common.security;

import com.gedoumi.quwabao.common.utils.WebUtil;
import org.apache.shiro.subject.Subject;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * 验证用户是否在线
 * @author LiuJincheng
 * @version 1.0
 */
public class UserAuthenticationFilter extends org.apache.shiro.web.filter.authc.UserFilter{

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String context = httpRequest.getContextPath();
        Subject subject = getSubject(request, response);
        Map<String, String> resultMap = new HashMap<String, String>();
        if (subject.getPrincipal() == null) {
            if (WebUtil.isAjax(httpRequest)) {
            	resultMap.put("code", "300");
            	resultMap.put("msg", "您尚未登录或登录时间过长,请重新登录!");
                WebUtil.out(httpResponse, resultMap);
            } else {
            	httpResponse.sendRedirect(context + "/admin/toLogin");
            }
        } else {
            if (WebUtil.isAjax(httpRequest)) {
            	resultMap.put("code", "301");
            	resultMap.put("msg", "您没有足够的权限执行该操作!");
                WebUtil.out(httpResponse, resultMap);
            } else {
                try {
					request.getRequestDispatcher(context + "/admin/toLogin").forward(request, response);
				} catch (ServletException e) {
					e.printStackTrace();
				}
            }
        }
        return false;
    } 
	
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		Subject subject = getSubject(request, response);
		if (subject.isAuthenticated() || isLoginRequest(request, response)) {
            return true;
        } else {
            return false;
        }
	}

}

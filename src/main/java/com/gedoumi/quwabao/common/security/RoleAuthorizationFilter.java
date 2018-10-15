package com.gedoumi.quwabao.common.security;

import com.gedoumi.quwabao.common.utils.WebUtil;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RoleAuthorizationFilter extends AuthorizationFilter{

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
		  
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        Subject subject = getSubject(request, response);  
        Map<String, String> resultMap = new HashMap<String, String>();
        if (subject.getPrincipal() == null) {  
            if (WebUtil.isAjax(httpRequest)) {  
            	resultMap.put("code", "300");
            	resultMap.put("msg", "您尚未登录或登录时间过长,请重新登录!");
                WebUtil.out(httpResponse, resultMap);  
            } else {  
                saveRequestAndRedirectToLogin(request, response);  
            }  
        } else {  
            if (WebUtil.isAjax(httpRequest)) {
            	resultMap.put("code", "301");
            	resultMap.put("msg", "您没有足够的权限执行该操作!");
                WebUtil.out(httpResponse, resultMap);  
            } else {  
                try {
					request.getRequestDispatcher("/main").forward(request, response);
				} catch (ServletException e) {
					e.printStackTrace();
				}  
            }  
        }  
        return false;  
    }  
  
	@Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
            throws IOException {  
  
        Subject subject = getSubject(request, response);  
        String[] rolesArray = (String[]) mappedValue;  
  
        if (rolesArray == null || rolesArray.length == 0) {  
            // no roles specified, so nothing to check - allow access.  
            return true;  
        }  
  
        Set<String> roles = CollectionUtils.asSet(rolesArray);  
        for (String role : roles) {  
            if (subject.hasRole(role)) {  
                return true;  
            }  
        }  
        return false;  
    }  

}

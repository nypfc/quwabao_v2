/**  
 * @Title ShiroAuthRealm.java
 * @date 2013-11-2 下午3:52:21
 * @Copyright: 2013 
 */
package com.gedoumi.quwabao.common.security;

import com.gedoumi.quwabao.common.enums.UserStatus;
import com.gedoumi.quwabao.sys.entity.SysUser;
import com.gedoumi.quwabao.sys.service.SysUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Map;


/**
 * 
 * @author LiuJincheng
 * @version 1.0
 */
public class ShiroAuthRealm extends AuthorizingRealm{
	public static Logger LOGGER = LoggerFactory.getLogger(ShiroAuthRealm.class);
	@Autowired
	private SysUserService sysUserService;
	
	/**
	 * 认证回调函数,登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		SysUser user = sysUserService.checkLoginUser(token.getUsername(), UserStatus.Enable);
		if (user == null) {
            // 用户名不存在抛出异常
            throw new UnknownAccountException();
        }
//		System.out.println("登录认证"+getName());
		LOGGER.debug("登录认证{}", getName());

		return new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), getName());
	}
	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		LOGGER.info("开始授权查询");
		String username=(String)principals.getPrimaryPrincipal();
		Map<String,Collection<String>> map = sysUserService.getRolesPowers(username);
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addRoles(map.get("roleIds"));
		info.addStringPermissions(map.get("powers"));
		return info;
		
	}
	

}

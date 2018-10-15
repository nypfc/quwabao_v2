package com.gedoumi.quwabao.sys.controller;

import com.gedoumi.quwabao.common.Constants;
import com.gedoumi.quwabao.common.annotation.PfcLogAspect;
import com.gedoumi.quwabao.common.base.LoginToken;
import com.gedoumi.quwabao.common.base.ResponseObject;
import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.sys.entity.SysUser;
import com.gedoumi.quwabao.sys.service.SysUserService;
import com.gedoumi.quwabao.user.dataobj.vo.LoginVO;
import com.gedoumi.quwabao.util.SessionUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class SysLoginController {
	static Logger LOGGER = LoggerFactory.getLogger(SysLoginController.class);

	@Resource
	private SysUserService sysUserService;

	@RequestMapping(value = "toLogin")
	public ModelAndView toLogin(){
		return new ModelAndView("login");
	}

    @RequestMapping(value = "logout")
    public ModelAndView logout(){
	    SessionUtil.getSession().removeAttribute(Constants.USER_KEY);
        return new ModelAndView("login");
    }



	@RequestMapping(value = "login")
	@PfcLogAspect
	public ResponseObject login(LoginVO loginVO){
		UsernamePasswordToken token = new UsernamePasswordToken(loginVO.getUsername(), new Md5Hash(loginVO.getPassword(),
                loginVO.getUsername()).toString().toCharArray(), null);
		Subject subject = SecurityUtils.getSubject();
		ResponseObject responseObject = new ResponseObject();
		try {
			subject.login(token);
			SysUser user = sysUserService.getByUsername(loginVO.getUsername());
			subject.getSession().setAttribute(Constants.USER_KEY, user);
			LoginToken loginToken = new LoginToken();
			loginToken.setUserName(user.getUsername());
			loginToken.setMobilePhone(user.getMobilePhone());
			loginToken.setToken(UUID.randomUUID().toString());
			responseObject.setData(loginToken);
			responseObject.setSuccess();
			user.setLastLoginIp(SessionUtil.getClientIp());
            sysUserService.updateLoginInfo(user);
		} catch (UnknownAccountException ex) {
			LOGGER.warn("用户不存在或者密码错误！");
			responseObject.setInfo(CodeEnum.UserLoginError);
		} catch (IncorrectCredentialsException ex) {
			LOGGER.warn("用户不存在或者密码错误！");
			responseObject.setInfo(CodeEnum.UserLoginError);
		} catch (AuthenticationException ex) {
			LOGGER.error(ex.getMessage()); // 自定义报错信息
			responseObject.setInfo(CodeEnum.SysError);
		} catch (Exception ex) {
			LOGGER.error("内部错误，请重试！", ex);
			responseObject.setInfo(CodeEnum.UnknowError);
		}

		return responseObject;
	}


}

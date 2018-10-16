package com.gedoumi.quwabao.common.base;

import com.gedoumi.quwabao.common.enums.CodeEnum;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/")
public class MainPathController {

	@RequestMapping("index")
	public ResponseObject index(){
		return new ResponseObject(CodeEnum.UnLogin);
	}

	@RequestMapping("deviceError")
	public ResponseObject deviceError(){
		return new ResponseObject(CodeEnum.DeviceError);
	}

	@RequestMapping("validateError")
	public ResponseObject validateError(){
		return new ResponseObject(CodeEnum.ValidateError);
	}

	@RequestMapping("admin/main")
	public ModelAndView login(){
		return new ModelAndView("main");
	}
	

}

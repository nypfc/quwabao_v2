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
		ResponseObject responseObject = new ResponseObject();
		responseObject.setInfo(CodeEnum.UnLogin);
		return responseObject;
	}

	@RequestMapping("deviceError")
	public ResponseObject deviceError(){
		ResponseObject responseObject = new ResponseObject();
		responseObject.setInfo(CodeEnum.DeviceError);
		return responseObject;
	}

	@RequestMapping("validateError")
	public ResponseObject validateError(){
		ResponseObject responseObject = new ResponseObject();
		responseObject.setInfo(CodeEnum.ValidateError);
		return responseObject;
	}

	@RequestMapping("admin/main")
	public ModelAndView login(){
		return new ModelAndView("main");
	}
	

}

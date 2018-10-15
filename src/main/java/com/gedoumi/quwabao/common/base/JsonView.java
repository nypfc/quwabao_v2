package com.gedoumi.quwabao.common.base;

import com.gedoumi.quwabao.common.utils.JsonUtil;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView extends AbstractView {

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,
                                           HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();  
		out.print(JsonUtil.objectToJson(model));
	}

}

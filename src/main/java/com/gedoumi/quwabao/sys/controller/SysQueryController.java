package com.gedoumi.quwabao.sys.controller;


import com.gedoumi.quwabao.sys.service.SysQueryService;
import com.google.common.collect.Lists;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/query")
public class SysQueryController {
	static Logger LOGGER = LoggerFactory.getLogger(SysQueryController.class);

	@Resource
	private SysQueryService sysQueryService;


	@RequestMapping(value = "/")
	public ModelAndView getUserTeam(){
		return new ModelAndView("query/query");
	}

	@RequiresPermissions("query:read")
    @RequestMapping("/doQuery")
    public ModelAndView doQuery(String queryStr){
		List data = Lists.newArrayList();
		ModelAndView modelAndView = new ModelAndView("query/query");
		if(StringUtils.isEmpty(queryStr)
				|| StringUtils.startsWithIgnoreCase(queryStr,"create")
				|| StringUtils.startsWithIgnoreCase(queryStr,"alter")
				|| StringUtils.startsWithIgnoreCase(queryStr,"insert")
				|| StringUtils.startsWithIgnoreCase(queryStr,"update")
				|| StringUtils.startsWithIgnoreCase(queryStr,"delete")
				){
			modelAndView.addObject("data", data);
			modelAndView.addObject("queryStr", queryStr);
			modelAndView.addObject("message", "sql不符合规范");
			return modelAndView;
		}

		try{
			data = sysQueryService.getList(queryStr);
			modelAndView.addObject("message", "查询成功");
		}catch (Exception e){
			LOGGER.error("qeury error", e);
			modelAndView.addObject("message", "sql不符合规范");
		}
		modelAndView.addObject("data", data);
		modelAndView.addObject("queryStr", queryStr);
	    return modelAndView;
    }

	@RequiresPermissions("query:read")
	@RequestMapping("/export")
	public void exportExcel(HttpServletRequest req, HttpServletResponse response, String queryStr)  {
		if(StringUtils.isEmpty(queryStr)
				|| StringUtils.startsWithIgnoreCase(queryStr,"create")
				|| StringUtils.startsWithIgnoreCase(queryStr,"alter")
				|| StringUtils.startsWithIgnoreCase(queryStr,"insert")
				|| StringUtils.startsWithIgnoreCase(queryStr,"update")
				|| StringUtils.startsWithIgnoreCase(queryStr,"delete")
				){
			try {
				req.getRequestDispatcher("/admin/query/doQuery").forward(req,response);
				return;
			} catch (Exception e) {
				LOGGER.error("forward error", e);
			}
		}



		List<Map> data = Lists.newArrayList();
		CSVFormat csvFormat = null;
		CSVPrinter csvPrinter = null;
		try {
			data = sysQueryService.getList(queryStr);
			response.setHeader("Content-disposition", "attachment; filename=report.csv");
			OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream(), "GBK");
			Map<String,Object> head = data.get(0);
			List<String> heads = Lists.newArrayList();
			for (String key : head.keySet()) {
				heads.add(key);
			}
			csvFormat = CSVFormat.DEFAULT.withHeader(heads.toArray(new String[heads.size()]));
			csvPrinter = new CSVPrinter(osw, csvFormat);
			for (Map map : data) {
				csvPrinter.printRecord(map.values());
			}


		}catch (Exception e){
			LOGGER.error("export error", e);
			try {
				req.getRequestDispatcher("/admin/query/doQuery").forward(req,response);
				return;
			}catch (Exception ex){
				LOGGER.error("forward error", ex);
			}

		}finally {
			if(csvPrinter != null){
				try {
					csvPrinter.flush();
					csvPrinter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}



	}




}

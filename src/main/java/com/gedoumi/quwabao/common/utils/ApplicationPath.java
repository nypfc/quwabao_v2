package com.gedoumi.quwabao.common.utils;

import java.io.InputStream;
import java.util.Properties;

public class ApplicationPath {
	public static Properties param;

	private static ApplicationPath instance = null;

	private ApplicationPath() {
		InputStream in = null;
		try {
			in = this.getClass().getResourceAsStream(
					"/app.properties");
			Properties p = new Properties();
			p.load(in);
			param = p;
			System.out.println("加载系统配置文件成功!1");
		} catch (Exception e) {
			System.out.println("加载系统配置文件错误!2");
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (Exception e) {
			}
		}
	}

	public static synchronized ApplicationPath getInstance() {
		if (instance == null) {
			instance = new ApplicationPath();
		}
		return instance;
	}

	/**
	 * 获得配置文件中某个参数的值
	 * 
	 * @param paramName
	 *            参数名称
	 * @return 参数值
	 * 
	 * @author 
	 */
	public static String getParameter(String propertyName) {
		if (instance == null) {
			instance = new ApplicationPath();
		}
		return param.getProperty(propertyName);
	}

	public static void main(String[] args) {
		System.out.println(ApplicationPath.getParameter("image.path"));
	}
}

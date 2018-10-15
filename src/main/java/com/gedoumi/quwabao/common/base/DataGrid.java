/**  
 * @Title: DataGrid.java
 * @date 2013-8-28 下午10:54:14
 * @Copyright: 2013 
 */
package com.gedoumi.quwabao.common.base;

import java.util.List;

/**
 * easyui 分页查询数据表格返回的结果
 * @author	LiuJincheng
 * @version	 1.0
 *
 */
public class DataGrid implements java.io.Serializable{
	/**
	 * @Fields serialVersionUID : 
	 */
	
	private static final long serialVersionUID = 1L;
	/**
	 * 总条数
	 */
	private Long total;// 总记录数
	/**
	 * 查询数据集合
	 */
	@SuppressWarnings("rawtypes")
	private List rows;
	/**
	 * 表格底部显示的信息,固定在底部
	 * 列名和数据列名相同
	 */
	@SuppressWarnings("rawtypes")
	private List footer;
	
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	@SuppressWarnings("rawtypes")
	public List getRows() {
		return rows;
	}
	@SuppressWarnings("rawtypes")
	public void setRows(List rows) {
		this.rows = rows;
	}
	@SuppressWarnings("rawtypes")
	public List getFooter() {
		return footer;
	}
	@SuppressWarnings("rawtypes")
	public void setFooter(List footer) {
		this.footer = footer;
	}
	
}

/**  
 * @Title: PageParam.java
 * @date 2013-8-28 下午10:36:05
 * @Copyright: 2013 
 */
package com.gedoumi.quwabao.common.base;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

/**
 * easyui 版分页查询参数
 * @version	 1.0
 *
 */
public class PageParam implements java.io.Serializable{
	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 起始页数
	 */
	private int page = 1;
	/**
	 * 每页显示的条数
	 */
	private int rows = 20;
	/**
	 * 排序字段名称
	 */
	private String sort;
	/**
	 * 排序方式，按顺序对应排序字段名称
	 */
	private String order;
	/**
	 * 条件查询规则： json格式 groups: rules: field,op,value,type groups: op:
	 */
	private String searchRules;

	/**
	 * 是否包含当前页之前的数据
	 */
	private boolean isAll = false;

	/**
	 * hql语句拼接排序语句
	 * 
	 * @param hql
	 */
	public void appendOrderBy(StringBuffer hql) {
		if (sort != null && !"".equals(sort)) {
			String[] sorts = sort.split(",");
			String[] orders = order.split(",");
			hql.append(" order by ");
			for (int i = 0, c = sorts.length; i < c; i++) {
				String sort = sorts[i].replace("_bm_", ".");
				if (i == 0) {
					hql.append(sort + " " + orders[i]);
				} else {
					hql.append("," + sort + " " + orders[i]);
				}
			}
		}
	}

	/**
	 * hql语句拼接排序语句
	 * 
	 * @param hql
	 */
	public void appendWhere(StringBuffer hql) {
		if (!StringUtils.isEmpty(searchRules)) {
			JSONObject jo = JSONObject.parseObject(searchRules);
			JSONArray ja = jo.getJSONArray("rules");
			for (int i = 0; i < ja.size(); i++) {
				jo = ja.getJSONObject(i);
				String field = jo.getString("field");
				String op = jo.getString("op");
				String value = jo.getString("value");
				String type = jo.getString("type");
				if(op.equals("equal"))
					hql.append(" and ").append(field).append(" ").append("=");
				else
					hql.append(" and ").append(field).append(" ").append(op);
				if ("like".equals(op)) {
					hql.append("'%").append(value).append("%'");
				} else {
					if ("string".equals(type)) {
						hql.append("'").append(value).append("'");
					} else {
						hql.append(value);
					}
				}
			}
		}
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getSearchRules() {
		return searchRules;
	}

	public void setSearchRules(String searchRules) {
		this.searchRules = searchRules;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isAll() {
		return isAll;
	}

	public void setAll(boolean isAll) {
		this.isAll = isAll;
	}
}

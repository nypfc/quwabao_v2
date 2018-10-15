package com.gedoumi.quwabao.sys.service;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;


/**
 * 
 * 类名：
 * 功能：
 *
 */
@Service
public class SysQueryService {

	@PersistenceContext
	private EntityManager entityManager;


	public List getList(String queryStr){
		if(!StringUtils.containsIgnoreCase(queryStr,"limit")){
			queryStr = queryStr + " limit 1000";
		}
		Query queryData = entityManager.createNativeQuery(queryStr);

		queryData.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List list = (List)queryData.getResultList();


		return list;
	}



}

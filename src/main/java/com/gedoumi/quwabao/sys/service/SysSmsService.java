package com.gedoumi.quwabao.sys.service;

import com.gedoumi.quwabao.common.constants.Constants;
import com.gedoumi.quwabao.common.utils.PfcDateUtils;
import com.gedoumi.quwabao.sys.dao.SysSmsDao;
import com.gedoumi.quwabao.sys.entity.SysSms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;


/**
 * 
 * 类名：
 * 功能：
 *
 */
@Service
public class SysSmsService {
	
	@Autowired
	private SysSmsDao smsDao;

	@Transactional
	public void add(SysSms sms){
		smsDao.save(sms);
	}

	public List<SysSms> findByQuery(SysSms query){
		//创建匹配器，即如何使用查询条件
		ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
				.withMatcher("mobilePhone", ExampleMatcher.GenericPropertyMatchers.exact())
				.withMatcher("sysStatus", ExampleMatcher.GenericPropertyMatchers.exact())
				.withMatcher("sysType", ExampleMatcher.GenericPropertyMatchers.exact())
				.withMatcher("code", ExampleMatcher.GenericPropertyMatchers.exact());

		//创建实例
		Example<SysSms> ex = Example.of(query, matcher);
		return smsDao.findAll(ex);
	}

	public SysSms getUserFul(SysSms query){
		List<SysSms> sysSmsList = findByQuery(query);
		for (SysSms sms : sysSmsList) {
			int senconds = calLastedTime(sms.getCreateTime());
			if(senconds < Constants.EXPIRE_TIMES){
				return sms;
			}
		}
		return null;
	}

	public int calLastedTime(Date startDate) {
		long a = new Date().getTime();
		long b = startDate.getTime();
		int c = (int)((a - b) / 1000);
		return c;
	}

	public int getCurrentDayCount(String mobile){
		List<SysSms> sysSmsList = smsDao.getByMobilePhoneAndCreateTimeAfter(mobile, PfcDateUtils.getDayStart(new Date()));
		if(CollectionUtils.isEmpty(sysSmsList)){
			return 0;
		}
		return sysSmsList.size();
	}


}

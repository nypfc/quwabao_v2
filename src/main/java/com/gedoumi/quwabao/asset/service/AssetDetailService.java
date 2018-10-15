package com.gedoumi.quwabao.asset.service;

import com.gedoumi.quwabao.asset.dao.UserAssetDetailDao;
import com.gedoumi.quwabao.asset.entity.UserAssetDetail;
import com.gedoumi.quwabao.asset.entity.UserRent;
import com.gedoumi.quwabao.asset.vo.DetailTypeVO;
import com.gedoumi.quwabao.common.AppConfig;
import com.gedoumi.quwabao.common.base.PageParam;
import com.gedoumi.quwabao.common.enums.DetailType;
import com.gedoumi.quwabao.common.enums.TransType;
import com.gedoumi.quwabao.common.enums.VersionType;
import com.gedoumi.quwabao.user.dao.UserDao;
import com.gedoumi.quwabao.user.dataobj.entity.User;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * 
 * 类名：
 * 功能：
 *
 */
@Service
public class AssetDetailService {

	Logger logger = LoggerFactory.getLogger(AssetDetailService.class);

	@Resource
	private UserDao userDao;

	@Resource
	private UserAssetDetailDao userAssetDetailDao;

	@Resource
	private AppConfig appConfig;


	@PersistenceContext
	private EntityManager entityManager;


	@SuppressWarnings("unchecked")
	public List<UserAssetDetail> getList(PageParam param, DetailTypeVO detailTypeVO){
//		DataGrid data=new DataGrid();
		String prefSql = " from UserAssetDetail t where 1=1 and t.user.id = :userId  ";
		StringBuffer sqlCount = new StringBuffer("select count(t.id)  ").append(prefSql);
		StringBuffer sqlData = new StringBuffer("select t ").append(prefSql);

		if(detailTypeVO.getDetailType() != DetailType.All.getValue()){
			sqlCount.append(" and t.transType in :transTypes ");
			sqlData.append(" and t.transType in :transTypes ");
		}
		sqlCount.append("order by id desc ");
		sqlData.append("order by id desc ");

		Query queryCount = entityManager.createQuery(sqlCount.toString());
		Query queryData = entityManager.createQuery(sqlData.toString());

		queryCount.setParameter("userId", detailTypeVO.getUserId());
		queryData.setParameter("userId", detailTypeVO.getUserId());
		List<Integer> transTypes = Lists.newArrayList();
		if(detailTypeVO.getDetailType() == DetailType.Trans.getValue()){
			transTypes.add(TransType.FrozenIn.getValue());
			transTypes.add(TransType.FrozenOut.getValue());
			transTypes.add(TransType.TeamInit.getValue());
			transTypes.add(TransType.TransIn.getValue());
			transTypes.add(TransType.TransOut.getValue());
			transTypes.add(TransType.Rent.getValue());
			transTypes.add(TransType.NetIn.getValue());
			transTypes.add(TransType.NetOut.getValue());
			transTypes.add(TransType.UnFrozenAsset.getValue());
			queryCount.setParameter("transTypes", transTypes);
			queryData.setParameter("transTypes", transTypes);
		}
		if(detailTypeVO.getDetailType() == DetailType.Profit.getValue()){
			transTypes.add(TransType.TeamReward.getValue());
			transTypes.add(TransType.RentBack.getValue());
			transTypes.add(TransType.Profit.getValue());
			transTypes.add(TransType.Reward.getValue());
			queryCount.setParameter("transTypes", transTypes);
			queryData.setParameter("transTypes", transTypes);
		}


		Long count = Long.parseLong(queryCount.getResultList().get(0).toString());

		List<UserAssetDetail> list = (List<UserAssetDetail>)queryData.setFirstResult((param.getPage() - 1) * param.getRows()).setMaxResults(param.getRows()).getResultList();

//		data.setTotal(count);
//		data.setRows(list);
		return list;
	}

	public List<UserAssetDetail> getRentDetail(UserRent userRent){
		List<UserAssetDetail> userAssetDetails = userAssetDetailDao.findByUserRent(userRent);
		return userAssetDetails;
	}

	public BigDecimal getProfitAsset(List<UserAssetDetail> userAssetDetails){
		BigDecimal rentAsset = BigDecimal.ZERO;
		for (UserAssetDetail userAssetDetail : userAssetDetails) {
			if(appConfig.getVersion() == VersionType.WithoutRecharge){
				rentAsset = rentAsset.add(userAssetDetail.getProfit());
			}else {
				rentAsset = rentAsset.add(userAssetDetail.getProfitExt());
			}
		}
		return rentAsset;
	}

	public List<UserAssetDetail> getDetailsByDigDate(User user, Date date){
		List<UserAssetDetail> userAssetDetails = userAssetDetailDao.findByUserAndDigDate(user,date);
		return userAssetDetails;
	}

	public UserAssetDetail getByApiTransSeq(String apiTransSeq){
		return userAssetDetailDao.findByApiTransSeq(apiTransSeq);
	}

	public List<UserAssetDetail> getByUserAndTransTypeAndCreateTimeAfter(User user, int transType, Date createTime){
		return userAssetDetailDao.findByUserAndTransTypeAndCreateTimeAfter(user, transType, createTime);
	}

}

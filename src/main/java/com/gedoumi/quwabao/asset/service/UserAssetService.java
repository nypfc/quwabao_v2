package com.gedoumi.quwabao.asset.service;

import com.gedoumi.quwabao.asset.dao.*;
import com.gedoumi.quwabao.asset.entity.*;
import com.gedoumi.quwabao.asset.vo.InitBaseAssetVO;
import com.gedoumi.quwabao.asset.vo.TeamRentVO;
import com.gedoumi.quwabao.asset.vo.TransferVO;
import com.gedoumi.quwabao.common.AppConfig;
import com.gedoumi.quwabao.common.constants.Constants;
import com.gedoumi.quwabao.common.enums.*;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.common.utils.PfcDateUtils;
import com.gedoumi.quwabao.sys.dao.SysRentDao;
import com.gedoumi.quwabao.sys.entity.SysRent;
import com.gedoumi.quwabao.sys.entity.SysUser;
import com.gedoumi.quwabao.user.mapper.UserMapper;
import com.gedoumi.quwabao.user.mapper.UserTreeDao;
import com.gedoumi.quwabao.team.dataobj.model.UserTree;
import com.gedoumi.quwabao.common.utils.CipherUtils;
import com.gedoumi.quwabao.common.utils.MD5EncryptUtil;
import com.gedoumi.quwabao.common.utils.NumberUtil;
import com.gedoumi.quwabao.common.utils.SessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.*;


/**
 * 
 * 类名：
 * 功能：
 *
 */
@Service
public class UserAssetService {

	Logger logger = LoggerFactory.getLogger(UserAssetService.class);
	
	@Autowired
	private UserAssetDao assetDao;

	@Autowired
	private UserAssetDetailDao assetDetailDao;

	@Autowired
	private TransDetailDao transDetailDao;

	@Autowired
	private UserRentDao userRentDao;

	@Resource
	private UserTreeDao userTreeDao;

	@Resource
	private UserTeamDao userTeamDao;

	@Resource
	private UserTeamUnFrozenDao userTeamUnFrozenDao;
	@Resource
	private UserTeamRewardDao userTeamRewardDao;

	@Resource
	private UserMapper userMapper;

	@Resource
	private SysRentDao sysRentDao;

	@Resource
	private UserRewardDao userRewardDao;

	@Resource
	private AppConfig appConfig;
	@PersistenceContext
    private EntityManager entityManager;

	@Transactional
	public void addAsset(UserAssetDetail assetDetail) {
		Date now = new Date();
		assetDetail.setCreateTime(now);
		assetDetail.setUpdateTime(now);
		assetDetail.setVersionType(appConfig.getVersion().getValue());
		assetDetailDao.save(assetDetail);

		UserAsset orgAsset = assetDao.findByUser(assetDetail.getUser());
		if(orgAsset == null){
			orgAsset = new UserAsset();
			orgAsset.setCreateTime(now);
			orgAsset.setFrozenAsset(BigDecimal.ZERO);
			orgAsset.setTotalAsset(BigDecimal.ZERO);
			orgAsset.setRemainAsset(BigDecimal.ZERO);
			orgAsset.setProfit(BigDecimal.ZERO);
			orgAsset.setUser(assetDetail.getUser());
		}
		Integer transType = assetDetail.getTransType();
		TransType type = TransType.fromValue(transType);
		BigDecimal profit = getProfitFromDetail(assetDetail);
		BigDecimal money = assetDetail.getMoney();
		BigDecimal _money = money.multiply(new BigDecimal(-1));
		switch (type) {
			case Init:
				break;
			case Profit:
				calAsset(orgAsset, assetDetail, profit);
				break;
			case Reward:
				calAsset(orgAsset, assetDetail, profit);
				break;
			case RentBack:
				calAsset(orgAsset, assetDetail, profit);
				break;
			case TransOut:
				calAsset(orgAsset, assetDetail, _money);
				break;
			case TransIn:
				calAsset(orgAsset, assetDetail, money);
				break;
			case Rent:
				calAsset(orgAsset, assetDetail, _money);
				break;
			case NetIn:
				calAsset(orgAsset, assetDetail, money);
				break;
			case NetOut:
				calAsset(orgAsset, assetDetail, _money);
				break;
			case FrozenIn:
				calFrozenAsset(orgAsset, assetDetail, money);
				break;
			case FrozenOut:
				calFrozenAsset(orgAsset, assetDetail, _money);
				break;
			case UnFrozenAsset:
				calUnFrozenAsset(orgAsset, assetDetail, money);
				break;
			case TeamReward:
				calAsset(orgAsset, assetDetail, profit);
				break;
			case TeamInit:
				calFrozenAsset(orgAsset, assetDetail, money);
				break;
		}
		orgAsset.setUpdateTime(now);

		assetDao.save(orgAsset);

	}

	public UserAsset getUserAsset(User user){
		return assetDao.findByUser(user);
	}

	private BigDecimal getProfitFromDetail(UserAssetDetail assetDetail) {
		if(appConfig.getVersion() == VersionType.WithoutRecharge){
			return assetDetail.getProfit();
		}
		return assetDetail.getProfitExt();
	}

	private void calAsset(UserAsset orgAsset, UserAssetDetail assetDetail, BigDecimal money) {
		BigDecimal profit = getProfitFromDetail(assetDetail);
		BigDecimal orgTotalAsset = orgAsset.getTotalAsset();
		BigDecimal orgRemainAsset = orgAsset.getRemainAsset();
		BigDecimal orgProfit = orgAsset.getProfit();

		orgAsset.setTotalAsset(orgTotalAsset.add(money));
		orgAsset.setRemainAsset(orgRemainAsset.add(money));
		orgAsset.setProfit(orgProfit.add(profit));
	}

	private void calFrozenAsset(UserAsset orgAsset, UserAssetDetail assetDetail, BigDecimal money) {
		BigDecimal profit = getProfitFromDetail(assetDetail);
		BigDecimal orgTotalAsset = orgAsset.getTotalAsset();
		BigDecimal orgFrozenAsset = orgAsset.getFrozenAsset();
		BigDecimal orgProfit = orgAsset.getProfit();

		orgAsset.setTotalAsset(orgTotalAsset.add(money));
		orgAsset.setFrozenAsset(orgFrozenAsset.add(money));
		orgAsset.setProfit(orgProfit.add(profit));
	}

	private void calUnFrozenAsset(UserAsset orgAsset, UserAssetDetail assetDetail, BigDecimal money) {
		BigDecimal orgTotalAsset = orgAsset.getTotalAsset();
		BigDecimal orgFrozenAsset = orgAsset.getFrozenAsset();
		BigDecimal orgRemainAsset = orgAsset.getRemainAsset();
		BigDecimal orgProfitAsset = orgAsset.getProfit();

		orgAsset.setTotalAsset(orgTotalAsset.add(money));
		orgAsset.setRemainAsset(orgRemainAsset.add(money));
		BigDecimal frozenAsset = orgFrozenAsset.subtract(money);
		if(frozenAsset.compareTo(BigDecimal.ZERO) <0){
			frozenAsset = BigDecimal.ZERO;
		}
		orgAsset.setFrozenAsset(frozenAsset);
		//解冻不计入收益
//		orgAsset.setProfit(orgProfitAsset.add(money));
	}



	@Transactional(rollbackFor = Exception.class)
	public void addRent(UserRent userRent) throws BusinessException {
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		int hour_of_day = cal.get(Calendar.HOUR_OF_DAY);
		if(hour_of_day>=23){
			throw new BusinessException(CodeEnum.AddRentTimeError);
		}
		User user = userRent.getUser();
		final BigDecimal rentAsset = userRent.getRentAsset();
		UserAsset asset = assetDao.findByUser(user);
		if(asset == null){
			throw new BusinessException(CodeEnum.RemainAssetError);
		}
		BigDecimal remainAsset = asset.getRemainAsset();
		//TODO 是否需要门槛
		if(remainAsset.compareTo(rentAsset)<0){
			throw new BusinessException(CodeEnum.RemainAssetError);
		}

		userRent.setRentStatus(RentStatus.Active.getValue());
		List<UserRent> userRents = userRentDao.findByUser(user);
		if(CollectionUtils.isEmpty(userRents)){
			userRent.setFirstRentType(FirstRentType.First.getValue());
		}else {
			if(hasActiveRent(userRents)){
				throw new BusinessException(CodeEnum.RentError);
			}
			userRent.setFirstRentType(FirstRentType.Init.getValue());
		}
		userRentDao.save(userRent);

		UserAssetDetail detail = new UserAssetDetail();
		Date now = new Date();
		detail.setUser(user);
		detail.setMoney(rentAsset);
		detail.setUserRent(userRent);
		detail.setProfit(BigDecimal.ZERO);
		detail.setProfitExt(BigDecimal.ZERO);
		detail.setTransType(TransType.Rent.getValue());
		detail.setDigDate(now);
		addAsset(detail);

		//计算推荐人收益
		UserTree userTree = userTreeDao.findByChild(user);
		BigDecimal rate = Constants.PROFIT_RATE;
		while (userTree != null){
			User parent = userTree.getParent();
//			UserAssetDetail parentDetail = new UserAssetDetail();
//			parentDetail.setUser(parent);
			BigDecimal reward = rentAsset.multiply(rate).setScale(5, BigDecimal.ROUND_HALF_UP);
//			parentDetail.setMoney(BigDecimal.ZERO);
//			parentDetail.setProfit(reward);
//			parentDetail.setProfitExt(reward);
//			parentDetail.setTransType(TransType.Reward.getValue());
//			parentDetail.setDigDate(now);
//			parentDetail.setRewardUser(user);
//			addAsset(parentDetail);

			UserReward userReward = new UserReward();
			userReward.setCreateTime(now);
			userReward.setUpdateTime(now);
			userReward.setReward(reward);
			userReward.setUser(parent);
			userReward.setRewardUser(user);
			userReward.setRemainFrozen(reward);
			userReward.setUnlockPerDay(reward.divide(Constants.REWARD_DAYS, Constants.SCALE, BigDecimal.ROUND_HALF_UP));
			userRewardDao.save(userReward);

			userTree = userTreeDao.findByChild(parent);
			rate = rate.divide(Constants.TWO , 4, BigDecimal.ROUND_HALF_UP);
		}
	}

	private boolean hasActiveRent(List<UserRent> userRents) {
		for (UserRent userRent : userRents) {
			if (userRent.getRentStatus() == RentStatus.Active.getValue()){
				return true;
			}
		}
		return false;
	}

	private UserRent getActiveRent(List<UserRent> userRents) {
		for (UserRent userRent : userRents) {
			if (userRent.getRentStatus() == RentStatus.Active.getValue()){
				return userRent;
			}
		}
		return null;
	}


	public void digJob(Date now){
		//TODO 分页实现
		List<UserRent> userRents = userRentDao.findByExpireDateAfterAndRentStatus(now, RentStatus.Active.getValue());
		if (CollectionUtils.isEmpty(userRents)){
			logger.info("没有符合条件的矿机 ");
			return;
		}
		logger.info("userRents size = {} ", userRents.size());
		for (UserRent userRent : userRents) {

			UserAssetDetail orgDetail = assetDetailDao.findByUserRentAndDigDateAndTransType(userRent, now, TransType.Profit.getValue());
			if (orgDetail != null){
				continue;
			}
			User user = userRent.getUser();
			UserAssetDetail detail = new UserAssetDetail();
			BigDecimal rentAsset = userRent.getRentAsset();
//			RentType rentType = RentType.fromValue(userRent.getRentType());
			SysRent rentType = sysRentDao.findByCode(String.valueOf(userRent.getRentType()));
			BigDecimal rate = rentType.getRate();
            int seed = NumberUtil.randomInt(9900,10050);
            rate = rate.multiply(new BigDecimal(seed)).divide(new BigDecimal(10000), Constants.SCALE, BigDecimal.ROUND_HALF_UP);
            BigDecimal rateExt = rate.add(BigDecimal.ONE);
			int days = rentType.getDays();
			detail.setUser(user);
			detail.setMoney(BigDecimal.ZERO);
			detail.setUserRent(userRent);

			detail.setProfit(rentAsset.multiply(rate).divide(new BigDecimal(days),Constants.SCALE_PROFIT,BigDecimal.ROUND_HALF_UP));
			detail.setProfitExt(rentAsset.multiply(rateExt).divide(new BigDecimal(days),Constants.SCALE_PROFIT,BigDecimal.ROUND_HALF_UP));
			detail.setTransType(TransType.Profit.getValue());
			detail.setDigDate(now);
			addAsset(detail);

			Date tomorrow = DateUtils.addDays(now,1);
			logger.info("tomorrow = {}, expireDate = {}", tomorrow, userRent.getExpireDate());
			if(tomorrow.compareTo(userRent.getExpireDate()) > 0){
				rentExpire(now, userRent, user, rentAsset);
			}
		}
	}

	@Transactional
	public void rentExpire(Date now, UserRent userRent, User user, BigDecimal rentAsset) {
		if(appConfig.getVersion() == VersionType.WithoutRecharge){
			UserAssetDetail rentBack = new UserAssetDetail();
			rentBack.setUser(user);
			rentBack.setMoney(rentAsset);
			rentBack.setProfit(BigDecimal.ZERO);
			rentBack.setProfitExt(BigDecimal.ZERO);
			rentBack.setTransType(TransType.RentBack.getValue());
			rentBack.setDigDate(now);
			addAsset(rentBack);
		}
		userRent.setRentStatus(RentStatus.Expired.getValue());
		userRentDao.save(userRent);
	}


	/**
	 * 计算团队租赁矿机的总币
	 * @param user
	 * @param first 第一级别，不计算业绩???又要算
	 * @return
	 */
	public TeamRentVO getTeamRentAsset(User user, boolean first){
		List<UserRent> userRents = userRentDao.findByUser(user);
		TeamRentVO teamRentVO = new TeamRentVO();
		BigDecimal total = BigDecimal.ZERO;
		int totalCount = 0;
//		if(!first){
			for (UserRent userRent : userRents) {
				total = total.add(userRent.getRentAsset());
				totalCount++;
			}
//		}

		List<UserTree> userTrees = userTreeDao.findByParent(user);
		if (!CollectionUtils.isEmpty(userTrees)){
			for (UserTree userTree : userTrees) {
				total = total.add(getTeamRentAsset(userTree.getChild(),false).getTotalAsset());
				totalCount += getTeamRentAsset(userTree.getChild(), false).getTotalCount();
			}
		}
		teamRentVO.setTotalAsset(total);
		teamRentVO.setTotalCount(totalCount);
		return teamRentVO;
	}


	/**
	 * 计算团队租赁矿机的总币
	 * @param user
	 * @return
	 */
	public InitBaseAssetVO getInitBaseAsset(User user){
		UserAsset userAsset = assetDao.findByUser(user);
		InitBaseAssetVO initBaseAssetVO = new InitBaseAssetVO();
		BigDecimal total = BigDecimal.ZERO;
		if(userAsset != null){
			total = total.add(userAsset.getFrozenAsset());
		}


		List<UserTree> userTrees = userTreeDao.findByParent(user);
		if (!CollectionUtils.isEmpty(userTrees)){
			for (UserTree userTree : userTrees) {
				total = total.add(getInitBaseAsset(userTree.getChild()).getTotalAsset());
			}
		}
		initBaseAssetVO.setTotalAsset(total);
		return initBaseAssetVO;
	}

	public UserTeam getUserTeam(User user){
		return userTeamDao.findByUser(user);
	}


	@Transactional
	public void addUserTeamReward(UserTeamReward userTeamReward){
		userTeamRewardDao.save(userTeamReward);
	}

	/**
	 * 申请团队奖励
	 * @param userTeam
	 * @throws BusinessException
	 */
	@Transactional
	public void applyReward(UserTeam userTeam) throws BusinessException {
		User user = userTeam.getUser();
		if(user.getUserType() != UserType.Level_Team.getValue()){
            throw new BusinessException(CodeEnum.ApplyTeamRewardError);
        }
		Date now = new Date();
		BigDecimal totalRentAsset = userTeam.getTotalRentAsset();
		int totalCount = userTeam.getTotalCount();
		TeamRewardType rewardType = TeamRewardType.Level_1;
		if(totalRentAsset.compareTo(TeamRewardType.Level_3.getAsset())>=0 && totalCount >= TeamRewardType.Level_3.getRentNum()){
			rewardType = TeamRewardType.Level_3;
		}else if(totalRentAsset.compareTo(TeamRewardType.Level_2.getAsset())>=0 && totalCount >= TeamRewardType.Level_2.getRentNum()){
			rewardType = TeamRewardType.Level_2;
		}else if(totalRentAsset.compareTo(TeamRewardType.Level_1.getAsset())>=0 && totalCount >= TeamRewardType.Level_1.getRentNum()){
			rewardType = TeamRewardType.Level_1;
		}else {
			throw new BusinessException(CodeEnum.ApplyTeamRewardError);
		}

		BigDecimal orgReward = BigDecimal.ZERO;
		List<UserTeamReward> teamRewards = userTeamRewardDao.findByUser(user);
		if(!CollectionUtils.isEmpty(teamRewards)){
			for (UserTeamReward teamReward : teamRewards) {
				TeamRewardType orgType = TeamRewardType.fromValue(teamReward.getTeamRewardType());
				orgReward = orgReward.add(orgType.getReward());
				if(orgType == rewardType){
					throw new BusinessException(CodeEnum.ApplyTeamRewardTwiceError);
				}
			}
		}
		BigDecimal reward = rewardType.getReward().subtract(orgReward);
		BigDecimal frozenAsset = reward.multiply(Constants.FROZEN_RATE).setScale(Constants.SCALE,BigDecimal.ROUND_HALF_UP);
		BigDecimal unlockPerday = frozenAsset.divide(Constants.FROZEN_DAYS, Constants.SCALE, BigDecimal.ROUND_HALF_UP);
		BigDecimal addMoney = reward.subtract(frozenAsset).setScale(Constants.SCALE,BigDecimal.ROUND_HALF_UP);
		UserTeamReward teamReward = new UserTeamReward();
		teamReward.setCreateTime(now);
		teamReward.setUpdateTime(now);
		teamReward.setReward(reward);
		teamReward.setFrozenAsset(frozenAsset);
		teamReward.setRemainFrozen(frozenAsset);
		teamReward.setUnlockPerDay(unlockPerday);
		teamReward.setTeamRewardType(rewardType.getValue());
		teamReward.setUser(user);
		userTeamRewardDao.save(teamReward);

		//添加奖励资产
		UserAssetDetail assetDetail = new UserAssetDetail();
		assetDetail.setUpdateTime(now);
		assetDetail.setCreateTime(now);
		assetDetail.setDigDate(now);
		assetDetail.setVersionType(appConfig.getVersion().getValue());
		assetDetail.setTransType(TransType.TeamReward.getValue());
		assetDetail.setMoney(BigDecimal.ZERO);
		assetDetail.setProfitExt(addMoney);
		assetDetail.setProfit(addMoney);
		assetDetail.setUser(user);
		addAsset(assetDetail);

	}



	/**
	 * 添加团队初始信息
	 */
	@Transactional
	public void addUserTeam(UserTeam userTeam) throws BusinessException {
		User user = userTeam.getUser();
		Date now = new Date();
		User orgUser = userMapper.findByMobilePhone(user.getMobilePhone());
		if(orgUser != null){
			UserTeam orgUserTeam = userTeamDao.findByUser(orgUser);
			if(orgUserTeam != null){
				throw new BusinessException(CodeEnum.UserTeamError);
			}

			orgUser.setUpdateTime(now);
			orgUser.setUserType(user.getUserType());
			userTeam.setUser(orgUser);
			UserAsset userAsset = assetDao.findByUser(orgUser);
			if(userAsset != null){
				userTeam.setBaseAsset(userAsset.getInitBaseAsset());
			}

		}else {
			//add new user
            String salt = MD5EncryptUtil.md5Encrypy(user.getMobilePhone()).toString();
			user.setPassword(MD5EncryptUtil.md5Encrypy(SysUser.PWD_INIT, salt).toString());
			user.setUserStatus(UserStatus.Enable.getValue());

			user.setUpdateTime(now);
			user.setRegisterTime(now);
			user.setLastLoginTime(now);
			user.setLastLoginIp(SessionUtil.getClientIp());
			user.setToken(UUID.randomUUID().toString());
			user.setInviteCode(CipherUtils.generateCode());
//			user.setUserType(UserType.Level_Team.getValue());
			while (true){
				orgUser = userMapper.findByInviteCode(user.getInviteCode());
				if(orgUser == null){
					break;
				}
				user.setInviteCode(CipherUtils.generateCode());
			}
			userMapper.save(user);
			int length = String.valueOf(user.getId()).length();
			length = length > 4 ? length : 4;
			String format = "%0" + length + "d";
			user.setUsername(User.PREFIX+NumberUtil.randomInt(0,999)+String.format(format,user.getId()));
			userMapper.save(user);

			//update asset detail
			UserAssetDetail assetDetail = new UserAssetDetail();
			assetDetail.setDigDate(now);
			assetDetail.setUser(userTeam.getUser());
			assetDetail.setVersionType(appConfig.getVersion().getValue());
			assetDetail.setTransType(TransType.TeamInit.getValue());
			assetDetail.setProfitExt(BigDecimal.ZERO);
			assetDetail.setProfit(BigDecimal.ZERO);
			assetDetail.setMoney(userTeam.getBaseAsset());
			assetDetail.setCreateTime(now);
			assetDetail.setUpdateTime(now);

			addAsset(assetDetail);
		}
		userTeam.setCreateTime(now);
		userTeam.setUpdateTime(now);
		userTeam.setTeamStatus(TeamStatus.Enable.getValue());
		userTeam.setTotalCount(0);
		userTeam.setTotalRentAsset(BigDecimal.ZERO);
		userTeamDao.save(userTeam);

	}

	/**
	 * 解冻
	 * @param userTeam
	 */
	@Transactional
	public void unFrozen(UserTeam userTeam) throws BusinessException {
		User user = userTeam.getUser();
		Date now = new Date();
		TeamRentVO teamRentVO = getTeamRentAsset(user,true);
		userTeam.setTotalCount(teamRentVO.getTotalCount());
		userTeam.setTotalRentAsset(teamRentVO.getTotalAsset());
		final BigDecimal baseAsset = userTeam.getBaseAsset();
		final BigDecimal rate = teamRentVO.getTotalAsset().divide(baseAsset,5,BigDecimal.ROUND_HALF_UP);
		BigDecimal orgUnFrozenAsset = userTeam.getUnFrozenAsset();
		BigDecimal unFrozenRate = BigDecimal.ZERO;
		UnFrozenType unFrozenType = UnFrozenType.Level_1;
		if(rate.compareTo(UnFrozenType.Level_3.getRate())>=0){
			unFrozenRate = UnFrozenType.Level_3.getUnFrozenRate();
			unFrozenType = UnFrozenType.Level_3;
		}else if(rate.compareTo(UnFrozenType.Level_2.getRate())>=0){
			unFrozenRate = UnFrozenType.Level_2.getUnFrozenRate();
			unFrozenType = UnFrozenType.Level_2;
		}else if(rate.compareTo(UnFrozenType.Level_1.getRate())>=0){
			unFrozenRate = UnFrozenType.Level_1.getUnFrozenRate();
		}else {
			throw new BusinessException(CodeEnum.UnFrozenError);
		}

		List<UserTeamUnFrozen> orgUnFrozens = userTeamUnFrozenDao.findByUser(user);
		BigDecimal maxOrgUnFrozenRate = BigDecimal.ZERO;
		for (UserTeamUnFrozen orgUnFrozen : orgUnFrozens) {
			UnFrozenType orgUnFrozenType = UnFrozenType.fromValue(orgUnFrozen.getUnFrozenType());
			//已经解冻过
			if(orgUnFrozen.getUnFrozenType() == unFrozenType.getValue()){
				throw new BusinessException(CodeEnum.UnFrozenTwiceError);
			}
			if(orgUnFrozenType.getUnFrozenRate().compareTo(maxOrgUnFrozenRate)>0){
			    maxOrgUnFrozenRate = orgUnFrozenType.getUnFrozenRate();
            }
		}
        //实际要解冻的比例
        unFrozenRate = unFrozenRate.subtract(maxOrgUnFrozenRate);
		UserTeamUnFrozen userTeamUnFrozen = new UserTeamUnFrozen();
		userTeamUnFrozen.setCreateTime(now);
		userTeamUnFrozen.setUpdateTime(now);
		userTeamUnFrozen.setUser(user);
		userTeamUnFrozen.setUnFrozenType(unFrozenType.getValue());
		userTeamUnFrozenDao.save(userTeamUnFrozen);

//		BigDecimal unFrozenAsset = baseAsset.multiply(unFrozenRate).setScale(5,BigDecimal.ROUND_HALF_UP);
//		orgUnFrozenAsset = orgUnFrozenAsset.add(unFrozenAsset);
//		userTeam.setUnFrozenAsset(orgUnFrozenAsset);
        userTeam.setUnFrozenRate(unFrozenType.getUnFrozenRate());
		userTeamDao.save(userTeam);

		// 解冻团队所有用户的资产
		unFrozenAsset(user, unFrozenRate , now);
	}

	/**
	 * 解冻团队所有用户的资产
	 * @param user
	 * @param unFrozenRate
	 */
	@Transactional
	public void unFrozenAsset(User user, BigDecimal unFrozenRate ,Date now) {
		UserAsset userAsset = assetDao.findByUser(user);

		if(userAsset == null){
			logger.info("user {} 还没有资产 " , user.getMobilePhone());
			return;
		}
		if(userAsset.getFrozenAsset().compareTo(BigDecimal.ZERO) ==0){
			logger.info("user {} 所有资产都已经解冻 " , user.getMobilePhone());
			return;
		}

		UserAssetDetail assetDetail = new UserAssetDetail();
		assetDetail.setUser(user);
		assetDetail.setUpdateTime(now);
		assetDetail.setCreateTime(now);
		BigDecimal unFrozenAsset = userAsset.getInitFrozenAsset().multiply(unFrozenRate).setScale(Constants.SCALE,BigDecimal.ROUND_HALF_UP);
		if(unFrozenAsset.compareTo(userAsset.getFrozenAsset())>0){
			unFrozenAsset = userAsset.getFrozenAsset();
		}
		assetDetail.setMoney(unFrozenAsset);
		assetDetail.setProfit(BigDecimal.ZERO);
		assetDetail.setProfitExt(BigDecimal.ZERO);
		assetDetail.setTransType(TransType.UnFrozenAsset.getValue());
		assetDetail.setVersionType(appConfig.getVersion().getValue());
		assetDetail.setDigDate(now);
		addAsset(assetDetail);

		//需求变更，不解冻所有用户，只处理当前用户
//		List<UserTree> userTrees = userTreeDao.findByParent(user);
//		if (!CollectionUtils.isEmpty(userTrees)){
//			for (UserTree userTree : userTrees) {
//				unFrozenAsset(userTree.getChild(), unFrozenRate, now);
//			}
//		}
	}

	/**
	 *
	 * @param date
	 * @param unFrozenRate
	 */
	public void unFrozenUser(Date date, BigDecimal unFrozenRate) {
		List<UserAsset> userAssets = assetDao.findByFrozenAssetAfter(BigDecimal.ZERO);
		if(CollectionUtils.isEmpty(userAssets)){
			logger.info("没有需要处理解冻的数据");
			return ;
		}
		logger.info("需要处理解冻的数据:{}", userAssets.size());
		for (UserAsset userAsset : userAssets) {
			BigDecimal frozenAsset = userAsset.getFrozenAsset();
			User user = userAsset.getUser();
			List<UserAssetDetail> orgDetails = assetDetailDao.findByUserAndDigDate(user, date);
			boolean flag = false;
			for (UserAssetDetail orgDetail : orgDetails) {
				if(orgDetail.getTransType() == TransType.UnFrozenAsset.getValue()){
					logger.info("{}用户{}解冻数据已经存在", user.getMobilePhone(),DateFormatUtils.format(date,"yyyy-MM-dd"));
					flag = true;
					break;
				}
			}

			if(flag){
				continue;
			}
			UserAssetDetail assetDetail = new UserAssetDetail();
			assetDetail.setUser(user);
			assetDetail.setUpdateTime(date);
			assetDetail.setCreateTime(date);
			BigDecimal initFrozenAsset = userAsset.getInitFrozenAsset();
			if(initFrozenAsset.compareTo(BigDecimal.ZERO)<=0){
                initFrozenAsset = userAsset.getFrozenAsset();
                userAsset.setInitFrozenAsset(initFrozenAsset);
                assetDao.save(userAsset);
            }
			BigDecimal unFrozenAsset = initFrozenAsset.multiply(unFrozenRate).setScale(Constants.SCALE,BigDecimal.ROUND_HALF_UP);
			if(unFrozenAsset.compareTo(frozenAsset)>0){
				unFrozenAsset = frozenAsset;
			}
			assetDetail.setMoney(unFrozenAsset);
			assetDetail.setProfit(BigDecimal.ZERO);
			assetDetail.setProfitExt(BigDecimal.ZERO);
			assetDetail.setTransType(TransType.UnFrozenAsset.getValue());
			assetDetail.setVersionType(appConfig.getVersion().getValue());
			assetDetail.setDigDate(date);
			addAsset(assetDetail);
		}
	}

	/**
	 * 解冻团队奖励
	 * @param date
	 */
	public void unFrozenReward(Date date) {
		List<UserTeamReward> userTeamRewards = userTeamRewardDao.findByRemainFrozenAfter(BigDecimal.ZERO);
		if(CollectionUtils.isEmpty(userTeamRewards)){
			logger.info("没有需要处理解冻的数据");
			return ;
		}
		logger.info("需要处理解冻的数据:{}", userTeamRewards.size());
		for (UserTeamReward teamReward : userTeamRewards) {
			BigDecimal unlockPerDay = teamReward.getUnlockPerDay();
			User user = teamReward.getUser();
			List<UserAssetDetail> orgDetails = assetDetailDao.findByUserAndDigDate(user, date);
			boolean flag = false;
			for (UserAssetDetail orgDetail : orgDetails) {
				if(orgDetail.getTransType() == TransType.TeamReward.getValue()){
					logger.info("{}用户团队奖励{}解冻数据已经存在", user.getMobilePhone(),DateFormatUtils.format(date,"yyyy-MM-dd"));
					flag = true;
					break;
				}
			}

			if(flag){
				continue;
			}

			UserAssetDetail assetDetail = new UserAssetDetail();
			assetDetail.setUser(user);
			assetDetail.setUpdateTime(date);
			assetDetail.setCreateTime(date);

			BigDecimal orgRemainFrozen = teamReward.getRemainFrozen();
			BigDecimal remainFrozen = orgRemainFrozen.subtract(unlockPerDay);
			if(remainFrozen.compareTo(BigDecimal.ZERO)<0){
				unlockPerDay = orgRemainFrozen;
				remainFrozen = BigDecimal.ZERO;
			}

			assetDetail.setMoney(BigDecimal.ZERO);
			assetDetail.setProfit(unlockPerDay);
			assetDetail.setProfitExt(unlockPerDay);
			assetDetail.setTransType(TransType.TeamReward.getValue());
			assetDetail.setVersionType(appConfig.getVersion().getValue());
			assetDetail.setDigDate(date);
			addAsset(assetDetail);


			teamReward.setRemainFrozen(remainFrozen);
			teamReward.setUpdateTime(date);
			userTeamRewardDao.save(teamReward);
		}
	}


	/**
	 * 推荐人奖励
	 * @param date
	 */
	public void rewardTask(Date date) {
		List<UserReward> userRewards = userRewardDao.findByRemainFrozenAfter(BigDecimal.ZERO);
		if(CollectionUtils.isEmpty(userRewards)){
			logger.info("没有需要处理奖励的数据");
			return ;
		}
		logger.info("需要处理奖励的数据:{}", userRewards.size());
		for (UserReward userReward : userRewards) {
			if(userReward.getUpdateTime().after(PfcDateUtils.getDayStart(date))
					&& userReward.getUpdateTime().after(userReward.getCreateTime())){
				logger.info("{}的推荐人奖励已经处理过，不再处理", userReward.getRewardUser().getMobilePhone());
				continue;
			}
			BigDecimal unlockPerDay = userReward.getUnlockPerDay();
			User user = userReward.getUser();
			User rewardUser = userReward.getRewardUser();
			BigDecimal orgRemainFrozen = userReward.getRemainFrozen();
			BigDecimal remainFrozen = orgRemainFrozen.subtract(unlockPerDay);
			if(remainFrozen.compareTo(BigDecimal.ZERO)<0){
				unlockPerDay = orgRemainFrozen;
				remainFrozen = BigDecimal.ZERO;
			}
			if(remainFrozen.compareTo(Constants.GAP_LOCKPERDAY)<=0){
				remainFrozen = BigDecimal.ZERO;
			}

			List<UserRent> userRentList = userRentDao.findByUserAndRentStatus(user, RentStatus.Active.getValue());
			if(CollectionUtils.isEmpty(userRentList)){
				logger.info("{}用户没有矿机，不发放推荐人奖励", user.getMobilePhone());
				userReward.setRemainFrozen(remainFrozen);
				userReward.setUpdateTime(date);
				userRewardDao.save(userReward);
				continue;
			}

			List<UserAssetDetail> orgDetails = assetDetailDao.findByUserAndRewardUserAndDigDate(user,rewardUser, date);
			boolean flag = false;
			for (UserAssetDetail orgDetail : orgDetails) {
				if(orgDetail.getTransType() == TransType.Reward.getValue()){
					logger.info("{}用户推荐人奖励{}奖励数据已经存在", user.getMobilePhone(),DateFormatUtils.format(date,"yyyy-MM-dd"));
					flag = true;
					break;
				}
			}

			if(flag){
				continue;
			}

			UserAssetDetail assetDetail = new UserAssetDetail();
			assetDetail.setUser(user);
			assetDetail.setRewardUser(userReward.getRewardUser());
			assetDetail.setUpdateTime(date);
			assetDetail.setCreateTime(date);



			assetDetail.setMoney(BigDecimal.ZERO);
			assetDetail.setProfit(unlockPerDay);
			assetDetail.setProfitExt(unlockPerDay);
			assetDetail.setTransType(TransType.Reward.getValue());
			assetDetail.setVersionType(appConfig.getVersion().getValue());
			assetDetail.setDigDate(date);
			addAsset(assetDetail);


			userReward.setRemainFrozen(remainFrozen);
			userReward.setUpdateTime(date);
			userRewardDao.save(userReward);
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void transfer(TransferVO transferVO) throws BusinessException {
		User from = userMapper.findByMobilePhone(transferVO.getFromMobile());
		User to = userMapper.findByMobilePhone(transferVO.getToMobile());

		String salt = MD5EncryptUtil.md5Encrypy(transferVO.getFromMobile());
		String pswd = MD5EncryptUtil.md5Encrypy(transferVO.getPassword(), salt);
		if(!StringUtils.equals(pswd,from.getPassword())){
			throw new BusinessException(CodeEnum.TransPswdError);
		}


		UserAsset fromAsset = getUserAsset(from);
		if(fromAsset == null){
			throw new BusinessException(CodeEnum.TransMoneyError);
		}
		BigDecimal transMoney = transferVO.getTransMoney();
		BigDecimal remainMoney = fromAsset.getRemainAsset();
		BigDecimal frozenMoney = fromAsset.getFrozenAsset();
		if(appConfig.isOnTrans()){
			trans(transMoney,remainMoney,from,to, TransDetailType.RemainTrans.getValue());
		}else {
            boolean checkTrans = checkTrans(from, to.getId());
            boolean checkTrans2 = checkTrans(to, from.getId());
            if(!checkTrans && !checkTrans2){
                throw new BusinessException(CodeEnum.TransChainError);
            }
			trans(transMoney,frozenMoney,from,to, TransDetailType.FrozenTrans.getValue());
		}
	}

    public boolean checkTrans(User user, Long checkUserid) throws BusinessException {
        List<UserTree> userTreeList = userTreeDao.findByParent(user);
        for (UserTree userTree : userTreeList) {
            if(userTree.getChild().getId().equals(checkUserid)){
                return true;
            }
            if(checkTrans(userTree.getChild(), checkUserid)){
                return true;
            }
        }
        return false;
    }

	/**
	 *
	 * @param transMoney 转账金额
	 * @param money      余额
	 * @param from       用户from
	 * @param to         用户to
	 * @param transType
	 */
	private void trans(BigDecimal transMoney, BigDecimal money, User from, User to , Integer transType) throws BusinessException {
		if(transMoney.compareTo(money)>0){
			throw new BusinessException(CodeEnum.TransMoneyError);
		}
		Date now = new Date();
		TransDetail transDetail = new TransDetail();
		transDetail.setCreateTime(now);
		transDetail.setUpdateTime(now);
		transDetail.setFromUser(from);
		transDetail.setToUser(to);
		transDetail.setMoney(transMoney);
		transDetail.setTransDetailType(transType);
		transDetail.setTransStatus(TransStatus.Success.getValue());

		transDetailDao.save(transDetail);

		int fromTransType = 0;
		int toTransType = 0;
		if(transType == TransDetailType.FrozenTrans.getValue()){
			fromTransType = TransType.FrozenOut.getValue();
			toTransType = TransType.FrozenIn.getValue();
		}else{
			fromTransType = TransType.TransOut.getValue();
			toTransType = TransType.TransIn.getValue();
		}
		UserAssetDetail assetDetailFrom = getUserAssetDetail(transMoney, from, fromTransType, now);
		assetDetailFrom.setRewardUser(to);

		if(transType == TransDetailType.FrozenTrans.getValue()){
			assetDetailFrom.setFee(BigDecimal.ZERO);
		}else {
			assetDetailFrom.setFee(transMoney.multiply(Constants.FEE).setScale(Constants.SCALE, BigDecimal.ROUND_HALF_UP));
		}

		addAsset(assetDetailFrom);


		UserAssetDetail assetDetailTo = getUserAssetDetail(transMoney, to, toTransType, now);
		assetDetailTo.setRewardUser(from);
		assetDetailTo.setMoney(transMoney.subtract(assetDetailFrom.getFee()));
		addAsset(assetDetailTo);
	}

	private UserAssetDetail getUserAssetDetail(BigDecimal transMoney, User user, Integer transType, Date now) {
		UserAssetDetail assetDetail = new UserAssetDetail();
		assetDetail.setUser(user);
		assetDetail.setUpdateTime(now);
		assetDetail.setCreateTime(now);
		assetDetail.setMoney(transMoney);
		assetDetail.setProfit(BigDecimal.ZERO);
		assetDetail.setProfitExt(BigDecimal.ZERO);
		assetDetail.setTransType(transType);
		assetDetail.setVersionType(appConfig.getVersion().getValue());
		assetDetail.setDigDate(now);
		return assetDetail;
	}

	@Transactional
	public void calInitBaseAsset() {
		List<UserAsset> userAssets = assetDao.findAll();

		if(CollectionUtils.isEmpty(userAssets)){
			logger.info("no data");
			return;
		}
		Date now = new Date();
		for (UserAsset userAsset : userAssets) {
			InitBaseAssetVO initBaseAssetVO = getInitBaseAsset(userAsset.getUser());
			userAsset.setInitBaseAsset(initBaseAssetVO.getTotalAsset());
			userAsset.setInitFrozenAsset(userAsset.getFrozenAsset());
			userAsset.setUpdateTime(now);
			logger.info("{} init base asset ", userAsset.getUser().getMobilePhone());
			assetDao.save(userAsset);
		}
	}
}

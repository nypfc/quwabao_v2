package com.gedoumi.quwabao.user.service;

import com.gedoumi.quwabao.api.face.IDApiResponse;
import com.gedoumi.quwabao.asset.dao.UserAssetDao;
import com.gedoumi.quwabao.asset.entity.UserAsset;
import com.gedoumi.quwabao.asset.vo.UserInfoVO;
import com.gedoumi.quwabao.common.base.DataGrid;
import com.gedoumi.quwabao.common.base.PageParam;
import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.enums.UserStatus;
import com.gedoumi.quwabao.common.enums.UserValidateStatus;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.common.utils.CipherUtils;
import com.gedoumi.quwabao.common.utils.JsonUtil;
import com.gedoumi.quwabao.common.utils.NumberUtil;
import com.gedoumi.quwabao.sys.dao.SysSmsDao;
import com.gedoumi.quwabao.team.dataobj.model.UserTree;
import com.gedoumi.quwabao.user.mapper.UserImageDao;
import com.gedoumi.quwabao.user.mapper.UserMapper;
import com.gedoumi.quwabao.user.mapper.UserTreeDao;
import com.gedoumi.quwabao.user.dataobj.model.User;
import com.gedoumi.quwabao.user.dataobj.model.UserImage;
import com.gedoumi.quwabao.user.dataobj.vo.ValidateUserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * 类名：SysUserService
 * 功能：用户管理 业务层
 */
@Slf4j
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private SysSmsDao smsDao;

    @Resource
    private UserTreeDao userTreeDao;

    @Resource
    private UserImageDao userImageDao;

    @Resource
    private UserAssetDao assetDao;

    /**
     * 根据令牌获取用户
     *
     * @param token 令牌
     * @return 用户对象
     */
    public User getByToken(String token) {
        return Optional.ofNullable(userMapper.queryByToken(token)).orElseThrow(() -> {
            log.error("token:{}未查询到用户", token);
            return new BusinessException(CodeEnum.UnLogin);
        });
    }

    @Transactional
    public void addUser(User user) throws BusinessException {
        user.setInviteCode(CipherUtils.generateCode());
        while (true) {
            User orgUser = userMapper.findByInviteCode(user.getInviteCode());
            if (orgUser == null) {
                break;
            }
            user.setInviteCode(CipherUtils.generateCode());
        }
        userMapper.save(user);
        if (StringUtils.isEmpty(user.getUsername())) {
            int length = String.valueOf(user.getId()).length();
            length = length > 4 ? length : 4;
            String format = "%0" + length + "d";
            user.setUsername(User.PREFIX + NumberUtil.randomInt(0, 999) + String.format(format, user.getId()));
            userMapper.save(user);
        }


        smsDao.updateSmsStatus(user.getMobilePhone());

        bindRegInviteCode(user);

        Date now = new Date();
        UserAsset userAsset = new UserAsset();
        userAsset.setUpdateTime(now);
        userAsset.setCreateTime(now);
        userAsset.setInitFrozenAsset(BigDecimal.ZERO);
        userAsset.setProfit(BigDecimal.ZERO);
        userAsset.setRemainAsset(BigDecimal.ZERO);
        userAsset.setInitBaseAsset(BigDecimal.ZERO);
        userAsset.setFrozenAsset(BigDecimal.ZERO);
        userAsset.setTotalAsset(BigDecimal.ZERO);
        userAsset.setUser(user);

        assetDao.save(userAsset);
    }

    public List<User> getAll() {
        return userMapper.findAll();
    }

    /**
     * 好友列表，2级
     *
     * @param param
     * @param user
     * @return
     */
    @SuppressWarnings("unchecked")
    public DataGrid getList(PageParam param, User user) {
        DataGrid data = new DataGrid();
        String prefSql = " from UserTree t where t.parent.id " +
                "in (select u.child.id from UserTree u where u.parent.id=?) " +
                "or t.parent.id=? ";
        StringBuffer sqlCount = new StringBuffer("select count(t.id)  ").append(prefSql);
        StringBuffer sqlData = new StringBuffer("select t.child ").append(prefSql);


//        Query queryCount = entityManager.createQuery(sqlCount.toString());
//        queryCount.setParameter(1, user.getId());
//        queryCount.setParameter(2, user.getId());
//        Long count = Long.parseLong(queryCount.getResultList().get(0).toString());
//        Query queryData = entityManager.createQuery(sqlData.toString());
//        queryData.setParameter(1, user.getId());
//        queryData.setParameter(2, user.getId());
//        List<User> list = (List<User>) queryData.setFirstResult((param.getPage() - 1) * param.getRows()).setMaxResults(param.getRows()).getResultList();

//        data.setTotal(count);
//        data.setRows(list);
        return data;
    }


    /**
     * @param param
     * @param user
     * @return
     */
    public DataGrid getPageList(PageParam param, UserInfoVO user) {
        DataGrid data = new DataGrid();
        String prefSql = " from User t ";
        StringBuffer sqlCount = new StringBuffer("select count(t.id)  ").append(prefSql);
        StringBuffer sqlData = new StringBuffer("select t ").append(prefSql);

        sqlCount.append("where 1=1 ");
        sqlData.append("where 1=1 ");

        if (StringUtils.isNotEmpty(user.getMobilePhone())) {
            sqlCount.append("and t.mobilePhone like :phone ");
            sqlData.append("and t.mobilePhone like :phone ");
        }
//
//        Query queryCount = entityManager.createQuery(sqlCount.toString());
//        Query queryData = entityManager.createQuery(sqlData.toString());
//        if (StringUtils.isNotEmpty(user.getMobilePhone())) {
//            queryCount.setParameter("phone", "%" + user.getMobilePhone() + "%");
//            queryData.setParameter("phone", "%" + user.getMobilePhone() + "%");
//        }
//
//        Long count = Long.parseLong(queryCount.getResultList().get(0).toString());
//
//
//        List<User> list = (List<User>) queryData.setFirstResult((param.getPage() - 1) * param.getRows()).setMaxResults(param.getRows()).getResultList();
//
//        data.setTotal(count);
//        data.setRows(list);
        return data;
    }

    public User getById(Long id) {
        return userMapper.findById(id).get();
    }

    public User create(User user) {
        if (StringUtils.isEmpty(user.getPassword()))
            user.setPassword(new Md5Hash(User.PWD_INIT, user.getMobilePhone()).toString());
        User loginUser = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        return userMapper.save(user);
    }

    public User update(Long id, User user) {
        User oldUser = userMapper.findById(id).get();
        oldUser.setRealName(user.getRealName());
        oldUser.setMobilePhone(user.getMobilePhone());
        oldUser.setUserStatus(user.getUserStatus());
        User loginUser = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        oldUser.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        return userMapper.save(oldUser);
    }

    public User update(User user) {
        return userMapper.save(user);
    }

    public Boolean delete(Long id) {
        userMapper.deleteById(id);
        return true;
    }

    public User getByIdCard(String idCard) {
        User user = userMapper.findByIdCard(idCard);
        return user;
    }

    public User getByInviteCode(String inviteCode) {
        User user = userMapper.findByInviteCode(inviteCode);
        return user;
    }

    public User checkLoginUser(String username, UserStatus userStatus) {
        User user = userMapper.findByMobilePhoneAndUserStatus(username, userStatus.getValue());
        return user;
    }

    @Transactional
    public void updateLoginInfo(User user) {
        user.setErrorCount(Short.valueOf("0"));
        user.setUpdateTime(new Date());
        userMapper.save(user);
    }

    @Transactional
    public void updateLogout(User user) {
        user.setUpdateTime(new Date());
        user.setToken(UUID.randomUUID().toString());
        userMapper.save(user);
    }

    @Transactional
    public void updateLoginError(User user) {
        Short errorCount = user.getErrorCount();
        errorCount++;
        user.setErrorCount(errorCount);
        user.setErrorTime(new Date());
        userMapper.save(user);
    }

    public void checkRegInviteCode(User user, Long checkUserid) throws BusinessException {
        List<UserTree> userTreeList = userTreeDao.findByParent(user);
        for (UserTree userTree : userTreeList) {
            if (userTree.getChild().getId().equals(checkUserid)) {
                throw new BusinessException(CodeEnum.BindInviteCodeError);
            }
            checkRegInviteCode(userTree.getChild(), checkUserid);
        }

    }

    @Transactional
    public void bindRegInviteCode(User user) throws BusinessException {

        User parentUser = userMapper.findByInviteCode(user.getRegInviteCode());
        if (parentUser != null) {
            checkRegInviteCode(user, parentUser.getId());
        }


        userMapper.save(user);

        if (parentUser != null) {
            UserTree userTree = new UserTree();
            userTree.setChild(user);
            userTree.setParent(parentUser);
            userTreeDao.save(userTree);
        }


    }

    @Transactional
    public void validateUser(ValidateUserVO validateUserVO, IDApiResponse faceApiResponse) {
        if (faceApiResponse != null)
            log.info("validateUser faceApiResponse = {}", JsonUtil.objectToJson(faceApiResponse));
        Date now = new Date();
        Long userId = validateUserVO.getUserId();
        UserImage orgUserImage = userImageDao.findByUserId(userId);
        if (orgUserImage != null) {
            orgUserImage.setUserImage(StringUtils.EMPTY);
//			orgUserImage.setUserImage(validateUserVO.getBase64Img());
            orgUserImage.setUpdateTime(now);
            orgUserImage.setMessage(faceApiResponse.getData().getMessage());
            orgUserImage.setValidateCode(faceApiResponse.getData().getCode());
            userImageDao.save(orgUserImage);
        } else {
            UserImage userImage = new UserImage();
            userImage.setUserId(validateUserVO.getUserId());
//			userImage.setUserImage(validateUserVO.getBase64Img());
            userImage.setUserImage(StringUtils.EMPTY);
            userImage.setCreateTime(now);
            userImage.setUpdateTime(now);
            userImage.setValidateCode(faceApiResponse.getData().getCode());
            userImage.setScore(StringUtils.EMPTY);
            userImage.setMessage(faceApiResponse.getData().getMessage());
            userImageDao.save(userImage);
        }

        if (faceApiResponse != null) {
            User user = userMapper.findById(userId).get();
            user.setUpdateTime(now);
            user.setRealName(validateUserVO.getRealName());
            user.setIdCard(validateUserVO.getIdCard());
            if (faceApiResponse.isSucess()) {
                user.setValidateStatus(UserValidateStatus.Pass.getValue());
            } else {
                user.setValidateStatus(UserValidateStatus.UnPass.getValue());
            }

            userMapper.save(user);
        }

    }

    public UserImage getUserImg(Long userId) {
        return userImageDao.findByUserId(userId);
    }

}

package com.gedoumi.quwabao.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gedoumi.quwabao.user.dataobj.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper
 *
 * @author Minced
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}

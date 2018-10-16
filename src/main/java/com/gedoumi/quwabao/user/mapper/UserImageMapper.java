package com.gedoumi.quwabao.user.mapper;

import com.gedoumi.quwabao.user.dataobj.model.UserImage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserImageMapper {

	UserImage queryByUserId(Long userId);

}

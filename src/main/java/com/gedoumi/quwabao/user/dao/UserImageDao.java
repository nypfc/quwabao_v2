package com.gedoumi.quwabao.user.dao;

import com.gedoumi.quwabao.user.dataobj.entity.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserImageDao extends JpaRepository<UserImage, Long> {

	public UserImage findByUserId(Long userId);

}

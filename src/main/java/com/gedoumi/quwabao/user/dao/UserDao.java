package com.gedoumi.quwabao.user.dao;

import com.gedoumi.quwabao.user.dataobj.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserDao extends JpaRepository<User, Long> {
	 User findByUsername(String username);

	 User findByMobilePhone(String mobilePhone);

	 User findByToken(String token);

	 User findByInviteCode(String inviteCode);

	 User findByIdCard(String idCard);

	 User findByUsernameAndUserStatus(String username, Integer userStatus);

	 User findByMobilePhoneAndUserStatus(String mobile, Integer userStatus);
}

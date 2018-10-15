package com.gedoumi.quwabao.asset.dao;

import com.gedoumi.quwabao.asset.entity.UserRent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserRentDao extends JpaRepository<UserRent, Long> {

     List<UserRent> findByUser(User user);

     List<UserRent> findByUserAndRentStatus(User user, int rentStatus);

     List<UserRent> findByExpireDateAfter(Date expireDate);

     List<UserRent> findByExpireDateAfterAndRentStatus(Date expireDate, int rentStatus);

     List<UserRent> findByRentType(int rentType);
}

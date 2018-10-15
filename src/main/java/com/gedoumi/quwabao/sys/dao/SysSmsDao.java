package com.gedoumi.quwabao.sys.dao;

import com.gedoumi.quwabao.sys.entity.SysSms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SysSmsDao extends JpaRepository<SysSms, Long> {

    @Modifying
    @Query("update SysSms set smsStatus=0 where mobilePhone=?1 ")
    int updateSmsStatus(String mobile);

    List<SysSms> getByMobilePhoneAndCreateTimeAfter(String mobile, Date time);
}

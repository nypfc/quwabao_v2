package com.gedoumi.quwabao.asset.dao;

import com.gedoumi.quwabao.asset.entity.TransDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransDetailDao extends JpaRepository<TransDetail, Long> {


}

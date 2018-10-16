package com.gedoumi.quwabao.user.dataobj.model;

import lombok.Data;

import java.util.Date;

/**
 * 用户--关联表
 */
@Data
public class UserImage implements java.io.Serializable {

	private Long id;

	private Long userId;

	private String userImage;

	private String validateCode;

	private String message;

	private String score;

	private Date createTime;

	private Date updateTime;

}
package com.gedoumi.quwabao.user.dataobj.model;

import lombok.Data;

/**
 * 用户--关联表
 */
@Data
public class UserTree {

	private Long id;

	private User childId;

	private User parentId;
}
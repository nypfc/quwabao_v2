package com.gedoumi.quwabao.team.dataobj.model;

import com.gedoumi.quwabao.user.dataobj.model.User;

import javax.persistence.*;

/**
 * 用户--关联表
 */
@Entity
@Table(name = "user_tree")
public class UserTree implements java.io.Serializable {


	private static final long serialVersionUID = -8831739400791148440L;
	private Long id;

	private User parent;
	private User child;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "parent_id", nullable = false)
	public User getParent() {
		return parent;
	}

	public void setParent(User parent) {
		this.parent = parent;
	}

	@ManyToOne
	@JoinColumn(name = "child_id", nullable = false)
	public User getChild() {
		return child;
	}

	public void setChild(User child) {
		this.child = child;
	}
}
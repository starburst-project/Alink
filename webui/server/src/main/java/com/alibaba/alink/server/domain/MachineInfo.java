package com.alibaba.alink.server.domain;

import java.util.Date;

/**
 * 获取机器唯一标识
 */
public class MachineInfo {
	/**
	 * id
	 */
	private Long id;

	/**
	 * 机器信息
	 */
	private String ip;

	/**
	 * 创建时间
	 */
	private Date gmtCreate;

	/**
	 * 注释
	 */
	private String comments;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
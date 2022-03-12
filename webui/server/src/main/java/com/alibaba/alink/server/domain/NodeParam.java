package com.alibaba.alink.server.domain;

import java.util.Date;

/**
 * 节点参数信息
 */
public class NodeParam {
	/**
	 * 节点参数信息id
	 */
	private Long nodeParamId;

	/**
	 * 创建时间
	 */
	private Date gmtCreate;

	/**
	 * 修改时间
	 */
	private Date gmtModified;

	/**
	 * 实验id
	 */
	private Long experimentId;

	/**
	 * 节点id
	 */
	private Long nodeId;

	/**
	 * 参数key
	 */
	private String key;

	/**
	 * 参数值
	 */
	private String value;

	public Long getNodeParamId() {
		return nodeParamId;
	}

	public void setNodeParamId(Long nodeParamId) {
		this.nodeParamId = nodeParamId;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

	public Long getExperimentId() {
		return experimentId;
	}

	public void setExperimentId(Long experimentId) {
		this.experimentId = experimentId;
	}

	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
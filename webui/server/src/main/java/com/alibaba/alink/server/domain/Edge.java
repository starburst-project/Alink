package com.alibaba.alink.server.domain;

import java.util.Date;

/**
 * 实验边信息
 */
public class Edge {
	/**
	 * 边id
	 */
	private Long edgeId;

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
	 * 源组件id
	 */
	private Long srcNodeId;

	/**
	 * 源组件端口
	 */
	private Short srcNodePort;

	/**
	 * 目标组件id
	 */
	private Long dstNodeId;

	/**
	 * 目标组件端口
	 */
	private Short dstNodePort;

	public Long getEdgeId() {
		return edgeId;
	}

	public void setEdgeId(Long edgeId) {
		this.edgeId = edgeId;
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

	public Long getSrcNodeId() {
		return srcNodeId;
	}

	public void setSrcNodeId(Long srcNodeId) {
		this.srcNodeId = srcNodeId;
	}

	public Short getSrcNodePort() {
		return srcNodePort;
	}

	public void setSrcNodePort(Short srcNodePort) {
		this.srcNodePort = srcNodePort;
	}

	public Long getDstNodeId() {
		return dstNodeId;
	}

	public void setDstNodeId(Long dstNodeId) {
		this.dstNodeId = dstNodeId;
	}

	public Short getDstNodePort() {
		return dstNodePort;
	}

	public void setDstNodePort(Short dstNodePort) {
		this.dstNodePort = dstNodePort;
	}
}
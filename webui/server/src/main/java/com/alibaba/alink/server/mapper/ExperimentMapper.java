package com.alibaba.alink.server.mapper;

import com.alibaba.alink.server.domain.Experiment;

public interface ExperimentMapper {
	int deleteByPrimaryKey(Long experimentId);

	int insert(Experiment record);

	int insertSelective(Experiment record);

	Experiment selectByPrimaryKey(Long experimentId);

	int updateByPrimaryKeySelective(Experiment record);

	int updateByPrimaryKey(Experiment record);
}
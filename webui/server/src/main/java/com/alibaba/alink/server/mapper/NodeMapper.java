package com.alibaba.alink.server.mapper;
import com.alibaba.alink.server.domain.Node;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NodeMapper {
	int deleteByPrimaryKey(Long nodeId);

	int insert(Node record);

	int insertSelective(Node record);

	Node selectByPrimaryKey(Long nodeId);

	int updateByPrimaryKeySelective(Node record);

	int updateByPrimaryKey(Node record);

	List<Node> selectByExperimentId(@Param("experimentId")Long experimentId);


}
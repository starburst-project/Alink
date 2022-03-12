package com.alibaba.alink.server.mapper;

import com.alibaba.alink.server.domain.NodeParam;import org.apache.ibatis.annotations.Param;import java.util.List;

public interface NodeParamMapper {
	int deleteByPrimaryKey(Long nodeParamId);

	int insert(NodeParam record);

	int insertSelective(NodeParam record);

	NodeParam selectByPrimaryKey(Long nodeParamId);

	int updateByPrimaryKeySelective(NodeParam record);

	int updateByPrimaryKey(NodeParam record);

	List<NodeParam> selectByExperimentIdAndNodeId(
        @Param("experimentId") Long experimentId, @Param("nodeId") Long nodeId
    );

	List<NodeParam> selectByExperimentId(@Param("experimentId")Long experimentId);
}
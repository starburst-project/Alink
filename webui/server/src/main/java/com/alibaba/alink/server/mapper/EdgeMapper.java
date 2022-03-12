package com.alibaba.alink.server.mapper;
import java.util.List;
import com.alibaba.alink.server.domain.Edge;

import org.apache.ibatis.annotations.Param;

public interface EdgeMapper {
    int deleteByPrimaryKey(Long edgeId);

    int deleteByEdgeIdAndExperimentId(@Param("edgeId")Long edgeId,@Param("experimentId")Long experimentId);

    int insert(Edge record);

    int insertSelective(Edge record);

    Edge selectByPrimaryKey(Long edgeId);

    int updateByPrimaryKeySelective(Edge record);

    int updateByPrimaryKey(Edge record);

    List<Edge> selectByExperimentId(@Param("experimentId")Long experimentId);


}
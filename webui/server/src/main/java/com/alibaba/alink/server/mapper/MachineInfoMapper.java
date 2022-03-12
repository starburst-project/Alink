package com.alibaba.alink.server.mapper;

import com.alibaba.alink.server.domain.MachineInfo;

public interface MachineInfoMapper {
	int deleteByPrimaryKey(Long id);

	int insert(MachineInfo record);

	int insertSelective(MachineInfo record);

	MachineInfo selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(MachineInfo record);

	int updateByPrimaryKey(MachineInfo record);

}
package com.alibaba.alink.params.huge;

import org.apache.flink.ml.api.misc.param.ParamInfo;
import org.apache.flink.ml.api.misc.param.ParamInfoFactory;
import org.apache.flink.ml.api.misc.param.WithParams;

import com.alibaba.alink.common.annotation.DescCn;
import com.alibaba.alink.common.annotation.NameCn;

public interface HasNumCheckpoint<T> extends WithParams <T> {
	@NameCn("checkPoint 数目")
	@DescCn("checkPoint 数目")
	ParamInfo <Integer> NUM_CHECKPOINT = ParamInfoFactory
		.createParamInfo("numCheckpoint", Integer.class)
		.setDescription("The number of checkpoint")
		.setHasDefaultValue(1)
		.setAlias(new String[] {"checkPointSize"})
		.build();

	default Integer getNumCheckpoint() {
		return get(NUM_CHECKPOINT);
	}

	default T setNumCheckpoint(Integer value) {
		return set(NUM_CHECKPOINT, value);
	}
}

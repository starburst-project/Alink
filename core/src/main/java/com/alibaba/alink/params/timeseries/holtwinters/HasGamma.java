package com.alibaba.alink.params.timeseries.holtwinters;

import org.apache.flink.ml.api.misc.param.ParamInfo;
import org.apache.flink.ml.api.misc.param.ParamInfoFactory;
import org.apache.flink.ml.api.misc.param.WithParams;

import com.alibaba.alink.common.annotation.DescCn;
import com.alibaba.alink.common.annotation.NameCn;

public interface HasGamma<T> extends WithParams <T> {

	@NameCn("gamma")
	@DescCn("gamma")
	ParamInfo <Double> GAMMA = ParamInfoFactory
		.createParamInfo("gamma", Double.class)
		.setDescription("The gamma.")
		.setHasDefaultValue(0.1)
		.setValidator(HasAlpha.validator)
		.build();

	default Double getGamma() {
		return get(GAMMA);
	}

	default T setGamma(Double value) {
		return set(GAMMA, value);
	}
}

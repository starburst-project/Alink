package com.alibaba.alink.pipeline.feature;

import org.apache.flink.ml.api.misc.param.Params;

import com.alibaba.alink.common.annotation.NameCn;
import com.alibaba.alink.operator.common.feature.QuantileDiscretizerModelMapper;
import com.alibaba.alink.params.feature.QuantileDiscretizerPredictParams;
import com.alibaba.alink.pipeline.MapModel;

/**
 * EqualWidth discretizer keeps every interval the same width, output the interval
 * as model, and can transform a new data using the model.
 * <p>The output is the index of the interval.
 */
@NameCn("等宽离散化模型")
public class EqualWidthDiscretizerModel extends MapModel <EqualWidthDiscretizerModel>
	implements QuantileDiscretizerPredictParams <EqualWidthDiscretizerModel> {

	private static final long serialVersionUID = -2243218025358016965L;

	public EqualWidthDiscretizerModel() {
		this(null);
	}

	public EqualWidthDiscretizerModel(Params params) {
		super(QuantileDiscretizerModelMapper::new, params);
	}
}

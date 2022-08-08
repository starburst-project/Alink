package com.alibaba.alink.pipeline.image;

import org.apache.flink.ml.api.misc.param.Params;

import com.alibaba.alink.common.annotation.NameCn;
import com.alibaba.alink.operator.common.image.ReadImageToTensorMapper;
import com.alibaba.alink.params.image.ReadImageToTensorParams;
import com.alibaba.alink.pipeline.MapTransformer;

@NameCn("图片转张量")
public class ReadImageToTensor extends MapTransformer <ReadImageToTensor>
	implements ReadImageToTensorParams <ReadImageToTensor> {

	public ReadImageToTensor() {
		this(null);
	}

	public ReadImageToTensor(Params params) {
		super(ReadImageToTensorMapper::new, params);
	}
}

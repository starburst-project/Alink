package com.alibaba.alink.pipeline.classification;

import org.apache.flink.ml.api.misc.param.Params;

import com.alibaba.alink.common.annotation.Internal;
import com.alibaba.alink.common.annotation.NameCn;
import com.alibaba.alink.operator.common.fm.FmModelMapper;
import com.alibaba.alink.pipeline.MapModel;

/**
 * Fm pipeline model.
 */
@Internal
@NameCn("FM模型")
public class FmModel<T extends FmModel <T>> extends MapModel<T> {

	private static final long serialVersionUID = 8702278778833625190L;

	public FmModel() {this(null);}

	public FmModel(Params params) {
		super(FmModelMapper::new, params);
	}

}

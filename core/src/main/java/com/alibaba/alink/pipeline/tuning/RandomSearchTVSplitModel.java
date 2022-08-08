package com.alibaba.alink.pipeline.tuning;

import com.alibaba.alink.common.annotation.NameCn;
import com.alibaba.alink.pipeline.TransformerBase;

/**
 * model of random search tv split.
 */
@NameCn("随机搜索TV模型")
public class RandomSearchTVSplitModel extends BaseTuningModel <RandomSearchTVSplitModel> {

	private static final long serialVersionUID = -4026951109275667353L;

	public RandomSearchTVSplitModel(TransformerBase transformer) {
		super(transformer);
	}

}

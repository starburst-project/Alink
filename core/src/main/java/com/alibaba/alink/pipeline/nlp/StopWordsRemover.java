package com.alibaba.alink.pipeline.nlp;

import org.apache.flink.ml.api.misc.param.Params;

import com.alibaba.alink.common.annotation.NameCn;
import com.alibaba.alink.operator.common.nlp.StopWordsRemoverMapper;
import com.alibaba.alink.params.nlp.StopWordsRemoverParams;
import com.alibaba.alink.pipeline.MapTransformer;

/**
 * Filter stop words in a document.
 */
@NameCn("停用词过滤")
public class StopWordsRemover extends MapTransformer <StopWordsRemover>
	implements StopWordsRemoverParams <StopWordsRemover> {

	private static final long serialVersionUID = -8173193256794432304L;

	public StopWordsRemover() {
		this(null);
	}

	public StopWordsRemover(Params params) {
		super(StopWordsRemoverMapper::new, params);
	}
}

package com.alibaba.alink.pipeline.nlp;

import org.apache.flink.ml.api.misc.param.Params;

import com.alibaba.alink.common.annotation.NameCn;
import com.alibaba.alink.operator.common.nlp.DocCountVectorizerModelMapper;
import com.alibaba.alink.params.nlp.DocCountVectorizerPredictParams;
import com.alibaba.alink.pipeline.MapModel;

/**
 * DocCountVectorizerModel saves the document frequency, word count and inverse document
 * frequency of every word in the dataset.
 */
@NameCn("文本特征生成模型")
public class DocCountVectorizerModel extends MapModel <DocCountVectorizerModel>
	implements DocCountVectorizerPredictParams <DocCountVectorizerModel> {

	private static final long serialVersionUID = 2528993726788807192L;

	public DocCountVectorizerModel() {this(null);}

	public DocCountVectorizerModel(Params params) {
		super(DocCountVectorizerModelMapper::new, params);
	}

}

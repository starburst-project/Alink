package com.alibaba.alink.operator.common.similarity;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.ml.api.misc.param.Params;
import org.apache.flink.table.api.TableSchema;

import com.alibaba.alink.common.exceptions.AkIllegalDataException;
import com.alibaba.alink.common.mapper.MISOMapper;
import com.alibaba.alink.operator.common.similarity.similarity.Similarity;
import com.alibaba.alink.params.similarity.HasMetric;
import com.alibaba.alink.params.similarity.StringTextPairwiseParams;

public class TextSimilarityPairwiseMapper extends MISOMapper {
	private static final long serialVersionUID = -2281014249293525621L;
	private final Similarity similarity;
	private final HasMetric.Metric method;

	public TextSimilarityPairwiseMapper(TableSchema dataSchema, Params params) {
		super(dataSchema, params);
		this.similarity = StringSimilarityPairwiseMapper.createSimilarity(this.params);
		this.method = this.params.get(StringTextPairwiseParams.METRIC);
	}

	@Override
	protected TypeInformation<?> initOutputColType() {
		return Types.DOUBLE;
	}

	@Override
	protected Object map(Object[] input) {
		if (input.length != 2) {
			throw new AkIllegalDataException("PairWise only supports two input columns!");
		}
		String[] s1 = ((String) input[0]).split(" ", -1);
		String[] s2 = ((String) input[1]).split(" ", -1);
		return StringSimilarityPairwiseMapper.calc(s1, s2, method, similarity);
	}
}

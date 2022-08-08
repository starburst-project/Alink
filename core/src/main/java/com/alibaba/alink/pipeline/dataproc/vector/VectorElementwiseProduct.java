package com.alibaba.alink.pipeline.dataproc.vector;

import org.apache.flink.ml.api.misc.param.Params;

import com.alibaba.alink.common.annotation.NameCn;
import com.alibaba.alink.operator.common.dataproc.vector.VectorElementwiseProductMapper;
import com.alibaba.alink.params.dataproc.vector.VectorElementwiseProductParams;
import com.alibaba.alink.pipeline.MapTransformer;

/**
 * VectorEleWiseProduct multiplies each input vector by a provided “scaling” vector.
 * In other words, it scales each column of the data set by a scalar multiplier. This represents the Hadamard product
 * between the input vector, v and transforming vector, w, to yield a result vector.
 */
@NameCn("向量元素依次相乘")
public class VectorElementwiseProduct extends MapTransformer <VectorElementwiseProduct>
	implements VectorElementwiseProductParams <VectorElementwiseProduct> {

	private static final long serialVersionUID = -278858475661623918L;

	public VectorElementwiseProduct() {
		this(null);
	}

	public VectorElementwiseProduct(Params params) {
		super(VectorElementwiseProductMapper::new, params);
	}
}

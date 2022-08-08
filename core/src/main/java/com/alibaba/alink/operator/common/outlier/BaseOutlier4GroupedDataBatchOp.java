package com.alibaba.alink.operator.common.outlier;

import org.apache.flink.ml.api.misc.param.Params;
import org.apache.flink.table.api.TableSchema;

import com.alibaba.alink.common.annotation.Internal;
import com.alibaba.alink.common.mapper.Mapper;
import com.alibaba.alink.operator.batch.utils.MapBatchOp;
import com.alibaba.alink.params.outlier.Outlier4GroupedDataParams;

import java.util.function.BiFunction;

@Internal
public class BaseOutlier4GroupedDataBatchOp<T extends BaseOutlier4GroupedDataBatchOp <T>> extends MapBatchOp <T>
	implements Outlier4GroupedDataParams <T> {

	public BaseOutlier4GroupedDataBatchOp(BiFunction <TableSchema, Params, Mapper> mapperBuilder, Params params) {
		super(mapperBuilder, params);
	}

}
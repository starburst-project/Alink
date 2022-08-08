package com.alibaba.alink.operator.stream.outlier;

import org.apache.flink.ml.api.misc.param.Params;

import com.alibaba.alink.common.annotation.NameCn;
import com.alibaba.alink.common.annotation.NameEn;
import com.alibaba.alink.operator.common.outlier.BaseOutlier4GroupedDataStreamOp;
import com.alibaba.alink.operator.common.outlier.CopodDetector;
import com.alibaba.alink.params.outlier.CopodDetectorParams;
@NameCn("COP序列异常检测")
@NameEn("COP Series Outlier")
public class CopodOutlier4GroupedDataStreamOp extends BaseOutlier4GroupedDataStreamOp <CopodOutlier4GroupedDataStreamOp>
	implements CopodDetectorParams <CopodOutlier4GroupedDataStreamOp> {

	public CopodOutlier4GroupedDataStreamOp() {
		this(null);
	}

	public CopodOutlier4GroupedDataStreamOp(Params params) {
		super(CopodDetector::new, params);
	}
}


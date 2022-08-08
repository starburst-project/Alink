package com.alibaba.alink.operator.common.evaluation;

import org.apache.flink.ml.api.misc.param.ParamInfo;
import org.apache.flink.ml.api.misc.param.ParamInfoFactory;
import org.apache.flink.ml.api.misc.param.Params;
import org.apache.flink.types.Row;

import com.alibaba.alink.common.exceptions.AkPreconditions;
import com.alibaba.alink.operator.common.utils.PrettyDisplayUtils;
import org.apache.commons.lang.ArrayUtils;

/**
 * Multi classification evaluation metrics.
 */
public final class MultiClassMetrics extends BaseSimpleClassifierMetrics <MultiClassMetrics> {

	private static final long serialVersionUID = 6867711877593763404L;

	@Override
	public String toString() {
		StringBuilder sbd = new StringBuilder(PrettyDisplayUtils.displayHeadline("Metrics:", '-'));
		String[] labels = getLabelArray();

		Long[][] confusionMatrixPri = new Long[labels.length][labels.length];
		long[][] confusionMatrix = getConfusionMatrix();
		for (int i = 0; i < labels.length; i++) {
			for (int j = 0; j < labels.length; j++) {
				confusionMatrixPri[i][j] = confusionMatrix[i][j];
			}
		}

		sbd.append("Accuracy:").append(PrettyDisplayUtils.display(getAccuracy())).append("\t")
			.append("Macro F1:").append(PrettyDisplayUtils.display(getMacroF1())).append("\t")
			.append("Micro F1:").append(PrettyDisplayUtils.display(getMicroF1())).append("\t")
			.append("Kappa:").append(PrettyDisplayUtils.display(getKappa())).append("\t");
		if (getLogLoss() != null) {
			sbd.append("LogLoss:").append(PrettyDisplayUtils.display(getLogLoss()));
		}
		sbd.append("\n").append(PrettyDisplayUtils.displayTable(confusionMatrixPri,
			labels.length, labels.length, labels, labels, "Pred\\Real"));
		return sbd.toString();
	}

	static final ParamInfo <long[]> PREDICT_LABEL_FREQUENCY = ParamInfoFactory
		.createParamInfo("PredictLabelFrequency", long[].class)
		.setDescription("predict label frequency")
		.setRequired()
		.build();

	static final ParamInfo <double[]> PREDICT_LABEL_PROPORTION = ParamInfoFactory
		.createParamInfo("PredictLabelProportion", double[].class)
		.setDescription("predict label proportion")
		.setRequired()
		.build();

	public MultiClassMetrics(Row row) {
		super(row);
	}

	public MultiClassMetrics(Params params) {
		super(params);
	}

	public long[] getPredictLabelFrequency() {
		return get(PREDICT_LABEL_FREQUENCY);
	}

	public double[] getPredictLabelProportion() {
		return get(PREDICT_LABEL_PROPORTION);
	}

	public double getTruePositiveRate(String label) {
		return getParams().get(TRUE_POSITIVE_RATE_ARRAY)[getLabelIndex(label)];
	}

	public double getTrueNegativeRate(String label) {
		return getParams().get(TRUE_NEGATIVE_RATE_ARRAY)[getLabelIndex(label)];
	}

	public double getFalsePositiveRate(String label) {
		return getParams().get(FALSE_POSITIVE_RATE_ARRAY)[getLabelIndex(label)];
	}

	public double getFalseNegativeRate(String label) {
		return getParams().get(FALSE_NEGATIVE_RATE_ARRAY)[getLabelIndex(label)];
	}

	public double getPrecision(String label) {
		return getParams().get(PRECISION_ARRAY)[getLabelIndex(label)];
	}

	public double getSpecificity(String label) {
		return getParams().get(SPECIFICITY_ARRAY)[getLabelIndex(label)];
	}

	public double getSensitivity(String label) {
		return getParams().get(SENSITIVITY_ARRAY)[getLabelIndex(label)];
	}

	public double getRecall(String label) {
		return getParams().get(RECALL_ARRAY)[getLabelIndex(label)];
	}

	public double getF1(String label) {
		return getParams().get(F1_ARRAY)[getLabelIndex(label)];
	}

	public double getAccuracy(String label) {
		return getParams().get(ACCURACY_ARRAY)[getLabelIndex(label)];
	}

	public double getKappa(String label) {
		return getParams().get(KAPPA_ARRAY)[getLabelIndex(label)];
	}

	private int getLabelIndex(String label) {
		int index = ArrayUtils.indexOf(getParams().get(LABEL_ARRAY), label);
		AkPreconditions.checkArgument(index >= 0, String.format("Not exist label %s", label));
		return index;
	}
}

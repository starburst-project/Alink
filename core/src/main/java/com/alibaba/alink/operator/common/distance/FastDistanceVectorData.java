package com.alibaba.alink.operator.common.distance;

import org.apache.flink.ml.api.misc.param.Params;
import org.apache.flink.types.Row;

import com.alibaba.alink.common.exceptions.AkPreconditions;
import com.alibaba.alink.common.linalg.DenseVector;
import com.alibaba.alink.common.linalg.SparseVector;
import com.alibaba.alink.common.linalg.Vector;
import com.alibaba.alink.common.linalg.VectorUtil;

/**
 * Save the data for calculating distance fast. The FastDistanceMatrixData
 */
public class FastDistanceVectorData extends FastDistanceData {
	private static final long serialVersionUID = 2149044894420822785L;
	/**
	 * Stores the vector(sparse or dense).
	 */
	final Vector vector;

	/**
	 * Stores some extra info extracted from the vector. For example, if we want to save the L1 norm and L2 norm of the
	 * vector, then the two values are viewed as a two-dimension label vector.
	 */
	DenseVector label;

	/**
	 * Constructor, initialize the vector data.
	 *
	 * @param vec vector.
	 */
	public FastDistanceVectorData(Vector vec) {
		this(vec, null);
	}

	/**
	 * Constructor, initialize the vector data and extra info.
	 *
	 * @param vec vector.
	 * @param row extra info besides the vector.
	 */
	public FastDistanceVectorData(Vector vec, Row row) {
		super(null == row ? null : new Row[] {row});
		AkPreconditions.checkNotNull(vec, "Vector should not be null!");
		this.vector = vec;
	}

	public FastDistanceVectorData(FastDistanceVectorData vectorData) {
		super(vectorData);
		if (vectorData.vector instanceof SparseVector) {
			this.vector = ((SparseVector) vectorData.vector).clone();
		} else {
			this.vector = ((DenseVector) vectorData.vector).clone();
		}
		this.label = (null == vectorData.label ? null : vectorData.label.clone());
	}

	public Vector getVector() {
		return vector;
	}

	public DenseVector getLabel() {
		return label;
	}

	@Override
	public String toString() {
		Params params = new Params();
		params.set("vector", VectorUtil.serialize(vector));
		params.set("label", label);
		if (rows != null) {
			params.set("rows", rows[0]);
		}
		return params.toJson();
	}

	public static FastDistanceVectorData fromString(String s) {
		Params params = Params.fromJson(s);
		Row row = params.getOrDefault("rows", Row.class, null);
		String vector = params.get("vector", String.class);
		DenseVector label = params.get("label", DenseVector.class);
		FastDistanceVectorData vectorData = new FastDistanceVectorData(VectorUtil.getVector(vector), row);
		vectorData.label = label;
		return vectorData;
	}
}

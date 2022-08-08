package com.alibaba.alink.common.pyrunner;

import org.apache.flink.types.Row;

import com.alibaba.alink.common.io.plugin.ResourcePluginFactory;
import com.alibaba.alink.common.utils.Functional.SerializableBiFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A runner which calls Python code to do calculation, the inputs and outputs are both a list of rows.
 *
 * @param <HANDLE> Python object handle type
 */
public class PyMIMOCalcRunner<HANDLE extends PyMIMOCalcHandle> extends PyCalcRunner <List <Row>, List <Row>, HANDLE> {

	private static final Logger LOG = LoggerFactory.getLogger(PyMIMOCalcRunner.class);

	public PyMIMOCalcRunner(String pythonClassName, SerializableBiFunction <String, String, String> getConfigFn) {
		this(pythonClassName, getConfigFn, new ResourcePluginFactory());
	}

	public PyMIMOCalcRunner(String pythonClassName, SerializableBiFunction <String, String, String> getConfigFn,
							ResourcePluginFactory factory) {
		super(pythonClassName, getConfigFn, factory);
	}

	static Object[] rowToObjectArray(Row row) {
		Object[] arr = new Object[row.getArity()];
		for (int i = 0; i < row.getArity(); i += 1) {
			arr[i] = row.getField(i);
		}
		return arr;
	}

	@Override
	public List <Row> calc(List <Row> in) {
		LOG.info("Entering PyMIMOCalcRunner.calc");
		PyListRowOutputCollector collector = new PyListRowOutputCollector();
		Object[][] inputs = in.stream().map(PyMIMOCalcRunner::rowToObjectArray).toArray(Object[][]::new);
		handle.setCollector(collector);
		LOG.info("Just before handle.calc");
		handle.calc(inputs);
		LOG.info("Just after handle.calc");
		LOG.info("Leaving PyMIMOCalcRunner.calc");
		return collector.getRows();
	}

	public List <Row> calc(Map <String, String> conf, List <Row> in1, List <Row> in2) {
		LOG.info("Entering PyMIMOCalcRunner.calc v2");
		PyListRowOutputCollector collector = new PyListRowOutputCollector();
		Object[][] inputs1 = in1.stream().map(PyMIMOCalcRunner::rowToObjectArray).toArray(Object[][]::new);
		Object[][] inputs2 = null;
		if (in2 != null) {
			inputs2 = in2.stream().map(PyMIMOCalcRunner::rowToObjectArray).toArray(Object[][]::new);
		}
		handle.setCollector(collector);
		LOG.info("Just before handle.calc");
		handle.calc(conf, inputs1, inputs2);
		LOG.info("Just after handle.calc");
		LOG.info("Leaving PyMIMOCalcRunner.calc v2");
		return collector.getRows();
	}

	/**
	 * Collect values from Python side as rows.
	 */
	public static class PyListRowOutputCollector {
		private final List <Row> rows = new ArrayList <>();

		public void collectRow(Object v0) {
			rows.add(Row.of(v0));
		}

		public void collectRow(Object v0, Object v1) {
			rows.add(Row.of(v0, v1));
		}

		public void collectRow(Object v0, Object v1, Object v2) {
			rows.add(Row.of(v0, v1, v2));
		}

		public List <Row> getRows() {
			return rows;
		}
	}
}

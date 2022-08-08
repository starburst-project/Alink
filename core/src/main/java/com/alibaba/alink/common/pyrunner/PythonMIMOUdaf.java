package com.alibaba.alink.common.pyrunner;

import org.apache.flink.table.functions.AggregateFunction;
import org.apache.flink.types.Row;

import com.alibaba.alink.common.io.plugin.ResourcePluginFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PythonMIMOUdaf<HANDLER extends PyMIMOCalcHandle>
	extends AggregateFunction <List <Row>, PythonMIMOUdaf.ItemAccumulator> {

	protected static class ItemAccumulator {
		public List <Row> items = new ArrayList <>();
	}

	private final String pythonClassName;
	private final Map <String, String> config;
	private final ResourcePluginFactory factory;

	public PythonMIMOUdaf(String pythonClassName, Map <String, String> config) {
		this.pythonClassName = pythonClassName;
		this.config = config;
		this.factory = new ResourcePluginFactory();
	}

	@Override
	public List <Row> getValue(ItemAccumulator acc) {
		PyMIMOCalcRunner <HANDLER> runner = new PyMIMOCalcRunner <>(pythonClassName, config::getOrDefault, factory);
		List <Row> inputs = acc.items;
		runner.open();
		List <Row> outputs = runner.calc(inputs);
		runner.close();
		return outputs;
	}

	@Override
	public ItemAccumulator createAccumulator() {
		return new ItemAccumulator();
	}

	public void accumulate(ItemAccumulator acc, Object... values) {
		acc.items.add(Row.of(values));
	}

	public void resetAccumulator(ItemAccumulator acc) {
		acc.items.clear();
	}
}

package com.alibaba.alink.operator.stream.utils;

import org.apache.flink.ml.api.misc.param.Params;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.types.Row;

import com.alibaba.alink.common.annotation.InputPorts;
import com.alibaba.alink.common.annotation.Internal;
import com.alibaba.alink.common.annotation.OutputPorts;
import com.alibaba.alink.common.annotation.PortDesc;
import com.alibaba.alink.common.annotation.PortSpec;
import com.alibaba.alink.common.annotation.PortType;
import com.alibaba.alink.common.annotation.ReservedColsWithFirstInputSpec;
import com.alibaba.alink.common.mapper.FlatMapper;
import com.alibaba.alink.common.mapper.FlatMapperAdapter;
import com.alibaba.alink.common.mapper.FlatMapperAdapterMT;
import com.alibaba.alink.operator.stream.StreamOperator;
import com.alibaba.alink.params.mapper.MapperParams;

import java.util.function.BiFunction;

/**
 * class for a flat map {@link StreamOperator}.
 *
 * @param <T> class type of the {@link FlatMapStreamOp} implementation itself.
 */
@InputPorts(values = {@PortSpec(PortType.DATA)})
@OutputPorts(values = {@PortSpec(value = PortType.DATA, desc = PortDesc.OUTPUT_RESULT)})
@ReservedColsWithFirstInputSpec
@Internal
public class FlatMapStreamOp<T extends FlatMapStreamOp <T>> extends StreamOperator <T> {

	private static final long serialVersionUID = -4636065442883889709L;
	private final BiFunction <TableSchema, Params, FlatMapper> mapperBuilder;

	public FlatMapStreamOp(BiFunction <TableSchema, Params, FlatMapper> mapperBuilder, Params params) {
		super(params);
		this.mapperBuilder = mapperBuilder;
	}

	@Override
	public T linkFrom(StreamOperator <?>... inputs) {
		StreamOperator <?> in = checkAndGetFirst(inputs);

		try {
			FlatMapper flatMapper = this.mapperBuilder.apply(in.getSchema(), this.getParams());
			DataStream <Row> resultRows = calcResultRows(in, flatMapper, getParams());
			this.setOutput(resultRows, flatMapper.getOutputSchema());
			//noinspection unchecked
			return (T) this;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static DataStream <Row> calcResultRows(StreamOperator <?> in, FlatMapper flatMapper, Params params) {
		if (params.get(MapperParams.NUM_THREADS) <= 1) {
			return in.getDataStream().flatMap(new FlatMapperAdapter(flatMapper));
		} else {
			return in.getDataStream().flatMap(
				new FlatMapperAdapterMT(flatMapper, params.get(MapperParams.NUM_THREADS))
			);
		}
	}
}

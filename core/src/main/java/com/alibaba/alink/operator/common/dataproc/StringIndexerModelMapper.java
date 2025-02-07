package com.alibaba.alink.operator.common.dataproc;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.ml.api.misc.param.Params;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.types.Row;

import com.alibaba.alink.common.exceptions.AkIllegalDataException;
import com.alibaba.alink.common.mapper.SISOModelMapper;
import com.alibaba.alink.params.dataproc.HasHandleInvalid;
import com.alibaba.alink.params.dataproc.StringIndexerPredictParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The model mapper for {@link com.alibaba.alink.pipeline.dataproc.StringIndexer}.
 */
public class StringIndexerModelMapper extends SISOModelMapper {

	private static final long serialVersionUID = 7357880400055968982L;
	private Map <String, Long> mapper;
	private Long defaultIndex;
	private HasHandleInvalid.HandleInvalid handleInvalidStrategy;

	public StringIndexerModelMapper(TableSchema modelSchema, TableSchema dataSchema, Params params) {
		super(modelSchema, dataSchema, params);
		handleInvalidStrategy = params.get(StringIndexerPredictParams.HANDLE_INVALID);
	}

	@Override
	protected TypeInformation initPredResultColType() {
		return Types.LONG;
	}

	@Override
	protected Object predictResult(Object input) throws Exception {
		String key = input == null ? null : String.valueOf(input);
		Long index = mapper.get(key);
		if (index != null) {
			return index;
		} else {
			switch (this.handleInvalidStrategy) {
				case KEEP:
					return defaultIndex;
				case SKIP:
					return null;
				case ERROR:
					throw new AkIllegalDataException("Unseen token: " + key);
				default:
					throw new IllegalArgumentException("Unknown strategy: " + handleInvalidStrategy);
			}
		}
	}

	@Override
	public void loadModel(List <Row> modelRows) {
		List <Tuple2 <String, Long>> model = new StringIndexerModelDataConverter().load(modelRows);
		this.mapper = new HashMap <>();
		long maxIndex = -1L;
		for (Tuple2 <String, Long> record : model) {
			maxIndex = Math.max(maxIndex, record.f1);
			this.mapper.put(record.f0, record.f1);
		}
		this.defaultIndex = maxIndex + 1L;
	}

}

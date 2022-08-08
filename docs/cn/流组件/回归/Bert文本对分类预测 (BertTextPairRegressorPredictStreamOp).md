# Bert文本对分类预测 (BertTextPairRegressorPredictStreamOp)
Java 类名：com.alibaba.alink.operator.stream.regression.BertTextPairRegressorPredictStreamOp

Python 类名：BertTextPairRegressorPredictStreamOp


## 功能介绍

与 BERT 文本对回归训练组件对应的预测组件。


## 参数说明

| 名称 | 中文名称 | 描述 | 类型 | 是否必须？ | 取值范围 | 默认值 |
| --- | --- | --- | --- | --- | --- | --- |
| predictionCol | 预测结果列名 | 预测结果列名 | String | ✓ |  |  |
| inferBatchSize | 推理数据批大小 | 推理数据批大小 | Integer |  |  | 256 |
| modelFilePath | 模型的文件路径 | 模型的文件路径 | String |  |  | null |
| reservedCols | 算法保留列名 | 算法保留列 | String[] |  |  | null |
| modelStreamFilePath | 模型流的文件路径 | 模型流的文件路径 | String |  |  | null |
| modelStreamScanInterval | 扫描模型路径的时间间隔 | 描模型路径的时间间隔，单位秒 | Integer |  |  | 10 |
| modelStreamStartTime | 模型流的起始时间 | 模型流的起始时间。默认从当前时刻开始读。使用yyyy-mm-dd hh:mm:ss.fffffffff格式，详见Timestamp.valueOf(String s) | String |  |  | null |

## 代码示例

** 以下代码仅用于示意，可能需要修改部分代码或者配置环境后才能正常运行！**

### Python 代码
```python
# If OOM encountered, uncomment the following line and/or use a smaller parallelism
# get_java_class("System").setProperty("direct.reader.policy", "local_file")

url = "http://alink-algo-packages.oss-cn-hangzhou-zmf.aliyuncs.com/data/MRPC/train.tsv"
schemaStr = "f_quality double, f_id_1 string, f_id_2 string, f_string_1 string, f_string_2 string"
data = CsvSourceStreamOp() \
    .setFilePath(url) \
    .setSchemaStr(schemaStr) \
    .setFieldDelimiter("\t") \
    .setIgnoreFirstLine(True) \
    .setQuoteChar(None)
model = CsvSourceBatchOp() \
    .setFilePath("http://alink-test.oss-cn-beijing.aliyuncs.com/jiqi-temp/tf_ut_files/bert_text_pair_regressor_model.csv") \
    .setSchemaStr("model_id bigint, model_info string, label_value double")
predict = BertTextPairRegressorPredictStreamOp(model) \
    .setPredictionCol("pred") \
    .setReservedCols(["f_quality"]) \
    .linkFrom(data)
predict.print()
StreamOperator.execute()
```

### Java 代码
```java
import com.alibaba.alink.common.io.directreader.DataBridgeGeneratorPolicy;
import com.alibaba.alink.common.io.directreader.LocalFileDataBridgeGenerator;
import com.alibaba.alink.operator.batch.BatchOperator;
import com.alibaba.alink.operator.batch.source.CsvSourceBatchOp;
import com.alibaba.alink.operator.stream.StreamOperator;
import com.alibaba.alink.operator.stream.regression.BertTextPairRegressorPredictStreamOp;
import com.alibaba.alink.operator.stream.source.CsvSourceStreamOp;
import org.junit.Test;

public class BertTextPairRegressorPredictStreamOpTest {
	@Test
	public void test() throws Exception {
		StreamOperator.setParallelism(2);	// a larger parallelism needs much more memory

		System.setProperty("direct.reader.policy",
			LocalFileDataBridgeGenerator.class.getAnnotation(DataBridgeGeneratorPolicy.class).policy());
		String url = "http://alink-algo-packages.oss-cn-hangzhou-zmf.aliyuncs.com/data/MRPC/train.tsv";
		String schemaStr = "f_quality double, f_id_1 string, f_id_2 string, f_string_1 string, f_string_2 string";
		StreamOperator <?> data = new CsvSourceStreamOp()
			.setFilePath(url)
			.setSchemaStr(schemaStr)
			.setFieldDelimiter("\t")
			.setIgnoreFirstLine(true)
			.setQuoteChar(null);
		BatchOperator <?> model = new CsvSourceBatchOp()
			.setFilePath("http://alink-test.oss-cn-beijing.aliyuncs.com/jiqi-temp/tf_ut_files/bert_text_pair_regressor_model.csv")
			.setSchemaStr("model_id bigint, model_info string, label_value double");
		BertTextPairRegressorPredictStreamOp predict = new BertTextPairRegressorPredictStreamOp(model)
			.setPredictionCol("pred")
			.setReservedCols("f_quality")
			.linkFrom(data);
		predict.print();
		StreamOperator.execute();
	}
}
```

### 运行结果

f_quality|pred
---------|----
0.0000|1.4043
1.0000|1.4043
0.0000|1.4038
0.0000|1.4044
1.0000|1.4046
...|...

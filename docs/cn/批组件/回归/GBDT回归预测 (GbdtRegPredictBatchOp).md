# GBDT回归预测 (GbdtRegPredictBatchOp)
Java 类名：com.alibaba.alink.operator.batch.regression.GbdtRegPredictBatchOp

Python 类名：GbdtRegPredictBatchOp


## 功能介绍

- gbdt(Gradient Boosting Decision Trees)回归，是经典的基于boosting的有监督学习模型，可以用来解决回归问题

- 支持连续特征和离散特征

- 支持数据采样和特征采样

## 参数说明


| 名称 | 中文名称 | 描述 | 类型 | 是否必须？ | 取值范围 | 默认值 |
| --- | --- | --- | --- | --- | --- | --- |
| predictionCol | 预测结果列名 | 预测结果列名 | String | ✓ |  |  |
| modelFilePath | 模型的文件路径 | 模型的文件路径 | String |  |  | null |
| reservedCols | 算法保留列名 | 算法保留列 | String[] |  |  | null |
| vectorCol | 向量列名 | 向量列对应的列名，默认值是null | String |  | 所选列类型为 [DENSE_VECTOR, SPARSE_VECTOR, STRING, VECTOR] | null |
| numThreads | 组件多线程线程个数 | 组件多线程线程个数 | Integer |  |  | 1 |



## 代码示例
### Python 代码
```python
from pyalink.alink import *

import pandas as pd

useLocalEnv(1)

df = pd.DataFrame([
    [1.0, "A", 0, 0, 0],
    [2.0, "B", 1, 1, 0],
    [3.0, "C", 2, 2, 1],
    [4.0, "D", 3, 3, 1]
])
batchSource = BatchOperator.fromDataframe(
    df, schemaStr='f0 double, f1 string, f2 int, f3 int, label int')
streamSource = StreamOperator.fromDataframe(
    df, schemaStr='f0 double, f1 string, f2 int, f3 int, label int')

trainOp = GbdtRegTrainBatchOp()\
    .setLearningRate(1.0)\
    .setNumTrees(3)\
    .setMinSamplesPerLeaf(1)\
    .setLabelCol('label')\
    .setFeatureCols(['f0', 'f1', 'f2', 'f3'])\
    .linkFrom(batchSource)
predictBatchOp = GbdtRegPredictBatchOp()\
    .setPredictionCol('pred')
predictStreamOp = GbdtRegPredictStreamOp(trainOp)\
    .setPredictionCol('pred')

predictBatchOp.linkFrom(trainOp, batchSource).print()
predictStreamOp.linkFrom(streamSource).print()

StreamOperator.execute()
```
### Java 代码
```java
import org.apache.flink.types.Row;

import com.alibaba.alink.operator.batch.BatchOperator;
import com.alibaba.alink.operator.batch.regression.GbdtRegPredictBatchOp;
import com.alibaba.alink.operator.batch.regression.GbdtRegTrainBatchOp;
import com.alibaba.alink.operator.batch.source.MemSourceBatchOp;
import com.alibaba.alink.operator.stream.StreamOperator;
import com.alibaba.alink.operator.stream.regression.GbdtRegPredictStreamOp;
import com.alibaba.alink.operator.stream.source.MemSourceStreamOp;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class GbdtRegPredictBatchOpTest {
	@Test
	public void testGbdtRegPredictBatchOp() throws Exception {
		List <Row> df = Arrays.asList(
			Row.of(1.0, "A", 0, 0, 0),
			Row.of(2.0, "B", 1, 1, 0),
			Row.of(3.0, "C", 2, 2, 1),
			Row.of(4.0, "D", 3, 3, 1)
		);
		BatchOperator <?> batchSource = new MemSourceBatchOp(
			df, "f0 double, f1 string, f2 int, f3 int, label int");
		StreamOperator <?> streamSource = new MemSourceStreamOp(
			df, "f0 double, f1 string, f2 int, f3 int, label int");
		BatchOperator <?> trainOp = new GbdtRegTrainBatchOp()
			.setLearningRate(1.0)
			.setNumTrees(3)
			.setMinSamplesPerLeaf(1)
			.setLabelCol("label")
			.setFeatureCols("f0", "f1", "f2", "f3")
			.linkFrom(batchSource);
		BatchOperator <?> predictBatchOp = new GbdtRegPredictBatchOp()
			.setPredictionCol("pred");
		StreamOperator <?> predictStreamOp = new GbdtRegPredictStreamOp(trainOp)
			.setPredictionCol("pred");
		predictBatchOp.linkFrom(trainOp, batchSource).print();
		predictStreamOp.linkFrom(streamSource).print();
		StreamOperator.execute();
	}
}
```
### 运行结果

#### 批预测结果

f0|f1|f2|f3|label|pred
---|---|---|---|-----|----
1.0000|A|0|0|0|0.0000
2.0000|B|1|1|0|0.0000
3.0000|C|2|2|1|1.0000
4.0000|D|3|3|1|1.0000

#### 流预测结果

f0|f1|f2|f3|label|pred
---|---|---|---|-----|----
1.0000|A|0|0|0|0.0000
4.0000|D|3|3|1|1.0000
3.0000|C|2|2|1|1.0000
2.0000|B|1|1|0|0.0000

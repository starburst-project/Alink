# 字符串近似最近邻预测 (StringApproxNearestNeighborPredictBatchOp)
Java 类名：com.alibaba.alink.operator.batch.similarity.StringApproxNearestNeighborPredictBatchOp

Python 类名：StringApproxNearestNeighborPredictBatchOp


## 功能介绍

该功能由训练和预测组成，支持计算1. 求最近邻topN 2. 求radius范围内的邻居。该功能由预测时候的topN和radius参数控制, 如果填写了topN，则输出最近邻，如果填写了radius，则输出radius范围内的邻居。

SimhashHamming（SimHash_Hamming_Distance)相似度=1-距离/64.0，应选择metric的参数为SIMHASH_HAMMING_SIM。

MinHash应选择metric的参数为MINHASH_SIM。

Jaccard应选择metric的参数为JACCARD_SIM。

## 参数说明
| 名称 | 中文名称 | 描述 | 类型 | 是否必须？ | 取值范围 | 默认值 |
| --- | --- | --- | --- | --- | --- | --- |
| selectedCol | 选中的列名 | 计算列对应的列名 | String | ✓ |  |  |
| modelFilePath | 模型的文件路径 | 模型的文件路径 | String |  |  | null |
| outputCol | 输出结果列 | 输出结果列列名，可选，默认null | String |  |  | null |
| radius | radius值 | radius值 | Double |  |  | null |
| reservedCols | 算法保留列名 | 算法保留列 | String[] |  |  | null |
| topN | TopN的值 | TopN的值 | Integer |  | [1, +inf) | null |
| numThreads | 组件多线程线程个数 | 组件多线程线程个数 | Integer |  |  | 1 |



## 代码示例
### Python 代码
```python
from pyalink.alink import *

import pandas as pd

useLocalEnv(1)

df = pd.DataFrame([
    [0, "abcde", "aabce"],
    [1, "aacedw", "aabbed"],
    [2, "cdefa", "bbcefa"],
    [3, "bdefh", "ddeac"],
    [4, "acedm", "aeefbc"]
])

inOp = BatchOperator.fromDataframe(df, schemaStr='id long, text1 string, text2 string')

train = StringApproxNearestNeighborTrainBatchOp().setIdCol("id").setSelectedCol("text1").setMetric("SIMHASH_HAMMING_SIM").linkFrom(inOp)
predict = StringApproxNearestNeighborPredictBatchOp().setSelectedCol("text2").setTopN(3).linkFrom(train, inOp)
predict.print()
```
### Java 代码
```java
import org.apache.flink.types.Row;

import com.alibaba.alink.operator.batch.BatchOperator;
import com.alibaba.alink.operator.batch.similarity.StringApproxNearestNeighborPredictBatchOp;
import com.alibaba.alink.operator.batch.similarity.StringApproxNearestNeighborTrainBatchOp;
import com.alibaba.alink.operator.batch.source.MemSourceBatchOp;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class StringApproxNearestNeighborPredictBatchOpTest {
	@Test
	public void testStringApproxNearestNeighborPredictBatchOp() throws Exception {
		List <Row> df = Arrays.asList(
			Row.of(0, "abcde", "aabce"),
			Row.of(1, "aacedw", "aabbed"),
			Row.of(2, "cdefa", "bbcefa"),
			Row.of(3, "bdefh", "ddeac"),
			Row.of(4, "acedm", "aeefbc")
		);
		BatchOperator <?> inOp = new MemSourceBatchOp(df, "id int, text1 string, text2 string");
		BatchOperator <?> train = new StringApproxNearestNeighborTrainBatchOp().setIdCol("id").setSelectedCol("text1")
			.setMetric("SIMHASH_HAMMING_SIM").linkFrom(inOp);
		BatchOperator <?> predict = new StringApproxNearestNeighborPredictBatchOp().setSelectedCol("text2").setTopN(3)
			.linkFrom(train, inOp);
		predict.print();
	}
}
```
### 运行结果
id  | text1                  |                            text2
 ---|---|---  
0  | abcde | {"ID":"[0,1,2]","METRIC":"[0.953125,0.921875,0...
1 | aacedw | {"ID":"[0,1,4]","METRIC":"[0.9375,0.90625,0.85...
2 |  cdefa | {"ID":"[0,1,4]","METRIC":"[0.890625,0.859375,0...
3 |  bdefh | {"ID":"[4,2,1]","METRIC":"[0.9375,0.90625,0.89...
4  | acedm | {"ID":"[1,0,4]","METRIC":"[0.921875,0.921875,0...

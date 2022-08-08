# 生存回归预测 (AftSurvivalRegPredictBatchOp)
Java 类名：com.alibaba.alink.operator.batch.regression.AftSurvivalRegPredictBatchOp

Python 类名：AftSurvivalRegPredictBatchOp


## 功能介绍
在生存分析领域，加速失效时间模型(accelerated failure time model,AFT 模型)可以作为比例风险模型的替代模型。生存回归组件支持稀疏、稠密两种数据格式。

### 算法原理
AFT模型将线性回归模型的建模方法引人到生存分析的领域， 将生存时间的对数作为反应变量，研究多协变量与对数生存时间之间的回归关系，在形式上，模型与一般的线性回归模型相似。对回归系数的解释也与一般的线性回归模型相似，较之Cox模型， AFT模型对分析结果的解释更加简单、直观且易于理解，并且可以预测个体的生存时间。

### 算法使用
生存回归分析是研究特定事件的发生与时间的关系的回归。这里特定事件可以是：病人死亡、病人康复、用户流失、商品下架等。

### 文献或出处
[1] Wei, Lee-Jen. "The accelerated failure time model: a useful alternative to the Cox regression model in survival analysis." Statistics in medicine 11.14‐15 (1992): 1871-1879.

[2] https://spark.apache.org/docs/latest/ml-classification-regression.html#survival-regression

## 参数说明
| 名称 | 中文名称 | 描述 | 类型 | 是否必须？ | 取值范围 | 默认值 |
| --- | --- | --- | --- | --- | --- | --- |
| predictionCol | 预测结果列名 | 预测结果列名 | String | ✓ |  |  |
| modelFilePath | 模型的文件路径 | 模型的文件路径 | String |  |  | null |
| predictionDetailCol | 预测详细信息列名 | 预测详细信息列名 | String |  |  |  |
| quantileProbabilities | 分位数概率数组 | 分位数概率数组 | double[] |  |  | [0.01,0.05,0.1,0.25,0.5,0.75,0.9,0.95,0.99] |
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
    [1.218, 1.0, "1.560,-0.605"],
    [2.949, 0.0, "0.346,2.158"],
    [3.627, 0.0, "1.380,0.231"],
    [0.273, 1.0, "0.520,1.151"],
    [4.199, 0.0, "0.795,-0.226"]
])

data = BatchOperator.fromDataframe(df, schemaStr="label double, censor double, features string")

trainOp = AftSurvivalRegTrainBatchOp()\
            .setVectorCol("features")\
            .setLabelCol("label")\
            .setCensorCol("censor")

model = trainOp.linkFrom(data)

predictOp = AftSurvivalRegPredictBatchOp()\
            .setPredictionCol("pred")

predictOp.linkFrom(model, data).print()
```
### Java 代码
```java
import org.apache.flink.types.Row;

import com.alibaba.alink.operator.batch.BatchOperator;
import com.alibaba.alink.operator.batch.regression.AftSurvivalRegPredictBatchOp;
import com.alibaba.alink.operator.batch.regression.AftSurvivalRegTrainBatchOp;
import com.alibaba.alink.operator.batch.source.MemSourceBatchOp;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class AftSurvivalRegPredictBatchOpTest {
	@Test
	public void testAftSurvivalRegPredictBatchOp() throws Exception {
		List <Row> df = Arrays.asList(
			Row.of(1.218, 1.0, "1.560,-0.605"),
			Row.of(2.949, 0.0, "0.346,2.158"),
			Row.of(3.627, 0.0, "1.380,0.231"),
			Row.of(0.273, 1.0, "0.520,1.151"),
			Row.of(4.199, 0.0, "0.795,-0.226")
		);
		BatchOperator <?> data = new MemSourceBatchOp(df, "label double, censor double, features string");
		BatchOperator <?> trainOp = new AftSurvivalRegTrainBatchOp()
			.setVectorCol("features")
			.setLabelCol("label")
			.setCensorCol("censor");
		BatchOperator model = trainOp.linkFrom(data);
		BatchOperator <?> predictOp = new AftSurvivalRegPredictBatchOp()
			.setPredictionCol("pred");
		predictOp.linkFrom(model, data).print();
	}
}
```

### 运行结果

#### 模型结果

| model_id   | model_info | label_value |
| --- | --- | --- |
| 0          | {"hasInterceptItem":"true","vectorCol":"\"features\"","modelName":"\"AFTSurvivalRegTrainBatchOp\"","labelCol":null,"linearModelType":"\"AFT\"","vectorSize":"3"} | NULL        |
| 1048576    | {"featureColNames":null,"featureColTypes":null,"coefVector":{"data":[2.6373721387804276,-0.49591581739360013,0.19847648151323818,1.5469720551612485]},"coefVectors":null} | NULL        |

#### 预测结果
| label      | censor     | features   | pred       |
| --- | --- | --- | --- |
| 0.273      | 1.0        | 0.520,1.151 | 13.571097451777327 |
| 1.218      | 1.0        | 1.560,-0.605 | 5.718263596902868 |
| 3.627      | 0.0        | 1.380,0.231 | 7.380610641992667 |
| 4.199      | 0.0        | 0.795,-0.226 | 9.009354073821902 |
| 2.949      | 0.0        | 0.346,2.158 | 18.067188679653064 |

# 岭回归 (RidgeRegression)
Java 类名：com.alibaba.alink.pipeline.regression.RidgeRegression

Python 类名：RidgeRegression


## 功能介绍
岭回归(Ridge regression)算法是一种经典的回归算法。岭回归组件支持稀疏、稠密两种数据格式，并且支持带权重样本训练。

### 算法原理
岭回归是一种专用于共线性数据分析的有偏估计回归方法，实质上是一种改良的最小二乘估计法，通过放弃最小二乘法的无偏性，以损失部分信息、降低精度为代价获得回归系数更为符合实际、更可靠的回归方法，对病态数据的拟合要强于最小二乘法。
### 算法使用
岭回归模型应用领域和线性回归类似，经常被用来做一些数值型变量的预测，类似房价预测、销售量预测、贷款额度预测、温度预测、适度预测等。

- 备注 ：该组件训练的时候 FeatureCols 和 VectorCol 是两个互斥参数，只能有一个参数来描述算法的输入特征。

### 文献或出处
[1] Hoerl, Arthur E., and Robert W. Kennard. "Ridge regression: Biased estimation for nonorthogonal problems." Technometrics 12.1 (1970): 55-67.

[2] https://baike.baidu.com/item/%E5%B2%AD%E5%9B%9E%E5%BD%92/554917?fr=aladdin
## 参数说明

| 名称 | 中文名称 | 描述 | 类型 | 是否必须？ | 取值范围 | 默认值 |
| --- | --- | --- | --- | --- | --- | --- |
| labelCol | 标签列名 | 输入表中的标签列名 | String | ✓ |  |  |
| lambda | 希腊字母：lambda | 惩罚因子，必选 | Double | ✓ |  |  |
| predictionCol | 预测结果列名 | 预测结果列名 | String | ✓ |  |  |
| epsilon | 收敛阈值 | 迭代方法的终止判断阈值，默认值为 1.0e-6 | Double |  | [0.0, +inf) | 1.0E-6 |
| featureCols | 特征列名数组 | 特征列名数组，默认全选 | String[] |  |  | null |
| maxIter | 最大迭代步数 | 最大迭代步数，默认为 100 | Integer |  | [1, +inf) | 100 |
| modelFilePath | 模型的文件路径 | 模型的文件路径 | String |  |  | null |
| optimMethod | 优化方法 | 优化问题求解时选择的优化方法 | String |  | "LBFGS", "GD", "Newton", "SGD", "OWLQN" | null |
| overwriteSink | 是否覆写已有数据 | 是否覆写已有数据 | Boolean |  |  | false |
| reservedCols | 算法保留列名 | 算法保留列 | String[] |  |  | null |
| standardization | 是否正则化 | 是否对训练数据做正则化，默认true | Boolean |  |  | true |
| vectorCol | 向量列名 | 向量列对应的列名，默认值是null | String |  |  | null |
| weightCol | 权重列名 | 权重列对应的列名 | String |  | 所选列类型为 [BIGDECIMAL, BIGINTEGER, BYTE, DOUBLE, FLOAT, INTEGER, LONG, SHORT] | null |
| withIntercept | 是否有常数项 | 是否有常数项，默认true | Boolean |  |  | true |
| numThreads | 组件多线程线程个数 | 组件多线程线程个数 | Integer |  |  | 1 |
| modelStreamFilePath | 模型流的文件路径 | 模型流的文件路径 | String |  |  | null |
| modelStreamScanInterval | 扫描模型路径的时间间隔 | 描模型路径的时间间隔，单位秒 | Integer |  |  | 10 |
| modelStreamStartTime | 模型流的起始时间 | 模型流的起始时间。默认从当前时刻开始读。使用yyyy-mm-dd hh:mm:ss.fffffffff格式，详见Timestamp.valueOf(String s) | String |  |  | null |



## 代码示例
### Python 代码
```python
from pyalink.alink import *

import pandas as pd

useLocalEnv(1)

df = pd.DataFrame([
    [2, 1, 1],
    [3, 2, 1],
    [4, 3, 2],
    [2, 4, 1],
    [2, 2, 1],
    [4, 3, 2],
    [1, 2, 1],
    [5, 3, 3]])

batchData = BatchOperator.fromDataframe(df, schemaStr='f0 int, f1 int, label int')

colnames = ["f0","f1"]

ridge = RidgeRegression()\
            .setFeatureCols(colnames)\
            .setLambda(0.1)\
            .setLabelCol("label")\
            .setPredictionCol("pred")

model = ridge.fit(batchData)
model.transform(batchData).print()
```
### Java 代码
```java
import org.apache.flink.types.Row;

import com.alibaba.alink.operator.batch.BatchOperator;
import com.alibaba.alink.operator.batch.source.MemSourceBatchOp;
import com.alibaba.alink.pipeline.regression.RidgeRegression;
import com.alibaba.alink.pipeline.regression.RidgeRegressionModel;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class RidgeRegressionTest {
	@Test
	public void testRidgeRegression() throws Exception {
		List <Row> df = Arrays.asList(
			Row.of(2, 1, 1),
			Row.of(3, 2, 1),
			Row.of(4, 3, 2),
			Row.of(2, 4, 1),
			Row.of(2, 2, 1),
			Row.of(4, 3, 2),
			Row.of(1, 2, 1)
		);
		BatchOperator <?> batchData = new MemSourceBatchOp(df, "f0 int, f1 int, label int");
		String[] colnames = new String[] {"f0", "f1"};
		RidgeRegression ridge = new RidgeRegression()
			.setFeatureCols(colnames)
			.setLambda(0.1)
			.setLabelCol("label")
			.setPredictionCol("pred");
		RidgeRegressionModel model = ridge.fit(batchData);
		model.transform(batchData).print();
	}
}
```
### 运行结果
f0|f1|label|pred
---|---|-----|----
2|1|1|0.8849
3|2|1|1.2828
4|3|2|1.6807
2|4|1|1.1334
2|2|1|0.9678
4|3|2|1.6807
1|2|1|0.6527




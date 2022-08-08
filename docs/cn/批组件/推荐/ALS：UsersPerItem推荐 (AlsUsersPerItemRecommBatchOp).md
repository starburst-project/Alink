# ALS：UsersPerItem推荐 (AlsUsersPerItemRecommBatchOp)
Java 类名：com.alibaba.alink.operator.batch.recommendation.AlsUsersPerItemRecommBatchOp

Python 类名：AlsUsersPerItemRecommBatchOp


## 功能介绍
使用ALS (Alternating Lease Square）model 为item 推荐users。这里的ALS模型可以是隐式模型，也可以是显式模型，输出格式是MTable。

## 参数说明

| 名称 | 中文名称 | 描述 | 类型 | 是否必须？ | 取值范围 | 默认值 |
| --- | --- | --- | --- | --- | --- | --- |
| itemCol | Item列列名 | Item列列名 | String | ✓ |  |  |
| recommCol | 推荐结果列名 | 推荐结果列名 | String | ✓ |  |  |
| excludeKnown | 排除已知的关联 | 推荐结果中是否排除训练数据中已知的关联 | Boolean |  |  | false |
| initRecommCol | 初始推荐列列名 | 初始推荐列列名 | String |  | 所选列类型为 [M_TABLE] | null |
| k | 推荐TOP数量 | 推荐TOP数量 | Integer |  |  | 10 |
| reservedCols | 算法保留列名 | 算法保留列 | String[] |  |  | null |
| numThreads | 组件多线程线程个数 | 组件多线程线程个数 | Integer |  |  | 1 |


## 代码示例
### Python 代码
```python
from pyalink.alink import *

import pandas as pd

useLocalEnv(1)

df_data = pd.DataFrame([
    [1, 1, 0.6],
    [2, 2, 0.8],
    [2, 3, 0.6],
    [4, 1, 0.6],
    [4, 2, 0.3],
    [4, 3, 0.4],
])

data = BatchOperator.fromDataframe(df_data, schemaStr='user bigint, item bigint, rating double')

als = AlsTrainBatchOp().setUserCol("user").setItemCol("item").setRateCol("rating") \
    .setNumIter(10).setRank(10).setLambda(0.01)

model = als.linkFrom(data)
predictor = AlsUsersPerItemRecommBatchOp() \
    .setItemCol("item").setRecommCol("rec").setK(1).setReservedCols(["item"])

predictor.linkFrom(model, data).print();

```
### Java 代码
```java
import org.apache.flink.types.Row;

import com.alibaba.alink.operator.batch.BatchOperator;
import com.alibaba.alink.operator.batch.recommendation.AlsTrainBatchOp;
import com.alibaba.alink.operator.batch.recommendation.AlsUsersPerItemRecommBatchOp;
import com.alibaba.alink.operator.batch.source.MemSourceBatchOp;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class AlsUsersPerItemRecommBatchOpTest {
	@Test
	public void testAlsUsersPerItemRecommBatchOp() throws Exception {
		List <Row> df_data = Arrays.asList(
			Row.of(1, 1, 0.6),
			Row.of(2, 2, 0.8),
			Row.of(2, 3, 0.6),
			Row.of(4, 1, 0.6),
			Row.of(4, 2, 0.3),
			Row.of(4, 3, 0.4)
		);
		BatchOperator <?> data = new MemSourceBatchOp(df_data, "user int, item int, rating double");
		BatchOperator <?> als = new AlsTrainBatchOp().setUserCol("user").setItemCol("item").setRateCol("rating")
			.setNumIter(10).setRank(10).setLambda(0.01);
		BatchOperator model = als.linkFrom(data);
		BatchOperator <?> predictor = new AlsUsersPerItemRecommBatchOp()
			.setItemCol("item").setRecommCol("rec").setK(1).setReservedCols("item");
		predictor.linkFrom(model, data).print();
	}
}
```

### 运行结果

user| rec
----|-------
1|	{"object":"[1]","rate":"[0.5796224474906921]"}
2|	{"object":"[2]","rate":"[0.7668506503105164]"}
3|	{"object":"[2]","rate":"[0.5810791850090027]"}
1|	{"object":"[1]","rate":"[0.5796224474906921]"}
2|	{"object":"[2]","rate":"[0.7668506503105164]"}
3|	{"object":"[2]","rate":"[0.5810791850090027]"}

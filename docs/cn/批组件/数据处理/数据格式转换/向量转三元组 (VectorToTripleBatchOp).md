# 向量转三元组 (VectorToTripleBatchOp)
Java 类名：com.alibaba.alink.operator.batch.dataproc.format.VectorToTripleBatchOp

Python 类名：VectorToTripleBatchOp


## 功能介绍
将数据格式从 Vector 转成 Triple，批式组件

一条输入数据可能对应多条输出结果，输入中vector的维度为多少，就会生成多少条结果。
因此输出数据时，最好保留记录的ID字段，也就是代码示例中的"row"字段，通过setReservedCols参数指定。

输出结果中除包含通过setReservedCols中指定列外，会输出vector的下标和对应位置的值两列。列名和格式通过
setTripleColumnValueSchemaStr指定。注意该schemaStr中并没有对格式做限定，但如果设置不对会导致运行失败。
下标列可以设置为int long和string类型，第二列只能设置为string或者double。


## 参数说明

| 名称 | 中文名称 | 描述 | 类型 | 是否必须？ | 取值范围 | 默认值 |
| --- | --- | --- | --- | --- | --- | --- |
| tripleColumnValueSchemaStr | 三元组结构中列信息和数据信息的Schema | 三元组结构中列信息和数据信息的Schema | String | ✓ |  |  |
| vectorCol | 向量列名 | 向量列对应的列名 | String | ✓ | 所选列类型为 [DENSE_VECTOR, SPARSE_VECTOR, STRING, VECTOR] |  |
| handleInvalid | 解析异常处理策略 | 解析异常处理策略，可选为ERROR（抛出异常）或者SKIP（输出NULL） | String |  | "ERROR", "SKIP" | "ERROR" |
| reservedCols | 算法保留列名 | 算法保留列 | String[] |  |  | [] |

## 代码示例
### Python 代码
```python
from pyalink.alink import *

import pandas as pd

useLocalEnv(1)

df = pd.DataFrame([
    ['1', '{"f0":"1.0","f1":"2.0"}', '$3$0:1.0 1:2.0', 'f0:1.0,f1:2.0', '1.0,2.0', 1.0, 2.0],
    ['2', '{"f0":"4.0","f1":"8.0"}', '$3$0:4.0 1:8.0', 'f0:4.0,f1:8.0', '4.0,8.0', 4.0, 8.0]])

data = BatchOperator.fromDataframe(df, schemaStr="row string, json string, vec string, kv string, csv string, f0 double, f1 double")

op = VectorToTripleBatchOp()\
    .setVectorCol("vec")\
    .setReservedCols(["row"])\
    .setTripleColumnValueSchemaStr("col string, val double")\
    .linkFrom(data)

op.print()
```
### Java 代码
```java
import org.apache.flink.types.Row;

import com.alibaba.alink.operator.batch.BatchOperator;
import com.alibaba.alink.operator.batch.dataproc.format.VectorToTripleBatchOp;
import com.alibaba.alink.operator.batch.source.MemSourceBatchOp;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class VectorToTripleBatchOpTest {
	@Test
	public void testVectorToTripleBatchOp() throws Exception {
		List <Row> df = Arrays.asList(
			Row.of("1", "{\"f0\":\"1.0\",\"f1\":\"2.0\"}", "$3$0:1.0 1:2.0", "f0:1.0,f1:2.0", "1.0,2.0", 1.0, 2.0),
			Row.of("2", "{\"f0\":\"1.0\",\"f1\":\"2.0\"}", "$3$0:4.0 1:8.0", "f0:1.0,f1:2.0", "1.0,2.0", 1.0, 2.0)
		);
		BatchOperator <?> data = new MemSourceBatchOp(df,
			"row string, json string, vec string, kv string, csv string, f0 double, f1 double");
		BatchOperator <?> op = new VectorToTripleBatchOp()
			.setVectorCol("vec")
			.setReservedCols("row")
			.setTripleColumnValueSchemaStr("col string, val double")
			.linkFrom(data);
		op.print();
	}
}
```

### 运行结果

row|col|val
---|---|---
2|0|4.0000
1|0|1.0000
1|1|2.0000
2|1|8.0000

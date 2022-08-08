# 转Tensor (ToTensorBatchOp)
Java 类名：com.alibaba.alink.operator.batch.dataproc.ToTensorBatchOp

Python 类名：ToTensorBatchOp


## 功能介绍

将指定列转为 Alink 的张量类型。

如果指定列为 String 类型，并且值为 Alink 张量或者向量 toString 的结果，那么张量类型和形状将自动获取。
否则的话，需要指定张量类型和张量形状。

## 参数说明
| 名称 | 中文名称 | 描述 | 类型 | 是否必须？ | 取值范围 | 默认值 |
| --- | --- | --- | --- | --- | --- | --- |
| selectedCol | 选中的列名 | 计算列对应的列名 | String | ✓ |  |  |
| handleInvalidMethod | 处理无效值的方法 | 处理无效值的方法，可取 error, skip | String |  | "ERROR", "SKIP" | "ERROR" |
| outputCol | 输出结果列 | 输出结果列列名，可选，默认null | String |  |  | null |
| reservedCols | 算法保留列名 | 算法保留列 | String[] |  |  | null |
| tensorDataType | 要转换的张量数据类型 | 要转换的张量数据类型。 | String |  | "FLOAT", "DOUBLE", "INT", "LONG", "BOOLEAN", "BYTE", "UBYTE", "STRING" |  |
| tensorShape | 张量形状 | 张量的形状，数组类型。 | Long[] |  |  | null |
| numThreads | 组件多线程线程个数 | 组件多线程线程个数 | Integer |  |  | 1 |

## 代码示例
### Python 代码
```python
from pyalink.alink import *

import pandas as pd

useLocalEnv(1)

df = pd.DataFrame(["FLOAT#6#0.0 0.1 1.0 1.1 2.0 2.1 "])
source = BatchOperator.fromDataframe(df, schemaStr='vec string')

source.link(
    ToTensorBatchOp()
        .setSelectedCol("vec")
        .setTensorShape([2, 3])
        .setTensorDataType("float")
).print()
```

### Java 代码
```java
import org.apache.flink.types.Row;

import com.alibaba.alink.operator.batch.dataproc.ToTensorBatchOp;
import com.alibaba.alink.operator.batch.source.MemSourceBatchOp;
import org.junit.Test;

public class ToTensorTest {

	@Test
	public void testToTensorBatchOp() throws Exception {
		Row[] rows = new Row[] {
			Row.of("FLOAT#6#0.0 0.1 1.0 1.1 2.0 2.1 ")
		};
		MemSourceBatchOp memSourceBatchOp = new MemSourceBatchOp(rows, new String[] {"vec"});

		memSourceBatchOp.link(
			new ToTensorBatchOp()
				.setSelectedCol("vec")
				.setTensorShape(2, 3)
				.setTensorDataType("float")
		).print();
	}
}
```

### 运行结果

| vec                               |
|-----------------------------------|
| FLOAT#2,3#0.0 0.1 1.0 1.1 2.0 2.1 |

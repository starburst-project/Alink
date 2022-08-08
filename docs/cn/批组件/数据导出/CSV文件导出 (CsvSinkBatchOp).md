# CSV文件导出 (CsvSinkBatchOp)
Java 类名：com.alibaba.alink.operator.batch.sink.CsvSinkBatchOp

Python 类名：CsvSinkBatchOp


## 功能介绍
将输入数据写出到CSV文件。支持写到本地、hdfs。

## 参数说明
| 名称 | 中文名称 | 描述 | 类型 | 是否必须？ | 取值范围 | 默认值 |
| --- | --- | --- | --- | --- | --- | --- |
| filePath | 文件路径 | 文件路径 | String | ✓ |  |  |
| fieldDelimiter | 字段分隔符 | 字段分隔符 | String |  |  | "," |
| numFiles | 文件数目 | 文件数目 | Integer |  |  | 1 |
| overwriteSink | 是否覆写已有数据 | 是否覆写已有数据 | Boolean |  |  | false |
| partitionCols | 分区列 | 创建分区使用的列名 | String[] |  |  | null |
| quoteChar | 引号字符 | 引号字符 | Character |  |  | "\"" |
| rowDelimiter | 行分隔符 | 行分隔符 | String |  |  | "\n" |

## 代码示例

### Python 代码

```python
filePath = 'https://alink-test-data.oss-cn-hangzhou.aliyuncs.com/iris.csv'
schema = 'sepal_length double, sepal_width double, petal_length double, petal_width double, category string'
csvSource = CsvSourceBatchOp()\
    .setFilePath(filePath)\
    .setSchemaStr(schema)\
    .setFieldDelimiter(",")
csvSink = CsvSinkBatchOp()\
    .setFilePath('~/csv_test.txt')

csvSource.link(csvSink)

BatchOperator.execute()
```
### Java 代码
```java
import com.alibaba.alink.operator.batch.BatchOperator;
import com.alibaba.alink.operator.batch.sink.CsvSinkBatchOp;
import com.alibaba.alink.operator.batch.source.CsvSourceBatchOp;
import org.junit.Test;

public class CsvSinkBatchOpTest {

	@Test
	public void testCsvSinkBatchOp() throws Exception {
		String filePath = "https://alink-test-data.oss-cn-hangzhou.aliyuncs.com/iris.csv";
		String schema
			= "sepal_length double, sepal_width double, petal_length double, petal_width double, category string";
		CsvSourceBatchOp csvSource = new CsvSourceBatchOp()
			.setFilePath(filePath)
			.setSchemaStr(schema)
			.setFieldDelimiter(",");
		CsvSinkBatchOp csvSink = new CsvSinkBatchOp()
			.setFilePath("~/csv_test.txt");

		csvSource.link(csvSink);

		BatchOperator.execute();
	}
}

```


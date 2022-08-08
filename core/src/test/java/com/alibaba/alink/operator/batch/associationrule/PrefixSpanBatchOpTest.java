package com.alibaba.alink.operator.batch.associationrule;

import org.apache.flink.types.Row;

import com.alibaba.alink.operator.batch.BatchOperator;
import com.alibaba.alink.operator.batch.source.MemSourceBatchOp;
import com.alibaba.alink.testutil.AlinkTestBase;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PrefixSpanBatchOpTest extends AlinkTestBase {
	@Test
	public void testPrefixSpan() throws Exception {
		Row[] rows = new Row[] {
			Row.of("a;a,b,c;a,c;d;c,f"),
			Row.of("a,d;c;b,c;a,e"),
			Row.of("e,f;a,b;d,f;c;b"),
			Row.of("e;g;a,f;c;b;c"),
		};

		BatchOperator<?> data = new MemSourceBatchOp(Arrays.asList(rows), new String[] {"sequence"});

		PrefixSpanBatchOp prefixSpan = new PrefixSpanBatchOp()
			.setItemsCol("sequence")
			.setMinSupportCount(2);

		prefixSpan.linkFrom(data);
		Assert.assertEquals(prefixSpan.count(), 53);
	}
}
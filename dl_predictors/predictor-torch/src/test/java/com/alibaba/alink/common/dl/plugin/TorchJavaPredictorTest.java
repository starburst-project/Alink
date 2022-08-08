package com.alibaba.alink.common.dl.plugin;

import com.alibaba.alink.common.dl.plugin.DLPredictServiceMapper.PredictorConfig;
import com.alibaba.alink.common.dl.utils.FileDownloadUtils;
import com.alibaba.alink.common.io.plugin.ResourcePluginFactory;
import com.alibaba.alink.common.linalg.tensor.IntTensor;
import com.alibaba.alink.common.linalg.tensor.LongTensor;
import com.alibaba.alink.common.linalg.tensor.Tensor;
import com.alibaba.alink.common.linalg.tensor.TensorInternalUtils;
import org.junit.Assert;
import org.junit.Test;
import org.tensorflow.ndarray.LongNdArray;
import org.tensorflow.ndarray.StdArrays;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static com.alibaba.alink.operator.common.pytorch.LibtorchUtils.getLibtorchPath;

public class TorchJavaPredictorTest {
	@Test
	public void test() throws Exception {
		final String LIBTORCH_VERSION = "1.8.1";
		final String libtorchPath = getLibtorchPath(new ResourcePluginFactory(), LIBTORCH_VERSION);
		String libraryPath = new File(libtorchPath, "lib").getAbsolutePath();
		BaseDLProcessPredictorService.addLibraryPath(libraryPath);
		String modelPath = "res:///pytorch.pt";
		File localModelFile = File.createTempFile("pytorch_", ".pt");
		FileDownloadUtils.downloadFile(modelPath, localModelFile);

		TorchJavaPredictor predictor = new TorchJavaPredictor();
		PredictorConfig config = new PredictorConfig();
		config.modelPath = localModelFile.getAbsolutePath();
		config.outputTypeClasses = new Class <?>[] {Tensor.class};
		config.threadMode = false;
		predictor.open(config);
		for (int i = 0; i < 10; i += 1) {
			List <?> inputs = Arrays.asList(
				new LongTensor(new long[][] {{1, 2, 3}, {4, 5, 6}}),
				new IntTensor(3));
			List <?> outputs = predictor.predict(inputs);
			LongTensor t = (LongTensor) outputs.get(0);
			Assert.assertArrayEquals(new long[] {11, 13, 15},
				StdArrays.array1dCopyOf((LongNdArray) TensorInternalUtils.getTensorData(t)));
		}
		predictor.close();
	}
}

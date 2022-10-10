package com.csy.test.commons.patch.state.sourcepath.state;

import java.util.List;

import com.csy.test.commons.patch.cache.CompileRefSourcePathCache;
import com.csy.test.commons.patch.state.sourcepath.executor.SourcePathExecutor;
import com.csy.test.commons.utils.file.FileUtils;

/**
 * 
 * 描述:状态接口 
 * <br>PS:可以根据自己项目实际写一套处理逻辑
 * <br>只要在PachInitParams -> sourcePathStateClazz设置开始实现类即可跑处理逻辑
 * <br>譬如：SpringBootSrcJavaState、TomcatSrcJavaPathState
 * @author csy
 * @date 2020 上午11:28:16
 */
public interface SourcePathState {
	
	void execute(SourcePathExecutor sourcePathExecutor);
	
	default void commonDeal(SourcePathExecutor sourcePathExecutor, String compileFilePath) {
		String sourcePath = sourcePathExecutor.getSourcePath();
		List<String> filePaths = FileUtils.getDirFilePaths(compileFilePath);
		sourcePathExecutor.getCompilePaths().addAll(filePaths);
		filePaths.forEach((e) -> {
			CompileRefSourcePathCache.put(e , sourcePath);
		});		
	}
}

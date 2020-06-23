package com.csy.test.commons.patch.state.sourcepath.state.defaults.springboot;

import java.util.List;

import com.csy.test.commons.patch.bean.PatchInitParams;
import com.csy.test.commons.patch.cache.CompileRefSourcePathCache;
import com.csy.test.commons.patch.constatns.PatchDefinedConstants;
import com.csy.test.commons.patch.state.sourcepath.executor.SourcePathExecutor;
import com.csy.test.commons.patch.state.sourcepath.state.SourcePathState;
import com.csy.test.commons.utils.ClassUtils;

/**
 * 
 * 描述:获取springboot src/main/java 编译文件路径
 * @author csy
 * @date 2020 下午10:59:56
 */
public class SpringBootSrcJavaState implements SourcePathState{

	@Override
	public void execute(SourcePathExecutor sourcePathExecutor) {
		String sourcePath = sourcePathExecutor.getSourcePath();
		if (sourcePath.startsWith(PatchDefinedConstants.SRC_JAVA_SUFFIX_ORIGIN)) {
			PatchInitParams pachInitParams = sourcePathExecutor.getPatchInitParams();
			String compileFilePath = new StringBuilder()
		            .append(pachInitParams.getCompilePathPrefix())
		            .append(pachInitParams.getProjectEnName())
		            .append(PatchDefinedConstants.TARGET_CLASS)
		            .append(sourcePath.replace(PatchDefinedConstants.SRC_JAVA_SUFFIX_ORIGIN, PatchDefinedConstants.EMPTY_STRING))
		            .toString();
			
			if (sourcePath.endsWith(PatchDefinedConstants.KEY_JAVA)){
				compileFilePath = compileFilePath.replace(PatchDefinedConstants.KEY_JAVA, PatchDefinedConstants.KEY_CLASS);
				List<String> innerClassPaths = ClassUtils.innerClazzFilePaths(compileFilePath);
				for (String innerClassPath:innerClassPaths) {
					CompileRefSourcePathCache.put(innerClassPath , sourcePath);
				}
				sourcePathExecutor.getCompilePaths().addAll(innerClassPaths);
			}

			CompileRefSourcePathCache.put(compileFilePath , sourcePath);
			sourcePathExecutor.getCompilePaths().add(compileFilePath);
		}else {
			sourcePathExecutor
			    .sourcePathState(new SpringbootSrcWebAppState())
			    .build()
			    .execute();
		}
	}
}

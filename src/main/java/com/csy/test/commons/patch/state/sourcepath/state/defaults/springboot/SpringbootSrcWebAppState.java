package com.csy.test.commons.patch.state.sourcepath.state.defaults.springboot;

import com.csy.test.commons.patch.bean.PatchInitParams;
import com.csy.test.commons.patch.cache.CompileRefSourcePathCache;
import com.csy.test.commons.patch.constatns.PatchDefinedConstants;
import com.csy.test.commons.patch.state.sourcepath.executor.SourcePathExecutor;
import com.csy.test.commons.patch.state.sourcepath.state.SourcePathState;

public class SpringbootSrcWebAppState implements SourcePathState{

	@Override
	public void execute(SourcePathExecutor sourcePathExecutor) {
		String sourcePath = sourcePathExecutor.getSourcePath();
		if (sourcePath.startsWith(PatchDefinedConstants.SRC_WEB_APP_SUFFIX_ORIGIN)) {
			PatchInitParams pachInitParams = sourcePathExecutor.getPatchInitParams();
			String compileFilePath = new StringBuilder()
		            .append(pachInitParams.getCompilePathPrefix())
		            .append(pachInitParams.getProjectEnName())
		            .append(PatchDefinedConstants.TARGET_CLASS)
		            .append(sourcePath.replace(PatchDefinedConstants.SRC_JAVA_SUFFIX_ORIGIN, PatchDefinedConstants.EMPTY_STRING))
		            .toString();

			CompileRefSourcePathCache.put(compileFilePath , sourcePath);
			sourcePathExecutor.getCompilePaths().add(compileFilePath);
		}else {
			sourcePathExecutor
			    .sourcePathState(new SpringBootDefaultState())
			    .build()
			    .execute();
		}
	}

}

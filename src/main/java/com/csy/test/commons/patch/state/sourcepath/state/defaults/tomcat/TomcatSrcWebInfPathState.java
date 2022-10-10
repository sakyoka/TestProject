package com.csy.test.commons.patch.state.sourcepath.state.defaults.tomcat;

import java.io.File;

import com.csy.test.commons.patch.bean.PatchInitParams;
import com.csy.test.commons.patch.cache.CompileRefSourcePathCache;
import com.csy.test.commons.patch.constatns.PatchDefinedConstants;
import com.csy.test.commons.patch.state.sourcepath.executor.SourcePathExecutor;
import com.csy.test.commons.patch.state.sourcepath.state.SourcePathState;

/**
 * 
 * 描述:处理src/main/webapp/WEB-INF
 * @author csy
 * @date 2020 上午11:33:18
 */
public class TomcatSrcWebInfPathState implements SourcePathState{

	@Override
	public void execute(SourcePathExecutor sourcePathExecutor) {
		String sourcePath = sourcePathExecutor.getSourcePath();
		if (sourcePath.startsWith(PatchDefinedConstants.SRC_WEB_INF_SUFFIX_ORIGIN)) {
			PatchInitParams pachInitParams = sourcePathExecutor.getPatchInitParams();
			
			String compileFilePath = new StringBuilder()
                     .append(pachInitParams.getCompilePathPrefix())
                     .append(pachInitParams.getProjectEnName())
                     .append(File.separator)
                     .append(PatchDefinedConstants.KEY_WEB_INF)
                     .append(sourcePath.replace(PatchDefinedConstants.SRC_WEB_INF_SUFFIX_ORIGIN, PatchDefinedConstants.EMPTY_STRING))
                     .toString();
			
			if (sourcePath.endsWith(PatchDefinedConstants.FORWARD_SLASH)) {
				this.commonDeal(sourcePathExecutor , compileFilePath);
				return ;
			}
			
			CompileRefSourcePathCache.put(compileFilePath , sourcePath);
			sourcePathExecutor.getCompilePaths().add(compileFilePath);
		}else {
			throw new RuntimeException("未知路径类型，需要待处理的sourcePath:" + sourcePath);
		}
	}

}

package com.csy.test.commons.patch.state.sourcepath.state.defaults.tomcat;

import java.io.File;

import com.csy.test.commons.patch.bean.PatchInitParams;
import com.csy.test.commons.patch.cache.CompileRefSourcePathCache;
import com.csy.test.commons.patch.constatns.PatchDefinedConstants;
import com.csy.test.commons.patch.state.sourcepath.executor.SourcePathExecutor;
import com.csy.test.commons.patch.state.sourcepath.state.SourcePathState;


/**
 * 
 * 描述:处理src/main/webapp/static
 * @author csy
 * @date 2020 上午11:32:50
 */
public class TomcatSrcStaticPathState implements SourcePathState{

	@Override
	public void execute(SourcePathExecutor sourcePathExecutor) {
		String sourcePath = sourcePathExecutor.getSourcePath();
		if (sourcePath.startsWith(PatchDefinedConstants.SRC_STATIC_SUFFIX_ORIGIN)) {
			PatchInitParams pachInitParams = sourcePathExecutor.getPatchInitParams();
			
			String compileFilePath = new StringBuilder()
                    .append(pachInitParams.getCompilePathPrefix())
                    .append(pachInitParams.getProjectEnName())
                    .append(File.separator)
                    .append(PatchDefinedConstants.KEY_STATIC)
                    .append(sourcePath.replace(PatchDefinedConstants.SRC_STATIC_SUFFIX_ORIGIN, PatchDefinedConstants.EMPTY_STRING))
                    .toString();
			
			if (sourcePath.endsWith(PatchDefinedConstants.FORWARD_SLASH)) {
				this.commonDeal(sourcePathExecutor , compileFilePath);
				return ;
			}
			
			CompileRefSourcePathCache.put(compileFilePath , sourcePath);
			sourcePathExecutor.getCompilePaths().add(compileFilePath);
		}else {
			sourcePathExecutor
			.sourcePathState(new TomcatSrcWebInfPathState())
			.build()
			.execute();
		}		
	}

}

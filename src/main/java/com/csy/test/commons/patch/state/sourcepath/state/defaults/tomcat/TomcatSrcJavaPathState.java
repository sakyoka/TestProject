package com.csy.test.commons.patch.state.sourcepath.state.defaults.tomcat;

import java.util.List;

import com.csy.test.commons.patch.bean.PatchInitParams;
import com.csy.test.commons.patch.cache.CompileRefSourcePathCache;
import com.csy.test.commons.patch.constatns.PatchDefinedConstants;
import com.csy.test.commons.patch.state.sourcepath.executor.SourcePathExecutor;
import com.csy.test.commons.patch.state.sourcepath.state.SourcePathState;
import com.csy.test.commons.utils.ClassUtils;

/**
 * 
 * 描述:处理src/main/java
 * @author csy
 * @date 2020 上午11:32:06
 */
public class TomcatSrcJavaPathState implements SourcePathState{

	@Override
	public void execute(SourcePathExecutor sourcePathExecutor) {
		String sourcePath = sourcePathExecutor.getSourcePath();
		if (sourcePath.startsWith(PatchDefinedConstants.SRC_JAVA_SUFFIX_ORIGIN)) {	
			PatchInitParams pachInitParams = sourcePathExecutor.getPatchInitParams();
			String compileFilePath = new StringBuilder()
		            .append(pachInitParams.getCompilePathPrefix())
		            .append(pachInitParams.getProjectEnName())
		            .append(PatchDefinedConstants.CLASS_PATH_SUFFIX)
		            .append(sourcePath.replace(PatchDefinedConstants.SRC_JAVA_SUFFIX_ORIGIN, PatchDefinedConstants.EMPTY_STRING))
		            .toString();
			
			if (sourcePath.endsWith(PatchDefinedConstants.FORWARD_SLASH)) {
				this.commonDeal(sourcePathExecutor , compileFilePath);
				return ;
			}
			
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
			.sourcePathState(new TomcatSrcResourcesPathState())
			.build()
			.execute();
		}
	}

}

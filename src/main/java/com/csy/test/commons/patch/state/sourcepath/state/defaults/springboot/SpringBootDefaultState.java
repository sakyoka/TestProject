package com.csy.test.commons.patch.state.sourcepath.state.defaults.springboot;

import com.csy.test.commons.patch.state.sourcepath.executor.SourcePathExecutor;
import com.csy.test.commons.patch.state.sourcepath.state.SourcePathState;

public class SpringBootDefaultState implements SourcePathState{

	@Override
	public void execute(SourcePathExecutor sourcePathExecutor) {
		throw new RuntimeException("处理编译文件路径失败:" + sourcePathExecutor.getSourcePath());
	}

}

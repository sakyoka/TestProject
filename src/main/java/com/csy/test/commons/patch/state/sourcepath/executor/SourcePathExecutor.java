package com.csy.test.commons.patch.state.sourcepath.executor;

import java.util.ArrayList;
import java.util.List;

import com.csy.test.commons.patch.bean.PatchInitParams;
import com.csy.test.commons.patch.state.sourcepath.executor.SourcePathExecutor;
import com.csy.test.commons.patch.state.sourcepath.state.SourcePathState;
import com.csy.test.commons.patch.state.sourcepath.state.defaults.springboot.SpringBootSrcJavaState;
import com.csy.test.commons.patch.state.sourcepath.state.defaults.tomcat.TomcatSrcJavaPathState;
import com.csy.test.commons.utils.ClassUtils;

/**
 * 
 * 描述:数据源path处理器
 * @author csy
 * @date 2020 上午11:39:35
 */
public class SourcePathExecutor {
	
	private SourcePathState sourcePathState;
	
	private PatchInitParams patchInitParams;
	
	private String sourcePath;
	
	private List<String> compilePaths;
	
	public SourcePathExecutor() {}
	
	public SourcePathExecutor(PatchInitParams patchInitParams , String sourcePath) {
		
		this.patchInitParams = patchInitParams;
		
		this.sourcePath = sourcePath;
		
		this.compilePaths = new ArrayList<String>();
		
		this.autoSelectSourcePathState();
	}
	
	public static SourcePathExecutor getBuilder() {
		return new SourcePathExecutor();
	}
	
	public SourcePathExecutor tomcatSourcePathState() {
		this.sourcePathState = new TomcatSrcJavaPathState();
		return this;
	}
	
	public SourcePathExecutor springbootSourcePathState() {
		this.sourcePathState = new SpringBootSrcJavaState();
		return this;
	}
	
	public SourcePathExecutor autoSelectSourcePathState() {
		
		if (this.patchInitParams != null && this.patchInitParams.getSourcePathStateClazz() != null) {
			this.sourcePathState = ClassUtils.newInstance(this.patchInitParams.getSourcePathStateClazz());
		}else {
			this.tomcatSourcePathState();
		}
		
		return this;
	}
	
	public SourcePathExecutor sourcePathState(SourcePathState sourcePathState) {
		this.sourcePathState = sourcePathState;
		return this;
	}

	public SourcePathExecutor patchInitParams(PatchInitParams patchInitParams) {
		this.patchInitParams = patchInitParams;
		return this;
	}

	public SourcePathExecutor sourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
		return this;
	}

	public SourcePathExecutor build() {
		
		if (this.compilePaths == null) 
			this.compilePaths = new ArrayList<String>();
		
		return this;
	}
	
	public void execute() {
		this.sourcePathState.execute(this);
	}
	
	public SourcePathState getSourcePathState() {
		return sourcePathState;
	}

	public PatchInitParams getPatchInitParams() {
		return patchInitParams;
	}

	public String getSourcePath() {
		return sourcePath;
	}

	public List<String> getCompilePaths() {
		return compilePaths;
	}
}

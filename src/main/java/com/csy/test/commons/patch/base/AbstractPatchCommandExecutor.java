package com.csy.test.commons.patch.base;

import java.util.List;

import com.csy.test.commons.patch.bean.PatchInitParams;

/**
 * 
 * 描述:补丁命令执行抽象积累
 * @author csy
 * @date 2020 下午8:39:29
 */
public abstract class AbstractPatchCommandExecutor {
	
	protected PatchInitParams patchInitParams;
	
	protected List<String> sourcePaths;
	
	public AbstractPatchCommandExecutor() {}
	
	public abstract List<String> execute();

	public PatchInitParams getPatchInitParams() {
		return patchInitParams;
	}

	public AbstractPatchCommandExecutor patchInitParams(PatchInitParams patchInitParams) {
		this.patchInitParams = patchInitParams;
		return this;
	}

	public List<String> getSourcePaths() {
		return sourcePaths;
	}
}

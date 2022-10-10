package com.csy.test.commons.patch.base;

import java.util.List;

import com.csy.test.commons.patch.base.defaults.DefaultPatchWriteRecordFile;
import com.csy.test.commons.patch.bean.PatchInitParams;
import com.csy.test.commons.utils.ClassUtils;

/**
 * 
 * 描述:补丁生成基类
 * @author csy
 * @date 2020 下午3:26:44
 */
public abstract class AbstractPatchGenerate {
	
	protected List<String> compileFilePaths;
	
	protected PatchInitParams patchInitParams;
	
	public AbstractPatchGenerate compileFilePaths(List<String> compileFilePaths) {
		this.compileFilePaths = compileFilePaths;
		return this;
	}

	public AbstractPatchGenerate patchInitParams(PatchInitParams patchInitParams) {
		this.patchInitParams = patchInitParams;
		return this;
	}

	/**
	 * 
	 * 描述：遍历复制
	 * @author csy
	 * @date 2020 下午3:31:39
	 * @return
	 */
	public AbstractPatchGenerate foreach() {
		for (String compilePath:this.compileFilePaths) {
			//需要实现复制逻辑
			this.copyFile(this.patchInitParams , compilePath);
		}
		return this;
	}
	
	/**
	 * 
	 * 描述：默认记录写入文件方法
	 * @author csy
	 * @date 2020 上午12:05:16
	 */
	public void writeRecordFile() {
		PatchInitParams pachInitParams = this.patchInitParams;
		//实现此类的autoWriteRecordFile方法
		AbstractWriteRecordFile writeRecordFile = (pachInitParams.getWriteRecordFileClazz() == null ? 
				                                                 new DefaultPatchWriteRecordFile() 
				                                                 : ClassUtils.newInstance(pachInitParams.getWriteRecordFileClazz()));
		writeRecordFile.compileFilePaths(this.compileFilePaths).pachInitParams(this.patchInitParams).autoWriteRecordFile();
	}

	/**
	 * 
	 * 描述：复制编译文件到特定目录
	 * @author csy
	 * @date 2020 下午3:25:53
	 * @param pachInitParams
	 * @param compilePath 编译文件路径
	 */
	protected abstract void copyFile( PatchInitParams pachInitParams, String compilePath);
}

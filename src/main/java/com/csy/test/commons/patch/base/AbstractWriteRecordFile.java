package com.csy.test.commons.patch.base;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.csy.test.commons.patch.bean.PatchInitParams;
import com.csy.test.commons.utils.UUID;
import com.csy.test.commons.utils.file.FileUtils;

public abstract class AbstractWriteRecordFile {
	
	protected PatchInitParams pachInitParams;
	
	protected List<String> compileFilePaths;
	
	public AbstractWriteRecordFile() {}
	
	public abstract void autoWriteRecordFile();
	

	public AbstractWriteRecordFile pachInitParams(PatchInitParams pachInitParams) {
		this.pachInitParams = pachInitParams;
		return this;
	}

	public AbstractWriteRecordFile compileFilePaths(List<String> compileFilePaths) {
		this.compileFilePaths = compileFilePaths;
		return this;
	}

	/**
	 * 
	 * 描述：默认写记录文件方法
	 * @author csy
	 * @date 2020 下午3:27:19
	 */
	protected void defaultWriteRecordFile() {
		
		String cachePathPrefix = pachInitParams.getCachePathPrefix();	
		String filePath =  new StringBuilder()
								.append(cachePathPrefix)
								.append(File.separator)
								.append(pachInitParams.getCachePathUuid())
								.append(File.separator)
								.append(pachInitParams.getPatchFileName())
								.toString();
		
		File outFile = new File(filePath);
		if (outFile.exists()){
			filePath = new StringBuilder().append(cachePathPrefix)
					.append(File.separator)
					.append(pachInitParams.getCachePathUuid())
					.append(UUID.getString())
					.append(this.pachInitParams.getPatchFileName().substring(pachInitParams.getPatchFileName().lastIndexOf(".") +1))
					.toString();
			outFile = new File(filePath);
		}
		try {
			outFile.createNewFile();
		} catch (IOException e) {
			throw new RuntimeException("创建文件失败!" , e);
		}
		
		String workPathPrefix = pachInitParams.getCompilePathPrefix();
		File tomcatFile = new File(workPathPrefix);
		String tomcatPath = tomcatFile.getAbsolutePath().replace('\\', '/');
		StringBuilder stringBuilder = new StringBuilder();
		for (String path:this.compileFilePaths){
			stringBuilder.append(path.replace('\\', '/').replace(tomcatPath, "")).append("\n");
		}
		
		FileUtils.writeFile(outFile, stringBuilder.toString());
		
		System.out.println("pach record file path:" + filePath);
	}
}

package com.csy.test.commons.patch.utils;

import java.util.ArrayList;
import java.util.List;

import com.csy.test.commons.patch.base.defaults.DefaultPackPatchFile;
import com.csy.test.commons.patch.base.defaults.DefaultPatchStandardGenerate;
import com.csy.test.commons.patch.base.defaults.TomcatPatchOriginalGenerate;
import com.csy.test.commons.patch.base.defaults.TomcatWriteFileRecordFile;
import com.csy.test.commons.patch.bean.PatchInitParams;
import com.csy.test.commons.patch.constatns.PatchInitConstants;
import com.csy.test.commons.patch.state.sourcepath.state.defaults.tomcat.TomcatSrcJavaPathState;
import com.csy.test.commons.valid.bean.ParamValidResult;
import com.csy.test.commons.valid.utils.ValidUtils;


public class Test {
	
	public static void main(String[] args) {
		List<String> projectList = new ArrayList<String>();
		projectList.add("ApprRule");
		PatchInitParams pachInitParams =  PatchInitParams.getBuilder()
				.sourcePathPrefix(PatchInitConstants.DEFAULT_SOURCE_PATH_PREFIX)//工作空间根目录
				.compilePathPrefix(PatchInitConstants.DEFAULT_WORK_PATH_PREFIX)//编译空间根目录
				.cachePathPrefix(PatchInitConstants.DEFAULT_CACHE_PATH_PREFIX)//补丁缓存地址
				.projectChName("测试项目")
				.sourcePathStateClazz(TomcatSrcJavaPathState.class)//解析路径开始
				.patchGenerateClazz(TomcatPatchOriginalGenerate.class)//DefaultPatchStandardGenerate 、TomcatPatchOriginalGenerate
				.writeRecordFileClazz(TomcatWriteFileRecordFile.class)//补丁记录文件
				.packPatchFileClazz(DefaultPackPatchFile.class)//打包
				.useSamePatchRecordFile(true)//是否用同一个记录文件
				.build();
		for (String projectName:projectList){
			pachInitParams.projectEnName(projectName) //项目包名
			              .useGitCommand();//git 命令获取更改文件
			
			ParamValidResult validResult = ValidUtils.valid(pachInitParams);
			if(validResult.getHasError()) {
				throw new RuntimeException(validResult.toString());
			}

			PatchUtils.generate(pachInitParams);//生成补丁文件
		}
	}
}

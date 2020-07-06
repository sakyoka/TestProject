package com.csy.test.commons.patch.utils;

import java.util.ArrayList;
import java.util.List;

import com.csy.test.commons.patch.base.defaults.DefaultPackPatchFile;
import com.csy.test.commons.patch.base.defaults.DefaultPatchStandardGenerate;
import com.csy.test.commons.patch.base.defaults.TomcatWriteFileRecordFile;
import com.csy.test.commons.patch.bean.PatchInitParams;
import com.csy.test.commons.patch.constatns.PatchInitConstants;
import com.csy.test.commons.patch.state.sourcepath.state.defaults.tomcat.TomcatSrcJavaPathState;
import com.csy.test.commons.valid.bean.ParamValidResult;
import com.csy.test.commons.valid.utils.ValidUtils;


public class Test {
	
	public static void main(String[] args) {
		List<String> projectList = new ArrayList<String>();
		//projectList.add("ApprSynthesis");
		//projectList.add("ApprControl");
		//projectList.add("ApprSupport");
		projectList.add(PatchInitConstants.DEFAULT_PROJECT_NAME);

		PatchInitParams pachInitParams =  PatchInitParams.getBuilder()
				.sourcePathPrefix(PatchInitConstants.DEFAULT_SOURCE_PATH_PREFIX)
				.compilePathPrefix(PatchInitConstants.DEFAULT_WORK_PATH_PREFIX)
				.cachePathPrefix(PatchInitConstants.DEFAULT_CACHE_PATH_PREFIX)
				.projectChName("测试项目")
				.sourcePathStateClazz(TomcatSrcJavaPathState.class)
				.patchGenerateClazz(DefaultPatchStandardGenerate.class)//DefaultPatchStandardGenerate 、TomcatPatchOriginalGenerate
				.writeRecordFileClazz(TomcatWriteFileRecordFile.class)
				.packPatchFileClazz(DefaultPackPatchFile.class)
				.useSamePatchRecordFile(true)
				.build();
		for (String projectName:projectList){
			pachInitParams.projectEnName(projectName)
			              .useGitCommand();//PatchInitConstants.DEFAULT_PROJECT_NAME 
			
			ParamValidResult validResult = ValidUtils.valid(pachInitParams);
			if(validResult.getHasError()) {
				throw new RuntimeException(validResult.toString());
			}
			
			PatchUtils.generate(pachInitParams);//生成补丁文件
		}

	}
}

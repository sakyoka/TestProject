package com.csy.test.commons.patch.bean;

import java.io.Serializable;

import com.csy.test.commons.patch.base.AbstractPackPatchFile;
import com.csy.test.commons.patch.base.AbstractPatchCommandExecutor;
import com.csy.test.commons.patch.base.AbstractPatchGenerate;
import com.csy.test.commons.patch.base.AbstractWriteRecordFile;
import com.csy.test.commons.patch.constatns.PatchInitConstants;
import com.csy.test.commons.patch.state.sourcepath.state.SourcePathState;
import com.csy.test.commons.patch.valid.FileDirValid;
import com.csy.test.commons.utils.UUID;
import com.csy.test.commons.valid.annotion.Valid;
import com.csy.test.commons.valid.base.defaults.BlankValid;

/**
 * 
 * 描述:初始化参数对象
 * @author csy
 * @date 2020 下午11:56:39
 */
public class PatchInitParams implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/**
	 * 项目英文名
	 */
	@Valid(validType = {BlankValid.class} , errorMessage = "项目英文名不能为空")
	private String projectEnName;
	
	/**
	 * 项目中文名
	 */
	private String projectChName;
	
	/**
	 * 数据源路径前缀
	 */
	@Valid(validType = {BlankValid.class , FileDirValid.class} , errorMessage = "数据源路径前缀不能为空且为系统路径")
	private String sourcePathPrefix;
	
	/**
	 * 编译文件路径前缀
	 */
	@Valid(validType = {BlankValid.class , FileDirValid.class} , errorMessage = "编译文件路径前缀不能为空且为系统路径")
	private String compilePathPrefix;
	
	/**
	 * 补丁文件输出路径前缀
	 */
	@Valid(validType = {BlankValid.class , FileDirValid.class} , errorMessage = "补丁文件输出路径前缀不能为空且为系统路径")
	private String cachePathPrefix;
	
	/**
	 * 补丁最终输出文件夹，如果多个项目公用一个uuid，最终会输出在同一个文件夹；
	 */
	private String cachePathUuid;
	
	/**
	 * 获取文件源的命令
	 */
	@Valid(validType = {BlankValid.class} , errorMessage = "获取文件源的命令不能为空")
	private String commandStr;
	
	/**
	 * 是否默认执行git命令true是false否
	 */
	private boolean defaultGitComnand = false;
	
	/**
	 * 补丁记录文件名需要加后缀
	 */
	@Valid(validType = {BlankValid.class} , errorMessage = "补丁记录文件名不能为空")
	private String patchFileName;
	
	/**
	 * 解析原路径、编译路径属于热插拔方式，默认TomcatSrcJavaPathState;
	 * <br>解析过程根据实际解释;
	 * <br>可现实SourcePathState进行扩展，形成一套解析规则
	 * <br>可以参照com.csy.test.commons.patch.state.sourcepath.state.defaults;
	 */
	private Class<? extends SourcePathState> sourcePathStateClazz;
	
	/**
	 * 补丁内容输出，也是属于热插拔方式，默认是DefaultPatchWriteRecordFile;
	 * <br>该类作用在于把补丁路径输出到文件里面，形成一个路径说明;
	 * <br>可实现AbstractWriteRecordFile进行扩展;
	 * <br>可以参照com.csy.test.commons.patch.base.defaults;
	 */
	private Class<? extends AbstractWriteRecordFile> writeRecordFileClazz;
	
	/**
	 * 补丁生成处理，也是属于热插拔方式，默认是DefaultPatchStandardGenerate;
	 * <br>可实现AbstractPachGenerate进行扩展;
	 * <br>可以参照com.csy.test.commons.patch.base.defaults;
	 */
	private Class<? extends AbstractPatchGenerate> patchGenerateClazz;
	
	/**
	 * 命令执行器，也是属于热插拔方式，默认是DefaultPatchCommandExecutor
	 * <br>可实现AbstractPatchCommandExecutor进行扩展
	 * <br>可以参照com.csy.test.commons.patch.base.defaults;
	 */
	private Class<? extends AbstractPatchCommandExecutor> patchCommandExecutorClazz;
	
	/**
	 * 命令执行器，也是属于热插拔方式，默认是AbstractPackPatchFile
	 * <br>可实现AbstractPackPatchFile进行扩展
	 * <br>可以参照com.csy.test.commons.patch.base.defaults;
	 */
	private Class<? extends AbstractPackPatchFile> packPatchFileClazz;
	
	/**
	 * 打包文件输出路径
	 */
	private String packFilePath;
	
	/**
	 * 如果目录下有了同名补丁名是否用同一个文件记录。默认否
	 */
	private Boolean useSamePatchRecordFile = false;
	
	public static PatchInitParams getBuilder(){
		return new PatchInitParams();
	}

	public PatchInitParams projectEnName(String projectEnName) {
		this.projectEnName = projectEnName;
		return this;
	}

	public PatchInitParams projectChName(String projectChName) {
		this.projectChName = projectChName;
		return this;
	}

	public PatchInitParams sourcePathPrefix(String sourcePathPrefix) {
		this.sourcePathPrefix = sourcePathPrefix;
		return this;
	}

	public PatchInitParams compilePathPrefix(String compilePathPrefix) {
		this.compilePathPrefix = compilePathPrefix;
		return this;
	}

	public PatchInitParams cachePathPrefix(String cachePathPrefix) {
		this.cachePathPrefix = cachePathPrefix;
		return this;
	}
	
	public PatchInitParams commandStr(String commandStr) {
		this.commandStr = commandStr;
		return this;
	}
	
	public PatchInitParams defaultGitCommand() {
		
		this.defaultGitComnand = true;
		
		return this;
	}
	
	public PatchInitParams useGitCommand() {
		
		if (this.projectEnName == null)
			throw new RuntimeException("useGitCommand before the projectEnName is not allow null");
		
		if (this.sourcePathPrefix == null)
			throw new RuntimeException("useGitCommand before the sourcePathPrefix is not allow null");
		
		this.commandStr = new StringBuilder()
	              .append("cmd /c cd ")
	              .append(this.sourcePathPrefix)
	              .append(this.projectEnName)
	              .append(" && git status")
	              .toString();
		return this;
	}
	
	public PatchInitParams useSvnCommand() {
		
		if (this.projectEnName == null)
			throw new RuntimeException("useSvnCommand before the projectEnName is not allow null");
		
		if (this.sourcePathPrefix == null)
			throw new RuntimeException("useSvnCommand before the sourcePathPrefix is not allow null");
		
		this.commandStr = new StringBuilder()
	              .append("cmd /c cd ")
	              .append(this.sourcePathPrefix)
	              .append(this.projectEnName)
	              .append(" && svn st")
	              .toString();
		return this;
	}
	
	public PatchInitParams patchFileName(String patchFileName) {
		this.patchFileName = patchFileName;
		return this;
	}
	
	public PatchInitParams cachePathUuid(String cachePathUuid) {
		this.cachePathUuid = cachePathUuid;
		return this;
	}
	
	public PatchInitParams sourcePathStateClazz(Class<? extends SourcePathState> sourcePathStateClazz) {
		this.sourcePathStateClazz = sourcePathStateClazz;
		return this;
	}
	
	public PatchInitParams writeRecordFileClazz(Class<? extends AbstractWriteRecordFile> writeRecordFileClazz) {
		this.writeRecordFileClazz = writeRecordFileClazz;
		return this;
	}
	
	public PatchInitParams patchGenerateClazz(Class<? extends AbstractPatchGenerate> patchGenerateClazz) {
		this.patchGenerateClazz = patchGenerateClazz;
		return this;
	}
	
	public PatchInitParams patchCommandExecutorClazz(Class<? extends AbstractPatchCommandExecutor> patchCommandExecutorClazz) {
		this.patchCommandExecutorClazz = patchCommandExecutorClazz;
		return this;
	}
	
	public PatchInitParams packPatchFileClazz(Class<? extends AbstractPackPatchFile> packPatchFileClazz) {
		this.packPatchFileClazz = packPatchFileClazz;
		return this;
	}
	
	public PatchInitParams packFilePath(String packFilePath){
		this.packFilePath = packFilePath;
		return this;
	}
	
	public PatchInitParams useSamePatchRecordFile(Boolean useSamePatchRecordFile){
		this.useSamePatchRecordFile = useSamePatchRecordFile;
		return this;
	}
	
	public PatchInitParams build(){
		
		if (this.cachePathUuid == null)
			this.cachePathUuid = UUID.getString();
		
		if (this.defaultGitComnand) {
			this.useGitCommand();
		}
		
		if (this.patchFileName == null) {
			this.patchFileName = PatchInitConstants.DEFAULT_PATCH_FILE_NAME;
		}
		
		return this;
	}
	
	public String getProjectEnName() {
		return projectEnName;
	}

	public void setProjectEnName(String projectEnName) {
		this.projectEnName = projectEnName;
	}

	public String getProjectChName() {
		return projectChName;
	}

	public void setProjectChName(String projectChName) {
		this.projectChName = projectChName;
	}

	public String getSourcePathPrefix() {
		return sourcePathPrefix;
	}

	public void setSourcePathPrefix(String sourcePathPrefix) {
		this.sourcePathPrefix = sourcePathPrefix;
	}

	public String getCompilePathPrefix() {
		return compilePathPrefix;
	}

	public void setCompilePathPrefix(String compilePathPrefix) {
		this.compilePathPrefix = compilePathPrefix;
	}

	public String getCachePathPrefix() {
		return cachePathPrefix;
	}

	public void setCachePathPrefix(String cachePathPrefix) {
		this.cachePathPrefix = cachePathPrefix;
	}

	public String getCachePathUuid() {
		return cachePathUuid;
	}

	public String getCommandStr() {
		return commandStr;
	}

	public void setCommandStr(String commandStr) {
		this.commandStr = commandStr;
	}
	
	public boolean isDefaultGitComnand() {
		return defaultGitComnand;
	}

	public String getPatchFileName() {
		return patchFileName;
	}

	public Class<? extends AbstractPatchGenerate> getPatchGenerateClazz() {
		return patchGenerateClazz;
	}

	public Class<? extends SourcePathState> getSourcePathStateClazz() {
		return sourcePathStateClazz;
	}

	public Class<? extends AbstractWriteRecordFile> getWriteRecordFileClazz() {
		return writeRecordFileClazz;
	}

	public Class<? extends AbstractPatchCommandExecutor> getPatchCommandExecutorClazz() {
		return patchCommandExecutorClazz;
	}

	public Class<? extends AbstractPackPatchFile> getPackPatchFileClazz() {
		return packPatchFileClazz;
	}

	public String getPackFilePath() {
		return packFilePath;
	}

	public Boolean getUseSamePatchRecordFile() {
		return useSamePatchRecordFile;
	}
}

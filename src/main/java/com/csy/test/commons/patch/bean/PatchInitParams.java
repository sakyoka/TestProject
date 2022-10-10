package com.csy.test.commons.patch.bean;

import java.io.Serializable;

import org.apache.commons.lang3.ArrayUtils;
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
	 * 解析原路径从而获取对应编译路径，属于热插拔方式，默认TomcatSrcJavaPathState;
	 * <br>解析过程根据实际解释;
	 * <br>可现实SourcePathState进行扩展，形成一套解析规则
	 * <br>可以参照com.csy.test.commons.patch.state.sourcepath.state.defaults;
	 */
	private Class<? extends SourcePathState> sourcePathStateClazz;
	
	/**
	 * 补丁记录文件内容输出，也是属于热插拔方式，默认是DefaultPatchWriteRecordFile;
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
	 * 打包压缩执行器，也是属于热插拔方式，没有默认值，如果不设置该值不执行打包压缩
	 * <br>可实现AbstractPackPatchFile进行扩展
	 * <br>可以参照com.csy.test.commons.patch.base.defaults;
	 */
	private Class<? extends AbstractPackPatchFile> packPatchFileClazz;
	
	/**
	 * 打包文件输出路径 , 如果packPatchFileClazz有值采用打包的话，当packFilePath不为空时候采用这个全路径值，否则输出到cachePathPrefix
	 */
	private String packFilePath;
	
	/**
	 * 如果目录下有了同名补丁记录名文件是否用同一个文件记录。默认否，如果是true会这存在文件中追加内容而不会重新一个
	 */
	private Boolean useSamePatchRecordFile = false;
	
	public static PatchInitParams getBuilder(){
		return new PatchInitParams();
	}

	/**
	 * 描述：项目包名
	 * @author csy 
	 * @date 2020年7月6日 下午2:04:30
	 * @param projectEnName
	 * @return PatchInitParams
	 */
	public PatchInitParams projectEnName(String projectEnName) {
		this.projectEnName = projectEnName;
		return this;
	}

	/**
	 * 描述：项目中文名 暂时没有用处
	 * @author csy 
	 * @date 2020年7月6日 下午2:03:53
	 * @param projectChName
	 * @return PatchInitParams
	 */
	public PatchInitParams projectChName(String projectChName) {
		this.projectChName = projectChName;
		return this;
	}

	/**
	 * 描述：源代码路径 一般是workspace目录 非空
	 * @author csy 
	 * @date 2020年7月6日 下午2:01:49
	 * @param sourcePathPrefix
	 * @return PatchInitParams
	 */
	public PatchInitParams sourcePathPrefix(String sourcePathPrefix) {
		this.sourcePathPrefix = sourcePathPrefix;
		return this;
	}

	/**
	 * 描述：编译代码前缀路径 非空
	 * <br> 如果是tomcat，一般是webapp目录；springboot一般是和workspace一致，因为编译文件在target下
	 * @author csy 
	 * @date 2020年7月6日 下午2:01:11
	 * @param compilePathPrefix
	 * @return PatchInitParams
	 */
	public PatchInitParams compilePathPrefix(String compilePathPrefix) {
		this.compilePathPrefix = compilePathPrefix;
		return this;
	}

	/**
	 * 描述：缓存文件夹路径 非空
	 * @author csy 
	 * @date 2020年7月6日 下午2:00:45
	 * @param cachePathPrefix
	 * @return PatchInitParams
	 */
	public PatchInitParams cachePathPrefix(String cachePathPrefix) {
		this.cachePathPrefix = cachePathPrefix;
		return this;
	}
	
	/**
	 * 描述：获取提交文件命令 
	 * <br>   注：useGitCommand、useSvnCommand有自动设置，这个是为了除了GIT、SVN之外而暴露出来
	 * @author csy 
	 * @date 2020年7月6日 下午1:58:31
	 * @param commandStr
	 * @return PatchInitParams
	 */
	public PatchInitParams commandStr(String commandStr) {
		this.commandStr = commandStr;
		return this;
	}
	
	/**
	 * 描述：默认使用GIT命令
	 * @author csy 
	 * @date 2020年7月6日 下午1:58:02
	 * @return PatchInitParams
	 */
	public PatchInitParams defaultGitCommand() {
		
		this.defaultGitComnand = true;
		
		return this;
	}
	
	/**
	 * 描述：使用GIT命令获取需要提交文件
	 * @author csy 
	 * @date 2020年7月6日 下午1:57:16
	 * @return PatchInitParams
	 */
	public PatchInitParams useGitCommand() {
		useGitCommand(ArrayUtils.EMPTY_STRING_ARRAY);
		return this;
	}
	
	/**
	 * 描述：使用GIT命令获取需要提交文件
	 * @author csy 
	 * @date 2022年1月10日 下午1:58:43
	 * @param commitId 提交id可选，为空时候取当前变化 
	 *                 <br>不为空时获取指定提交文件，且仅支持对应项目
	 * @return PatchInitParams
	 */
	public PatchInitParams useGitCommand(String ...commitIds) {
		
		if (this.projectEnName == null)
			throw new RuntimeException("useGitCommand before the projectEnName is not allow null");
		
		if (this.sourcePathPrefix == null)
			throw new RuntimeException("useGitCommand before the sourcePathPrefix is not allow null");
		
		String dir = this.sourcePathPrefix.substring(0, this.sourcePathPrefix.lastIndexOf(":") + 1);
		if (ArrayUtils.isNotEmpty(commitIds)){
			StringBuilder stringBuilder = new StringBuilder()
		              .append("cmd /c ")
		              .append(dir)
		              .append(" && cd ")
		              .append(this.sourcePathPrefix)
		              .append(this.projectEnName);
			for (String commitId:commitIds){
				stringBuilder.append("&& git show ").append(commitId).append(" --name-only ");
			}
			this.commandStr = stringBuilder.toString();
		}else{
			this.commandStr = new StringBuilder()
		              .append("cmd /c ")
		              .append(dir)
		              .append(" && cd ")
		              .append(this.sourcePathPrefix)
		              .append(this.projectEnName)
		              .append(" && git status")
		              .toString();
		}
		return this;
	}
	
	/**
	 * 描述：使用SVN命令获取需要提交文件
	 * @author csy 
	 * @date 2020年7月6日 下午1:56:11
	 * @return PatchInitParams
	 */
	public PatchInitParams useSvnCommand() {
		
		if (this.projectEnName == null)
			throw new RuntimeException("useSvnCommand before the projectEnName is not allow null");
		
		if (this.sourcePathPrefix == null)
			throw new RuntimeException("useSvnCommand before the sourcePathPrefix is not allow null");
		
		String dir = this.sourcePathPrefix.substring(0, this.sourcePathPrefix.lastIndexOf(":") + 1);
		this.commandStr = new StringBuilder()
	              .append("cmd /c ")
	              .append(dir)
	              .append(" && cd ")
	              .append(this.sourcePathPrefix)
	              .append(this.projectEnName)
	              .append(" && svn st")
	              .toString();
		return this;
	}
	
	/**
	 * 描述：补丁记录名，带后缀，有默认值:补丁文件.txt
	 * @author csy 
	 * @date 2020年7月6日 下午1:55:03
	 * @param patchFileName
	 * @return  PatchInitParams
	 */
	public PatchInitParams patchFileName(String patchFileName) {
		this.patchFileName = patchFileName;
		return this;
	}
	
	/**
	 * 描述：缓存路径下创建目录名。
	 *       如果使用同一个值，补丁会生成在同一个目录下。
	 * @author csy 
	 * @date 2020年7月6日 下午1:53:18
	 * @param cachePathUuid
	 * @return PatchInitParams
	 */
	public PatchInitParams cachePathUuid(String cachePathUuid) {
		this.cachePathUuid = cachePathUuid;
		return this;
	}
	
	/**
	 * 描述：设置解析源路径执行器 默认TomcatSrcJavaPathState
	 * <br>注：此功能由一系列路径判断把源文件转换成编译路径，而编译路径需要结合项目结构。
	 * @author csy 
	 * @date 2020年7月6日 上午11:55:13
	 * @param sourcePathStateClazz
	 * @return PatchInitParams
	 */ 
	public PatchInitParams sourcePathStateClazz(Class<? extends SourcePathState> sourcePathStateClazz) {
		this.sourcePathStateClazz = sourcePathStateClazz;
		return this;
	}
	
	/**
	 * 描述：设置补丁文件内容生成 ，默认DefaultPatchWriteRecordFile
	 * @author csy 
	 * @date 2020年7月6日 上午11:54:30
	 * @param writeRecordFileClazz
	 * @return PatchInitParams
	 */
	public PatchInitParams writeRecordFileClazz(Class<? extends AbstractWriteRecordFile> writeRecordFileClazz) {
		this.writeRecordFileClazz = writeRecordFileClazz;
		return this;
	}
	
	/**
	 * 描述：设置生成补丁文件执行器 ， 有默认值DefaultPatchStandardGenerate
	 * @author csy 
	 * @date 2020年7月6日 上午11:51:02
	 * @param patchGenerateClazz
	 * @return PatchInitParams
	 */
	public PatchInitParams patchGenerateClazz(Class<? extends AbstractPatchGenerate> patchGenerateClazz) {
		this.patchGenerateClazz = patchGenerateClazz;
		return this;
	}
	
	/**
	 * 描述：设置获取补丁文件路径执行器 ，该执行器会使用commandStr执行，然后解析出路径集合；默认值DefaultPatchCommandExecutor
	 * @author csy 
	 * @date 2020年7月6日 上午11:50:12
	 * @param patchCommandExecutorClazz
	 * @return PatchInitParams
	 */
	public PatchInitParams patchCommandExecutorClazz(Class<? extends AbstractPatchCommandExecutor> patchCommandExecutorClazz) {
		this.patchCommandExecutorClazz = patchCommandExecutorClazz;
		return this;
	}
	
	/**
	 * 描述：设置打包压缩执行器 ，没有默认值，不设置不执行打包
	 * @author csy 
	 * @date 2020年7月6日 上午11:49:47
	 * @param packPatchFileClazz
	 * @return PatchInitParams
	 */
	public PatchInitParams packPatchFileClazz(Class<? extends AbstractPackPatchFile> packPatchFileClazz) {
		this.packPatchFileClazz = packPatchFileClazz;
		return this;
	}
	
	/**
	 * 描述：设置压缩补丁文件输出的全路径，默认是缓存路径下
	 * @author csy 
	 * @date 2020年7月6日 上午11:49:04
	 * @param packFilePath
	 * @return PatchInitParams
	 */
	public PatchInitParams packFilePath(String packFilePath){
		this.packFilePath = packFilePath;
		return this;
	}
	
	/**
	 * 描述：设置是否使用同一个记录补丁文件，默认否
	 * @author csy 
	 * @date 2020年7月6日 上午11:48:27
	 * @param useSamePatchRecordFile
	 * @return PatchInitParams
	 */
	public PatchInitParams useSamePatchRecordFile(Boolean useSamePatchRecordFile){
		this.useSamePatchRecordFile = useSamePatchRecordFile;
		return this;
	}
	
	/**
	 * 描述：此方法不一定要执行
	 * @author csy 
	 * @date 2020年7月6日 下午2:05:40
	 * @return PatchInitParams
	 */
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

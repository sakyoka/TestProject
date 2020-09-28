package com.csy.test.ui.patch.bean;

import java.util.List;

/**
 * 
 * 描述：配置实体类
 * @author csy
 * @date 2020年9月28日 下午1:50:53
 */
public class DeployBean {

	/**
	 * 数据源路径前缀
	 */
	private String sourcePathPrefix;
	
	/**
	 * 编译文件路径前缀
	 */
	private String compilePathPrefix;
	
	/**
	 * 补丁文件输出路径前缀
	 */
	private String cachePathPrefix;
	
	/**
	 * 项目中文名
	 */
	private List<String> projectChNameList;
	
	/**
	 * 补丁展示效果
	 */
	private Integer patchStyle;//1列表，2树状
	
	public static DeployBean getBuilder(){
		return new DeployBean();
	}
	
	public DeployBean sourcePathPrefix(String sourcePathPrefix) {
		this.sourcePathPrefix = sourcePathPrefix;
		return this;
	}
	
	public DeployBean compilePathPrefix(String compilePathPrefix) {
		this.compilePathPrefix = compilePathPrefix;
		return this;
	}
	
	public DeployBean cachePathPrefix(String cachePathPrefix) {
		this.cachePathPrefix = cachePathPrefix;
		return this;
	}
	
	public DeployBean projectChNameList(List<String> projectChNameList) {
		this.projectChNameList = projectChNameList;
		return this;
	}
	
	public DeployBean patchStyle(Integer patchStyle) {
		this.patchStyle = patchStyle;
		return this;
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

	public List<String> getProjectChNameList() {
		return projectChNameList;
	}

	public void setProjectChNameList(List<String> projectChNameList) {
		this.projectChNameList = projectChNameList;
	}

	public Integer getPatchStyle() {
		return patchStyle;
	}

	public void setPatchStyle(Integer patchStyle) {
		this.patchStyle = patchStyle;
	}
}

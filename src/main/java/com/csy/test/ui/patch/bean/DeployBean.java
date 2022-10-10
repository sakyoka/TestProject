package com.csy.test.ui.patch.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.csy.test.commons.patch.constatns.PatchInitConstants;
import com.csy.test.ui.patch.annotation.JComponent;
import com.csy.test.ui.patch.cache.DeloyParamCache;

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
	@JComponent(name = "数据源" , id = "sourcePathPrefix")
	private String sourcePathPrefix;
	
	/**
	 * 编译文件路径前缀
	 */
	@JComponent(name = "编译空间" , id = "compilePathPrefix")
	private String compilePathPrefix;
	
	/**
	 * 补丁文件输出路径前缀
	 */
	@JComponent(name = "补丁路径" , id = "cachePathPrefix")
	private String cachePathPrefix;
	
	/**
	 * 项目名
	 */
	private String projectNames;
 	
	/**
	 * 补丁展示效果
	 */
	@JComponent(name = "补丁展示" , id = "patchStyle")
	private Integer patchStyle;//1列表，2树状
	
	/**
	 * 项目中文名
	 */
	@JComponent(name = "项目" , id = "projectEnNameList")
	private List<String> projectEnNameList;
	
	/**
	 * 描述：实例化
	 * @author csy 
	 * @date 2020年9月29日 上午9:25:37
	 * @return DeployBean
	 */
	public static DeployBean getBuilder(){
		return new DeployBean();
	}
	
	/**
	 * 描述：数据源
	 * @author csy 
	 * @date 2020年9月29日 上午9:23:02
	 * @param sourcePathPrefix
	 * @return DeployBean
	 */
	public DeployBean sourcePathPrefix(String sourcePathPrefix) {
		this.sourcePathPrefix = sourcePathPrefix;
		return this;
	}
	
	/**
	 * 描述：编译源
	 * @author csy 
	 * @date 2020年9月29日 上午9:23:15
	 * @param compilePathPrefix
	 * @return DeployBean
	 */ 
	public DeployBean compilePathPrefix(String compilePathPrefix) {
		this.compilePathPrefix = compilePathPrefix;
		return this;
	}
	
	/**
	 * 描述：缓存空间
	 * @author csy 
	 * @date 2020年9月29日 上午9:23:28
	 * @param cachePathPrefix
	 * @return DeployBean
	 */
	public DeployBean cachePathPrefix(String cachePathPrefix) {
		this.cachePathPrefix = cachePathPrefix;
		return this;
	}
	
	/**
	 * 描述：项目英文名集合
	 * @author csy 
	 * @date 2020年9月29日 上午9:23:40
	 * @param projectChNameList
	 * @return DeployBean
	 */
	public DeployBean projectEnNameList(List<String> projectEnNameList) {
		this.projectEnNameList = projectEnNameList;
		return this;
	}
	
	/**
	 * 描述：补丁展示格式
	 * @author csy 
	 * @date 2020年9月29日 上午9:23:55
	 * @param patchStyle
	 * @return DeployBean
	 */
	public DeployBean patchStyle(Integer patchStyle) {
		this.patchStyle = patchStyle;
		return this;
	}
	
	/**
	 * 
	 * 描述：项目英文名，多个、隔开
	 * @author csy 
	 * @date 2020年9月29日 上午9:24:24
	 * @param projectNames
	 * @return DeployBean
	 */
	public DeployBean projectNames(String projectNames) {
		this.projectNames = projectNames;
		return this;
	}
	
	/**
	 * 
	 * 描述：读取缓存数据
	 * @author csy
	 * @date 2020年9月28日 下午10:22:40
	 * @Param isNullSetDefault是否设置默认值
	 * @return DeployBean
	 */
	@SuppressWarnings("serial")
	public static DeployBean readCache(boolean isNullSetDefault) {
		
		String contents = DeloyParamCache.get();
		if (contents != null){
			return JSON.toJavaObject(JSON.parseObject(contents), DeployBean.class).projectList2String();
		}else {
			if (isNullSetDefault)
				return DeployBean.getBuilder()
			          .cachePathPrefix(PatchInitConstants.DEFAULT_CACHE_PATH_PREFIX)
			          .sourcePathPrefix(PatchInitConstants.DEFAULT_SOURCE_PATH_PREFIX)
			          .compilePathPrefix(PatchInitConstants.DEFAULT_WORK_PATH_PREFIX)
			          .projectNames(PatchInitConstants.DEFAULT_PROJECT_NAME)
			          .projectEnNameList(new ArrayList<String>(){{this.add(PatchInitConstants.DEFAULT_PROJECT_NAME);}})
			          .patchStyle(1);
		}
		
		return DeployBean.getBuilder();
	}
	
	/**
	 * 描述：projectChNameList 转 projectNames
	 * @author csy 
	 * @date 2020年9月29日 上午9:14:22
	 * @return DeployBean
	 */
	public DeployBean projectList2String(){
		if (CollectionUtils.isNotEmpty(this.projectEnNameList)){
			StringBuilder stringBuilder = new StringBuilder();
			this.projectEnNameList.forEach((e) -> {
				stringBuilder.append(e).append("、");
			});
			this.projectNames = stringBuilder.substring(0, stringBuilder.length() - 1);
		}
		return this;
	}
	
	/**
	 * 描述：projectNames 转 projectChNameList
	 * @author csy 
	 * @date 2020年9月29日 上午9:17:53
	 * @return DeployBean
	 */
	public DeployBean projectString2List(){
		if (StringUtils.isNotBlank(this.projectNames)){
			String[] arr = this.projectNames.split("、");
			this.projectEnNameList = Arrays.asList(arr);
		}
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

	public List<String> getProjectEnNameList() {
		return projectEnNameList;
	}

	public void setProjectEnNameList(List<String> projectEnNameList) {
		this.projectEnNameList = projectEnNameList;
	}

	public Integer getPatchStyle() {
		return patchStyle;
	}

	public void setPatchStyle(Integer patchStyle) {
		this.patchStyle = patchStyle;
	}

	public String getProjectNames() {
		return projectNames;
	}

	public void setProjectNames(String projectNames) {
		this.projectNames = projectNames;
	}
	
}

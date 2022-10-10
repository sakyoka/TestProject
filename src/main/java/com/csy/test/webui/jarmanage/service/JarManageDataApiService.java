package com.csy.test.webui.jarmanage.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.csy.test.commons.jarmanage.bean.JarGobalConfigDto;
import com.csy.test.commons.jarmanage.bean.JarManageDto;
import com.csy.test.commons.result.ResultBean;

/**
 * 
 * 描述：JarManageDataApi业务接口
 * @author csy
 * @date 2022年1月17日 下午2:05:05
 */
public interface JarManageDataApiService {

	/**
	 * 上传jar文件
	 * 上传完之后会返回一个dirUuid，记录这次存储的缓存位置
	 */
	ResultBean fileUpload(String fileName, MultipartFile file);

	/**
	 * 删除jar文件
	 * 可能性：
	 */
	void fileDelete(String jarId, String fileName, String dirUuid, String sufixName);

	/**
	 * 新增或修改jar配置
	 */
	String saveOrUpdateJar(JarManageDto jarManageDto);

	/**
	 * 移除jar配置
	 */
	void removeJar(String jarId);

	/**
	 * 描述：启动jar
	 */
	Map<String, Boolean> runjar(String jarIds);

	/**
	 * 描述：重启
	 */
	void reloadjar(String jarId);

	/**
	 * 描述：停止进程
	 */
	Map<String, Boolean> stopjar(String jarId);

	/**
	 * 描述：下载
	 */
	void download(String jarId, String fileType, HttpServletResponse response);

	/**
	 * 描述：保存全局配置数据
	 */
	void saveGobalConfig(JarGobalConfigDto jarGobalConfigDto);


	/**
	 * 获取jar配置对象
	 */
	Object get(String jarId, boolean reloadProperties);

	/**
	 * 描述：修复jar配置数据
	 */
	void repeat();

	/**
	 * 描述：导出jar配置（jar root目录下所有的文件）
	 */
	void exportconfig(HttpServletResponse response);

	/**
	 * 描述：导入jar配置（jar root目录下所有的文件）
	 */
	ResultBean importconfig(MultipartFile file);

	/**
	 * 描述：清除系统垃圾
	 */
	ResultBean clearSystemRubbish();

	/**
	 * 描述：清除日志文件内容
	 */
	ResultBean emptylog(String jarId);

	/**
	 * 描述：导出jar基础数据（xls）
	 */
	void exportJarBaseData(HttpServletResponse response);
}

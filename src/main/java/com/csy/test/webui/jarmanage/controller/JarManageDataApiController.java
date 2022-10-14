package com.csy.test.webui.jarmanage.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.csy.test.commons.jarmanage.bean.JarGobalConfigDto;
import com.csy.test.commons.jarmanage.bean.JarManageDto;
import com.csy.test.commons.jarmanage.manage.JarBeanHolder;
import com.csy.test.commons.jarmanage.service.JarManageService;
import com.csy.test.commons.jarmanage.utils.JarGobalConfigUtils;
import com.csy.test.commons.result.ResultBean;
import com.csy.test.webui.jarmanage.service.JarManageDataApiService;
import com.csy.test.webui.systemconfig.annotation.Ignore;

/**
 * 
 * 描述：jarManage 接口
 * @author csy
 * @date 2022年1月7日 下午5:52:37
 */
@RequestMapping("/api/jarmanage/")
@RestController
public class JarManageDataApiController {
	
	static JarManageService jarManageService;
	
	static{
		jarManageService = JarBeanHolder.getBean("jarManageService");
	}
	
	@Autowired
	JarManageDataApiService jarManageDataApiService;
	
	/**
	 * 
	 * 描述：获取列表数据集合
	 * @author csy
	 * @date 2022年1月16日 下午6:46:03
	 * @return ResultBean
	 */
	@GetMapping("/list")
	public ResultBean list(){
		return ResultBean.ok("请求成功", jarManageService.getJarManageList());
	}
	
	/**
	 * 描述：根据JarId获取对象
	 * @author csy 
	 * @date 2022年1月24日 上午9:09:02
	 * @param jarId
	 * @return ResultBean
	 */
	@GetMapping("/get")
	public ResultBean get(@RequestParam("jarId")String jarId, 
			@RequestParam(value = "reloadProperties", required = false)boolean reloadProperties){
		return ResultBean.ok("请求成功", jarManageDataApiService.get(jarId, reloadProperties));
	}
	
	/**
	 * 
	 * 描述：保存jar配置
	 * @author csy
	 * @date 2022年1月16日 下午6:46:07
	 * @param jarManageDto
	 * @return ResultBean
	 */
	@PostMapping("/addjar")
	public ResultBean addjar(JarManageDto jarManageDto){
		String jarId = jarManageDataApiService.saveOrUpdateJar(jarManageDto);
		return ResultBean.ok("请求成功", jarId);
	}
	
	/**
	 * 描述：启动jar
	 * @author csy 
	 * @date 2022年1月24日 上午11:40:16
	 * @param jarIds
	 * @return ResultBean
	 */
	@GetMapping("/runjar")
	public ResultBean runJar(@RequestParam("jarIds")String jarIds){
		Map<String, Boolean> result = jarManageDataApiService.runjar(jarIds);
		return ResultBean.ok("请求成功", result);
	}
	
	/**
	 * 描述：重新启动
	 * @author csy 
	 * @date 2022年1月24日 下午1:44:53
	 * @param jarId
	 * @return ResultBean
	 */
	@GetMapping("/reloadjar")
	public ResultBean reloadjar(@RequestParam("jarId")String jarId){
		jarManageDataApiService.reloadjar(jarId);
		return ResultBean.ok("请求成功");
	}
	
	/**
	 * 描述：停止进程
	 * @author csy 
	 * @date 2022年1月24日 下午1:44:59
	 * @param jarId
	 * @return ResultBean
	 */
	@GetMapping("/stopjar")
	public ResultBean stopjar(@RequestParam("jarIds")String jarIds){
		Map<String, Boolean> result = jarManageDataApiService.stopjar(jarIds);
		return ResultBean.ok("请求成功", result);
	}
	
	/**
	 * 描述：移除jar配置
	 * @author csy 
	 * @date 2022年1月19日 上午10:19:56
	 * @param jarManageDto
	 * @return jarIds
	 */
	@PostMapping("/rermovejar")
	public ResultBean rermovejar(@RequestParam("jarIds")String jarIds){
		jarManageDataApiService.removeJar(jarIds);
		return ResultBean.ok("请求成功");
	}
	
	/**
	 * 描述：下载文件
	 * @author csy 
	 * @date 2022年1月24日 下午1:45:03
	 * @param jarId
	 * @param response
	 * @return ResultBean
	 */
	@GetMapping("/download")
	public void download(@RequestParam("jarId")String jarId, 
			@RequestParam(value = "fileType", required = false)String fileType, HttpServletResponse response){
		jarManageDataApiService.download(jarId, fileType, response);
	}
	
	/**
	 * 描述：上传文件
	 * @author csy 
	 * @date 2022年1月17日 下午1:50:28
	 * @param file 文件对象
	 * @return ResultBean
	 */
	@PostMapping("/file/upload")
	@Ignore
	public ResultBean upload(@RequestParam("file")MultipartFile file, @RequestParam("fileName")String fileName){
		return jarManageDataApiService.fileUpload(fileName, file);
	}
	
	/**
	 * 描述：删除文件
	 * @author csy 
	 * @date 2022年1月17日 下午2:56:57
	 * @param jarId 
	 * @param fileName 文件名
	 * @param dirUuid
	 * @param sufixName
	 * @return ResultBean
	 */
	@PostMapping("/file/delete")
	public ResultBean upload(@RequestParam("jarId")String jarId, @RequestParam("fileName")String fileName, 
			@RequestParam("dirUuid")String dirUuid, @RequestParam("sufixName")String sufixName){
		jarManageDataApiService.fileDelete(jarId, fileName, dirUuid, sufixName);
		return ResultBean.ok("删除文件成功");
	}
	
	/**
	 * 描述：保存全局配置数据
	 * @author csy 
	 * @date 2022年2月10日 下午2:23:53
	 * @param jarGobalConfigDto
	 * @return ResultBean
	 */
	@PostMapping("/gobalconfig/save")
	public ResultBean saveGobalConfig(JarGobalConfigDto jarGobalConfigDto){
		jarManageDataApiService.saveGobalConfig(jarGobalConfigDto);
		return ResultBean.ok("保存成功");
	}
	
	/**
	 * 描述：获取全局配置数据
	 * @author csy 
	 * @date 2022年2月10日 下午2:24:16
	 * @return ResultBean
	 */
	@GetMapping("/gobalconfig/get")
	public ResultBean getGobalConfig(){
		return ResultBean.ok("获取数据成功", JarGobalConfigUtils.readConfig());
	}
	
	/**
	 * 描述：修复JAR配置
	 * @author csy 
	 * @date 2022年3月4日 下午2:16:00
	 * @return ResultBean
	 */
	@GetMapping("/repeat")
	public ResultBean repeat(){
		jarManageDataApiService.repeat();
		return ResultBean.ok("请求成功");
	}
	
	/**
	 * 描述：导出配置（jar root目录下所有的文件）
	 * @author csy 
	 * @date 2022年3月4日 下午5:08:41
	 * @param response
	 */
	@GetMapping("/exportconfig")
	public ResultBean exportconfig(HttpServletResponse response){
		jarManageDataApiService.exportconfig(response);
		return ResultBean.ok("请求成功");
	}
	
	/**
	 * 
	 * 描述：导入配置（jar root目录下所有的文件）
	 * @author csy
	 * @date 2022年3月5日 下午8:58:36
	 * @param file
	 * @return ResultBean
	 */
	@PostMapping("/importconfig")
	@Ignore
	public ResultBean importconfig(@RequestParam("file")MultipartFile file){
		return jarManageDataApiService.importconfig(file);
	}
	
	/**
	 * 描述：清除系统垃圾
	 * @author csy 
	 * @date 2022年3月10日 下午4:26:15
	 * @return ResultBean
	 */
	@PostMapping("/clearsystemrubbish")
	public ResultBean clearsystemrubbish(){
		return jarManageDataApiService.clearSystemRubbish();
	}
	
	/**
	 * 
	 * 描述：清空日志内容
	 * @author csy
	 * @date 2022年8月24日 下午3:17:45
	 * @param jarId
	 * @return ResultBean
	 */
	@GetMapping("/emptylog")
	public ResultBean emptylog(@RequestParam("jarId")String jarId){
		return jarManageDataApiService.emptylog(jarId);
	}
	
	/**
	 * 
	 * 描述：导出jar基础数据（xls）
	 * @author csy
	 * @date 2022年9月11日 下午9:56:12
	 * @param response
	 * @return ResultBean
	 */
	@GetMapping("/exportbasedata")
	public void exportJarBaseData(HttpServletResponse response){
		jarManageDataApiService.exportJarBaseData(response);
	}
}

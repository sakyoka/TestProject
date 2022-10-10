package com.csy.test.webui.jarmanage.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.csy.test.commons.excel.utils.ExcelUtils;
import com.csy.test.commons.jarmanage.bean.JarGobalConfigBean;
import com.csy.test.commons.jarmanage.bean.JarGobalConfigDto;
import com.csy.test.commons.jarmanage.bean.JarManageBean;
import com.csy.test.commons.jarmanage.bean.JarManageDto;
import com.csy.test.commons.jarmanage.bean.JarPropertiesBean;
import com.csy.test.commons.jarmanage.constants.BakSufifxNameConstants;
import com.csy.test.commons.jarmanage.constants.JarInitBaseConstants;
import com.csy.test.commons.jarmanage.constants.JarNumberEnum;
import com.csy.test.commons.jarmanage.constants.JarPropertiesTypeEnum;
import com.csy.test.commons.jarmanage.manage.JarBeanHolder;
import com.csy.test.commons.jarmanage.manage.JarManageNeetInit;
import com.csy.test.commons.jarmanage.service.JarManageService;
import com.csy.test.commons.jarmanage.utils.BakFileUtils;
import com.csy.test.commons.jarmanage.utils.JarEntityUtils;
import com.csy.test.commons.jarmanage.utils.JarGobalConfigUtils;
import com.csy.test.commons.result.ResultBean;
import com.csy.test.commons.utils.Objects;
import com.csy.test.commons.utils.PrintUtils;
import com.csy.test.commons.utils.UUID;
import com.csy.test.commons.utils.file.DownloadUtils;
import com.csy.test.commons.utils.file.FileUtils;
import com.csy.test.webui.jarmanage.constants.JarSystemConstant;
import com.csy.test.webui.jarmanage.model.JarBaseDataExportBean;
import com.csy.test.webui.jarmanage.service.JarManageDataApiService;
import com.csy.test.webui.jarmanage.task.impl.AutoClearTask;
import com.csy.test.webui.systemconfig.exception.CommonException;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.log4j.Log4j2;

/**
 * 
 * 描述：JarManageDataApi业务实现类
 * @author csy
 * @date 2022年1月17日 下午2:04:35
 */
@Log4j2
@Service
public class JarManageDataApiServiceImpl implements JarManageDataApiService {
	
	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;
	
	static JarManageService jarManageService;
	
	static{
		jarManageService = JarBeanHolder.getBean("jarManageService");
	}
	
	@SuppressWarnings("resource")
	@Override
	public ResultBean fileUpload(String fileName, MultipartFile file) {
		fileName = this.assertToValidFileName(fileName);
		FileChannel outFileChannel = null;
		String sufixName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
		try (InputStream inputStream = file.getInputStream();
			 ReadableByteChannel inputFileChannel = Channels.newChannel(inputStream);){
			String uuid = UUID.getString();
			//这是缓存文件
			String filePath = this.getFilePath(uuid, JarInitBaseConstants.DEFAULT_CACHE_FILE_NAME, fileName, sufixName);
			FileUtils.ifNotExistsCreate(filePath);
			outFileChannel = new FileOutputStream(new File(filePath)).getChannel();
			outFileChannel.transferFrom(inputFileChannel, 0, file.getSize());
			return ResultBean.ok(uuid);
		} catch (IOException e) {
			throw new CommonException("上传文件失败", e);
		}finally {
			if (outFileChannel != null){
				try {
					outFileChannel.close();
				} catch (IOException e) {
					
				}
			}
		}
	}

	@Override
	public void fileDelete(String jarId, String fileName, String dirUuid, String sufixName) {
		fileName = this.assertToValidFileName(fileName);
		dirUuid = this.assertToValidFileName(dirUuid);
		//先判断jar是否在运行，如果运行不可以移除
		if (StringUtils.isNotBlank(jarId)){
			JarManageBean jarManageBean = jarManageService.get(jarId);
			Objects.isConditionAssert( JarNumberEnum.UN_RUNING.equalValue(jarManageBean.getIsRuning()), 
					CommonException.class, 
					"项目正在运行，请停止项目之后再删除jar文件");
			//删除运行文件
			String filePath = this.getFilePath(dirUuid, JarInitBaseConstants.DEFAULT_JAR_PATH_NAME, fileName, sufixName);
			this.deleteFile(filePath);			
		}
		//删除缓存文件
		String cacheFilePath = this.getFilePath(dirUuid, JarInitBaseConstants.DEFAULT_CACHE_FILE_NAME, fileName, sufixName);
		this.deleteFile(cacheFilePath);

	}

	@Override
	public String saveOrUpdateJar(JarManageDto jarManageDto) {
		
		String jarId = jarManageDto.getJarId();
		String dirUuid = this.assertToValidFileName(jarManageDto.getDirUuid());
		String jarEnName = this.assertToValidFileName(jarManageDto.getJarEnName());
		
		JarManageBean jarManageBean = null;
		if (StringUtils.isNotBlank(jarId)){
			//更新...
			//运行时不可以更新
			jarManageBean = jarManageService.get(jarId);
			Objects.isConditionAssert(JarNumberEnum.UN_RUNING.equalValue(jarManageBean.getIsRuning()), 
					CommonException.class, 
					"当前jar在运行当中不能修改，若修改需要停止后再操作.");
			
			//如果目录不一样把原来的标识为删除
			if (!dirUuid.equals(jarManageBean.getDirUuid())){
				JarEntityUtils.storageSingleEntityCache(jarManageBean, JarNumberEnum.DELETED.getValue());
			}
		}
		
		try {
			String filePath = this.getFilePath(dirUuid, JarInitBaseConstants.DEFAULT_JAR_PATH_NAME, jarEnName, jarManageDto.getSufixName());
			String cacheFilePath = this.getFilePath(dirUuid, JarInitBaseConstants.DEFAULT_CACHE_FILE_NAME, jarEnName, jarManageDto.getSufixName());
			Path cacheFile = Paths.get(cacheFilePath);
			Path jarFile = Paths.get(filePath);
			//如果不存在的时候，复制不了，不替换了
			if (!cacheFile.toFile().exists()){
				//如果运行的jar不存在，直接报异常
				Objects.isConditionAssert(jarFile.toFile().exists(), CommonException.class, 
						"缓存目录文件不存在，转移文件失败，请重新上传jar，缓存文件:" + cacheFilePath);
			}else{
				FileUtils.ifNotExistsCreate(filePath);
				FileUtils.copyTo(cacheFile, jarFile);
			}
			return jarManageService.storageProject(jarManageDto);	
		} catch (Exception e) {
			//如果中途出现异常，需要把标识改回未删除
			if (Objects.notNull(jarManageBean)){
				JarEntityUtils.storageSingleEntityCache(jarManageBean, JarNumberEnum.UN_DELETE.getValue());
			}
			
			throw new CommonException(e);
		}
	}
	
	@Override
	public void removeJar(String jarIds) {
		//移除配置先停止项目,再删除文件，再删除配置
		String[] jarIdArr = jarIds.split(",");
		List<String> jarList = Arrays.asList(jarIdArr);
		jarManageService.removeProjectByJarIds(jarList);
	}
	
	@Override
	public Map<String, Boolean> runjar(String jarIds) {
		Objects.isConditionAssert(StringUtils.isNotBlank(jarIds), 
				CommonException.class, "启动失败，传入相关jarId为空");
		String[] jarIdArr = jarIds.split(",");
		Map<String, Boolean> resultMap = new HashMap<String, Boolean>(jarIdArr.length);
		JarManageBean jarManageBean = null;
		boolean runResult = true;
		for (String jarId:jarIdArr){
			jarManageBean = jarManageService.get(jarId);
			if (JarNumberEnum.RUNING.equalValue(jarManageBean.getIsRuning())){
				//如果运行着的无需启动直接判断运行OK
				runResult = true;
			}else{
				try {
					jarManageService.jarStart(jarId);
					runResult = true;
				} catch (Exception e) {
					//如果启动异常的，判断启动失败
					runResult = false;
					log.error("启动失败", e);
				}
			}
			resultMap.put(jarId, runResult);
		}
		return resultMap;
	}
	
	@Override
	public void reloadjar(String jarId) {
		jarManageService.jarRestart(jarId);
	}

	@Override
	public Map<String, Boolean> stopjar(String jarIds) {
		Objects.isConditionAssert(StringUtils.isNotBlank(jarIds), 
				CommonException.class, "停止失败，传入相关jarId为空");
		String[] jarIdArr = jarIds.split(",");
		Map<String, Boolean> resultMap = new HashMap<String, Boolean>(jarIdArr.length);
		JarManageBean jarManageBean = null;
		boolean stopResult = true;
		for (String jarId:jarIdArr){
			jarManageBean = jarManageService.get(jarId);
			if (JarNumberEnum.UN_RUNING.equalValue(jarManageBean.getIsRuning())){
				//如果运行着的无需启动直接判断运行OK
				stopResult = true;
			}else{
				try {
					jarManageService.jarStop(jarId);
					stopResult = true;
				} catch (Exception e) {
					//如果启动异常的，判断停止失败
					stopResult = false;
					log.error("停止失败", e);
				}
			}
			resultMap.put(jarId, stopResult);
		}
		return resultMap;
	}

	@Override
	public void download(String jarId, String fileType, HttpServletResponse response) {
		String path = null;
		String fileName = null;
		if (JarSystemConstant.FILE_TYPE_JAR.equals(fileType)){
			JarManageBean jarManageBean = jarManageService.get(jarId);
			path = JarInitBaseConstants.getJarRootPath() + File.separator + jarManageBean.getJarPath();
		}else{
			if (JarSystemConstant.isSystemType(jarId)){
				path = JarInitBaseConstants.getSystemLogFilePath();
			}else{
				JarManageBean jarManageBean = jarManageService.get(jarId);
				path = JarInitBaseConstants.getJarRootPath() + File.separator + jarManageBean.getLogPath();
			}
		}
		fileName = path.substring(path.lastIndexOf(File.separator) + 1);
		DownloadUtils.download(response, path, fileName);
	}
	
	@Override
	public void saveGobalConfig(JarGobalConfigDto jarGobalConfigDto) {
		JarGobalConfigBean jarGobalConfigBean = JarGobalConfigUtils.readConfig();
		if (Objects.isNull(jarGobalConfigBean)){
			jarGobalConfigBean = JarGobalConfigBean.builder().build();
			jarGobalConfigBean.setCreateTime(new Date());
		}else{
			jarGobalConfigBean.setUpdateTime(new Date());
		}
		BeanUtils.copyProperties(jarGobalConfigDto, jarGobalConfigBean);
		JarGobalConfigUtils.stoage(jarGobalConfigBean);
		
		AutoClearTask.addOrRemoveClearCacheTask(jarGobalConfigDto.getAutoClearCache());
		AutoClearTask.addOrRemoveClearNoAvailJarTask(jarGobalConfigDto.getAutoClearNoAvailJar());
	}

	private String getFilePath(String dirUuid, String dirName, String fileName, String sufixName){
		return new StringBuilder().append(JarInitBaseConstants.getJarRootPath())
				.append(File.separator).append(dirName)
				.append(File.separator).append(dirUuid)
				.append(File.separator).append(fileName)
				.append(File.separator).append(fileName).append(".").append(sufixName)
				.toString();
	}
	
	private String assertToValidFileName(String fileName){
		Objects.isConditionAssert(StringUtils.isNotBlank(fileName), 
				CommonException.class, "fileName不能为空");
		fileName = fileName.replace("/", "");
		fileName = fileName.replace("\\", "");
		fileName = fileName.replace(" ", "");
		fileName = fileName.replace(" ", "");
		fileName = fileName.trim();
		return fileName;
	}
	
	private void deleteFile(String filePath){
		File file = new File(filePath);
		Objects.isConditionAssert(file.exists(), CommonException.class, 
				PrintUtils.getFormatString("删除文件失败，文件不存在，filePath:%s", filePath));
		Objects.isConditionAssert(file.isFile(), CommonException.class, 
				PrintUtils.getFormatString("删除文件失败，删除路径失败文件，filePath:%s", filePath));
		file.delete();		
	}

	@Override
	public Object get(String jarId, boolean reloadProperties) {
		JarManageBean jarManageBean = jarManageService.get(jarId);
		JarPropertiesBean jarPropertiesBean = jarManageBean.getJarPropertiesBean();
		if (reloadProperties 
				&& Objects.notNull(jarPropertiesBean)
				&& !JarPropertiesTypeEnum.NONE.equalsType(jarPropertiesBean.getPropertiesType())){
			String filePath = JarInitBaseConstants.getJarRootPath() + jarManageBean.getJarDir() 
			     + JarPropertiesTypeEnum.getPropertiesName(jarPropertiesBean.getPropertiesType());
			String propertiesContent = FileUtils.read(filePath);
			jarPropertiesBean.setPropertiesContent(propertiesContent);
		}
		return jarManageBean;
	}

	@Override
	public synchronized void repeat() {
		jarManageService.repeat();
	}

	@Override
	public void exportconfig(HttpServletResponse response) {
		
		//这个业务不写在jarManageService，不对jar，而是整体
		String rootDir = JarInitBaseConstants.getJarRootPath();
		
		//导出前先把cacheUpload下文件删除，减少导出数据
		FileUtils.deletesExceptRoot(rootDir + File.separator + JarInitBaseConstants.DEFAULT_CACHE_FILE_NAME);
		
		String bakDir = JarInitBaseConstants.getJarRootPath() 
				+ File.separator + JarInitBaseConstants.DEFAULT_BAK_PATH_NAME + File.separator
				+ JarInitBaseConstants.DEFATUL_EXPORT_NAME;
		String filePath = bakDir + File.separator + BakSufifxNameConstants.SUFFIX_JAR_MANAGE;
		FileUtils.ifNotExistsCreate(filePath);
		//压缩文件夹rootDir 转存到 filePath
		FileUtils.compressZip(filePath, rootDir);
		
		//下载
		DownloadUtils.download(response, filePath, BakSufifxNameConstants.SUFFIX_JAR_MANAGE);
		
		//删除
		FileUtils.deletes(bakDir);
	}

	@Override
	public synchronized ResultBean importconfig(MultipartFile file) {
		//输出目录
		String bakDir = JarInitBaseConstants.getJarRootPath() 
				+ File.separator + JarInitBaseConstants.DEFAULT_BAK_PATH_NAME + File.separator
				+ JarInitBaseConstants.DEFAULT_IMPORT_NAME;
		String fileName = file.getOriginalFilename();
		//文件
		String filePath = bakDir + File.separator + fileName;
		
		//创建
		FileUtils.ifNotExistsCreate(filePath);
		
		//写入文件
		try (InputStream inputStream = file.getInputStream();){
			FileUtils.writeFile(filePath, inputStream);
		} catch (IOException e) {
			//失败需要删除目录
			FileUtils.deletes(bakDir);
			throw new CommonException("导入配置失败", e);
		}
		
		//解压导入文件
		try {
			FileUtils.uncompressZip(filePath, bakDir);
			/*
			 * 处理：gobalConfig.json、allJarEntity.json 
			 */
			String uploadDirName = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));
			String rootUploadDir = bakDir + File.separator + uploadDirName;//这就是解压后的目录
			
			this.disposeBakGobalConfig(rootUploadDir);
			
			this.disposeBakAllEntityConfig(rootUploadDir);
		} finally {
			//删除导入目录
			FileUtils.deletes(bakDir);
		}
		return ResultBean.ok("导入成功!");
	}
	
	/**
	 * 
	 * 描述：处理全局配置
	 * @author csy
	 * @date 2022年3月6日 下午2:55:54
	 * @param rootUploadDir
	 */
	private void disposeBakGobalConfig(String rootUploadDir) {
		log.debug("开始处理全局配置内容...");
		String bakGobalConfigFilePath = rootUploadDir + File.separator + JarInitBaseConstants.DEFAULT_GOBALCONFIG_FILE_NAME;
		File file = new File(bakGobalConfigFilePath);
		if (!file.exists()) {
			log.debug(PrintUtils.getFormatString("导入文件中，全局配置文件不存在，不进行处理全局配置，path:%s", bakGobalConfigFilePath));
			return ;
		}
		String content = FileUtils.read(file);
		if (StringUtils.isBlank(content)) {
			log.debug(PrintUtils.getFormatString("导入文件中，全局配置内容为空，不进行处理全局配置，path:%s", bakGobalConfigFilePath));
			return ;
		}
		JarGobalConfigBean jarGobalConfigBean = 
				JSON.toJavaObject(JSON.parseObject(content), JarGobalConfigBean.class);
		BakFileUtils.bakGobalConfigFile(BakSufifxNameConstants.SUFFIX_GOBALFONCIG, 5);
		JarGobalConfigUtils.stoage(jarGobalConfigBean);
	}
	
	/**
	 * 
	 * 描述：处理jar实例
	 * @author csy
	 * @date 2022年3月6日 下午2:56:42
	 * @param rootUploadDir
	 */
	private void disposeBakAllEntityConfig(String rootUploadDir) {
		log.debug("开始处理jar实例内容...");
		String bakAllEntityFilePath = rootUploadDir + File.separator + JarInitBaseConstants.DEFAULT_ALLJARENTITY_FILE_NAME;
		File file = new File(bakAllEntityFilePath);
		if (!file.exists()) {
			log.debug(PrintUtils.getFormatString("导入文件中，jar配置文件不存在，不进行处理jar配置，path:%s", bakAllEntityFilePath));
			return ;
		}
		String content = FileUtils.read(file);
		if (StringUtils.isBlank(content)) {
			log.debug(PrintUtils.getFormatString("导入文件中，jar容为空，不进行处理jar配置，path:%s", bakAllEntityFilePath));
			return ;
		}
		
		@SuppressWarnings("unchecked")
		Map<String, String> localContectMap = JSON.parseObject(content, Map.class);
		Map<String, JarManageBean> bakJarManageBeanMap = new HashMap<String, JarManageBean>(localContectMap.size());
		Set<Entry<String, String>> sets = localContectMap.entrySet();
		for (Entry<String, String> set:sets){
			bakJarManageBeanMap.put(set.getKey(), JSON.toJavaObject((JSON)JSON.toJSON(set.getValue()), JarManageBean.class));
		}
		
		Map<String, JarManageBean> cacheJarManageBeanMap = JarEntityUtils.getUnmodifiableMap();
		List<String> jarNamesList = new ArrayList<String>();
		bakJarManageBeanMap.forEach((k, v) -> {
			//在现在的存储中不存在
			if (!cacheJarManageBeanMap.containsKey(k)) {
				//迁移jarLog、jarStorage
				//jarLog
				String bakJarLogDir = rootUploadDir + File.separator 
						+ JarInitBaseConstants.DEFAULT_LOG_PATH_NAME + File.separator + v.getDirUuid();
				String jarLogDir = JarInitBaseConstants.getJarLogDir() + File.separator + v.getDirUuid();
				FileUtils.ifNotExistsCreate(jarLogDir);
				FileUtils.copyTo(Paths.get(bakJarLogDir), Paths.get(jarLogDir));
				
				//jarStorage
				String bakJarStorageDir = rootUploadDir + File.separator
						+ JarInitBaseConstants.DEFAULT_JAR_PATH_NAME + File.separator + v.getDirUuid();
				String jarStorageDir = JarInitBaseConstants.getJarStorageDir() + File.separator + v.getDirUuid();
				FileUtils.ifNotExistsCreate(jarStorageDir);
				FileUtils.copyTo(Paths.get(bakJarStorageDir), Paths.get(jarStorageDir));
				
				JarEntityUtils.storage(v);
				
				jarNamesList.add(v.getJarId() + "(" +v.getJarChName() + ")");
			}
		});
		
		if (jarNamesList.isEmpty()) {
			log.debug("本次导入没有需要修改jar配置内容.");
		}else {
			log.debug(PrintUtils.getFormatString("本次导入共有：%s 个jar实例，有：%s", jarNamesList.size(), jarNamesList));
			//刷新状态
			log.debug("开始刷新jar状态...");
			JarManageNeetInit.checkJarState(true);
		}
	}

	@Override
	public ResultBean clearSystemRubbish() {
		AutoClearTask.clearCache();
		AutoClearTask.clearNoAvailJar();
		return ResultBean.ok(null);
	}
	
	@Override
	public ResultBean emptylog(String jarId) {
		String logPath = null;
		if (JarSystemConstant.isSystemType(jarId)){
			logPath = JarInitBaseConstants.getSystemLogFilePath();
		}else{
			JarManageBean jarManageBean = JarEntityUtils.get(jarId);
			logPath = JarInitBaseConstants.getJarRootPath() + jarManageBean.getLogPath();
		}

		File logFile = new File(logPath);
		if (logFile.exists()){
			FileUtils.writeFile(logFile, "", false);
			log.info("主动清空文件内容:" + logPath);
			simpMessagingTemplate.convertAndSend("/topic/emptylog", jarId);
		}	
		return ResultBean.ok(null); 
	}

	@Override
	public void exportJarBaseData(HttpServletResponse response) {
		List<JarManageBean> jarManageBeans = jarManageService.getJarManageList();
		List<JarBaseDataExportBean> jarBaseDataExportBeans = 
				new ArrayList<JarBaseDataExportBean>(jarManageBeans.size());
		jarManageBeans.forEach((e) -> {
			JarBaseDataExportBean jarBaseDataExportBean = new JarBaseDataExportBean();
			BeanUtil.copyProperties(e, jarBaseDataExportBean);
			jarBaseDataExportBeans.add(jarBaseDataExportBean);
		});
		String fileName = "JAR基础数据" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + ".xls";
		ExcelUtils.exportExcel(response, fileName, jarBaseDataExportBeans, JarBaseDataExportBean.class);
	}
}

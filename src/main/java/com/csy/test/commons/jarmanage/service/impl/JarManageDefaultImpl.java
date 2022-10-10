package com.csy.test.commons.jarmanage.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson.JSON;
import com.csy.test.commons.jarmanage.bean.JarJdkManageBean;
import com.csy.test.commons.jarmanage.bean.JarManageBean;
import com.csy.test.commons.jarmanage.bean.JarManageDto;
import com.csy.test.commons.jarmanage.bean.JarPropertiesBean;
import com.csy.test.commons.jarmanage.constants.BakSufifxNameConstants;
import com.csy.test.commons.jarmanage.constants.JarInitBaseConstants;
import com.csy.test.commons.jarmanage.constants.JarNumberEnum;
import com.csy.test.commons.jarmanage.constants.JarPropertiesTypeEnum;
import com.csy.test.commons.jarmanage.manage.JarManageNeetInit;
import com.csy.test.commons.jarmanage.service.JarManageService;
import com.csy.test.commons.jarmanage.utils.BakFileUtils;
import com.csy.test.commons.jarmanage.utils.JarEntityUtils;
import com.csy.test.commons.jarmanage.utils.JarTaskUtils;
import com.csy.test.commons.utils.Objects;
import com.csy.test.commons.utils.PrintUtils;
import com.csy.test.commons.utils.file.FileUtils;

import lombok.extern.log4j.Log4j2;

/**
 * 
 * 描述：jar包管理
 * @author csy
 * @date 2022年1月7日 下午3:40:04
 */
@Log4j2
public class JarManageDefaultImpl implements JarManageService{
	
	@Override
	public String storageProject(JarManageDto jarManageDto) {
		JarManageBean jarManageBean = JarManageBean.builder().build();
		BeanUtils.copyProperties(jarManageDto, jarManageBean);
		jarManageBean.setJarJdkManageBean(JarJdkManageBean.builder()
						                          .jdkPath(jarManageDto.getJarJdkManage().getJdkPath())
						                          .jvm(jarManageDto.getJarJdkManage().getJvm())
						                          .build());
		jarManageBean.setJarPropertiesBean(this.getSaveJavaPropertiesBean(jarManageDto));
		jarManageBean.preStroage();
		JarEntityUtils.storage(jarManageBean);
		return jarManageBean.getJarId();
	}

	/**
	 * 描述：获取保存的配置文件对象
	 * @author csy 
	 * @date 2022年3月2日 上午10:37:16
	 * @param jarManageDto
	 * @return JarPropertiesBean
	 */
	private JarPropertiesBean getSaveJavaPropertiesBean(JarManageDto jarManageDto) {
		//运行的jar文件夹
		String jarDirPath = JarInitBaseConstants.getJarRootPath() + File.separator 
				+ JarInitBaseConstants.DEFAULT_JAR_PATH_NAME + File.separator
				+ jarManageDto.getDirUuid() + File.separator + jarManageDto.getJarEnName() + File.separator;
		File jarDir = new File(jarDirPath);
		Objects.isConditionAssert(jarDir.exists(), RuntimeException.class, "处理文件失败，目录不存在.dir:" + jarDir);
		
		BakFileUtils.juageFileNumberThenDeletes(jarDir, BakSufifxNameConstants.SUFFIX_PROPERTIES, 5, (path, paths) -> {
			if (path.endsWith(JarPropertiesTypeEnum.BOOTSTRAP_PROPERTIES.getPropertiesName()) 
					|| path.endsWith(JarPropertiesTypeEnum.BOOTSTRAP_YML.getPropertiesName())
					|| path.endsWith(JarPropertiesTypeEnum.APPLICATION_PROPERTIES.getPropertiesName())
					|| path.endsWith(JarPropertiesTypeEnum.APPLICATION_YML.getPropertiesName())){
				//备份-删除
				String newPath = path + BakSufifxNameConstants.SUFFIX_PROPERTIES 
						+ DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
				FileUtils.copyTo(Paths.get(path), Paths.get(newPath));
				FileUtils.deletes(path);
			}
		});
		
		Integer propertiesType = jarManageDto.getPropertiesType();
		if (Objects.isNull(propertiesType) 
				|| JarPropertiesTypeEnum.NONE.equalsType(propertiesType)){
			//返回默认无
			return JarPropertiesBean.DETAULT_NONE;
		}
		//保存文件
		String filePath = jarDirPath + JarPropertiesTypeEnum.getPropertiesName(propertiesType);
		//配置内容不保存在对象，保存在文件
		FileUtils.writeFile(filePath, jarManageDto.getPropertiesContent());
		//返回类型对象
		return JarPropertiesBean.builder().propertiesType(propertiesType).build();
	}

	@Override
	public void removeProjectByJarIds(List<String> jarIds) {
		for (String jarId:jarIds) {
			JarManageBean jarManageBean = JarEntityUtils.get(jarId);
			Objects.notNullAssert(jarManageBean, "删除失败，获取jar配置对象失败");
			//运行中的不能删除操作
			if (JarNumberEnum.RUNING.equalValue(jarManageBean.getIsRuning())){
				throw new RuntimeException("应用（"+ jarManageBean.getJarChName() +"）正在运行，删除失败");
			}
			//移除配置
			JarEntityUtils.remove(jarId);			
		}
	}
		
	@Override
	public void jarStart(String jarId) {
		JarTaskUtils.start(jarId);
	}

	@Override
	public void jarStop(String jarId) {
		JarTaskUtils.kill(jarId);
	}
	
	@Override
	public void jarRestart(String jarId) {
		JarTaskUtils.restart(jarId);
	}

	@Override
	public void jarAllStop() {
		Map<String, JarManageBean> jarsMap = JarEntityUtils.getUnmodifiableMap();
		jarsMap.forEach((k, v) -> {
			this.jarStop(k);
		});
	}

	@Override
	public void jarAllStart() {
		Map<String, JarManageBean> jarsMap = JarEntityUtils.getUnmodifiableMap();
		jarsMap.forEach((k, v) -> {
			this.jarStart(k);
		});
	}

	@Override
	public List<JarManageBean> getJarManageList() {
		Map<String, JarManageBean> jarsMap = JarEntityUtils.getUnmodifiableMap();
		List<JarManageBean> jarManageBeans = new ArrayList<JarManageBean>(jarsMap.size());
		jarsMap.forEach((k, v) -> {jarManageBeans.add(v);});
		Collections.sort(jarManageBeans, (o1, o2) -> {
			Integer orderNumber1 = o1.getOrderNumber();
			Integer orderNumber2 = o2.getOrderNumber();
			if (orderNumber1 == null && orderNumber2 == null){
				return 0;
			}
			if (orderNumber1 == null && orderNumber2 != null){
				return -1;
			}
			if (orderNumber1 != null && orderNumber2 == null){
				return 1;
			}
			if (orderNumber1 > orderNumber2){
				return 1;
			}
			return -1;
		});
		return jarManageBeans;
	}

	@Override
	public void addJDKConfigForJar(String jarId, JarJdkManageBean jarJdkManageBean) {
		JarManageBean jarManageBean = JarEntityUtils.get(jarId);
		jarManageBean.setJarJdkManageBean(jarJdkManageBean);
		JarEntityUtils.storage(jarManageBean);
	}

	@Override
	public JarManageBean get(String jarId) {
		return JarEntityUtils.get(jarId);
	}

	@Override
	public void repeat() {
		
		List<JarManageBean> jarManageBeans = null;
		try {
			jarManageBeans = this.getUnDeleteSingleJarConfig();
		} catch (Exception e) {
			throw new RuntimeException("查找jar配置过程存在异常", e);
		}
		
		if (jarManageBeans.isEmpty()){
			log.debug("本次修复jar配置，没有找到没有被删除数据。");
			return ;
		}
		
		log.debug(PrintUtils.getFormatString("本次共检测到：%s条数据.", jarManageBeans.size()));
		
		log.debug("开始备份allJarEnity.json文件");
		BakFileUtils.bakAllEntityFile(BakSufifxNameConstants.SUFFIX_ENTITY_REPEAT, 5);
		log.debug("备份allJarEnity.json文件完毕。");
		
		log.debug("开始更新配置...");
		jarManageBeans.forEach((e) -> {
			JarEntityUtils.storage(e);
		});
		
		log.debug("重新检测JAR状态...");
		JarManageNeetInit.checkJarState(true);
	}
	
	private List<JarManageBean> getUnDeleteSingleJarConfig() throws IOException{
		List<JarManageBean> jarManageBeans = new ArrayList<JarManageBean>();
		String jarDirPath = JarInitBaseConstants.getJarRootPath() + File.separator + JarInitBaseConstants.DEFAULT_JAR_PATH_NAME;
		log.debug(PrintUtils.getFormatString("开始扫描目录：%s下的jar配置", jarDirPath));
		Files.walkFileTree(Paths.get(jarDirPath), new SimpleFileVisitor<Path> (){	
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				String filePath = file.toFile().getAbsolutePath();
				//entity.json文件
				if (filePath.endsWith(JarInitBaseConstants.DETAULT_SINGLE_ENTITY_NAME)){
					String contents = FileUtils.read(filePath);
					JarManageBean jarManageBean = JSON.toJavaObject(JSON.parseObject(contents), JarManageBean.class);
					//收集没有删除标识的jar配置
					if (JarNumberEnum.UN_DELETE.equalValue(jarManageBean.getDeleteFlag())){
						jarManageBeans.add(jarManageBean);
					}
				}
				return FileVisitResult.CONTINUE;
			}
			
		});
		return jarManageBeans;	
	}
}

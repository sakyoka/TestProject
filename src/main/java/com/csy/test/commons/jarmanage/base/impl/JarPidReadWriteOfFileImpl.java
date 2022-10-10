package com.csy.test.commons.jarmanage.base.impl;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.csy.test.commons.jarmanage.base.JarReadWriteBase;
import com.csy.test.commons.jarmanage.constants.JarInitBaseConstants;
import com.csy.test.commons.utils.file.FileUtils;

/**
 * 
 * 描述：jarPid存储读取方案(写入文件)
 * @author csy
 * @date 2022年1月13日 上午11:46:33
 */
public class JarPidReadWriteOfFileImpl implements JarReadWriteBase<Map<String, String>> {

	@Override
	public void write(Map<String, String> jarPidMap) {
		String contents = JSON.toJSONString(jarPidMap, SerializerFeature.PrettyFormat, SerializerFeature.WriteDateUseDateFormat);
		FileUtils.writeFile(JarInitBaseConstants.getJarProcessFilePath(), contents);		
	}

	@Override
	public Map<String, String> read(Map<String, String> resultMap) {
		String content = FileUtils.read(JarInitBaseConstants.getJarProcessFilePath());
		if (StringUtils.isBlank(content)){
			return resultMap;
		}
		
		@SuppressWarnings("unchecked")
		Map<String, String> localContectMap = JSON.parseObject(content, Map.class);
		resultMap.putAll(localContectMap);
		
		this.write(resultMap);
		return resultMap;
	}

}

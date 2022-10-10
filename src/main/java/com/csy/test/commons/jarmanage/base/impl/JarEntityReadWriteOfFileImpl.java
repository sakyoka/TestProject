package com.csy.test.commons.jarmanage.base.impl;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.csy.test.commons.jarmanage.base.JarReadWriteBase;
import com.csy.test.commons.jarmanage.bean.JarManageBean;
import com.csy.test.commons.jarmanage.constants.BakSufifxNameConstants;
import com.csy.test.commons.jarmanage.constants.JarInitBaseConstants;
import com.csy.test.commons.jarmanage.utils.BakFileUtils;
import com.csy.test.commons.utils.Objects;
import com.csy.test.commons.utils.file.FileUtils;

/**
 * 
 * 描述：jarEntity存储读取方案(写入文件)
 * @author csy
 * @date 2022年1月13日 上午11:46:33
 */
public class JarEntityReadWriteOfFileImpl implements JarReadWriteBase<Map<String, JarManageBean>> {

	@Override
	public void write(Map<String, JarManageBean> jarEntiryMap) {
		String contents = JSON.toJSONString(jarEntiryMap, SerializerFeature.PrettyFormat, SerializerFeature.WriteDateUseDateFormat);
		String jarEntityFilePath = JarInitBaseConstants.getJarEntityFilePath();
		
		BakFileUtils.bakAllEntityFile(BakSufifxNameConstants.SUFFIX_ENTITY, 10);

		FileUtils.writeFile(jarEntityFilePath, contents);		
	}

	@Override
	public Map<String, JarManageBean> read(Map<String, JarManageBean> resultMap) {
		String content = FileUtils.read(JarInitBaseConstants.getJarEntityFilePath());
		if (StringUtils.isBlank(content)){
			return resultMap;
		}
		
		@SuppressWarnings("unchecked")
		Map<String, String> localContectMap = JSON.parseObject(content, Map.class);
		if (Objects.notNull(localContectMap)){
			Set<Entry<String, String>> sets = localContectMap.entrySet();
			for (Entry<String, String> set:sets){
				resultMap.put(set.getKey(), JSON.toJavaObject((JSON)JSON.toJSON(set.getValue()), JarManageBean.class));
			}
		}
		
		this.write(resultMap);
		return resultMap;
	}

}

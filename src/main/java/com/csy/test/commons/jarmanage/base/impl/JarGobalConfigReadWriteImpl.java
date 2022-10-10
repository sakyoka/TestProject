package com.csy.test.commons.jarmanage.base.impl;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.csy.test.commons.jarmanage.base.JarReadWriteBase;
import com.csy.test.commons.jarmanage.bean.JarGobalConfigBean;
import com.csy.test.commons.jarmanage.constants.JarInitBaseConstants;
import com.csy.test.commons.utils.file.FileUtils;

/**
 * 
 * 描述：全局参数读写实现类(写入文件)
 * @author csy
 * @date 2022年1月13日 下午2:24:54
 */
public class JarGobalConfigReadWriteImpl implements JarReadWriteBase<JarGobalConfigBean> {

	@Override
	public void write(JarGobalConfigBean writeObject) {
		String contents = JSON.toJSONString(writeObject, SerializerFeature.PrettyFormat, SerializerFeature.WriteDateUseDateFormat);
		FileUtils.writeFile(JarInitBaseConstants.getJarGobalConfigFilePath(), contents);	
	}

	@Override
	public JarGobalConfigBean read(JarGobalConfigBean readObject) {
		String content = FileUtils.read(JarInitBaseConstants.getJarGobalConfigFilePath());
		if (StringUtils.isBlank(content)){
			return JarGobalConfigBean.builder().build();
		}

		JarGobalConfigBean localJarGobalConfigBean = JSON.parseObject(content, JarGobalConfigBean.class);

		return localJarGobalConfigBean;
	}

}

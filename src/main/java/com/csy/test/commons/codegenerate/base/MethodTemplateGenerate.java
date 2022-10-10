package com.csy.test.commons.codegenerate.base;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.csy.test.commons.codegenerate.base.bean.CodeGenerateBaseInitParams;
import com.csy.test.commons.codegenerate.bean.CodeGenerateParams;
import com.csy.test.commons.codegenerate.bean.MethodGenerateRecord;
import com.csy.test.commons.codegenerate.constants.ClassifyConstants;
import com.csy.test.commons.codegenerate.database.bean.base.DataMetaBase;
import com.csy.test.commons.database.bean.TableMessage;
import com.csy.test.commons.utils.StrUtil;

/**
 * 
 * 描述：方法模板的生成
 * @author csy
 * @date 2022年3月15日 下午12:52:40
 */
public interface MethodTemplateGenerate {
	
	/**
	 * 描述：排序值(越小越前面)
	 * @author csy 
	 * @date 2022年7月5日 上午10:40:05
	 * @return
	 */
	int order();
	
	/**
	 * 描述：方法模板生成
	 * @author csy 
	 * @date 2022年3月15日 下午12:54:33
	 * @param codeGenerateBaseInitParams
	 * @param codeGenerateParams 
	 * @return MethodGenerateRecord 模板记录对象
	 */
	MethodGenerateRecord generateMethodTemplate(
			CodeGenerateBaseInitParams codeGenerateBaseInitParams, CodeGenerateParams codeGenerateParams); 
	
	/**
	 * 描述：公共参数值
	 * @author csy 
	 * @date 2022年7月5日 下午3:30:43
	 * @param codeGenerateBaseInitParams
	 * @param codeGenerateParams
	 * @return Map<String, String>
	 */
	default Map<String, String> getCommonParam(CodeGenerateBaseInitParams codeGenerateBaseInitParams,
			CodeGenerateParams codeGenerateParams){
		DataMetaBase dataMetaBase = codeGenerateParams.getDataMetaBase();
		TableMessage tableMessage = dataMetaBase.getTableMessage();
		
		Map<String, String> tempValueMap = new HashMap<String, String>(14);
		
		String beanName = codeGenerateBaseInitParams.getBeanName();
		String modelName = codeGenerateBaseInitParams.getFullBeanName();
		tempValueMap.put("modelObject", modelName);
		tempValueMap.put("modelObjectParam", StrUtil.lowerFirst(modelName));
		
		String tableRemarks = tableMessage.getRemarks();
		tempValueMap.put("tableRemarks", tableRemarks);
		tempValueMap.put("author", codeGenerateParams.getAuthor());
		tempValueMap.put("createTime", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		
		String queryObjectBeanName = beanName + StrUtil.upperFirst(ClassifyConstants.QUERY);
		tempValueMap.put("queryObjectParam", StrUtil.lowerFirst(queryObjectBeanName));
		tempValueMap.put("queryObject", queryObjectBeanName);
		
		String voObjectBeanName = beanName + StrUtil.upperFirst(ClassifyConstants.VO);
		tempValueMap.put("voObject", voObjectBeanName);
		tempValueMap.put("voObjectParam", StrUtil.lowerFirst(voObjectBeanName));
		
		String dtoObjectBeanName = beanName + StrUtil.upperFirst(ClassifyConstants.DTO);
		tempValueMap.put("dtoObjectParam", StrUtil.lowerFirst(dtoObjectBeanName));
		tempValueMap.put("dtoObject", dtoObjectBeanName);
		
		String primaryKey = tableMessage.getPrimaryKey();
		tempValueMap.put("primaryKey", StrUtil.toCamelCase(primaryKey.toLowerCase()));
		tempValueMap.put("primaryKeyGet", "get" + StrUtil.upperFirst(primaryKey.toLowerCase()));
		
		String daoName = codeGenerateBaseInitParams.getFullDaoName();
		tempValueMap.put("daoObject", daoName);
		tempValueMap.put("daoObjectParam", StrUtil.lowerFirst(daoName));
		return tempValueMap;
	}
}

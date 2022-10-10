package com.csy.test.commons.codegenerate.base.defaults.methodtemplate.bean;

import java.util.ArrayList;
import java.util.List;

import com.csy.test.commons.codegenerate.base.MethodTemplateGenerate;
import com.csy.test.commons.codegenerate.base.bean.CodeGenerateBaseInitParams;
import com.csy.test.commons.codegenerate.bean.BeanFieldMessage;
import com.csy.test.commons.codegenerate.bean.CodeGenerateParams;
import com.csy.test.commons.codegenerate.bean.MethodGenerateRecord;
import com.csy.test.commons.codegenerate.constants.LineConstants;
import com.csy.test.commons.codegenerate.database.bean.base.DataMetaBase;
import com.csy.test.commons.codegenerate.database.util.DataMetaUtils;
import com.csy.test.commons.utils.StrUtil;

/**
 * 
 * 描述：bean生成
 * @author csy
 * @date 2022年7月5日 下午2:47:26
 */
public class BeanMethodCommonImpl implements MethodTemplateGenerate{

	@Override
	public int order() {
		return 0;
	}

	@SuppressWarnings("serial")
	@Override
	public MethodGenerateRecord generateMethodTemplate(CodeGenerateBaseInitParams codeGenerateBaseInitParams,
			CodeGenerateParams codeGenerateParams) {
		DataMetaBase dataMetaBase = codeGenerateParams.getDataMetaBase();
		StringBuilder stringBuilder = new StringBuilder();
		List<BeanFieldMessage> beanFieldMessages = DataMetaUtils.tranferToBeanFields(dataMetaBase.getColumnMetaDatas());
		beanFieldMessages.forEach((e) -> {
			stringBuilder.append(LineConstants.BLANK_SPACE_FOUR)
			             .append("/** ").append(StrUtil.removeAllLineBreaks(e.getRemarks())).append("*/").append(LineConstants.WRAP)
			             .append(LineConstants.BLANK_SPACE_FOUR)
			             .append("private ").append(e.getFieldType()).append(" ").append(e.getFieldName()).append(";")
			             .append(LineConstants.WRAP).append(LineConstants.WRAP);
		}); 
		
		beanFieldMessages.forEach((e) -> {
			stringBuilder.append(LineConstants.BLANK_SPACE_FOUR)
			             .append("public ").append(e.getFieldType()).append(" ")
			             .append("get").append(StrUtil.upperFirst(e.getFieldName())).append("(){")
			             .append(LineConstants.WRAP)
			             .append(LineConstants.BLANK_SPACE_FOUR).append(LineConstants.BLANK_SPACE_FOUR)
			             .append("return ") .append(e.getFieldName()) .append(";").append(LineConstants.WRAP)
			             .append(LineConstants.BLANK_SPACE_FOUR)
			             .append("}")
			             .append(LineConstants.WRAP)
			             .append(LineConstants.WRAP);
			
			stringBuilder.append(LineConstants.BLANK_SPACE_FOUR)
			             .append("public void set").append(StrUtil.upperFirst(e.getFieldName())).append("(").append(e.getFieldType()).append(" ").append(e.getFieldName()).append("){")
			             .append(LineConstants.WRAP)
			             .append(LineConstants.BLANK_SPACE_FOUR)
			             .append(LineConstants.BLANK_SPACE_FOUR)
			             .append("this.").append(e.getFieldName()).append(" = ").append(e.getFieldName()).append(";")
			             .append(LineConstants.WRAP)
			             .append(LineConstants.BLANK_SPACE_FOUR)
			             .append("}")
			             .append(LineConstants.WRAP)
			             .append(LineConstants.WRAP);
		}); 
		return new MethodGenerateRecord(new ArrayList<String>(){{
			this.add("java.util.*");
		}}, stringBuilder.toString());
	}

}

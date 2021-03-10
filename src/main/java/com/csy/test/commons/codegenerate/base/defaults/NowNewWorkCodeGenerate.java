package com.csy.test.commons.codegenerate.base.defaults;

import java.util.List;

import com.csy.test.commons.codegenerate.base.CodeGenerateBase;
import com.csy.test.commons.codegenerate.base.bean.CodeGenerateBaseInitParams;
import com.csy.test.commons.codegenerate.base.utils.NoteStringUitls;
import com.csy.test.commons.codegenerate.bean.BeanFieldMessage;
import com.csy.test.commons.codegenerate.bean.CodeGenerateParams;
import com.csy.test.commons.codegenerate.constants.FileSuffixEnum;
import com.csy.test.commons.codegenerate.constants.LineConstants;
import com.csy.test.commons.codegenerate.database.bean.base.DataMetaBase;
import com.csy.test.commons.codegenerate.database.util.DataMetaUtils;
import com.csy.test.commons.utils.Objects;
import com.csy.test.commons.utils.StrUtil;

/**
 * 
 * 描述：基于现在最新的项目生成代码
 * @author csy
 * @date 2021年3月10日 上午11:39:41
 */
public class NowNewWorkCodeGenerate implements CodeGenerateBase {

	private CodeGenerateParams codeGenerateParams;
	
	private DefaultCodeGenerate defaultCodeGenerate;
	
	private CodeGenerateBaseInitParams codeGenerateBaseInitParams;
	
	private DataMetaBase dataMetaBase;
	
	private List<BeanFieldMessage> beanFieldMessages;
	
	public NowNewWorkCodeGenerate(CodeGenerateParams codeGenerateParams) {
		this.codeGenerateParams = codeGenerateParams;
	}
	
	@Override
	public CodeGenerateBase preInit(DataMetaBase dataMetaBase) {
		
		this.dataMetaBase = dataMetaBase;
		
		this.beanFieldMessages = DataMetaUtils.tranferToBeanFields(dataMetaBase.getColumnMetaDatas());
		
		this.defaultCodeGenerate = new DefaultCodeGenerate(this.codeGenerateParams);
		this.defaultCodeGenerate.preInit(dataMetaBase);
		
		this.codeGenerateBaseInitParams = this.initCodeGenerateBaseInitParams(this.codeGenerateParams, dataMetaBase);
		return this;
	}

	@Override
	public CodeGenerateBase generateBean() {
		
		Objects.notNullAssert(this.codeGenerateBaseInitParams.getTableName(), "tableName is not allow null");
		
		String beanPath = codeGenerateBaseInitParams.getBeanPath();
		String fullBeanName = codeGenerateBaseInitParams.getFullBeanName();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder
			.append("package ").append(beanPath).append(";")
			.append(LineConstants.WRAP).append(LineConstants.WRAP)
		    .append("import java.util.*;")
		    .append(LineConstants.WRAP)
		    .append("import lombok.Builder;")
		    .append(LineConstants.WRAP)
		    .append("import lombok.Data;")
		    .append(LineConstants.WRAP)
		    .append("import io.swagger.annotations.ApiModelProperty;")
		    .append(LineConstants.WRAP).append(LineConstants.WRAP);
		NoteStringUitls.appendClassNote(stringBuilder , codeGenerateParams.getAuthor(), dataMetaBase.getTableMessage().getRemarks() + "实体类")
		    .append("@Data")
		    .append(LineConstants.WRAP)
		    .append("@Builder")
		    .append(LineConstants.WRAP)
			.append("public class ").append(fullBeanName)
			.append(" {").append(LineConstants.WRAP).append(LineConstants.WRAP);
		
		this.beanFieldMessages.forEach((e) -> {
			stringBuilder.append(LineConstants.BLANK_SPACE_FOUR)
			            .append("@ApiModelProperty(name = \""+ e.getRemarks() +"\")")
			            .append(LineConstants.WRAP)
			            .append(LineConstants.BLANK_SPACE_FOUR)
			             .append("private ").append(e.getFieldType()).append(" ").append(e.getFieldName()).append(";")
			             .append("//").append(StrUtil.removeAllLineBreaks(e.getRemarks()))
			             .append(LineConstants.WRAP).append(LineConstants.WRAP);
		}); 
		stringBuilder.append("}");
		System.out.println("execuete generateBean starting to create file:" + this.codeGenerateBaseInitParams.getBeanPath());
		codeGenerateParams.getWriteFileBase().write(codeGenerateParams , this.codeGenerateBaseInitParams.getTableName() , stringBuilder.toString() , FileSuffixEnum.BEAN);
		System.out.println();
		
		//dto
		//vo
		return this;
	}

	@Override
	public CodeGenerateBase generateMapper() {
		defaultCodeGenerate.generateMapper();
		return this;
	}

	@Override
	public CodeGenerateBase generateDao() {
		defaultCodeGenerate.generateDao();
		return this;
	}

	@Override
	public CodeGenerateBase generateService() {
		defaultCodeGenerate.generateService();
		return this;
	}

	@Override
	public CodeGenerateBase generateServiceImpl() {
		defaultCodeGenerate.generateServiceImpl();
		return this;
	}

	@Override
	public CodeGenerateBase generateController() {
		defaultCodeGenerate.generateController();
		return this;
	}
}

package com.csy.test.commons.codegenerate.base.defaults.codegenerate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.csy.test.commons.codegenerate.base.CodeGenerateBase;
import com.csy.test.commons.codegenerate.base.WriteFileBase;
import com.csy.test.commons.codegenerate.base.bean.CodeGenerateBaseInitParams;
import com.csy.test.commons.codegenerate.base.utils.NoteStringUitls;
import com.csy.test.commons.codegenerate.bean.BeanFieldMessage;
import com.csy.test.commons.codegenerate.bean.CodeGenerateParams;
import com.csy.test.commons.codegenerate.constants.ClassifyConstants;
import com.csy.test.commons.codegenerate.constants.FileSuffixEnum;
import com.csy.test.commons.codegenerate.constants.LineConstants;
import com.csy.test.commons.codegenerate.database.bean.base.DataMetaBase;
import com.csy.test.commons.codegenerate.database.util.DataMetaUtils;
import com.csy.test.commons.utils.Objects;
import com.csy.test.commons.utils.StrUtil;
import com.csy.test.commons.utils.file.FileUtils;

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
	
	private String tableName;
	
	private WriteFileBase writeFileBase;
	
	public NowNewWorkCodeGenerate(CodeGenerateParams _codeGenerateParams) {
		this.codeGenerateParams = _codeGenerateParams;
		
		this.writeFileBase = ((codeGenerateParams , tableName , contents , fileSuffixEnum) -> {
			StringBuilder stringBuilder = new StringBuilder(codeGenerateParams.getBasePathMap().get(tableName));
			if (ClassifyConstants.QUERY.equals(fileSuffixEnum.getFileType())) {
				stringBuilder.append(File.separator).append(ClassifyConstants.BEAN).append(File.separator).append(ClassifyConstants.QUERY).append(File.separator);
			}
			
			if (ClassifyConstants.DTO.equals(fileSuffixEnum.getFileType())) {
				stringBuilder.append(File.separator).append(ClassifyConstants.BEAN).append(File.separator).append(ClassifyConstants.DTO).append(File.separator);
			}
			
			if (ClassifyConstants.VO.equals(fileSuffixEnum.getFileType())) {
				stringBuilder.append(File.separator).append(ClassifyConstants.BEAN).append(File.separator).append(ClassifyConstants.VO).append(File.separator);
			}
			
			String path = stringBuilder.toString();
			try {
				Files.createDirectories(Paths.get(path));
			} catch (IOException e) {
				throw new RuntimeException("创建路径失败:" + path, e);
			}
			path = stringBuilder
					.append(StrUtil.upperFirst(StrUtil.toCamelCase(tableName)))
					.append(codeGenerateParams.getFileSuffixNameMap().get(fileSuffixEnum.getFileType()))
					.append(".").append(fileSuffixEnum.getSuffixName()).toString();
			FileUtils.writeFile(path , contents);
			
			codeGenerateParams.getCodeFilePaths().add(path);			
		});
	}
	
	@Override
	public CodeGenerateBase preInit(DataMetaBase dataMetaBase) {
		
		this.dataMetaBase = dataMetaBase;
		this.tableName = dataMetaBase.getTableMessage().getTableName();
		
		String basePath = this.codeGenerateParams.getBasePathMap().get(this.tableName) + StringUtils.lowerCase(this.tableName).replace("_", "");
		this.codeGenerateParams.getBasePathMap().put(this.tableName, basePath);
		
		this.beanFieldMessages = DataMetaUtils.tranferToBeanFields(dataMetaBase.getColumnMetaDatas());
		
		this.defaultCodeGenerate = new DefaultCodeGenerate(this.codeGenerateParams);
		this.defaultCodeGenerate.preInit(dataMetaBase);
		
		this.codeGenerateBaseInitParams = this.initCodeGenerateBaseInitParams(this.codeGenerateParams, dataMetaBase);
		return this;
	}

	@Override
	public CodeGenerateBase generateBean() {
		
		Objects.notNullAssert(this.tableName , "tableName is not allow null");
		
		String beanPath = codeGenerateBaseInitParams.getBeanPath();
		String fullBeanName = codeGenerateBaseInitParams.getFullBeanName();
		String modelStr = this.getBaseEntityTemplateStr(beanPath, fullBeanName , "实体类", true, false);
		System.out.println("execuete generateBean starting to create file...");
		codeGenerateParams.getWriteFileBase().write(codeGenerateParams , this.tableName , modelStr , FileSuffixEnum.BEAN);
		System.out.println();
		
		String beanName = codeGenerateBaseInitParams.getBeanName();
		
		//query
		String queryStr = this.getBaseEntityTemplateStr(beanPath + "." + ClassifyConstants.QUERY, 
				beanName + StrUtil.upperFirst(ClassifyConstants.QUERY) , 
				"query对象", false, true);
		System.out.println("execuete generateBeanQuery starting to create file...");
		writeFileBase.write(codeGenerateParams , this.tableName , queryStr , FileSuffixEnum.QUERY);
		
		System.out.println();
		//dto
		String dtoStr = this.getBaseEntityTemplateStr(beanPath + "." + ClassifyConstants.DTO, 
				beanName + StrUtil.upperFirst(ClassifyConstants.DTO) , 
				"DTO对象", false, true);
		System.out.println("execuete generateBeanDto starting to create file...");
		writeFileBase.write(codeGenerateParams , this.tableName , dtoStr , FileSuffixEnum.DTO);
		System.out.println();
		
		//vo
		String voStr = this.getBaseEntityTemplateStr(beanPath + "." + ClassifyConstants.VO, 
				beanName + StrUtil.upperFirst(ClassifyConstants.VO) , 
				"VO对象", false, true);
		System.out.println("execuete generateBeanVo starting to create file...");
		writeFileBase.write(codeGenerateParams , this.tableName , voStr , FileSuffixEnum.VO);
		System.out.println();
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
	
	/**
	 * 描述：获取基本的实体类模板
	 * @author csy 
	 * @date 2021年3月17日 下午12:53:56
	 * @param basePath
	 * @param entityName
	 * @param entityClassify
	 * @return
	 */
	private String getBaseEntityTemplateStr(String basePath , String entityName , String entityClassify, boolean needNote, boolean needApi){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder
			.append("package ").append(basePath).append(";")
			.append(LineConstants.WRAP).append(LineConstants.WRAP)
		    .append("import java.util.*;")
		    .append(LineConstants.WRAP)
		    .append("import lombok.Builder;")
		    .append(LineConstants.WRAP)
		    .append("import lombok.Data;")
		    .append(LineConstants.WRAP)
		    .append("import lombok.NoArgsConstructor;")
		    .append(LineConstants.WRAP)
		    .append("import lombok.AllArgsConstructor;")
		    .append(LineConstants.WRAP);
	    if (needApi){
	    	stringBuilder.append("import io.swagger.annotations.ApiModelProperty;");
	    }
		stringBuilder.append(LineConstants.WRAP);
		NoteStringUitls.appendClassNote(stringBuilder , codeGenerateParams.getAuthor(), dataMetaBase.getTableMessage().getRemarks() + entityClassify)
		    .append("@Data")
		    .append(LineConstants.WRAP)
		    .append("@Builder")
		    .append(LineConstants.WRAP)
		    .append("@NoArgsConstructor")
		    .append(LineConstants.WRAP)
		    .append("@AllArgsConstructor")
		    .append(LineConstants.WRAP)
			.append("public class ").append(entityName)
			.append(" {").append(LineConstants.WRAP).append(LineConstants.WRAP);
		
		this.beanFieldMessages.forEach((e) -> {
			if (needNote){
				stringBuilder.append(LineConstants.BLANK_SPACE_FOUR)
				    .append("/** ").append(StrUtil.removeAllLineBreaks(e.getRemarks())).append("*/").append(LineConstants.WRAP);		
			}
			if (needApi){
				stringBuilder.append(LineConstants.BLANK_SPACE_FOUR)
				    .append("@ApiModelProperty(name = \""+ e.getFieldName() +"\" , value = \""+ e.getRemarks() +"\")").append(LineConstants.WRAP);		
			}
			stringBuilder.append(LineConstants.BLANK_SPACE_FOUR)
			             .append("private ").append(e.getFieldType()).append(" ").append(e.getFieldName()).append(";")
			             .append(LineConstants.WRAP).append(LineConstants.WRAP);
		}); 
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
}

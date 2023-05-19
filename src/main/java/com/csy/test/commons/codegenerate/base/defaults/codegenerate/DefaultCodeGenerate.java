package com.csy.test.commons.codegenerate.base.defaults.codegenerate;

import org.apache.commons.lang3.StringUtils;

import com.csy.test.commons.codegenerate.base.CodeGenerateBase;
import com.csy.test.commons.codegenerate.base.bean.CodeGenerateBaseInitParams;
import com.csy.test.commons.codegenerate.base.utils.NoteStringUitls;
import com.csy.test.commons.codegenerate.bean.CodeGenerateParams;
import com.csy.test.commons.codegenerate.bean.MethodGenerateRecord;
import com.csy.test.commons.codegenerate.constants.ClassifyConstants;
import com.csy.test.commons.codegenerate.constants.FileSuffixEnum;
import com.csy.test.commons.codegenerate.constants.LineConstants;
import com.csy.test.commons.codegenerate.database.bean.base.DataMetaBase;
import com.csy.test.commons.utils.Objects;
import com.csy.test.commons.utils.StrUtil;

/**
 * 
 * 描述:生成各种文件
 * @author csy
 * @date 2021年1月23日 下午8:08:55
 */
public class DefaultCodeGenerate implements CodeGenerateBase{

	private CodeGenerateParams codeGenerateParams;
	
	private DataMetaBase dataMetaBase;
	
	private String beanName;
	
	private String tableName;
	
	private CodeGenerateBaseInitParams codeGenerateBaseInitParams;
	
	public DefaultCodeGenerate(CodeGenerateParams codeGenerateParams) {
		this.codeGenerateParams = codeGenerateParams;
	}
	
	@Override
	public DefaultCodeGenerate preInit(DataMetaBase dataMetaBase) {
		
		this.dataMetaBase = dataMetaBase;
		this.tableName = dataMetaBase.getTableMessage().getTableName();
		
		String initPackageName = codeGenerateParams.getExtraInitPackageNameMap().get(tableName);
		String basePath = this.codeGenerateParams.getBasePathMap().get(this.tableName) 
				+ (Objects.isNull(initPackageName) ? StringUtils.lowerCase(this.tableName).replace("_", "") : initPackageName) ;
		this.codeGenerateParams.getBasePathMap().put(this.tableName, basePath);
		
		this.codeGenerateBaseInitParams = this.initCodeGenerateBaseInitParams(this.codeGenerateParams, dataMetaBase);
		
		this.beanName = this.codeGenerateBaseInitParams.getBeanName();

 		return this;
	}
	
	@Override
	public CodeGenerateBase generateBean() {
		
		this.preAssert();
		
		String contents = this.getBeanContents();
		System.out.println("execuete generateBean starting to create file:" + this.codeGenerateBaseInitParams.getBeanPath());
		codeGenerateParams.getWriteFileBase().write(codeGenerateParams , this.tableName , contents , FileSuffixEnum.BEAN);
		System.out.println();
		return this;
	}
	
	@Override
	public CodeGenerateBase generateMapper() {
		
		this.preAssert();
		
		String contents = this.getMapperContents();
		System.out.println("execuete generateMapper starting to create file:" + this.codeGenerateBaseInitParams.getXmlPath());
		codeGenerateParams.getWriteFileBase().write(codeGenerateParams , this.tableName , contents , FileSuffixEnum.MAPPER);
		System.out.println();
		return this;
	}
	
	@Override
	public CodeGenerateBase generateDao() {
		
		this.preAssert();
		
		String contents = this.getDaoContents();
		System.out.println("execuete generateDao starting to create file:" + this.codeGenerateBaseInitParams.getDaoPath());
		codeGenerateParams.getWriteFileBase().write(codeGenerateParams , this.tableName , contents , FileSuffixEnum.DAO);
		System.out.println();
		return this;
	}
	
	@Override
	public CodeGenerateBase generateService() {
		
		this.preAssert();
		
		String contents = this.getServiceContents();
		System.out.println("execuete generateService starting to create file:" + this.codeGenerateBaseInitParams.getServicePath());
		codeGenerateParams.getWriteFileBase().write(codeGenerateParams , this.tableName , contents , FileSuffixEnum.SERVICE);
		System.out.println();
		return this;
	}
	

	@Override
	public CodeGenerateBase generateServiceImpl() {
		
		this.preAssert();
		
		String contents = this.getServiceImplContents();
		System.out.println("execuete generateServiceImpl starting to create file:" + this.codeGenerateBaseInitParams.getServiceImplPath());
		codeGenerateParams.getWriteFileBase().write(codeGenerateParams , this.tableName , contents , FileSuffixEnum.SERVICE_IMPL);
		System.out.println();
		return this;
	}
	
	@Override
	public CodeGenerateBase generateController() {
		
		this.preAssert();
		
		String contents = this.getControllerContents();
		System.out.println("execuete generateController starting to create file:" + this.codeGenerateBaseInitParams.getControllerPath());
		codeGenerateParams.getWriteFileBase().write(codeGenerateParams , this.tableName , contents , FileSuffixEnum.CONTRLLER);
		System.out.println();
		return this;
	}

	/**
	 * 
	 * 描述：获取bean内容
	 * @author csy
	 * @date 2021年1月24日 下午1:52:29
	 * @return bean内容
	 */
	private String getBeanContents() {
		String beanPath = this.codeGenerateBaseInitParams.getBeanPath();
		String fullBeanName = this.codeGenerateBaseInitParams.getFullBeanName();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder
			.append("package ").append(beanPath).append(";")
			.append(LineConstants.WRAP);
			
		MethodGenerateRecord methodGenerateRecord = this.collectClassMethod(FileSuffixEnum.BEAN, codeGenerateParams, codeGenerateBaseInitParams);
		//方法生成器收集的需要引入的包
		if (Objects.notNull(methodGenerateRecord)){
			methodGenerateRecord.getImpportBeans().forEach((e) -> {
				stringBuilder.append("import ").append(e).append(";")
				             .append(LineConstants.WRAP);
			});
		}

		stringBuilder.append(LineConstants.WRAP);
		NoteStringUitls.appendClassNote(stringBuilder , codeGenerateParams.getAuthor(), dataMetaBase.getTableMessage().getRemarks() + "实体类")
			.append("public class ").append(fullBeanName)
			.append(" {").append(LineConstants.WRAP);
		
		//方法生成器收集需要添加的方法
		if (Objects.notNull(methodGenerateRecord)){
			stringBuilder.append(methodGenerateRecord.getMethodContent());
		}
		
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * 
	 * 描述：获取xml内容
	 * @author csy
	 * @date 2021年1月24日 下午1:51:16
	 * @return xml内容
	 */
	private String getMapperContents() {
		
		StringBuilder stringBuilder = new StringBuilder();
		MethodGenerateRecord methodGenerateRecord = this.collectClassMethod(FileSuffixEnum.MAPPER, codeGenerateParams, codeGenerateBaseInitParams);

		//方法生成器收集需要添加的方法
		if (Objects.notNull(methodGenerateRecord)){
			stringBuilder.append(methodGenerateRecord.getMethodContent());
		}

		return stringBuilder.toString();
	}
	
	/**
	 * 
	 * 描述：获取dao内容
	 * @author csy
	 * @date 2021年1月24日 下午2:01:31
	 * @return dao内容
	 */
	private String getDaoContents() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder
			.append("package ").append(this.codeGenerateBaseInitParams.getDaoPath()).append(";")
			.append(LineConstants.WRAP);
		
		MethodGenerateRecord methodGenerateRecord = this.collectClassMethod(FileSuffixEnum.DAO, codeGenerateParams, codeGenerateBaseInitParams);
		//方法生成器收集的需要引入的包
		if (Objects.notNull(methodGenerateRecord)){
			stringBuilder.append(LineConstants.WRAP);
			methodGenerateRecord.getImpportBeans().forEach((e) -> {
				stringBuilder.append("import ").append(e).append(";")
				             .append(LineConstants.WRAP);
			});
		}
		
		stringBuilder.append(LineConstants.WRAP);
		NoteStringUitls.appendClassNote(stringBuilder , codeGenerateParams.getAuthor() , dataMetaBase.getTableMessage().getRemarks() + "dao")
			.append("public interface ").append(this.beanName).append(this.codeGenerateParams.getFileSuffixNameMap().get(ClassifyConstants.DAO)).append(" {")
			.append(LineConstants.WRAP);

		//方法生成器收集需要添加的方法
		if (Objects.notNull(methodGenerateRecord)){
			stringBuilder.append(methodGenerateRecord.getMethodContent());
		}
		
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * 
	 * 描述：获取serviceImpl内容
	 * @author csy
	 * @date 2021年1月24日 下午2:01:08
	 * @return serviceImpl内容
	 */
	private String getServiceImplContents() {
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder
			.append("package ").append(this.codeGenerateBaseInitParams.getServiceImplPath()).append(";")
			.append(LineConstants.WRAP)
			.append(LineConstants.WRAP);
		
		MethodGenerateRecord methodGenerateRecord = this.collectClassMethod(FileSuffixEnum.SERVICE_IMPL, codeGenerateParams, codeGenerateBaseInitParams);
		//方法生成器收集的需要引入的包
		if (Objects.notNull(methodGenerateRecord)){
			methodGenerateRecord.getImpportBeans().forEach((e) -> {
				stringBuilder.append("import ").append(e).append(";")
				             .append(LineConstants.WRAP);
			});
		}
		
		stringBuilder.append("import org.springframework.stereotype.Service;").append(LineConstants.WRAP)
		    .append("import ").append(this.codeGenerateBaseInitParams.getServicePath()).append(".").append(this.codeGenerateBaseInitParams.getFullServiceName()).append(";")
			.append(LineConstants.WRAP).append(LineConstants.WRAP);
		
		NoteStringUitls.appendClassNote(stringBuilder , codeGenerateParams.getAuthor(), dataMetaBase.getTableMessage().getRemarks() + "业务实现类")
		    .append("@Service(\"" + StrUtil.lowerFirst(this.codeGenerateBaseInitParams.getFullServiceName()) + "\")").append(LineConstants.WRAP)
			.append("public class ").append(this.codeGenerateBaseInitParams.getFullServiceImplName())
			.append(" implements ").append(this.codeGenerateBaseInitParams.getFullServiceName())
			.append(" {")
			.append(LineConstants.WRAP);

		//方法生成器收集需要添加的方法
		if (Objects.notNull(methodGenerateRecord)){
			stringBuilder.append(methodGenerateRecord.getMethodContent());
		}
		
		stringBuilder.append(LineConstants.WRAP).append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * 
	 * 描述：获取service内容
	 * @author csy
	 * @date 2021年1月24日 下午2:01:42
	 * @return service内容
	 */
	private String getServiceContents() {
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder
			.append("package ").append(this.codeGenerateBaseInitParams.getServicePath()).append(";")
			.append(LineConstants.WRAP)
			.append(LineConstants.WRAP);
		
		MethodGenerateRecord methodGenerateRecord = this.collectClassMethod(FileSuffixEnum.SERVICE, codeGenerateParams, codeGenerateBaseInitParams);
		//方法生成器收集的需要引入的包
		if (Objects.notNull(methodGenerateRecord)){
			methodGenerateRecord.getImpportBeans().forEach((e) -> {
				stringBuilder.append("import ").append(e).append(";")
				             .append(LineConstants.WRAP);
			});
		}
		
		NoteStringUitls.appendClassNote(stringBuilder , codeGenerateParams.getAuthor(), dataMetaBase.getTableMessage().getRemarks() + "业务接口")
			.append("public interface ").append(this.codeGenerateBaseInitParams.getFullServiceName()).append(" {")
			.append(LineConstants.WRAP);

		//方法生成器收集需要添加的方法
		if (Objects.notNull(methodGenerateRecord)){
			stringBuilder.append(methodGenerateRecord.getMethodContent());
		}
		
		stringBuilder.append(LineConstants.WRAP).append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * 
	 * 描述：获取controller内容
	 * @author csy
	 * @date 2021年1月24日 下午3:39:15
	 * @return controller内容
	 */
	private String getControllerContents() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder
			.append("package ").append(this.codeGenerateBaseInitParams.getControllerPath()).append(";")
			.append(LineConstants.WRAP)
			.append(LineConstants.WRAP);
		
		MethodGenerateRecord methodGenerateRecord = this.collectClassMethod(FileSuffixEnum.CONTRLLER, codeGenerateParams, codeGenerateBaseInitParams);
		//方法生成器收集的需要引入的包
		if (Objects.notNull(methodGenerateRecord)){
			methodGenerateRecord.getImpportBeans().forEach((e) -> {
				stringBuilder.append("import ").append(e).append(";")
				             .append(LineConstants.WRAP);
			});
		}
		
		NoteStringUitls.appendClassNote(stringBuilder , codeGenerateParams.getAuthor() , dataMetaBase.getTableMessage().getRemarks() + "控制层")
			.append("public class ").append(this.codeGenerateBaseInitParams.getFullControllerName()).append(" {")
			.append(LineConstants.WRAP);

		//方法生成器收集需要添加的方法
		if (Objects.notNull(methodGenerateRecord)){
			stringBuilder.append(methodGenerateRecord.getMethodContent());
		}
		
		stringBuilder.append(LineConstants.WRAP).append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * 
	 * 描述：判断前置条件是否执行没有
	 * @author csy
	 * @date 2021年1月24日 下午2:11:21
	 */
	private void preAssert() {
		
		Objects.notNullAssert(this.dataMetaBase, "dataMetaBase is not allow null");
		
		Objects.notNullAssert(this.tableName, "tableName is not allow null");
	}
}

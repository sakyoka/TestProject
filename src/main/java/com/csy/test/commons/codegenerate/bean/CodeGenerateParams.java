package com.csy.test.commons.codegenerate.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.csy.test.commons.codegenerate.base.TranferFileBase;
import com.csy.test.commons.codegenerate.base.WriteFileBase;
import com.csy.test.commons.codegenerate.base.defaults.DefaultTranferFile;
import com.csy.test.commons.codegenerate.base.defaults.WriteFileGeneralDefault;
import com.csy.test.commons.codegenerate.constants.ClassifyConstants;
import com.csy.test.commons.codegenerate.database.bean.base.DataMetaBase;
import com.csy.test.commons.codegenerate.database.bean.base.defaults.DataMetaJdbc;
import com.csy.test.commons.utils.UUID;
import com.csy.test.commons.valid.annotion.Valid;
import com.csy.test.commons.valid.base.defaults.BlankValid;
import com.csy.test.commons.valid.base.defaults.NullValid;
import com.csy.test.commons.valid.bean.ParamValidResult;
import com.csy.test.commons.valid.utils.ValidUtils;

/**
 * 
 * 描述：配置对象
 * @author csy
 * @date 2021年1月22日 下午6:08:26
 */
public class CodeGenerateParams {

	@Valid(validType = {NullValid.class} , errorMessage = "表对应路径map不能为null")
	private Map<String, String> basePackageMap;
	
	@Valid(validType = {BlankValid.class} , errorMessage = "文件目录地址不能为空")
	private String codeCacheBasePath;
	
	private Map<String, String> basePathMap;
	
	private String uuidPath;
	
	private Boolean isProjectDir = false;
	
	private Boolean isBuild = false;
	
	private List<String> codeFilePaths;
	
	private WriteFileBase writeFileBase;
	
	private String author;
	
	private Class<? extends DataMetaBase> dataMetaBaseClass;

	private Map<String , String> baseProjectPathMap;
	
	private Class<? extends TranferFileBase> tranferFileBaseClass;
	
	@SuppressWarnings("serial")
	private Map<String, String> fileSuffixNameMap = new HashMap<String, String>(){{
		this.put(ClassifyConstants.BEAN, "Model");
		this.put(ClassifyConstants.XML, "Mapper");
		this.put(ClassifyConstants.DAO, "Dao");
		this.put(ClassifyConstants.SERVICE, "Service");
		this.put(ClassifyConstants.SERVICE_IMPL, "ServiceImpl");
		this.put(ClassifyConstants.CONTROLLER, "Controller");
	}};
	
	private CodeGenerateParams() {}
	
	public static CodeGenerateParams getBuilder() {
		return new CodeGenerateParams();
	}
	
	/**
	 * 
	 * 描述：设置包目录
	 * <br> tableName : com.csy.test.commons
	 * @author csy
	 * @date 2021年1月23日 下午2:29:46
	 * @param basePackageMap
	 * @return CodeGenerateParams
	 */
	public CodeGenerateParams basePackageMap(Map<String, String> basePackageMap) {
		 if(this.basePackageMap == null)
			 this.basePackageMap = new HashMap<String, String>();
		 basePackageMap.forEach((k , v) ->{
			 this.basePackageMap(k , v);
		 });
		return this;
	}
	
	/**
	 * 描述：设置包目录
	 * <br> tableName : com.csy.test.commons
	 * @author csy
	 * @date 2021年1月25日 下午10:25:14
	 * @param tableName   eg:test_table
	 * @param packageName eg:com.csy.test.commons
	 * @return CodeGenerateParams
	 */
	public CodeGenerateParams basePackageMap(String tableName , String packageName) {
		if (this.basePackageMap == null)
			this.basePackageMap = new HashMap<String, String>();
		
		this.basePackageMap.put(tableName.toLowerCase() , packageName);
		return this;
	}

	/**
	 * 
	 * 描述：设置生成文件目录地址
	 * <br>D:\\codeCache\\
	 * @author csy
	 * @date 2021年1月23日 下午2:30:16
	 * @param codeCacheBasePath 文件目录地址
	 * @return CodeGenerateParams
	 */
	public CodeGenerateParams codeCacheBasePath(String codeCacheBasePath) {
		this.codeCacheBasePath = codeCacheBasePath;
		return this;
	}
	
	/**
	 * 
	 * 描述：targetBasePath后面拼接的uuidPath
	 * @author csy
	 * @date 2021年1月23日 下午2:44:39
	 * @param uuidPath uuid
	 * @return CodeGenerateParams
	 */
	public CodeGenerateParams uuidPath(String uuidPath) {
		this.uuidPath = uuidPath;
		return this;
	}
	
	/**
	 * 
	 * 描述：设置是否需要生成包路径
	 * <br> true是，false否
	 * @author csy
	 * @date 2021年1月23日 下午3:29:35
	 * @param isProjectDir 是否需要生成包路径
	 * @return CodeGenerateParams
	 */
	public CodeGenerateParams isProjectDir(Boolean isProjectDir) {
		this.isProjectDir = isProjectDir;
		return this;
	}
	
	/**
	 * 描述：文件输出器
	 * @author csy
	 * @date 2021年1月23日 下午5:36:00
	 * @param writeFileBase
	 * @return CodeGenerateParams
	 */
	public CodeGenerateParams writeFileBase(WriteFileBase writeFileBase) {
		this.writeFileBase = writeFileBase;
		return this;
	}
	
	/**
	 * 
	 * 描述：文件后缀
	 * @author csy
	 * @date 2021年1月24日 下午1:39:29
	 * @param fileSuffixNameMap
	 * @return CodeGenerateParams
	 */
	public CodeGenerateParams fileSuffixNameMap(Map<String, String> fileSuffixNameMap) {
		this.fileSuffixNameMap = fileSuffixNameMap;
		return this;
	}
	
	/**
	 * 
	 * 描述：编写人
	 * @author csy
	 * @date 2021年1月24日 下午4:10:58
	 * @param author
	 * @return CodeGenerateParams
	 */
	public CodeGenerateParams author(String author) {
		this.author = author;
		return this;
	}
	
	/**
	 * 
	 * 描述：设置获取数据库连接方式
	 * @author csy
	 * @date 2021年1月24日 下午8:13:41
	 * @param dataMetaBaseClass
	 * @return CodeGenerateParams
	 */
	public CodeGenerateParams dataMetaBaseClass(Class<? extends DataMetaBase> dataMetaBaseClass) {
		this.dataMetaBaseClass = dataMetaBaseClass;
		return this;
	}
	
	/**
	 * 描述：文件转移
	 * @author csy 
	 * @date 2021年1月29日 上午11:14:13
	 * @param tranferFileBaseClass
	 * @return CodeGenerateParams
	 */
	public CodeGenerateParams tranferFileBaseClass(Class<? extends TranferFileBase> tranferFileBaseClass) {
		this.tranferFileBaseClass = tranferFileBaseClass;
		return this;
	}
	
	/**
	 * 描述：生成的代码是否要转移到对应目录;
	 * 如果有配置表对应目录会转移
	 * @author csy 
	 * @date 2021年1月29日 上午10:43:22
	 * @param baseProjectPathMap
	 * @return CodeGenerateParams
	 */
	public CodeGenerateParams baseProjectPathMap(Map<String, String> baseProjectPathMap) {
		 if(this.baseProjectPathMap == null)
			 this.baseProjectPathMap = new HashMap<String, String>();
		 
		 baseProjectPathMap.forEach((k , v) ->{
			 this.baseProjectPathMap(k , v);
		 });
		return this;
	}
	
	/**
	 * 描述：生成的代码是否要转移到对应目录;
	 * 如果有配置表对应目录会转移
	 * @author csy 
	 * @date 2021年1月29日 上午10:44:53
	 * @param tableName 表名
	 * @param baseProjectPath 项目路径
	 * @return CodeGenerateParams
	 */
	public CodeGenerateParams baseProjectPathMap(String tableName , String baseProjectPath) {
		if (this.baseProjectPathMap == null)
			this.baseProjectPathMap = new HashMap<String , String>();
		
		this.baseProjectPathMap.put(tableName.toLowerCase(), baseProjectPath);
		return this;
	}
	
	/**
	 * 描述：设置tableName 对应的项目路径、包路径
	 * <br>等于baseProjectPathMap、basePackageMap
	 * @author csy 
	 * @date 2021年1月29日 下午5:49:21
	 * @param tableName 表名
	 * @param baseProjectPath 目标项目路径 eg:D:xxx\Project
	 * @param packageName     类路径           eg:com.xxx.test
	 * @return CodeGenerateParams
	 */
	public CodeGenerateParams baseProjectPathAndPackageMap(String tableName , String baseProjectPath , String packageName) {
		return this.basePackageMap(tableName , packageName)
		           .baseProjectPathMap(tableName , baseProjectPath);
				   
	}
		
	/**
	 * 
	 * 描述：构建必要参数
	 * @author csy
	 * @date 2021年1月23日 下午2:53:20
	 * @return CodeGenerateParams
	 */
	public CodeGenerateParams build() {
		
		ParamValidResult validResult = ValidUtils.valid(this);
		if(validResult.getHasError()) {
			this.isBuild = false;
			throw new RuntimeException(validResult.toString());
		}
		
		if (this.uuidPath == null)
			this.uuidPath = UUID.getString();
		
		this.basePathMap = new HashMap<String , String>();
		String basePath = new StringBuilder()
				.append(this.codeCacheBasePath)
		        .append(File.separator)
		        .append(this.uuidPath)
		        .append(File.separator)
		        .toString();
		this.basePackageMap.forEach((k , v) -> {
			String path = this.isProjectDir ? v.replace(".", File.separator) : "";
			this.basePathMap.put(k , basePath + path);
		});
		
		this.isBuild = true;
		
		this.codeFilePaths = new ArrayList<String>();
		
		if (this.writeFileBase == null) this.writeFileBase = new WriteFileGeneralDefault();
		
		if (this.dataMetaBaseClass == null) this.dataMetaBaseClass = DataMetaJdbc.class;
		
		if (this.tranferFileBaseClass == null) this.tranferFileBaseClass = DefaultTranferFile.class;
		
		return this;
	}
	
	public Map<String, String> getBasePackageMap() {
		return basePackageMap;
	}

	public Map<String, String> getBasePathMap() {
		return basePathMap;
	}

	public String getCodeCacheBasePath() {
		return codeCacheBasePath;
	}
	
	public String getUuidPath() {
		return uuidPath;
	}

	public Boolean getIsProjectDir() {
		return isProjectDir;
	}

	public Boolean getIsBuild() {
		return isBuild;
	}

	public List<String> getCodeFilePaths() {
		return codeFilePaths;
	}

	public WriteFileBase getWriteFileBase() {
		return writeFileBase;
	}

	public Map<String, String> getFileSuffixNameMap() {
		return fileSuffixNameMap;
	}

	public String getAuthor() {
		return author;
	}

	public Class<? extends DataMetaBase> getDataMetaBaseClass() {
		return dataMetaBaseClass;
	}
	
	public Map<String, String> getBaseProjectPathMap() {
		return baseProjectPathMap;
	}

	public Class<? extends TranferFileBase> getTranferFileBaseClass() {
		return tranferFileBaseClass;
	}
}

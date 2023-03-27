package com.csy.test.commons.codegenerate.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.csy.test.commons.codegenerate.base.MethodTemplateGenerate;
import com.csy.test.commons.codegenerate.base.TranferFileBase;
import com.csy.test.commons.codegenerate.base.WriteFileBase;
import com.csy.test.commons.codegenerate.base.defaults.methodtemplate.bean.BeanMethodCommonImpl;
import com.csy.test.commons.codegenerate.base.defaults.methodtemplate.dao.DaoMethodCommonImpl;
import com.csy.test.commons.codegenerate.base.defaults.methodtemplate.dao.MapperMethodCommonImpl;
import com.csy.test.commons.codegenerate.base.defaults.transferfile.DefaultTranferFile;
import com.csy.test.commons.codegenerate.base.defaults.writefile.WriteFileGeneralDefault;
import com.csy.test.commons.codegenerate.constants.ClassifyConstants;
import com.csy.test.commons.codegenerate.constants.FileSuffixEnum;
import com.csy.test.commons.codegenerate.database.bean.base.DataMetaBase;
import com.csy.test.commons.codegenerate.database.bean.base.defaults.DataMetaJdbc;
import com.csy.test.commons.utils.Objects;
import com.csy.test.commons.utils.UUID;
import com.csy.test.commons.valid.annotion.Valid;
import com.csy.test.commons.valid.base.defaults.BlankValid;
import com.csy.test.commons.valid.base.defaults.NullValid;
import com.csy.test.commons.valid.bean.ParamValidResult;
import com.csy.test.commons.valid.utils.ValidUtils;

import cn.hutool.core.util.StrUtil;

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
	
	private DataMetaBase dataMetaBase;

	private Map<String , String> baseProjectPathMap;
	
	private TranferFileBase tranferFileBase;
	
	private Map<String, String> extraInitPackageNameMap;

	/**前缀类名*/
	private Map<String, String> preFixClassNameMap;
	
	private Map<FileSuffixEnum, List<MethodTemplateGenerate>> classGenerateMethodsMap;
	
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
		 if(Objects.isNull(this.basePackageMap))
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
		if (Objects.isNull(this.basePackageMap))
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
	 * <br>暂存目录的uuid
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
	 * 描述：设置是否需要生成包路径 针对缓存目录
	 * <br> true 是(生成文件已包形式存在)
	 * <br> false 否(所有文件生成在同一个文件夹) 
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
	 * <br>com.csy.test.commons.codegenerate.base.WriteFileBase  
	 * 实现该类，把文件内容输出
	 * @see com.csy.test.commons.codegenerate.base.defaults.WriteFileGeneralDefault
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
	 * 描述：文件后缀 e: xxxx.java , 即table_name 对应 java后缀，不需要带点
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
	 * 描述：文件后缀 e: xxxx.java , 即table_name 对应 java后缀，不需要带点
	 * @author csy 
	 * @date 2021年3月17日 下午1:49:17
	 * @param k 文件类型
	 * @param v 文件后缀
	 * @return CodeGenerateParams
	 */
	public CodeGenerateParams fileSuffixNameMap(String k , String v) {
		if (this.fileSuffixNameMap == null)
			this.fileSuffixNameMap = new HashMap<String , String>();
		
		this.fileSuffixNameMap.put(k, v);
		return this;
	}
	
	/**
	 * 
	 * 描述：编写人 注释的编写人
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
	 * <br>com.csy.test.commons.codegenerate.database.bean.base.DataMetaBase 
	 * 可实现该类，获取表的数据结构
	 * @see com.csy.test.commons.codegenerate.database.bean.base.defaults.DataMetaJdbc
	 * @author csy
	 * @date 2021年1月24日 下午8:13:41
	 * @param dataMetaBaseClass
	 * @return CodeGenerateParams
	 */
	public CodeGenerateParams dataMetaBase(DataMetaBase dataMetaBase) {
		this.dataMetaBase = dataMetaBase;
		return this;
	}
	
	/**
	 * 描述：文件转移 
	 * <br>com.csy.test.commons.codegenerate.base.TranferFileBase 
	 * 可实现该类对应处理文件转移
	 * @see com.csy.test.commons.codegenerate.base.defaults.DefaultTranferFile
	 * @author csy 
	 * @date 2021年1月29日 上午11:14:13
	 * @param tranferFileBaseClass
	 * @return CodeGenerateParams
	 */
	public CodeGenerateParams tranferFileBase(TranferFileBase tranferFileBase) {
		this.tranferFileBase = tranferFileBase;
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
		 if(Objects.isNull(this.baseProjectPathMap))
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
		if (Objects.isNull(this.baseProjectPathMap))
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
	 * @param baseProjectPath 目标项目路径 eg:D:\\xxx\\Project 空时候，不对tableName对应生成的代码转移
	 * @param packageName     类路径           eg:com.xxx.test
	 * @return CodeGenerateParams
	 */
	public CodeGenerateParams baseProjectPathAndPackageMap(String tableName , String baseProjectPath , String packageName) {
		return this.basePackageMap(tableName , packageName)
		           .baseProjectPathMap(tableName , baseProjectPath);
				   
	}
	
	/**
	 * 描述：扩展basePackageMap下面的包目录，由原来的默认表名，扩展为自定义，没有设置就按照表名逻辑
	 * @author csy 
	 * @date 2021年10月20日 上午10:04:29
	 * @param tableName
	 * @param initPackageName
	 * @return CodeGenerateParams
	 */
	public CodeGenerateParams extraInitPackageNameMap(String tableName , String initPackageName) {
		if (Objects.isNull(this.extraInitPackageNameMap))
			this.extraInitPackageNameMap = new HashMap<String , String>();
		
		this.extraInitPackageNameMap.put(tableName.toLowerCase(), initPackageName);
		return this;
	}
	
	/**
	 * 描述：设置tableName 对应的项目路径、包路径
	 * @author csy 
	 * @date 2021年10月20日 上午10:04:33
	 * @param tableName            数据库表
	 * @param baseProjectPath      项目包目录
	 * @param packageName          项目目录下级
	 * @param initPackageName      代码目录
	 * @return CodeGenerateParams
	 */
	public CodeGenerateParams baseProjectPathAndPackageAndInitPackageMap(String tableName, 
			String baseProjectPath, String packageName, String initPackageName) {
		return this.basePackageMap(tableName , packageName)
		           .baseProjectPathMap(tableName , baseProjectPath)
		           .extraInitPackageNameMap(tableName, initPackageName);
				   
	}
	
	/**
	 * 描述：设置tableName 对应的项目路径、包路径
	 * @author csy 
	 * @date 2021年10月20日 上午10:04:33
	 * @param tableName            数据库表
	 * @param baseProjectPath      项目包目录
	 * @param packageName          项目目录下级
	 * @param initPackageName      代码目录
	 * @param beanName             bean name
	 * @return CodeGenerateParams
	 */
	public CodeGenerateParams baseProjectPathAndPackageAndInitPackageAndBeanNameMap(String tableName, 
			String baseProjectPath, String packageName, String initPackageName, String beanName) {
		return this.basePackageMap(tableName , packageName)
		           .baseProjectPathMap(tableName , baseProjectPath)
		           .extraInitPackageNameMap(tableName, initPackageName)
		           .preFixClassNameMap(tableName, beanName);
				   
	}
	
	/**
	 * 描述：类对应的类内部方法生成器
	 * @author csy 
	 * @date 2022年3月15日 下午5:30:11
	 * @param fileSuffixEnum   类分类
	 * @param methodTemplateGenerates 类内部方法生成器集合
	 * @return CodeGenerateParams
	 */
	@SuppressWarnings("serial")
	public CodeGenerateParams classGenerateMethodsMap(FileSuffixEnum fileSuffixEnum, 
			List<MethodTemplateGenerate> methodTemplateGenerates){
		
		if (Objects.isNull(this.classGenerateMethodsMap)){
			this.classGenerateMethodsMap = new HashMap<FileSuffixEnum, List<MethodTemplateGenerate>>();
		}
		
		if (!this.classGenerateMethodsMap.containsKey(fileSuffixEnum)){
			this.classGenerateMethodsMap.put(fileSuffixEnum, new ArrayList<MethodTemplateGenerate>(){{
				this.addAll(methodTemplateGenerates);
			}});
		}else{
			this.classGenerateMethodsMap.get(fileSuffixEnum).addAll(methodTemplateGenerates);
		}
		return this;
	}
	
	/**
	 * 描述：类对应的类内部方法生成器
	 * @author csy 
	 * @date 2022年3月15日 下午5:34:13
	 * @param fileSuffixEnum 类分类
	 * @param methodTemplateGenerate 类内部方法生成器
	 * @return CodeGenerateParams
	 */
	@SuppressWarnings("serial")
	public CodeGenerateParams classGenerateMethodsMap(FileSuffixEnum fileSuffixEnum, 
			MethodTemplateGenerate methodTemplateGenerate){
		
		if (Objects.isNull(this.classGenerateMethodsMap)){
			this.classGenerateMethodsMap = new HashMap<FileSuffixEnum, List<MethodTemplateGenerate>>();
		}
		
		if (!this.classGenerateMethodsMap.containsKey(fileSuffixEnum)){
			this.classGenerateMethodsMap.put(fileSuffixEnum, new ArrayList<MethodTemplateGenerate>(){{
				this.add(methodTemplateGenerate);
			}});
		}else{
			this.classGenerateMethodsMap.get(fileSuffixEnum).add(methodTemplateGenerate);
		}
		return this;
	}
	
	/**
	 * 描述：类对应的类内部方法生成器
	 * <br> 会在生成类的模板中调用对应的方法生成集合（集合为空时候生成空方法）
	 * @author csy 
	 * @date 2022年3月15日 下午5:34:18
	 * @param classGenerateMethodsMap 类分类及对应类内部方法生成器集合
	 * @return CodeGenerateParams
	 */
	public CodeGenerateParams classGenerateMethodsMap(Map<FileSuffixEnum, 
			List<MethodTemplateGenerate>> classGenerateMethodsMap){
		
		if (Objects.isNull(this.classGenerateMethodsMap)){
			this.classGenerateMethodsMap = new HashMap<FileSuffixEnum, 
					List<MethodTemplateGenerate>>(classGenerateMethodsMap.size());
		}
		
		this.classGenerateMethodsMap.putAll(classGenerateMethodsMap);
		return this;
	}
	
	/**
	 * 
	 * 描述：类前缀名（为空时，使用表名转成类名）
	 * @author csy
	 * @date 2023年3月15日 下午7:57:27
	 * @param prefixClassName
	 * @return CodeGenerateParams
	 */
	public CodeGenerateParams preFixClassNameMap(String tableName, String preFixClassName){
		if (Objects.isNull(this.preFixClassNameMap)){
			this.preFixClassNameMap = new HashMap<String, String>(12);
		}
		this.preFixClassNameMap.put(tableName.toLowerCase(), preFixClassName);
		return this;
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
		
		if (Objects.isNull(this.uuidPath))
			this.uuidPath = UUID.getString();
		
		this.basePathMap = new HashMap<String , String>();
		String basePath = new StringBuilder()
				.append(this.codeCacheBasePath)
		        .append(File.separator)
		        .append(this.uuidPath)
		        .append(File.separator)
		        .toString();
		this.basePackageMap.forEach((k , v) -> {
			String path = this.isProjectDir ? v.replace(".", File.separator) + File.separator : "";
			this.basePathMap.put(k , basePath + path);
		});
		
		this.isBuild = true;
		
		this.codeFilePaths = new ArrayList<String>();
		
		if (Objects.isNull(extraInitPackageNameMap)){
			this.extraInitPackageNameMap = new HashMap<>();
		}
		
		if (Objects.isNull(this.writeFileBase)) {
			this.writeFileBase = new WriteFileGeneralDefault();
		}
		
		if (Objects.isNull(this.dataMetaBase)) {
			this.dataMetaBase = new DataMetaJdbc();
		}
		
		if (Objects.isNull(this.tranferFileBase)) {
			this.tranferFileBase = new DefaultTranferFile();
		}
		
		if (Objects.isNull(this.classGenerateMethodsMap)){
			this.classGenerateMethodsMap = new HashMap<FileSuffixEnum, List<MethodTemplateGenerate>>(0);
		}
		
		if (!this.classGenerateMethodsMap.containsKey(FileSuffixEnum.BEAN)){
			this.classGenerateMethodsMap(FileSuffixEnum.BEAN, new BeanMethodCommonImpl());
		}
		
		if (!this.classGenerateMethodsMap.containsKey(FileSuffixEnum.DAO)){
			this.classGenerateMethodsMap(FileSuffixEnum.DAO, new DaoMethodCommonImpl());
		}
		
		if (!this.classGenerateMethodsMap.containsKey(FileSuffixEnum.MAPPER)){
			this.classGenerateMethodsMap(FileSuffixEnum.MAPPER, new MapperMethodCommonImpl());
		}
		
		if (Objects.isNull(this.preFixClassNameMap)){
			this.preFixClassNameMap = new HashMap<String, String>(this.basePackageMap.size());
		}
		
		this.basePackageMap.forEach((k, v) -> {
			if (!this.preFixClassNameMap.containsKey(k)){
				this.preFixClassNameMap.put(k, StrUtil.upperFirst(k.replace("_", "").toLowerCase()));
			}
		});
		
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

	public DataMetaBase getDataMetaBase() {
		return dataMetaBase;
	}
	
	public Map<String, String> getBaseProjectPathMap() {
		return baseProjectPathMap;
	}

	public TranferFileBase getTranferFileBase() {
		return tranferFileBase;
	}

	public Map<String, String> getExtraInitPackageNameMap() {
		return extraInitPackageNameMap;
	}

	public Map<FileSuffixEnum, List<MethodTemplateGenerate>> getClassGenerateMethodsMap() {
		return classGenerateMethodsMap;
	}
	
	public Map<String, String> getPreFixClassNameMap() {
		return preFixClassNameMap;
	}
}

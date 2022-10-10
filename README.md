# TestProject-补丁文件获取
功能：本地svn、git改动文件，需要提取出作为补丁  

## 测试例子
```   
PatchInitParams pachInitParams = PatchInitParams.getBuilder()  
    .sourcePathPrefix("D:\\svnproject\\") //源代码项目路径前缀  
    .compilePathPrefix("D:\\svnproject\\") //编译代码路径前缀  
    .projectChName("测试项目") //项目中文名  
    .projectEnName("TestProject") //项目英文名(包名)  
    .cachePathPrefix(PatchInitConstants.DEFAULT_CACHE_PATH_PREFIX)//补丁最终生成路径前缀  
    .useSvnCommand()//使用svn命令获取改动过的文件即补丁文件（另外还有git命令）  
    .sourcePathStateClazz(SpringBootSrcJavaState.class)//源路径解析类  
    .patchGenerateClazz(SpringbootPatchOriginalGenerate.class)//补丁生成生成类  
    .writeRecordFileClazz(SpringbootWirteFileRecordFile.class)//补丁记录类  
    .build();  
ParamValidResult validResult = ValidUtils.valid(pachInitParams);//校验对象参数  
if(validResult.getHasError()) {  
  throw new RuntimeException(validResult.toString());  
}  
PatchUtils.generate(pachInitParams);//执行生成补丁文件 
``` 

# TestProject-代码生成
功能：根据数据库表生成代码

## 测试例子
```   
CodeGenerateParams codeGenerateParams = CodeGenerateParams.getBuilder()
        //.basePackageMap(basePackageMap)
        .codeCacheBasePath("D:\\code_cache")
        .author("csy")
        .writeFileBase(new WriteFileNowWork())
        //.baseProjectPathMap("appr_approriate_record", "D:\\githubwordpacenew\\TestProject")
        .baseProjectPathAndPackageMap("appr_approriate_record", "D:\\githubwordpacenew\\TestProject", "com.csy.test.commons")
        .build();

if (!codeGenerateParams.getIsBuild()) {
    throw new RuntimeException("需要执行build");
}

CodeGenerateUtils.generate(codeGenerateParams);
``` 
# TestProject-jar管理
功能：访问地址http://127.0.0.1:8888/view/jarmanage/main



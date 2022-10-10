package com.csy.test.commons.codegenerate.base.defaults.transferfile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import com.csy.test.commons.codegenerate.base.TranferFileBase;
import com.csy.test.commons.codegenerate.bean.CodeGenerateParams;
import com.csy.test.commons.codegenerate.constants.ClassifyConstants;
import com.csy.test.commons.utils.Objects;
import com.csy.test.commons.utils.file.FileUtils;

/**
 * 
 * 描述：正使用的项目
 * @author csy
 * @date 2021年3月17日 下午2:14:43
 */
public class NowNewWorkTransferFile implements TranferFileBase{
	
	private static final String PREFIXX_PATH = "src" + File.separator + "main" + File.separator + "java";
	
	public static final String PREFIXX_MAPPER_PATH = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "mapper";
	
	private String mapperRootDir;
	
	public NowNewWorkTransferFile(){}
	
	public NowNewWorkTransferFile mapperRootDir(String mapperRootDir){
		this.mapperRootDir = mapperRootDir;
		return this;
	}
	
	public String getMapperRootDir() {
		return mapperRootDir;
	}
	
	public NowNewWorkTransferFile build(){
		return this;
	}

	@Override
	public void tranfer(CodeGenerateParams codeGenerateParams, String tableName, String sourceDir) {
		String initPackageName = codeGenerateParams.getExtraInitPackageNameMap().get(tableName);
		//targetDir
		String basePackage = codeGenerateParams.getBasePackageMap().get(tableName);
		final String finallyTargetPath =  new StringBuilder()
				.append(codeGenerateParams.getBaseProjectPathMap().get(tableName))
				.append(File.separator)
				.append(PREFIXX_PATH)
				.append(File.separator)
				.append(basePackage.replace(".", File.separator))
				.append(File.separator)
				.append(Objects.isNull(initPackageName) ? tableName.replace("_", "") : initPackageName + File.separator)
				.toString();
		
		//xmlDir
		String xmlDir =  basePackage.substring(basePackage.lastIndexOf(".") + 1, basePackage.length());
		String mapperPath = (Objects.notNull(mapperRootDir) ? mapperRootDir : PREFIXX_MAPPER_PATH) + File.separator + xmlDir + File.separator
				+ (Objects.isNull(initPackageName) ? tableName.replace("_", "") : initPackageName) ;
		final String finallyXmlTargetPath =  new StringBuilder()
				.append(codeGenerateParams.getBaseProjectPathMap().get(tableName))
				.append(File.separator)
				.append(mapperPath)
				.toString();
		
		Path inputPath = Paths.get(sourceDir);
		try {
			Files.walkFileTree(inputPath, new SimpleFileVisitor<Path> (){
				
				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					String endPath = dir.toString().substring(inputPath.toString().length());
					String finallyPath = null;
					if (endPath.endsWith(ClassifyConstants.XML)){
						finallyPath = finallyXmlTargetPath;
					}else{
						finallyPath = finallyTargetPath + endPath;
					}
	                FileUtils.ifNotExistsCreate(finallyPath);
					return FileVisitResult.CONTINUE;
				}
				
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					String endPath = file.toString().substring(inputPath.toString().length());
	                String finallyPath = null;
		            
		            if (endPath.endsWith(ClassifyConstants.XML)){
		            	endPath = endPath.substring(endPath.lastIndexOf("\\") , endPath.length());
		            	finallyPath = finallyXmlTargetPath + endPath;
		            }else{
		            	finallyPath = finallyTargetPath + endPath;
		            }
		            Path targetPath = Paths.get(finallyPath);
		            File targetFile = targetPath.toFile();
		            System.out.println();
		            FileUtils.ifNotExistsCreate(targetFile.getAbsolutePath());
		            FileUtils.coppyTo(file.toFile() , targetFile);
					return FileVisitResult.CONTINUE;
				}
				
			});			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}	
}

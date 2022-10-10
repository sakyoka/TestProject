package com.csy.test.commons.patch.utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import com.csy.test.commons.entity.utils.EntityUtils;
import com.csy.test.commons.patch.base.AbstractPackPatchFile;
import com.csy.test.commons.patch.base.AbstractPatchCommandExecutor;
import com.csy.test.commons.patch.base.AbstractPatchGenerate;
import com.csy.test.commons.patch.base.defaults.DefaultPatchStandardGenerate;
import com.csy.test.commons.patch.base.defaults.DefaultPatchCommandExecutor;
import com.csy.test.commons.patch.bean.PatchInitParams;
import com.csy.test.commons.patch.cache.CompileRefSourcePathCache;
import com.csy.test.commons.patch.constatns.PatchDefinedConstants;
import com.csy.test.commons.patch.state.sourcepath.executor.SourcePathExecutor;
import com.csy.test.commons.utils.ClassUtils;
import com.csy.test.commons.utils.Objects;
import com.csy.test.commons.utils.file.FileUtils;

/**
 * 
 * 描述:补丁工具类
 * @author csy
 * @date 2020 上午12:42:14
 */
public class PatchUtils {

	/**
	 * 
	 * 描述：生成补丁文件
	 * @author csy
	 * @date 2020 上午12:41:58
	 * @param patchInitParams 初始化参数对象
	 */
	public static void generate(PatchInitParams patchInitParams) {
		
		List<String> sourceFilePaths = getSourceFilePaths(patchInitParams);
		
		generate(patchInitParams, sourceFilePaths);
	}
	
	/**
	 * 
	 * 描述：生成补丁文件
	 * @author csy
	 * @date 2020 下午8:47:10
	 * @param patchInitParams  初始化参数对象
	 * @param sourceFilePaths 源文件相对路径集合
	 */
	public static void generate(PatchInitParams patchInitParams , List<String> sourceFilePaths) {
		
        if (CollectionUtils.isEmpty(sourceFilePaths)){
        	System.out.println("don't need to generate patch file , process stop.");
        	return ;
        }
        
        try {
            System.out.println("starting to transfer sourcePath to compilePath...");
        	List<String> compileFilePaths = sourceListToCompileList(patchInitParams , sourceFilePaths);
        	
        	transfer(patchInitParams, compileFilePaths);
        	
        	//执行打包
        	Class<? extends AbstractPackPatchFile> packPatchFileClass = patchInitParams.getPackPatchFileClazz();
        	if (Objects.notNull(packPatchFileClass))
        		ClassUtils.newInstance(packPatchFileClass).packFile(patchInitParams);
		} finally {
			CompileRefSourcePathCache.remove();
		}
	}
	
	/**
	 * 
	 * 描述：获取源文件路径集合
	 * @author csy
	 * @date 2020 下午12:23:37
	 * @param patchInitParams
	 * @return 源文件相对路径集合
	 */
	private static List<String> getSourceFilePaths(PatchInitParams patchInitParams){
		Class<? extends AbstractPatchCommandExecutor> cls = Objects.ifNullDefault(patchInitParams.getPatchCommandExecutorClazz(), DefaultPatchCommandExecutor.class);
		AbstractPatchCommandExecutor abstractPatchCommandExecutor = ClassUtils.newInstance(cls);
		abstractPatchCommandExecutor.patchInitParams(EntityUtils.deepCopy(patchInitParams)).execute();
        return abstractPatchCommandExecutor.getSourcePaths();
	}
	
	/**
	 * 
	 * 描述：源文件获取转换成编译文件路径
	 * @author csy
	 * @date 2020 下午12:22:04
	 * @param patchInitParams 初始化参数对象
	 * @param sourcePaths    源文件路径
	 * @return 编译文件路径集合
	 */
	private static List<String> sourceListToCompileList(PatchInitParams patchInitParams, List<String> sourcePaths){
		SourcePathExecutor sourcePathExecutor = SourcePathExecutor.getBuilder().patchInitParams(EntityUtils.deepCopy(patchInitParams));
     	for (String sourcePath:sourcePaths){
     		sourcePathExecutor.autoSelectSourcePathState().sourcePath(sourcePath.replace('\\', '/')).build().execute();
    	}
     	
     	List<String> compileFilePaths = sourcePathExecutor.getCompilePaths().stream()
     			.map(e -> e.replace(PatchDefinedConstants.SYSTEM_REVERSE_SEPARATOR, File.separator))
     			.collect(Collectors.toList());
    	return compileFilePaths;
	}
	
	/**
	 * 
	 * 描述：编译文件复制转移
	 * @author csy
	 * @date 2020 下午1:59:36
	 * @param patchInitParams
	 * @param compileFilePaths
	 */
	private static void transfer(PatchInitParams patchInitParams , List<String> compileFilePaths) {
		String cachePathPrefix = patchInitParams.getCachePathPrefix();
    	String uuid = patchInitParams.getCachePathUuid();
		String cachePath = cachePathPrefix + File.separator + uuid;
    	try {
    		System.out.println(String.format("starting to generate cache dir and uuid key: %s" , uuid));
    		//创建临时路径
    		Files.createDirectories(Paths.get(cachePath));
    		
    		System.out.println("starting to copy compile file...");
    		
    		Class<? extends AbstractPatchGenerate> cls = Objects.ifNullDefault(patchInitParams.getPatchGenerateClazz(), DefaultPatchStandardGenerate.class);
    		AbstractPatchGenerate abstractPachGenerate = ClassUtils.newInstance(cls);
    		abstractPachGenerate.compileFilePaths(compileFilePaths)
    		                    .patchInitParams(EntityUtils.deepCopy(patchInitParams))
				    		    .foreach()
				    		    .writeRecordFile();
    		System.out.println("generate patch file finished.");
		} catch (Exception e) {
			
			System.out.println(String.format("error generate patch and then delete cache dir uuid key:%s", uuid));
			try {
				FileUtils.deletes(cachePath);
			} catch (Exception e2) {
				e2.printStackTrace();
				System.out.println(String.format("delete dir:%s error", cachePath));
			}
			
			throw new RuntimeException(e);
		}		
	}
}

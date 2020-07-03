package com.csy.test.commons.patch.utils;

import java.io.File;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.csy.test.commons.entity.utils.EntityUtils;
import com.csy.test.commons.patch.base.AbstractPackPatchFile;
import com.csy.test.commons.patch.base.AbstractPatchCommandExecutor;
import com.csy.test.commons.patch.base.AbstractPatchGenerate;
import com.csy.test.commons.patch.base.defaults.DefaultPatchStandardGenerate;
import com.csy.test.commons.patch.base.defaults.DefaultPatchCommandExecutor;
import com.csy.test.commons.patch.bean.PatchInitParams;
import com.csy.test.commons.patch.cache.CompileRefSourcePathCache;
import com.csy.test.commons.patch.state.sourcepath.executor.SourcePathExecutor;
import com.csy.test.commons.utils.ClassUtils;

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
        	if (packPatchFileClass != null)
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
		Class<? extends AbstractPatchCommandExecutor> clazz = patchInitParams.getPatchCommandExecutorClazz();
		AbstractPatchCommandExecutor abstractPatchCommandExecutor = (clazz == null) ? new DefaultPatchCommandExecutor() : ClassUtils.newInstance(clazz);
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
    	return sourcePathExecutor.getCompilePaths();
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
		File cacheDir = null;
    	try {
    		System.out.println(String.format("starting to generate cache dir and uuid key: %s" , uuid));
    		//创建临时路径
    		cacheDir = new File(cachePath);
    		if (!cacheDir.exists()){
    			cacheDir.mkdirs();
    		}
    		
    		System.out.println("starting to copy compile file...");
    		AbstractPatchGenerate abstractPachGenerate = ( patchInitParams.getPatchGenerateClazz() == null ? 
    				                                               new DefaultPatchStandardGenerate() 
    				                                               : ClassUtils.newInstance(patchInitParams.getPatchGenerateClazz()) );
    		abstractPachGenerate.compileFilePaths(compileFilePaths).patchInitParams(EntityUtils.deepCopy(patchInitParams))
													    		   .foreach()
													    		   .writeRecordFile();
    		System.out.println("generate patch file finished.");
		} catch (Exception e) {
			if (cacheDir != null){
				System.out.println(String.format("error generate patch and then delete cache dir uuid key:%s", uuid));
				cacheDir.delete();
			}
			throw new RuntimeException(e);
		}		
	}
}

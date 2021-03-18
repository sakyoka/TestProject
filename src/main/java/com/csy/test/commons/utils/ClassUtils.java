package com.csy.test.commons.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * 描述:class文件处理
 * @author csy
 * @date 2020 上午9:59:39
 */
public class ClassUtils {

	/**
	 * 
	 * 描述：找到包下面class文件，不包含子包
	 * @author csy
	 * @date 2020 上午9:59:00
	 * @param packageNames
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("rawtypes")
	public static Set<Class> getClassesByPackageNames(String ...packageNames) throws IOException, ClassNotFoundException{
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Set<Class> clazzSet = new HashSet<Class>();
		for (String packageName:packageNames) {
			String packageDirName = packageName.replace('.' , '/');
			Enumeration<URL> dirs = loader.getResources(packageDirName);
			while (dirs.hasMoreElements()) {
				URL url = (URL) dirs.nextElement();
				File file = new File(url.getFile());
				if (file.isDirectory()) {
					File[] files = file.listFiles();
					for (File file2:files) {
						clazzSet.add( loader.loadClass(packageName + "." + file2.getName().substring(0, file2.getName().length() - 6)) );
					}					
				}
			}			
		}
		return clazzSet;
	}
	
	/**
	 * 
	 * 描述：获取当前编译文件的内部类文件
	 * @author csy
	 * @date 2020 下午10:58:12
	 * @param path 当前编译文件全路径
	 * @return 内部类集合
	 */
	public static List<String> innerClazzFilePaths(String path){
		int len = path.lastIndexOf('/');
		String dirPath = path.substring(0 , len);
		File dir = new File(dirPath);
		if (!dir.exists() && !dir.isDirectory())
			throw new RuntimeException("不存在的路径:" + dirPath);
		
		File[] files = dir.listFiles();
		String fileName = path.substring(len + 1, path.lastIndexOf("."));
		List<String> innerClazzFilePaths = new ArrayList<String>();
		String innerFileName = null;
		for (File file:files){
			innerFileName = file.getName();
			if (!innerFileName.equals(fileName) && innerFileName.startsWith(fileName + "$")){
				innerClazzFilePaths.add(file.getAbsolutePath());
			}
		}
		return innerClazzFilePaths;
	}
	
	/**
	 * 
	 * 描述：创建实例
	 * @author csy
	 * @date 2020 上午11:02:51
	 * @param <T>
	 * @param clazz
	 * @return T
	 */
	public static <T> T newInstance(Class<? extends T> clazz){
		try {
			String className = clazz.getName();
			//如果是内部类
			if (className.contains("$")){
				String parentClassName = className.substring(0, className.lastIndexOf("$"));
				Class<?> parentClazz = Class.forName(parentClassName);
				Object parentInstance = parentClazz.newInstance();
				Constructor<? extends T> ctor = clazz.getDeclaredConstructor(parentClazz);
				return ctor.newInstance(parentInstance);
			}
			return clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("创建实例失败" , e);
		} 
	}
	
	/**
	 * 描述：根据字段获取对应值
	 * @author csy 
	 * @date 2020年9月29日 上午11:06:51
	 * @param fieldName 字段
	 * @param entity 对象
	 * @return Object 返回值
	 */
	@SuppressWarnings("rawtypes")
	public static <T> Object getBeanValue(String fieldName , T entity){
		Class clazz = entity.getClass();
		Field field = null;
		try {
			field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(entity);
		} catch (Exception e) {
			throw new RuntimeException("获取对象值失败!");
		}finally {
			if (field != null)
				field.setAccessible(false);
		}
	}
	
	/**
	 * 描述：使用构造器实例化
	 * @author csy 
	 * @date 2021年3月18日 上午9:34:10
	 * @param clazz
	 * @param paramTypes
	 * @param params
	 * @return T
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstanceByConstrutor(Class<T> clazz , Class<?>[] paramTypes , Object ...params){
		Constructor<?> constructor = null;
		try {
			constructor = clazz.getConstructor(paramTypes);
		} catch (Exception e) { 
			throw new RuntimeException("获取class构造方法失败:" + clazz.getName());
		} 
		
		T entity = null;
		try {
			constructor.setAccessible(true);
			entity = (T) constructor.newInstance(params);
		} catch (Exception e) {
			throw new RuntimeException("class " + clazz.getName() + " 不可实例化");
		} 
		
		return entity;
	}
}

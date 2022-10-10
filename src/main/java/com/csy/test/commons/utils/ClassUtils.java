package com.csy.test.commons.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 * 描述:class文件处理
 * @author csy
 * @date 2020 上午9:59:39
 */
public class ClassUtils {

	private ClassUtils(){}
	
	private static final String TYPE_JAR = "jar";
	
	/**
	 * 
	 * 描述：找到包下面class文件
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
				if (TYPE_JAR.equals(url.getProtocol())){
					disposeJarClass(loader, url, packageDirName, clazzSet);
				}else{
					File file = new File(url.getFile());
					if (file.isDirectory()) {
						File[] files = file.listFiles();
						for (File subFile:files) {
							collectCls(loader, packageName, clazzSet, subFile, "");
						}
					}else {
						clazzSet.add(loader.loadClass(packageName + "." 
					            + file.getName().substring(0, file.getName().length() - 6)));
					}	
				}	
			}			
		}
		return clazzSet;
	}
	
	/**
	 * 
	 * 描述：收集jar里面的class
	 * @author csy
	 * @date 2022年9月16日 下午5:46:34
	 * @param loader
	 * @param url
	 * @param packageDirName
	 * @param clazzSet
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("rawtypes")
	private static void disposeJarClass(ClassLoader loader, URL url, String packageDirName, 
			Set<Class> clazzSet) throws IOException, ClassNotFoundException {
		JarFile jarFile = ((JarURLConnection)url.openConnection()).getJarFile();
		try {
			Enumeration<JarEntry> jarEntitys = jarFile.entries();
			while (jarEntitys.hasMoreElements()) {
				JarEntry jarEntry = (JarEntry) jarEntitys.nextElement();
				String name = jarEntry.getName();
				if (name.charAt(0) == '/'){
					name = name.substring(1);
				}
				
				if (name.startsWith(packageDirName) && name.endsWith(".class")){
					name = name.replace("/", ".");
					clazzSet.add(loader.loadClass(name.substring(0, name.length() - 6)));
				}
				
			}
		} finally {
			if (Objects.notNull(jarFile)){
				jarFile.close();
			}
		}
	}

	/**
	 * 
	 * 描述：收集class
	 * @author csy
	 * @date 2022年9月12日 下午2:08:43
	 * @param loader
	 * @param packageName
	 * @param clazzSet
	 * @param file
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("rawtypes")
	private static void collectCls(ClassLoader loader, String packageName, 
			Set<Class> clazzSet, File file, String dirName) throws ClassNotFoundException {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File subFile:files) {
				String subDirName = dirName + "." + file.getName();
				collectCls(loader, packageName, clazzSet, subFile, subDirName);
			}
		}else {
			dirName = StringUtils.isBlank(dirName) ? "." : dirName + ".";
			clazzSet.add(loader.loadClass(packageName + dirName 
					+ file.getName().substring(0, file.getName().length() - 6)));
		}		
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
	
	/**
	 * 描述：setAccessible为false
	 * @author csy 
	 * @date 2020年12月28日 下午6:27:26
	 * @param field
	 */
	public static void accessibleFalse(Field field){
		if (field != null){
			field.setAccessible(false);
		}
	}
	
	/**
	 * 描述：获取Clazz所有字段
	 * @author csy 
	 * @date 2021年3月19日 下午12:40:24
	 * @param clazz
	 * @return List<Field>
	 */
	public static List<Field> getAllFields(Class<?> clazz){
		Field[] fields = clazz.getDeclaredFields();
		List<Field> fieldList = new ArrayList<Field>();
		fieldList.addAll(Arrays.asList(fields));
		Class<?> superClazz = clazz.getSuperclass();
		while (superClazz != null){
			fieldList.addAll(new ArrayList<>(Arrays.asList(superClazz.getDeclaredFields())));
			superClazz = superClazz.getSuperclass();
	    }	
		return fieldList;
	}
	
	/**
	 * 描述：fieldName获取 Field对象
	 * @author csy 
	 * @date 2021年3月19日 下午1:39:42
	 * @param clazz
	 * @param fieldName
	 * @return Field
	 */
	public static Field getFieldByFieldName(Class<?> clazz , String fieldName) {
		Field field = null;
		try {
			field = clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			Class<?> superClazz = clazz.getSuperclass();
			if (superClazz == null){
				throw new RuntimeException("not fount fieldName " + fieldName);
			}
			
			try {
				field = superClazz.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e1) {
				return getFieldByFieldName(superClazz, fieldName);
			} 
		} 
		return field;
	}
	
	/**
	 * 描述：设置字段值
	 * @author csy 
	 * @date 2021年3月19日 下午1:57:55
	 * @param field
	 * @param entity
	 * @param value
	 */
	public static void setFieldValue(Field field , Object entity , Object value){
		try {
			field.setAccessible(true);
			field.set(entity, value);
		} catch (Exception e) {
			throw new RuntimeException("设置目标字段值失败" + field.getName() , e);
		}finally {
			accessibleFalse(field);
		}		
	}
	
	/**
	 * 描述：获取字段值
	 * @author csy 
	 * @date 2021年3月19日 下午1:57:42
	 * @param field
	 * @param entity
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Object getFieldValue(Field field , Object entity) throws IllegalArgumentException, IllegalAccessException{
		try {
			field.setAccessible(true);
			return field.get(entity);
		}  finally {
			accessibleFalse(field);
		}		
	}
	
	/**
	 * 描述：获取字段值
	 * @author csy 
	 * @date 2021年3月19日 下午2:04:56
	 * @param fieldName
	 * @param entity
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Object getFieldValueByFieldName(String fieldName , Object entity) 
			throws IllegalArgumentException, IllegalAccessException{
		Field field = getFieldByFieldName(entity.getClass(), fieldName);
		return getFieldValue(field, entity);
	}
	
	/**
	 * 
	 * 描述：查找是否实现了InitTaskBase接口
	 * @author csy
	 * @date 2022年9月29日 下午6:06:19
	 * @param cls
	 * @return
	 */
	public static boolean isContainTargetInterface(Class<?> cls, 
			final Class<?> targetInterfaceCls){
		
		if (Objects.isNull(cls)){
			return false;
		}
		
		Class<?>[] interfaces = cls.getInterfaces();
		if (ArrayUtils.isEmpty(interfaces)){
			return false;
		}
		
		for (Class<?> interfaceCls:cls.getInterfaces()) {
			
			if (interfaceCls == targetInterfaceCls) {
				return true;
			}
			
			if (Modifier.isAbstract(cls.getModifiers())
					|| Modifier.isInterface(cls.getModifiers())){
				return isContainTargetInterface(cls, targetInterfaceCls);
			}
		}
		
		return false;
	}
}

package com.csy.test.commons.jarmanage.base;

/**
 * 
 * 描述 :写或读基类 
 * @author csy
 * @date 2022年1月13日 下午2:07:43
 * @param <T>
 */
public interface JarReadWriteBase<T> {
	
	/**
	 * 描述：存储writeObject
	 * @author csy 
	 * @date 2022年1月13日 下午2:09:28
	 * @param writeObject
	 */
	void write(T writeObject);
	
	/**
	 * 描述：读取 
	 * @author csy 
	 * @date 2022年1月13日 下午2:09:46
	 * @param readObject
	 * @return T
	 */
	T read(T readObject);
}

package com.csy.test.commons.utils;

import java.util.Collections;
import java.util.List;

/**
 * 
 * 描述：Object 工具类
 * @author csy
 * @date 2021年2月4日 下午5:21:20
 */
public class Objects {
	
	private Objects(){}
	
	private static final Objects OBJECT = new Objects();
	
	@SuppressWarnings("rawtypes")
	public static final List EMPTY_LIST = Collections.EMPTY_LIST;
	
	public static final String EMPTY_STRING = "";
	
	public static final String NULL_STRING = null;
	
	/**
	 * 描述：判空，默认值
	 * @author csy 
	 * @date 2021年2月4日 下午5:20:42
	 * @param o 判空对象
	 * @param defaultObject 默认值
	 * @return T
	 */
	public static <T> T ifNullDefault(T o , T defaultObject){
		return isNull(o) ? defaultObject : o;
	}
	
	/**
	 * 描述：非空对象 断言
	 * @author csy 
	 * @date 2021年2月4日 下午5:19:54
	 * @param o 判空对象
	 * @param exceptionMessage 异常信息
	 */
	public static void notNullAssert(Object o , String exceptionMessage){
		notNullAssert(o, RuntimeException.class, exceptionMessage);
	}
	
	/**
	 * 描述：非空对象 断言
	 * @author csy 
	 * @date 2021年2月4日 下午5:19:54
	 * @param o 判空对象
	 * @param exceptionClazz 自定义异常类
	 * @param exceptionMessage 异常信息
	 */
	public static void notNullAssert(Object o ,Class<? extends RuntimeException> exceptionClazz, String exceptionMessage){
		isConditionAssert(notNull(o), exceptionClazz, exceptionMessage);
	}
	
	/**
	 * 描述：空对象 断言
	 * @author csy 
	 * @date 2021年2月4日 下午5:19:57
	 * @param o 判空对象
	 * @param exceptionMessage 异常信息
	 */
	public static void isNullAssert(Object o , String exceptionMessage){
		isNullAssert(o, RuntimeException.class, exceptionMessage);
	}
	
	/**
	 * 描述：空对象 断言
	 * @author csy 
	 * @date 2021年2月4日 下午5:19:57
	 * @param o 判空对象
	 * @param exceptionClazz 自定义异常类
	 * @param exceptionMessage 异常信息
	 */
	public static void isNullAssert(Object o ,Class<? extends RuntimeException> exceptionClazz , String exceptionMessage){
		isConditionAssert(isNull(o), exceptionClazz, exceptionMessage);
	}
	
	/**
	 * 描述：判空
	 * @author csy 
	 * @date 2021年2月4日 下午5:19:18
	 * @param o
	 * @return true非空
	 */
	public static boolean notNull(Object o){
		return !isNull(o);
	}
	
	/**
	 * 描述：对象为空
	 * @author csy 
	 * @date 2021年2月4日 下午5:18:56
	 * @param o
	 * @return true空
	 */
	public static boolean isNull(Object o){
		return o == null;
	}
	
	/**
	 * 描述：判断为false抛异常
	 * @author csy 
	 * @date 2021年3月18日 上午9:38:16
	 * @param condition
	 * @param clazz
	 * @param message
	 */
	public static <T extends RuntimeException> void isConditionAssert(boolean condition , Class<T> clazz , String message){
		isConditionAssert(condition, clazz, new Class[]{String.class} , message);
	}
	
	/**
	 * 描述：判断为false抛异常
	 * @author csy 
	 * @date 2021年3月18日 上午9:34:49
	 * @param condition
	 * @param clazz
	 * @param paramTypes
	 * @param params
	 */
	public static <T extends RuntimeException> void isConditionAssert(boolean condition , Class<T> clazz , Class<?>[] paramTypes , Object ...params){
		if (!condition){
			throw ClassUtils.newInstanceByConstrutor(clazz, paramTypes, params);
		}
	}
	
	/**
	 * 描述：判断
	 * @author csy 
	 * @date 2021年4月1日 下午12:58:21
	 * @param object
	 * @param rs
	 * @return ThenObject
	 */
	public static <T> ThenObject<T> ifTrue(T source , boolean rs){
		return OBJECT.new ThenObject<T>(source , rs);
	}
	
	/**
	 * 描述：判断
	 * @author csy 
	 * @date 2021年4月1日 下午12:58:30
	 * @param object
	 * @return IfObject
	 */
	public static <T> IfObject<T> ifObject(T source){
		return OBJECT.new IfObject<T>(source);
	}
	
	public class IfObject<T>{
		
		private T source;
		
		public IfObject(T source){
			this.source = source;
		}
		
		public ThenObject<T> ifNull(){
			return OBJECT.new ThenObject<T>(this.source , this.source == null);
		}
		
		public ThenObject<T> ifNotNull(){
			return OBJECT.new ThenObject<T>(this.source , this.source != null);
		}
	}
	
	public class ThenObject<T>{
		
		private boolean select;
		
		private T source;
		
		private Object result;
		
		public ThenObject(boolean select){
			this.select = select;
		}
		
		public ThenObject(T source , boolean select){
			this.select = select;
			this.source = source;
		}
		
		public ThenObject<T> then(DoWork<T> work){
			if (select){
				result = work.todo(this.source);
			}
			return this;
		}
		
		public ThenObject<T> thenElse(DoWork<T> work){
			if (!select){
				result = work.todo(this.source);
			}
			return this;
		}

		@SuppressWarnings("unchecked")
		public <W> W getResult() {
			return (W) result;
		}
	}
	
	public interface DoWork <T> {
		
		Object todo(T object);
	}
}

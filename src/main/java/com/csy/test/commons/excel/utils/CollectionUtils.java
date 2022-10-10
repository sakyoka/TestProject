package com.csy.test.commons.excel.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 集合处理
 * @author csy
 * @date 2020年1月16日
 */
public class CollectionUtils {

    /**
     * 描述：分割集合
     * <br> 注：subList多线程操作移除时候会有异常
     * @author csy
     * @date 2020年1月6日
     * @param sourceList
     * @param length
     * @return List<List<T>>
     */
    public static <T> List<List<T>> cutListByLength(final List<T> sourceList , final int length){

        List<List<T>> groupList = new ArrayList<List<T>>();
        if (sourceList == null || sourceList.isEmpty())
            return groupList;

        int size = sourceList.size();
        if (length == 0 || length == 1 || size <= length){
            groupList.add(sourceList);
            return groupList;
        }

        int endSize = length;
        for (int i = 0 ; i < size ; i += length){
            if (endSize + i > size){
                groupList.add( sourceList.subList( i , size) );
            }else{
                groupList.add(sourceList.subList( i, i + endSize));
            }
        }

        return groupList;
    }
    
    /**
     * 描述：分割集合（采用新集合存储）
     * @author csy 
     * @date 2022年4月18日 下午5:56:44
     * @param sourceList  数据源
     * @param size        分割集合每个大小
     * @return List<List<T>>
     */
    public static <T> List<List<T>> cutListByLengthOfNew(List<T> sourceList, int size) {
    	return cutListByLengthOfNew(sourceList, size, ArrayList.class);
    }
    
    /**
     * 描述：分割集合（采用新集合存储）
     * @author csy 
     * @date 2022年4月18日 下午5:56:44
     * @param sourceList  数据源
     * @param size        分割集合每个大小
     * @param cls         子集合的实现类型
     * @return List<List<T>>
     */
    public static <T> List<List<T>> cutListByLengthOfNew(List<T> sourceList, int size, Class<?> cls) {
        
    	List<List<T>> result = new ArrayList<List<T>>();
        
        if (sourceList.size() <= size) {
            result.add(sourceList);
            return result;
        }  

        //第一次初始化
        List<T> subValues = (cls == ArrayList.class ? new ArrayList<T>(size) : new LinkedList<T>());
        result.add(subValues);
        int maxSize = sourceList.size();
        int total = 0;
        int count = 0;
        for (T value : sourceList) {
            subValues.add(value);
            count++;
            total++;
            if (count == size) {
                count = 0;
                subValues = null;
                if (total < maxSize){
                    subValues = (cls == ArrayList.class ? new ArrayList<T>(size) : new LinkedList<T>());
                    result.add(subValues);
                }
            }
        }
        return result;
    }
    
    /**
     * 描述：判断是空集合
     * @author csy 
     * @date 2020年1月16日
     * @param collection
     * @return boolean
     */
    public static <E> boolean isEmpty(Collection<E> collection){
    	return collection == null || collection.isEmpty();
    }
    
    /**
     * 描述：判断非空集合
     * @author csy 
     * @date 2020年1月16日
     * @param collection
     * @return boolean
     */
    public static <E> boolean isNotEmpty(Collection<E> collection){
    	return !isEmpty(collection);
    }
    
    /**
     * 描述：获取实体类集合中fieldName列值集合
     * @author csy 
     * @date 2020年10月23日 上午10:56:08
     * @param collections entity集合
     * @param fieldName entity中的fieldName
     * @param clazz 目标集合实现类的class，如HashSet.class、ArrayList.class
     * @return 目标C类型fieldName值集合
     */
	@SuppressWarnings({ "unchecked" })
	public static <T, V , C extends Collection<V>> C getEntityFieldValues(Collection<T> collections , String fieldName , Class<C> clazz){

    	C results = null;
		try {
			results = clazz.newInstance();
	    	if (isEmpty(collections)){
	    		return results;
	    	}
		} catch (InstantiationException e1) {
			throw new RuntimeException("实例化clazz失败" , e1);
		} catch (IllegalAccessException e1) {
			throw new RuntimeException("实例化clazz失败" , e1);
		}
		
		Field field = null;
    	try {
    		Object value = null;
        	for (T entity:collections){
        		field = entity.getClass().getDeclaredField(fieldName);
        		field.setAccessible(true);
        		value = field.get(entity);
        		results.add((value == null ? null : (V)value));
        		field.setAccessible(false);
        	}
		} catch (Exception e) {
			throw new RuntimeException("获取字段值集合失败" ,e);
		}finally {
			if (field != null){
				field.setAccessible(false);
			}
		}

    	return results;
    }
    
    /**
     * 描述：如果list是空集，取默认defaults
     * @author csy 
     * @date 2020年2月27日
     * @param list
     * @param defaults
     * @return List<T>
     */
    public static <T> List<T> emptyDefault(List<T> list , List<T> defaults){
    	return isEmpty(list) ? defaults : list;
    }
    
    /**
     * 描述：如果list是空集，取默认Collections.EMPTY_LIST
     * @author csy 
     * @date 2020年2月27日
     * @param list
     * @return List<T>
     */
    @SuppressWarnings("unchecked")
	public static <T> List<T> emptyDefault(List<T> list){
    	return emptyDefault(list , Collections.EMPTY_LIST);
    }
}

package com.csy.test.commons.excel.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 集合处理
 * @author csy
 * @date 2020年1月16日
 */
public class CollectionUtils {

    /**
     * 描述：分割集合
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
     * 描述：
     * @author csy 
     * @date 2020年1月16日
     * @param collection
     * @return boolean
     */
    public static <E> boolean isEmpty(Collection<E> collection){
    	return collection == null || collection.isEmpty();
    }
    
    /**
     * 描述：
     * @author csy 
     * @date 2020年1月16日
     * @param collection
     * @return boolean
     */
    public static <E> boolean isNotEmpty(Collection<E> collection){
    	return !isEmpty(collection);
    }
}

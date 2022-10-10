package com.csy.test.commons.excel.bean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.csy.test.commons.utils.Objects;

public class ListCount {

	private int listCount = 0;
	
	/**
	 * 描述：判断集合数据大于1 直接抛异常
	 * @author csy 
	 * @date 2021年11月26日 上午9:51:35
	 * @param field
	 */
    public void oneAccessAssert(Field field){
    	if (field.getType() == List.class
    			|| field.getType() == ArrayList.class
    			|| field.getType() == LinkedList.class
    			|| field.getType() == Set.class
    			|| field.getType() == HashSet.class
    			|| field.getType() == LinkedHashSet.class){
    		
    		Objects.isConditionAssert(listCount == 0, RuntimeException.class, "对象(子对象不许存在集合)里面仅且只有一个集合类型");

    		listCount++;
    		return ;
    	}
    	
    	throw new RuntimeException("field subClass must be collection");
    }
    
    /**
     * 描述：重置listCount
     * @author csy 
     * @date 2021年11月26日 下午5:48:26
     */
    public void resetListCount(){
    	listCount = 0;
    }
}

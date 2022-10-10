package com.csy.test.ui.patch.cache;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 描述：mainFrame 按钮缓存
 * @author csy
 * @date 2020年9月29日 下午5:05:48
 */
public class ButtonParamCache {
	
	private ButtonParamCache(){}
	
	/**
	 * 缓存
	 */
	private static Integer RADIO_LOCAL;
	
	/**
	 * 缓存
	 */
	private static List<String> CHECK_BOX_LOCAL = null;
	
	/**
	 * 描述：获取类型
	 * @author csy 
	 * @date 2020年9月29日 下午5:20:10
	 * @return
	 */
	public static Integer getRadio(){
		return RADIO_LOCAL;
	}
	
	/**
	 * 描述：设置类型
	 * @author csy 
	 * @date 2020年9月29日 下午5:20:26
	 * @param v
	 */
	public static void setRadio(Integer v){
		 RADIO_LOCAL = v;
	}
	
	/**
	 * 描述：移除类型
	 * @author csy 
	 * @date 2020年9月29日 下午5:20:36
	 */
	public static void removeRadio(){
		RADIO_LOCAL = null;
	}
	
	/**
	 * 描述：获取复选
	 * @author csy 
	 * @date 2020年9月29日 下午5:20:10
	 * @return
	 */
	public static List<String> getCheckBox(){
		
		if (CHECK_BOX_LOCAL== null)
			setCheckBox(new ArrayList<String>());
		
		return CHECK_BOX_LOCAL;
	}
	
	/**
	 * 描述：设置复选
	 * @author csy 
	 * @date 2020年9月29日 下午5:20:26
	 * @param v
	 */
	public static void setCheckBox(List<String> v){
		CHECK_BOX_LOCAL = v;
	}
	
	/**
	 * 描述：移除复选
	 * @author csy 
	 * @date 2020年9月29日 下午5:20:36
	 */
	public static void removeCheckBox(){
		if (CHECK_BOX_LOCAL != null)
			CHECK_BOX_LOCAL.clear();
		
		CHECK_BOX_LOCAL = null;
	}
	
	/**
	 * 描述：移除全部
	 * @author csy 
	 * @date 2020年9月29日 下午5:22:21
	 */
	public static void removeAll(){
		
		removeCheckBox();
		
		removeRadio();
	}
}

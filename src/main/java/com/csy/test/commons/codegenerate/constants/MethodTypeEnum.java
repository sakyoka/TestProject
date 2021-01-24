package com.csy.test.commons.codegenerate.constants;

/**
 * 
 * 描述:方法类型-对应mapper
 * @author csy
 * @date 2021年1月24日 下午3:10:01
 */
public enum MethodTypeEnum {
	
	FIND_LIST(0),
	GET_ONE(1),
	INSERT(2),
	UPDATE(3),
	DELETE(4);
	
	private Integer type;
	
	private MethodTypeEnum(Integer type) {
		this.type = type;
	}

	public Integer getType() {
		return type;
	}
	
	/**
	 * 描述：判断值是否相等
	 * @author csy
	 * @date 2021年1月24日 下午3:13:28
	 * @param type 传入值
	 * @return true相等
	 */
	public boolean equalsType(Integer type) {
		return this.type == type;
	}
}

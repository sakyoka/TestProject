package com.csy.test.commons.codegenerate.base;

/**
 * 
 * 描述：文件转移
 * @author csy
 * @date 2021年1月29日 上午10:54:12
 */
public interface TranferFileBase {
	
	/**
	 * 描述：转移方法
	 * @author csy 
	 * @date 2021年1月29日 上午10:54:07
	 * @param sourceDir 源目录
	 * @param targetDir 目标目录
	 */
	void tranfer(String sourceDir , String targetDir);
}

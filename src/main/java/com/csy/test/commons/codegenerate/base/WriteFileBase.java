package com.csy.test.commons.codegenerate.base;

import com.csy.test.commons.codegenerate.bean.CodeGenerateParams;
import com.csy.test.commons.codegenerate.constants.FileSuffixEnum;

public interface WriteFileBase {

	/**
	 * 
	 * 描述：输出文件
	 * @author csy
	 * @date 2021年1月23日 下午5:22:15
	 * @param codeGenerateParams
	 * @param tableName
	 * @param contents 文件内容
	 * @param fileSuffixEnum 记录文件后缀及类型对象
	 */
	void write(CodeGenerateParams codeGenerateParams , String tableName , String contents , FileSuffixEnum fileSuffixEnum);
}

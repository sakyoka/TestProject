package com.csy.test.commons.codegenerate.base;

import com.csy.test.commons.codegenerate.database.bean.base.DataMetaBase;

public interface CodeGenerateBase {

	/**
	 * 
	 * 描述：初始化一些参数
	 * @author csy
	 * @date 2021年1月23日 下午9:32:51
	 * @param dataMetaBase
	 * @return CodeGenerateBase
	 */
	CodeGenerateBase preInit(DataMetaBase dataMetaBase);
	
	/**
	 * 
	 * 描述：生成bean文件
	 * @author csy
	 * @date 2021年1月23日 下午9:33:07
	 * @return dataMetaBase
	 */
	CodeGenerateBase generateBean();
	
	/**
	 * 
	 * 描述：生成xml
	 * @author csy
	 * @date 2021年1月23日 下午9:33:13
	 * @return dataMetaBase
	 */
	CodeGenerateBase generateMapper();
	
	/**
	 * 
	 * 描述：生成DAO
	 * @author csy
	 * @date 2021年1月23日 下午9:33:17
	 * @return dataMetaBase
	 */
	CodeGenerateBase generateDao();
	
	/**
	 * 
	 * 描述：生成service
	 * @author csy
	 * @date 2021年1月23日 下午9:33:21
	 * @return dataMetaBase
	 */
	CodeGenerateBase generateService();
	
	/**
	 * 
	 * 描述：生成service impl
	 * @author csy
	 * @date 2021年1月23日 下午9:33:25
	 * @return dataMetaBase
	 */
	CodeGenerateBase generateServiceImpl();
	
	/**
	 * 
	 * 描述：生成controller
	 * @author csy
	 * @date 2021年1月24日 下午3:34:19
	 * @return CodeGenerateBase
	 */
	CodeGenerateBase generateController();
}

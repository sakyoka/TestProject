package com.csy.test.webui.users.model;

import java.util.Date;

import com.csy.test.commons.valid.annotion.Valid;
import com.csy.test.commons.valid.base.defaults.BlankValid;
import com.csy.test.commons.valid.base.defaults.NullValid;

import lombok.Data;

/**
 * 
 * 描述：用户信息对象
 * @author csy
 * @date 2022年9月12日 上午11:57:21
 */
@Data
public class UserBean {

	private String uuid;
	
	@Valid(validType = BlankValid.class, errorMessage = "用户中文不能为空")
	private String userChName;
	
	@Valid(validType = BlankValid.class, errorMessage = "用户英文不能为空")
	private String userEnName;
	
	@Valid(validType = BlankValid.class, errorMessage = "账号不能为空")
	private String userName;
	
	@Valid(validType = BlankValid.class, errorMessage = "密码不能为空")
	private String passWord;
	
	private Date createTime;
	
	private Date updateTime;
	
	@Valid(validType = NullValid.class, errorMessage = "是否有效不能为空")
	private Integer valid;
}

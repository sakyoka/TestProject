package com.csy.test.commons.jarmanage.bean;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JarJdkManageBean implements Serializable{
	
	private static final long serialVersionUID = 4575100846618361228L;

	private String jdkPath;
	
	private String jvm;
}

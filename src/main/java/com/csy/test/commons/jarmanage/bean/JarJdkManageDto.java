package com.csy.test.commons.jarmanage.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class JarJdkManageDto {
	
	private String jdkPath;
	
	private String jvm;
}

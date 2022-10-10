package com.csy.test.commons.jarmanage.bean;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JarRefJmxRmiIpPortBean implements Serializable{

	private static final long serialVersionUID = -64159281660834544L;

	private String ip;
	
	private Integer port;
}

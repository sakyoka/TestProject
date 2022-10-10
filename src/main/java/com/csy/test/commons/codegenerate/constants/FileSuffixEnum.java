package com.csy.test.commons.codegenerate.constants;

/**
 * 
 * 描述:类型对应文件的后缀 ps:.java .xml
 * @author csy
 * @date 2021年1月24日 下午3:37:43
 */
public enum FileSuffixEnum {

	BEAN(ClassifyConstants.BEAN , ClassifyConstants.JAVA),
	
	MAPPER(ClassifyConstants.XML , ClassifyConstants.XML),
	
	DAO(ClassifyConstants.DAO , ClassifyConstants.JAVA),
	
	SERVICE(ClassifyConstants.SERVICE , ClassifyConstants.JAVA),
	
	SERVICE_IMPL(ClassifyConstants.SERVICE_IMPL , ClassifyConstants.JAVA),
	
	CONTRLLER(ClassifyConstants.CONTROLLER , ClassifyConstants.JAVA),
	
	QUERY(ClassifyConstants.QUERY , ClassifyConstants.JAVA),
	
	DTO(ClassifyConstants.DTO , ClassifyConstants.JAVA),
	
	VO(ClassifyConstants.VO , ClassifyConstants.JAVA);
	
	private String suffixName;
	
	private String fileType;
	
	private FileSuffixEnum(String fileType , String suffixName) {
		this.fileType = fileType;
		this.suffixName = suffixName;
	}

	public String getSuffixName() {
		return suffixName;
	}

	public String getFileType() {
		return fileType;
	}
}

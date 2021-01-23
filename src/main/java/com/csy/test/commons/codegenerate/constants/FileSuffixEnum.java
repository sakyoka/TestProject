package com.csy.test.commons.codegenerate.constants;

public enum FileSuffixEnum {

	BEAN(ClassifyConstants.BEAN , ClassifyConstants.JAVA),
	
	MAPPER(ClassifyConstants.XML , ClassifyConstants.XML),
	
	DAO(ClassifyConstants.DAO , ClassifyConstants.JAVA),
	
	SERVICE(ClassifyConstants.SERVICE , ClassifyConstants.JAVA),
	
	SERVICE_IMPL(ClassifyConstants.SERVICE_IMPL , ClassifyConstants.JAVA);
	
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

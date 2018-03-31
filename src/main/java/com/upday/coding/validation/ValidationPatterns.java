package com.upday.coding.validation;

public interface ValidationPatterns {
	static final String VALID_AUTHOR_NAME_PATTERN = "^((?!#).)*$";
	static final String VALID_ARTICLE_AUTHORS_PATTERN = "(#[\\w\\d\\s,']+#)+";
	static final String VALID_KEYWORD_PATTERN = "^((?!#).)*$";
	static final String VALID_ARTICLE_KEWORDS_PATTERN = "[#[\\w\\d\\s]#]+";
	static final String VALID_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";		
}

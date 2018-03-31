package com.upday.test.utils;

import java.util.Date;

import com.upday.coding.exceptions.InvalidDateFormatException;
import com.upday.coding.jpa.entities.Article;
import com.upday.coding.util.DateFormatUtil;

public abstract class ArticleStubs {

	public static Long ID = 10L;
	public static String HEADER = "HEADER";
	public static String DESCRIPTION = "DESCRIPTION";
	public static Date PUBLISH_DATE;
	public static String FORMATTED_PUBLISH_DATE;
	public static String TEXT = "TEXT";
	public static String AUTHOR1 = "AUTHOR1";
	public static String AUTHOR2 = "AUTHOR2";
	public static String KEYWORD1 = "KEYWORD1";
	public static String KEYWORD2 = "KEYWORD2";
	public static String KEYWORD3 = "KEYWORD3";
	static {
		try {
			// To Make Tests Deterministic
			PUBLISH_DATE = DateFormatUtil.parseUsingDefaultFormat("2017-10-05 10:25:14");
			FORMATTED_PUBLISH_DATE = DateFormatUtil.formatUsingDefaultFormat(PUBLISH_DATE);
		} catch (InvalidDateFormatException e) {
			e.printStackTrace();
			PUBLISH_DATE = null;
			FORMATTED_PUBLISH_DATE = null;
		}
	}

	public static Article stubArticleWithOnlyOneKeywrodAndAuthor() {
		Article article = new Article();

		String authors = "#" + AUTHOR1 + "#";
		article.setAuthors(authors);
		article.setDescription(DESCRIPTION);
		article.setHeader(HEADER);
		article.setId(ID);
		String keywords = "#" + KEYWORD1 + "#";
		article.setKeywords(keywords);
		article.setPublishDate(PUBLISH_DATE);
		article.setText(TEXT);

		return article;
	}
	
	public static Article stubNewArticleWithOnlyOneKeywrodAndAuthor() {
		Article article = new Article();

		String authors = "#" + AUTHOR1 + "#";
		article.setAuthors(authors);
		article.setDescription(DESCRIPTION);
		article.setHeader(HEADER);
		article.setId(null);
		String keywords = "#" + KEYWORD1 + "#";
		article.setKeywords(keywords);
		article.setPublishDate(PUBLISH_DATE);
		article.setText(TEXT);

		return article;
	}

	public static Article stubArticleWithMultipleKeywrodsAndAuthors() {
		Article article = new Article();

		String authors = "#" + AUTHOR1 + "##" + AUTHOR2 + "#";
		article.setAuthors(authors);
		article.setDescription(DESCRIPTION);
		article.setHeader(HEADER);
		article.setId(ID);
		String keywords = "#" + KEYWORD1 + "##" + KEYWORD2 + "##" + KEYWORD3 + "#";
		article.setKeywords(keywords);
		article.setPublishDate(PUBLISH_DATE);
		article.setText(TEXT);

		return article;
	}

	public static Article stubNewArticleWithMultipleKeywrodsAndAuthors() {
		Article article = new Article();

		String authors = "#" + AUTHOR1 + "##" + AUTHOR2 + "#";
		article.setAuthors(authors);
		article.setDescription(DESCRIPTION);
		article.setHeader(HEADER);
		String keywords = "#" + KEYWORD1 + "##" + KEYWORD2 + "##" + KEYWORD3 + "#";
		article.setKeywords(keywords);
		article.setPublishDate(PUBLISH_DATE);
		article.setText(TEXT);

		return article;
	}

	public static String getArticleStubWithOneAuthorOneKeywordJSON() {
		StringBuilder jsonBuilder = new StringBuilder();
		jsonBuilder.append("{\"id\":10");
		jsonBuilder.append(",\"header\":\"HEADER\"");
		jsonBuilder.append(",\"description\":\"DESCRIPTION\"");
		jsonBuilder.append(",\"text\":\"TEXT\"");
		jsonBuilder.append(",\"publishDate\":\"" + FORMATTED_PUBLISH_DATE + "\"");
		jsonBuilder.append(",\"authors\":[\"AUTHOR1\"]");
		jsonBuilder.append(",\"keywords\":[\"KEYWORD1\"]}");
		return jsonBuilder.toString();
	}
	
	

	public static String getArticleStubWithMultipleAuthorsKeywordsJSON() {
		StringBuilder jsonBuilder = new StringBuilder();
		jsonBuilder.append("{\"id\":10");
		jsonBuilder.append(",\"header\":\"HEADER\"");
		jsonBuilder.append(",\"description\":\"DESCRIPTION\"");
		jsonBuilder.append(",\"text\":\"TEXT\"");
		jsonBuilder.append(",\"publishDate\":\"" + FORMATTED_PUBLISH_DATE + "\"");
		jsonBuilder.append(",\"authors\":[\"AUTHOR1\",\"AUTHOR2\"]");
		jsonBuilder.append(",\"keywords\":[\"KEYWORD1\",\"KEYWORD2\",\"KEYWORD3\"]}");
		return jsonBuilder.toString();
	}
	
	public static String getArticleWithInvalidPublishDateJSON() {
		StringBuilder jsonBuilder = new StringBuilder();
		jsonBuilder.append("{\"id\":10");
		jsonBuilder.append(",\"header\":\"HEADER\"");
		jsonBuilder.append(",\"description\":\"DESCRIPTION\"");
		jsonBuilder.append(",\"text\":\"TEXT\"");
		jsonBuilder.append(",\"publishDate\":\"INVALID_DATE_FORMAT\"");
		jsonBuilder.append(",\"authors\":[\"AUTHOR1\",\"AUTHOR2\"]");
		jsonBuilder.append(",\"keywords\":[\"KEYWORD1\",\"KEYWORD2\",\"KEYWORD3\"]}");
		return jsonBuilder.toString();
	}
	
	public static String getArticleWithInvalidAuthorNameJSON() {
		StringBuilder jsonBuilder = new StringBuilder();
		jsonBuilder.append("{\"id\":10");
		jsonBuilder.append(",\"header\":\"HEADER\"");
		jsonBuilder.append(",\"description\":\"DESCRIPTION\"");
		jsonBuilder.append(",\"text\":\"TEXT\"");
		jsonBuilder.append(",\"publishDate\":\"INVALID_DATE_FORMAT\"");
		jsonBuilder.append(",\"authors\":[\"AUTH#OR1\",\"AUTHOR2\"]");
		jsonBuilder.append(",\"keywords\":[\"KEYWORD1\",\"KEYWORD2\",\"KEYWORD3\"]}");
		return jsonBuilder.toString();
	}
	
	public static String getArticleWithInvalidKeywordJSON() {
		StringBuilder jsonBuilder = new StringBuilder();
		jsonBuilder.append("{\"id\":10");
		jsonBuilder.append(",\"header\":\"HEADER\"");
		jsonBuilder.append(",\"description\":\"DESCRIPTION\"");
		jsonBuilder.append(",\"text\":\"TEXT\"");
		jsonBuilder.append(",\"publishDate\":\"INVALID_DATE_FORMAT\"");
		jsonBuilder.append(",\"authors\":[\"AUTHOR1\",\"AUTHOR2\"]");
		jsonBuilder.append(",\"keywords\":[\"KEYWORD1\",\"KEY#WORD2\",\"KEYWORD3\"]}");
		return jsonBuilder.toString();
	}

	public static String toJson(Article article) {
		StringBuilder jsonBuilder = new StringBuilder("{");
		if (article.getId() != null) {
			jsonBuilder.append("\"id\":" + article.getId());
			jsonBuilder.append(",");
		}
		if (article.getHeader() != null) {
			jsonBuilder.append("\"header\":\"" + article.getHeader() + "\"");
			jsonBuilder.append(",");
		}
		if (article.getDescription() != null) {
			jsonBuilder.append("\"description\":\"" + article.getDescription() + "\"");
			jsonBuilder.append(",");
		}
		if (article.getText() != null) {
			jsonBuilder.append("\"text\":\"" + article.getText() + "\"");
			jsonBuilder.append(",");
		}
		if (article.getPublishDate() != null) {
			String formattedPublishDate = DateFormatUtil.formatUsingDefaultFormat(article.getPublishDate());
			jsonBuilder.append("\"publishDate\":\"" + formattedPublishDate + "\"");
			jsonBuilder.append(",");
		}
		if (article.getAuthors() != null) {
			String[] authors = article.getAuthors().split("##");
			jsonBuilder.append("\"authors\":[");
			for (int i = 0; i < authors.length; ++i) {
				String author = authors[i];
				author = author.replace("#", "");
				jsonBuilder.append("\"" + author + "\"");
				if (i < authors.length - 1) {
					jsonBuilder.append(",");
				}
			}
			jsonBuilder.append("]");
			jsonBuilder.append(",");
		}
		if (article.getKeywords() != null) {
			String[] keywords = article.getKeywords().split("##");
			jsonBuilder.append("\"keywords\":[");
			for (int i = 0; i < keywords.length; ++i) {
				String keyword = keywords[i];
				keyword = keyword.replace("#", "");
				jsonBuilder.append("\"" + keyword + "\"");
				if (i < keywords.length - 1) {
					jsonBuilder.append(",");
				}
			}
			jsonBuilder.append("]");
		}
		jsonBuilder.append("}");
		return jsonBuilder.toString();
	}
}

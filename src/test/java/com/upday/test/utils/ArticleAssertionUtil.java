package com.upday.test.utils;

import static com.upday.coding.validation.ValidationMessages.INVALID_DATE_FORMAT_MSG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

public class ArticleAssertionUtil {
	
	public static void assertThatHasID(JsonPath articleObj) {
		long id = articleObj.getLong("id");
		assertThat(id).isNotNull();
	}
	
	public static void assertThatIdIsEqualsExpected(JsonPath articleObj, Long expectedId) {
		Long id = articleObj.getLong("id");
		assertThat(id).isEqualTo(expectedId);
	}

	public static void assertThatAuthorsIsEqualsExpected(JsonPath articleObj, String... expectedAuthors) {
		List<String> authors = articleObj.getList("authors", String.class);
		assertThat(authors.size()).isEqualTo(expectedAuthors.length);
		int i = 0;
		for(String author : authors) {
			assertThat(author).isEqualTo(expectedAuthors[i]);
			++i;
		}
	}
	
	public static void assertThatKeywordsIsEqualsExpected(JsonPath articleObj, String... expectedKeywords) {
		List<String> keywords = articleObj.getList("keywords", String.class);
		assertThat(keywords.size()).isEqualTo(expectedKeywords.length);
		int i = 0;
		for(String keyword : keywords) {
			assertThat(keyword).isEqualTo(expectedKeywords[i]);
			++i;
		}
	}
	
	public static void assertBadRequestResponseWithMessage(Response response, String expectedMessage) {
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		String message = response.getBody().jsonPath().getString("message");
		assertThat(message).isEqualTo(expectedMessage);
	}
	
	public static void assertBadRequestResponseWithBeanValidationMessage(Response response, String expectedMessage) {
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		String message = response.getBody().jsonPath().getString("errors[0].defaultMessage");
		assertThat(message).isEqualTo(expectedMessage);
	}
	
	public static void assertThatHasPublishDateAndFormattedAsExpected(JsonPath articleObj) {
		String formattedPublishDate = articleObj.getString("publishDate");
		assertThat(formattedPublishDate).isNotNull();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
		try {			
			dateFormat.parse(formattedPublishDate);
		} catch (ParseException e) {
			fail("Un-Expected Date Format:" + e.getMessage());
		}
	}
	
	public static void assertThatHasExpectedDescription(JsonPath articleObj, String expectedDescription) {
		String description = articleObj.getString("description");
		assertThat(description).isEqualTo(expectedDescription);
	}
	
	public static void assertThatHasExpectedText(JsonPath articleObj, String expectedText) {
		String text = articleObj.getString("text");
		assertThat(text).isEqualTo(expectedText);
	}
	
	public static void assertThatHasExpectedHeader(JsonPath articleObj, String expectedHeader) {
		String header = articleObj.getString("header");
		assertThat(header).isEqualTo(expectedHeader);
	}
}

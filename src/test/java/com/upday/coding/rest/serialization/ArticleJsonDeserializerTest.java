package com.upday.coding.rest.serialization;

import static com.upday.test.utils.ArticleStubs.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.upday.coding.exceptions.InvalidAuthorNameException;
import com.upday.coding.exceptions.InvalidDateFormatException;
import com.upday.coding.exceptions.InvalidKeywordException;
import com.upday.coding.jpa.entities.Article;

public class ArticleJsonDeserializerTest {

	@Test
	public void testDeserializeArticleWithOneAuthorAndOneKeywordShouldBeSerializeWithTwoArraysOfOneEelment() {
		try {
			String articleJson = getArticleStubWithOneAuthorOneKeywordJSON();
			ArticleJsonDeserializer targetDeserializer = new ArticleJsonDeserializer();
			ObjectMapper mapper = new ObjectMapper();
			JsonParser parser = mapper.getFactory().createParser(articleJson);
			DeserializationContext context = mapper.getDeserializationContext();
			Article actualArticle = targetDeserializer.deserialize(parser, context);
			Article expectedArticle = stubArticleWithOnlyOneKeywrodAndAuthor();
			assertEquals(expectedArticle.getAuthors(), actualArticle.getAuthors());
			assertEquals(expectedArticle.getDescription(), actualArticle.getDescription());
			assertEquals(expectedArticle.getHeader(), actualArticle.getHeader());
			assertEquals(expectedArticle.getId(), actualArticle.getId());
			assertEquals(expectedArticle.getKeywords(), actualArticle.getKeywords());
			assertTrue(expectedArticle.getPublishDate().equals(actualArticle.getPublishDate()));
			assertEquals(expectedArticle.getText(), actualArticle.getText());
		} catch (Exception e) {
			fail("Un Expected Exception:" + e.getMessage());
		}
	}

	@Test
	public void testDeserializeArticleWithMultipleAuthoraAndKeywordsShouldBeSerializeWithTwoArraysOfMultipleEelments() {
		try {
			String articleJson = getArticleStubWithMultipleAuthorsKeywordsJSON();
			ArticleJsonDeserializer targetDeserializer = new ArticleJsonDeserializer();
			ObjectMapper mapper = new ObjectMapper();
			JsonParser parser = mapper.getFactory().createParser(articleJson);
			DeserializationContext context = mapper.getDeserializationContext();
			Article actualArticle = targetDeserializer.deserialize(parser, context);
			Article expectedArticle = stubArticleWithMultipleKeywrodsAndAuthors();
			assertEquals(expectedArticle.getAuthors(), actualArticle.getAuthors());
			assertEquals(expectedArticle.getDescription(), actualArticle.getDescription());
			assertEquals(expectedArticle.getHeader(), actualArticle.getHeader());
			assertEquals(expectedArticle.getId(), actualArticle.getId());
			assertEquals(expectedArticle.getKeywords(), actualArticle.getKeywords());
			assertTrue(expectedArticle.getPublishDate().equals(actualArticle.getPublishDate()));
			assertEquals(expectedArticle.getText(), actualArticle.getText());
		} catch (Exception e) {
			fail("Un Expected Exception:" + e.getMessage());
		}
	}

	@Test(expected = InvalidDateFormatException.class)
	public void testDeserializeArticleWithInvalidPublishDateFormatExpectedInvalidDateFormatException()
			throws Exception {

		String articleJson = getArticleWithInvalidPublishDateJSON();
		ArticleJsonDeserializer targetDeserializer = new ArticleJsonDeserializer();
		ObjectMapper mapper = new ObjectMapper();
		JsonParser parser = mapper.getFactory().createParser(articleJson);
		DeserializationContext context = mapper.getDeserializationContext();
		targetDeserializer.deserialize(parser, context);
	}

	@Test(expected = InvalidAuthorNameException.class)
	public void testDeserializeArticleWithInvalidAuthorNameExpectedInvalidAuthorNameException()
			throws Exception {

		String articleJson = getArticleWithInvalidAuthorNameJSON();
		ArticleJsonDeserializer targetDeserializer = new ArticleJsonDeserializer();
		ObjectMapper mapper = new ObjectMapper();
		JsonParser parser = mapper.getFactory().createParser(articleJson);
		DeserializationContext context = mapper.getDeserializationContext();
		targetDeserializer.deserialize(parser, context);
	}
	
	@Test(expected = InvalidKeywordException.class)
	public void testDeserializeArticleWithInvalidKeywordExpectedInvalidKeywordException()
			throws Exception {

		String articleJson = getArticleWithInvalidKeywordJSON();
		ArticleJsonDeserializer targetDeserializer = new ArticleJsonDeserializer();
		ObjectMapper mapper = new ObjectMapper();
		JsonParser parser = mapper.getFactory().createParser(articleJson);
		DeserializationContext context = mapper.getDeserializationContext();
		targetDeserializer.deserialize(parser, context);
	}
}

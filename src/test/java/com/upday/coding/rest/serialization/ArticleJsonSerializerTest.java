package com.upday.coding.rest.serialization;

import static com.upday.test.utils.ArticleStubs.getArticleStubWithMultipleAuthorsKeywordsJSON;
import static com.upday.test.utils.ArticleStubs.getArticleStubWithOneAuthorOneKeywordJSON;
import static com.upday.test.utils.ArticleStubs.stubArticleWithMultipleKeywrodsAndAuthors;
import static com.upday.test.utils.ArticleStubs.stubArticleWithOnlyOneKeywrodAndAuthor;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.upday.coding.jpa.entities.Article;

public class ArticleJsonSerializerTest {
	
	@Test
	public void testSerializeArticleWithOneAuthorAndOneKeywordShouldBeSerializeWithTwoArraysOfOneEelment() {		
		try {
			ArticleJsonSerializer targetSerializer = new ArticleJsonSerializer();
			Writer jsonWriter = new StringWriter();
			JsonGenerator jsonGenerator = new JsonFactory().createGenerator(jsonWriter);
			SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();
			
			Article article = stubArticleWithOnlyOneKeywrodAndAuthor();
			targetSerializer.serialize(article, jsonGenerator, serializerProvider);
			
			jsonGenerator.flush();
			
			String actualJson = jsonWriter.toString();
			String expectedJson = getArticleStubWithOneAuthorOneKeywordJSON();
			assertEquals("Invalid Output JSON", expectedJson, actualJson);			
		} catch (Exception e) {
			fail("Un Expected Exception:" + e.getMessage());
		}
	}
	
	@Test
	public void testSerializeArticleWithMultipleAuthoraAndKeywordsShouldBeSerializeWithTwoArraysOfMultipleEelments() {
		try {
			ArticleJsonSerializer targetSerializer = new ArticleJsonSerializer();
			Writer jsonWriter = new StringWriter();
			JsonGenerator jsonGenerator = new JsonFactory().createGenerator(jsonWriter);
			SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();
			
			Article article = stubArticleWithMultipleKeywrodsAndAuthors();
			targetSerializer.serialize(article, jsonGenerator, serializerProvider);
			
			jsonGenerator.flush();
			
			String actualJson = jsonWriter.toString();
			String expectedJson = getArticleStubWithMultipleAuthorsKeywordsJSON();
			assertEquals("Invalid Output JSON", expectedJson, actualJson);			
		} catch (Exception e) {
			fail("Un Expected Exception:" + e.getMessage());
		}
	}
	
}

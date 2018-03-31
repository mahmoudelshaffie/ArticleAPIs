package com.upday.coding.rest.serialization;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.upday.coding.exceptions.InvalidAuthorNameException;
import com.upday.coding.exceptions.InvalidKeywordException;
import com.upday.coding.jpa.entities.Article;
import com.upday.coding.util.DateFormatUtil;
import com.upday.coding.validation.ValidationPatterns;

public class ArticleJsonDeserializer extends JsonDeserializer<Article> {

	@Override
	public Article deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		ObjectCodec codec = parser.getCodec();
		JsonNode node = codec.readTree(parser);
		ObjectMapper mapper = new ObjectMapper();
		CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);

		Article article = new Article();

		if (node.has("authors")) {
			String authorsStr = node.get("authors").toString();
			if (authorsStr.matches(ValidationPatterns.VALID_AUTHOR_NAME_PATTERN)) {
				if (authorsStr != null) {
					List<String> authors = mapper.readValue(authorsStr, listType);
					article.setAuthors(concatArrayIntoOneString(authors.iterator()));
				}
			} else {
				throw new InvalidAuthorNameException();
			}
		}

		if (node.has("description")) {
			article.setDescription(node.get("description").asText());
		}
		if (node.has("header")) {
			article.setHeader(node.get("header").asText());
		}
		if (node.has("id")) {
			article.setId(node.get("id").asLong());
		}

		if (node.has("keywords")) {
			String keywordsStr = node.get("keywords").toString();
			if (keywordsStr.matches(ValidationPatterns.VALID_KEYWORD_PATTERN)) {
				if (keywordsStr != null) {
					List<String> keywords = mapper.readValue(keywordsStr, listType);
					article.setKeywords(concatArrayIntoOneString(keywords.iterator()));
				}
			} else {
				throw new InvalidKeywordException();
			}
		}

		if (node.has("publishDate")) {
			String formattedDate = node.get("publishDate").asText();
			if (formattedDate != null) {
				Date publishDate = DateFormatUtil.parseUsingDefaultFormat(formattedDate);
				article.setPublishDate(publishDate);
			}
		}

		if (node.has("text")) {
			article.setText(node.get("text").asText());
		}
		return article;
	}

	private String concatArrayIntoOneString(Iterator<String> it) {
		StringBuilder builder = new StringBuilder();
		while (it.hasNext()) {
			String next = it.next();
			builder.append("#" + next + "#");
		}

		return builder.toString();
	}

}

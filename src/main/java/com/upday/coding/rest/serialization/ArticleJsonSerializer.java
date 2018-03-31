package com.upday.coding.rest.serialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.upday.coding.jpa.entities.Article;
import com.upday.coding.util.DateFormatUtil;

public class ArticleJsonSerializer extends com.fasterxml.jackson.databind.JsonSerializer<Article> {

	@Override
	public void serialize(Article article, JsonGenerator generator, SerializerProvider provider) throws IOException {
		generator.writeStartObject();
		generator.writeNumberField("id", article.getId());
		generator.writeStringField("header", article.getHeader());
		generator.writeStringField("description", article.getDescription());
		generator.writeStringField("text", article.getText());
		String formattedDate = DateFormatUtil.formatUsingDefaultFormat(article.getPublishDate());
		generator.writeStringField("publishDate", formattedDate);

		String[] authors = article.getAuthors().split("##");
		cleanFirstAndLastElement(authors);
		generator.writeArrayFieldStart("authors");
		for (String author : authors) {
			generator.writeString(author);
		}
		generator.writeEndArray();

		String[] keywords = article.getKeywords().split("##");
		cleanFirstAndLastElement(keywords);
		generator.writeArrayFieldStart("keywords");
		for (String keyword : keywords) {
			generator.writeString(keyword);
		}
		generator.writeEndArray();

		generator.writeEndObject();
	}
	
	private void cleanFirstAndLastElement(String[] arr) {
		int length = arr.length;
		if (length > 0) {
			arr[0] = arr[0].replaceAll("#", "");
			arr[length - 1] = arr[length - 1].replaceAll("#", "");
		}
	}
}

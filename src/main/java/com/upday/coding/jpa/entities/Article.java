package com.upday.coding.jpa.entities;

import static com.upday.coding.validation.ValidationPatterns.VALID_ARTICLE_AUTHORS_PATTERN;
import static com.upday.coding.validation.ValidationPatterns.VALID_ARTICLE_KEWORDS_PATTERN;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.upday.coding.rest.serialization.ArticleJsonDeserializer;
import com.upday.coding.rest.serialization.ArticleJsonSerializer;
import static com.upday.coding.validation.ValidationMessages.*;

@JsonSerialize(using = ArticleJsonSerializer.class)
@JsonDeserialize(using = ArticleJsonDeserializer.class)
@Entity
public class Article {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = HEADER_IS_REQUIRED_MSG)
	@Basic(optional=false)
	private String header;
	
	private String description;
	
	@NotNull(message = TEXT_IS_REQUIRED_MSG)
	@Basic(optional=false)
	private String text;
	
	@Basic(optional=false)
	@Column(updatable=false)
	private Date publishDate;
	
	@NotNull
	@Pattern(regexp=VALID_ARTICLE_AUTHORS_PATTERN, message = INVALID_AUTHOR_NAME_MSG)
	@Basic(optional=false)
	private String authors;

	@Pattern(regexp=VALID_ARTICLE_KEWORDS_PATTERN, message = INVALID_KEYWORD_MSG)
	private String keywords;
	
	public Article() {
		publishDate = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

}

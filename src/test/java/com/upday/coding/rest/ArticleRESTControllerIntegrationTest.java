package com.upday.coding.rest;

import static com.upday.test.utils.ArticleStubs.*;
import static org.assertj.core.api.Assertions.assertThat;
import static com.upday.test.utils.ArticleAssertionUtil.*;
import static com.upday.coding.validation.ValidationMessages.*;

import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.upday.ApplicationConfig;
import com.upday.coding.jpa.entities.Article;
import com.upday.coding.jpa.repositories.ArticleRep;
import com.upday.coding.util.DateFormatUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ApplicationConfig.class }, webEnvironment = WebEnvironment.DEFINED_PORT)
public class ArticleRESTControllerIntegrationTest {
	@Autowired
	private ArticleRep articleRep;

	private static final String BASE_URL = "http://localhost:8080/articles";

	@Before
	public void beforeEach() {
		articleRep.deleteAll();
	}

	@Test
	public void testGetAllArticlesShouldReturnAllPersistedArticlesSuccessfully() {
		Article article = stubNewArticleWithMultipleKeywrodsAndAuthors();
		article = articleRep.save(article);
		Article article2 = stubNewArticleWithOnlyOneKeywrodAndAuthor();
		article2 = articleRep.save(article2);

		Response response = RestAssured.get(BASE_URL);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
		String actualResp = response.getBody().asString();
		String expectedResp = "[" + toJson(article) + "," + toJson(article2) + "]";
		assertThat(actualResp).isEqualTo(expectedResp);
	}

	@Test
	public void testCreateWithValidArticleShouldBeCreatedSuccessfully() {
		Article article = stubNewArticleWithMultipleKeywrodsAndAuthors();
		String postBody = toJson(article);
		RequestSpecification requestSpecification = RestAssured.given();
		requestSpecification.body(postBody);
		requestSpecification.contentType(ContentType.JSON);

		Response response = RestAssured.given(requestSpecification).post(BASE_URL);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());

		JsonPath actualResp = response.getBody().jsonPath();
		assertThatHasID(actualResp);
		assertThatAuthorsIsEqualsExpected(actualResp, AUTHOR1, AUTHOR2);
		assertThatKeywordsIsEqualsExpected(actualResp, KEYWORD1, KEYWORD2, KEYWORD3);
		assertThatHasPublishDateAndFormattedAsExpected(actualResp);
		assertThatHasExpectedDescription(actualResp, article.getDescription());
		assertThatHasExpectedHeader(actualResp, article.getHeader());
		assertThatHasExpectedText(actualResp, article.getText());
	}

	@Test
	public void testCreateArticleWithoutHeaderShouldFailAndReturnBadRequestCode() {
		Article articleWithoutHeader = stubNewArticleWithMultipleKeywrodsAndAuthors();
		articleWithoutHeader.setHeader(null);

		RequestSpecification requestSpecification = prepareRequestBody(articleWithoutHeader);
		Response response = RestAssured.given(requestSpecification).post(BASE_URL);

		assertBadRequestResponseWithBeanValidationMessage(response, HEADER_IS_REQUIRED_MSG);
	}

	@Test
	public void testCreateArticleWithoutTextShouldFailAndReturnBadRequestCode() {
		Article articleWithoutText = stubNewArticleWithMultipleKeywrodsAndAuthors();
		articleWithoutText.setText(null);

		RequestSpecification requestSpecification = prepareRequestBody(articleWithoutText);
		Response response = RestAssured.given(requestSpecification).post(BASE_URL);

		assertBadRequestResponseWithBeanValidationMessage(response, TEXT_IS_REQUIRED_MSG);
	}
	
	@Test
	public void testCreateWithInvalidAuthorNameExpectedBadRequestWithInvalidAuthorNameMessage() {
		String postBody = getArticleWithInvalidAuthorNameJSON();
		RequestSpecification requestSpecification = RestAssured.given();
		requestSpecification.body(postBody);
		requestSpecification.contentType(ContentType.JSON);

		Response response = RestAssured.given(requestSpecification).post(BASE_URL);

		assertBadRequestResponseWithMessage(response, INVALID_AUTHOR_NAME_MSG);
	}
	
	@Test
	public void testCreateWithInvalidKeywordExpectedBadRequestWithInvalidKeywordMessage() {
		String postBody = getArticleWithInvalidKeywordJSON();
		RequestSpecification requestSpecification = RestAssured.given();
		requestSpecification.body(postBody);
		requestSpecification.contentType(ContentType.JSON);

		Response response = RestAssured.given(requestSpecification).post(BASE_URL);

		assertBadRequestResponseWithMessage(response, INVALID_KEYWORD_MSG);
	}
	
	@Test
	public void testCreateWithInvalidPublishDateFormatExpectedBadRequestWithInvalidDateFormatMessage() {
		String postBody = getArticleWithInvalidPublishDateJSON();
		RequestSpecification requestSpecification = RestAssured.given();
		requestSpecification.body(postBody);
		requestSpecification.contentType(ContentType.JSON);

		Response response = RestAssured.given(requestSpecification).post(BASE_URL);

		assertBadRequestResponseWithMessage(response, INVALID_DATE_FORMAT_MSG);
	}

	@Test
	public void testUpdateArticleShouldBeUpdatedSuccessfully() {
		Article article = stubNewArticleWithMultipleKeywrodsAndAuthors();
		article = articleRep.save(article);

		article.setHeader("New Header");
		RequestSpecification requestSpecification = prepareRequestBody(article);
		Response response = RestAssured.given(requestSpecification).put(BASE_URL + "/" + article.getId());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());

		JsonPath actualResp = response.getBody().jsonPath();
		assertThatIdIsEqualsExpected(actualResp, article.getId());
		assertThatHasExpectedHeader(actualResp, article.getHeader());
	}

	@Test
	public void testUpdateArticleAuthorsAndKeywordsNameShouldBeUpdateSuccessfully() {
		Article article = stubNewArticleWithMultipleKeywrodsAndAuthors();
		article = articleRep.save(article);

		String authors = article.getAuthors();
		authors += "#AUTHOR3#";
		article.setAuthors(authors);

		String keywords = "#" + KEYWORD2 + "##" + KEYWORD1 + "#";
		article.setKeywords(keywords);

		RequestSpecification requestSpecification = prepareRequestBody(article);
		Response response = RestAssured.given(requestSpecification).put(BASE_URL + "/" + article.getId());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());

		JsonPath actualResp = response.getBody().jsonPath();
		assertThatIdIsEqualsExpected(actualResp, article.getId());
		assertThatAuthorsIsEqualsExpected(actualResp, AUTHOR1, AUTHOR2, "AUTHOR3");
		assertThatKeywordsIsEqualsExpected(actualResp, KEYWORD2, KEYWORD1);
	}

	@Test
	public void testDeleteAlreadyExistArticleShouldBeDeletedSuccessfully() {
		Article article = stubNewArticleWithMultipleKeywrodsAndAuthors();
		article = articleRep.save(article);

		Response response = RestAssured.delete(BASE_URL + "/" + article.getId());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
		Boolean deleted = response.getBody().as(Boolean.class);
		assertThat(deleted).isTrue();
	}

	@Test
	public void testDeleteUnExistArticleShouldReturnFalse() {
		Long ANY_ID = 100L;
		Response response = RestAssured.delete(BASE_URL + "/" + ANY_ID);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
		Boolean deleted = response.getBody().as(Boolean.class);
		assertThat(deleted).isFalse();
	}

	@Test
	public void testGetAlreadyExistArticleShouldBeReturnedSuccessfully() {
		Article article = stubNewArticleWithMultipleKeywrodsAndAuthors();
		article = articleRep.save(article);

		Response response = RestAssured.get(BASE_URL + "/" + article.getId());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());

		JsonPath actualResp = response.getBody().jsonPath();
		assertThatHasID(actualResp);
		assertThatAuthorsIsEqualsExpected(actualResp, AUTHOR1, AUTHOR2);
		assertThatKeywordsIsEqualsExpected(actualResp, KEYWORD1, KEYWORD2, KEYWORD3);
		assertThatHasPublishDateAndFormattedAsExpected(actualResp);
		assertThatHasExpectedDescription(actualResp, article.getDescription());
		assertThatHasExpectedHeader(actualResp, article.getHeader());
		assertThatHasExpectedText(actualResp, article.getText());
	}

	@Test
	public void testGetUnExistArticleShouldBeReturnedSuccessfully() {
		Long ANY_ID = 100L;
		Response response = RestAssured.get(BASE_URL + "/" + ANY_ID);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());

		String body = response.getBody().asString();
		assertThat(body).isEmpty();
	}

	@Test
	public void testSearchByAuthorNameExpectedOnlyOneArticleSuccessfully() {
		Article article = stubNewArticleWithMultipleKeywrodsAndAuthors();
		article = articleRep.save(article);
		Article article2 = stubNewArticleWithOnlyOneKeywrodAndAuthor();
		article2 = articleRep.save(article2);

		Response response = RestAssured.given().queryParam("authorName", "AUTHOR2").get(BASE_URL + "/search/by/author");
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
		String actualResults = response.getBody().asString();
		String expectedResults = "[" + toJson(article) + "]";
		assertThat(actualResults).isEqualTo(expectedResults);
	}

	@Test
	public void testSearchByPartOfAuthorNameShouldReturnAllArticlesHasAuthorHasThisSequence() {
		Article article = stubNewArticleWithMultipleKeywrodsAndAuthors();
		article.setAuthors("#Kennedy, Martin##Johnny, Edward#");
		article = articleRep.save(article);
		Article article2 = stubNewArticleWithOnlyOneKeywrodAndAuthor();
		article2.setAuthors("#Kevin, John##Lewis, Katia#");
		article2 = articleRep.save(article2);
		Article article3 = stubNewArticleWithOnlyOneKeywrodAndAuthor();
		article3 = articleRep.save(article3);

		Response response = RestAssured.given().queryParam("authorName", "John").get(BASE_URL + "/search/by/author");
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
		String actualResults = response.getBody().asString();
		String expectedResults = "[" + toJson(article) + "," + toJson(article2) + "]";
		assertThat(actualResults).isEqualTo(expectedResults);
	}
	
	@Test
	public void testSearchByInvalidAuthorNameExpectedErrorWithInvalidAuthorNameMessage() {
		Article article = stubNewArticleWithMultipleKeywrodsAndAuthors();
		article.setAuthors("#Kennedy, Martin##Johnny, Edward#");
		article = articleRep.save(article);
		Article article2 = stubNewArticleWithOnlyOneKeywrodAndAuthor();
		article2.setAuthors("#Kevin, John##Lewis, Katia#");
		article2 = articleRep.save(article2);
		Article article3 = stubNewArticleWithOnlyOneKeywrodAndAuthor();
		article3 = articleRep.save(article3);

		Response response = RestAssured.given().queryParam("authorName", "INVALID###AUTHOR##NAME").get(BASE_URL + "/search/by/author");
		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
		assertThat(response.getBody().jsonPath().getString("message")).contains(INVALID_AUTHOR_NAME_MSG);
	}

	@Test
	public void testSearchByKeywordWithAlreadyExistKeywordShouldReturnArticlesWithThisSpecificWord() {
		Article article = stubNewArticleWithMultipleKeywrodsAndAuthors();
		article = articleRep.save(article);
		Article article2 = stubNewArticleWithOnlyOneKeywrodAndAuthor();
		article2 = articleRep.save(article2);
		Article article3 = stubNewArticleWithOnlyOneKeywrodAndAuthor();
		article3.setKeywords("#" + KEYWORD2 + "#");
		article3 = articleRep.save(article3);

		Response response = RestAssured.given().queryParam("keyword", KEYWORD2).get(BASE_URL + "/search/by/keyword");
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
		String actualResults = response.getBody().asString();
		String expectedResults = "[" + toJson(article) + "," + toJson(article3) + "]";
		assertThat(actualResults).isEqualTo(expectedResults);
	}

	@Test
	public void testSearchByKeywordWithNonExistKeywordShouldReturnEmptyResultsSuccessfully() {
		Article article = stubNewArticleWithMultipleKeywrodsAndAuthors();
		article = articleRep.save(article);
		Article article2 = stubNewArticleWithOnlyOneKeywrodAndAuthor();
		article2 = articleRep.save(article2);
		Article article3 = stubNewArticleWithOnlyOneKeywrodAndAuthor();
		article3.setKeywords("#" + KEYWORD2 + "#");
		article3 = articleRep.save(article3);

		String NON_EXIST_KEYWORD = "NON_EXIST_KEYWORD";

		Response response = RestAssured.given().queryParam("keyword", NON_EXIST_KEYWORD)
				.get(BASE_URL + "/search/by/keyword");
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
		String actualResults = response.getBody().asString();
		String expectedResults = "[]";
		assertThat(actualResults).isEqualTo(expectedResults);
	}

	@Test
	public void testSearchByKeywordWithKeywordShouldReturnOnlyArticleResultsSuccessfully() {
		String ONLY_ONE_EXIST_KEYWORD = "ONLY_ONE_EXIST_KEYWORD";
		
		Article article = stubNewArticleWithMultipleKeywrodsAndAuthors();
		article = articleRep.save(article);
		Article article2 = stubNewArticleWithOnlyOneKeywrodAndAuthor();
		article2 = articleRep.save(article2);
		Article article3 = stubNewArticleWithOnlyOneKeywrodAndAuthor();
		article3.setKeywords("#" + ONLY_ONE_EXIST_KEYWORD + "#");
		
		article3 = articleRep.save(article3);

		Response response = RestAssured.given().queryParam("keyword", ONLY_ONE_EXIST_KEYWORD)
				.get(BASE_URL + "/search/by/keyword");
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
		String actualResults = response.getBody().asString();
		String expectedResults = "[" + toJson(article3) + "]";
		assertThat(actualResults).isEqualTo(expectedResults);
	}
	
	@Test
	public void testSearchByKeywordWithInValidKeywordExpectedErrorWithInvalidKeywordMessage() {
		
		Article article = stubNewArticleWithMultipleKeywrodsAndAuthors();
		article = articleRep.save(article);
		Article article2 = stubNewArticleWithOnlyOneKeywrodAndAuthor();
		article2 = articleRep.save(article2);

		Response response = RestAssured.given().queryParam("keyword", "#INVAL###IDKeyword")
				.get(BASE_URL + "/search/by/keyword");
		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
		assertThat(response.getBody().jsonPath().getString("message")).contains(INVALID_KEYWORD_MSG);
	}

	@Test
	public void testSearchWithInPeriodForOnlyOneArticle() throws Exception {
		Date date1 = DateFormatUtil.parseUsingDefaultFormat("2017-10-25 00:05:00");
		Article article = stubNewArticleWithMultipleKeywrodsAndAuthors();
		article.setPublishDate(date1);
		article = articleRep.save(article);

		Date date2 = DateFormatUtil.parseUsingDefaultFormat("2018-01-04 10:25:00");
		Article article2 = stubNewArticleWithOnlyOneKeywrodAndAuthor();
		article2.setPublishDate(date2);
		article2 = articleRep.save(article2);

		Date date3 = DateFormatUtil.parseUsingDefaultFormat("2018-01-25 15:57:32");
		Article article3 = stubNewArticleWithOnlyOneKeywrodAndAuthor();
		article3.setPublishDate(date3);
		article3 = articleRep.save(article3);

		String start = "2017-01-01 00:00:00";
		String end = "2017-12-31 00:00:00";
		Response response = RestAssured.given().queryParam("start", start).queryParam("end", end)
				.get(BASE_URL + "/search/by/date");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
		String actualResults = response.getBody().asString();
		String expectedResults = "[" + toJson(article) + "]";
		assertThat(actualResults).isEqualTo(expectedResults);
	}
	
	@Test
	public void testSearchWithInPeriodForMoreThanOneArticle() throws Exception {
		Date date1 = DateFormatUtil.parseUsingDefaultFormat("2017-10-25 00:05:00");
		Article article = stubNewArticleWithMultipleKeywrodsAndAuthors();
		article.setPublishDate(date1);
		article = articleRep.save(article);

		Date date2 = DateFormatUtil.parseUsingDefaultFormat("2018-01-04 10:25:00");
		Article article2 = stubNewArticleWithOnlyOneKeywrodAndAuthor();
		article2.setPublishDate(date2);
		article2 = articleRep.save(article2);

		Date date3 = DateFormatUtil.parseUsingDefaultFormat("2018-01-25 15:57:32");
		Article article3 = stubNewArticleWithOnlyOneKeywrodAndAuthor();
		article3.setPublishDate(date3);
		article3 = articleRep.save(article3);

		String start = "2018-01-01 00:00:00";
		String end = "2018-12-31 23:59:59";
		Response response = RestAssured.given().queryParam("start", start).queryParam("end", end)
				.get(BASE_URL + "/search/by/date");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
		String actualResults = response.getBody().asString();
		String expectedResults = "[" + toJson(article2) + "," + toJson(article3) + "]";
		assertThat(actualResults).isEqualTo(expectedResults);
	}
	
	@Test
	public void testSearchWithinPeriodWithValidStartAndEndExpectedEmptyResults() throws Exception {
		Date date1 = DateFormatUtil.parseUsingDefaultFormat("2017-10-25 00:05:00");
		Article article = stubNewArticleWithMultipleKeywrodsAndAuthors();
		article.setPublishDate(date1);
		article = articleRep.save(article);

		Date date2 = DateFormatUtil.parseUsingDefaultFormat("2018-01-04 10:25:00");
		Article article2 = stubNewArticleWithOnlyOneKeywrodAndAuthor();
		article2.setPublishDate(date2);
		article2 = articleRep.save(article2);

		Date date3 = DateFormatUtil.parseUsingDefaultFormat("2018-01-25 15:57:32");
		Article article3 = stubNewArticleWithOnlyOneKeywrodAndAuthor();
		article3.setPublishDate(date3);
		article3 = articleRep.save(article3);

		String start = "2016-0-01 00:00:00";
		String end = "2016-12-31 23:59:59";
		Response response = RestAssured.given().queryParam("start", start).queryParam("end", end)
				.get(BASE_URL + "/search/by/date");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
		String actualResults = response.getBody().asString();
		String expectedResults = "[]";
		assertThat(actualResults).isEqualTo(expectedResults);
	}
	
	@Test
	public void testSearchWithInPeriodWithInvalidStartExpectedBadRequestWithInvalidDateFormatMessage() throws Exception {
		Date date1 = DateFormatUtil.parseUsingDefaultFormat("2017-10-25 00:05:00");
		Article article = stubNewArticleWithMultipleKeywrodsAndAuthors();
		article.setPublishDate(date1);
		article = articleRep.save(article);

		Date date2 = DateFormatUtil.parseUsingDefaultFormat("2018-01-04 10:25:00");
		Article article2 = stubNewArticleWithOnlyOneKeywrodAndAuthor();
		article2.setPublishDate(date2);
		article2 = articleRep.save(article2);

		Date date3 = DateFormatUtil.parseUsingDefaultFormat("2018-01-25 15:57:32");
		Article article3 = stubNewArticleWithOnlyOneKeywrodAndAuthor();
		article3.setPublishDate(date3);
		article3 = articleRep.save(article3);

		String start = "INVALID START";
		String end = "2016-12-31 23:59:59";
		Response response = RestAssured.given().queryParam("start", start).queryParam("end", end)
				.get(BASE_URL + "/search/by/date");

		assertBadRequestResponseWithMessage(response, INVALID_DATE_FORMAT_MSG);
	}
	
	@Test
	public void testSearchWithInPeriodWithInvalidEndExpectedBadRequestWithInvalidDateFormatMessage() throws Exception {
		Date date1 = DateFormatUtil.parseUsingDefaultFormat("2017-10-25 00:05:00");
		Article article = stubNewArticleWithMultipleKeywrodsAndAuthors();
		article.setPublishDate(date1);
		article = articleRep.save(article);

		Date date2 = DateFormatUtil.parseUsingDefaultFormat("2018-01-04 10:25:00");
		Article article2 = stubNewArticleWithOnlyOneKeywrodAndAuthor();
		article2.setPublishDate(date2);
		article2 = articleRep.save(article2);

		Date date3 = DateFormatUtil.parseUsingDefaultFormat("2018-01-25 15:57:32");
		Article article3 = stubNewArticleWithOnlyOneKeywrodAndAuthor();
		article3.setPublishDate(date3);
		article3 = articleRep.save(article3);

		String start = "2016-12-31 23:59:59";
		String end = "INVALID END";
		Response response = RestAssured.given().queryParam("start", start).queryParam("end", end)
				.get(BASE_URL + "/search/by/date");

		assertBadRequestResponseWithMessage(response, INVALID_DATE_FORMAT_MSG);
	}
	
	

	private RequestSpecification prepareRequestBody(Article article) {
		String postBody = toJson(article);
		RequestSpecification requestSpecification = RestAssured.given();
		requestSpecification.body(postBody);
		requestSpecification.contentType(ContentType.JSON);

		return requestSpecification;
	}

}

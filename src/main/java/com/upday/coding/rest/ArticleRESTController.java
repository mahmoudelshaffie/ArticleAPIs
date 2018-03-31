package com.upday.coding.rest;

import static com.upday.coding.validation.ValidationPatterns.VALID_AUTHOR_NAME_PATTERN;
import static com.upday.coding.validation.ValidationPatterns.VALID_KEYWORD_PATTERN;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.upday.coding.jpa.entities.Article;
import com.upday.coding.jpa.repositories.ArticleRep;
import com.upday.coding.util.DateFormatUtil;

import static com.upday.coding.validation.ValidationMessages.*;

@RestController
@RequestMapping(value = "/articles")
@Validated
@Cacheable
public class ArticleRESTController {

	@Autowired
	private ArticleRep rep;

	@GetMapping
	public Iterable<Article> getAllArticles() {
		return rep.findAll();
	}

	@PostMapping
	public @ResponseBody Article createArticle(@Valid @RequestBody Article article) {
		return rep.save(article);
	}

	@PutMapping(value = "/{id}")
	public @ResponseBody Article updateArticle(@PathVariable("id") Integer id, @RequestBody Article article) {
		return rep.save(article);
	}

	@DeleteMapping(value = "/{id}")
	public @ResponseBody boolean deleteArticle(@PathVariable("id") Long id) {
		try {
			rep.deleteById(id);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@GetMapping(value = "/{id}")
	@Cacheable(value = "articles", key = "#id", condition = "#result != null", sync = true)
	public @ResponseBody Article getArticle(@PathVariable("id") Long id) {
		Optional<Article> result = this.rep.findById(id);
		if (result.isPresent()) {
			return result.get();
		}
		return null;
	}

	@GetMapping(value = "/search/by/author")
	@Cacheable(value = "articles", key = "#authorName", condition = "#result != null", sync = true)
	public @ResponseBody List<Article> searchByAuthor(
			@Pattern(regexp = VALID_AUTHOR_NAME_PATTERN, message=INVALID_AUTHOR_NAME_MSG) @RequestParam("authorName") String authorName) {
		return this.rep.findByAuthor(authorName);
	}

	@GetMapping(value = "/search/by/keyword")
	@Cacheable(value = "articles", key = "#keyword", condition = "#result != null", sync = true)
	public @ResponseBody List<Article> searchBySpecificKeyword(
			@Pattern(regexp = VALID_KEYWORD_PATTERN, message=INVALID_KEYWORD_MSG) @RequestParam("keyword") String keyword) {
		keyword = "#" + keyword + "#";
		return this.rep.findBySpecificKeyword(keyword);
	}

	@GetMapping(value = "/search/by/date")
	@Cacheable(value = "articles", key = "#authorName", condition = "#result != null", sync = true)
	public @ResponseBody List<Article> searchWithinPeriod(
			@RequestParam("start") String start,
			@RequestParam("end") String end) {
		
		Date startDate = DateFormatUtil.parseUsingDefaultFormat(start);
		Date endDate = DateFormatUtil.parseUsingDefaultFormat(end);
		

		List<Article> results = this.rep.findWithinPeriod(startDate, endDate);
		return results;
	}
}

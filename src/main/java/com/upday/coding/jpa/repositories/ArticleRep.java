package com.upday.coding.jpa.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.upday.coding.jpa.entities.Article;

@Repository
public interface ArticleRep extends PagingAndSortingRepository<Article, Long> {

	@Query("SELECT A FROM Article A WHERE A.authors LIKE %?1%")
	List<Article> findByAuthor(String authorName);
	
	@Query("SELECT A FROM Article A WHERE A.publishDate BETWEEN ?1 AND ?2 ")
	List<Article> findWithinPeriod(Date startDate, Date endDate);
	
	@Query("SELECT A FROM Article A WHERE A.keywords LIKE %?1%")
	List<Article> findBySpecificKeyword(String keyword);
}

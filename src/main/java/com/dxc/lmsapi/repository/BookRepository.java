package com.dxc.lmsapi.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dxc.lmsapi.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

	Book findByTitle(String title);
	List<Book> findAllByPublishedDate(LocalDate publishedDate);

	@Query("SELECT b FROM Book b WHERE b.price between :lowerBound and :upperBound")
	List<Book> getAllInPriceRange(double lowerBound, double upperBound);

	
}

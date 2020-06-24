package com.dxc.lmsApi;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.dxc.lmsapi.entity.Book;
import com.dxc.lmsapi.repository.BookRepository;

@DataJpaTest // configuring H2, an in-memory database,setting Hibernate, Spring Data, and the
				// DataSource,performing an @EntityScan turning on SQL logging
public class BookRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private BookRepository bookRepository;

	private Book[] testData;

	@BeforeEach
	public void fillTestData() {
		testData = new Book[] { new Book(101, "Arthashastra", "Kautilya", 2090, LocalDate.now()),
				new Book(102, "Broken_Wings", "Sarojini_Naydu", 2000, LocalDate.now()),
				new Book(103, "Ram_Charit_Manas", "TulsiDas", 5000, LocalDate.now()),
				new Book(104, "Life_Of_Pie", "Yann_Martel", 4000, LocalDate.now()),
				new Book(105, "Half_Girlfriend", "Chetan_bhagat", 3009, LocalDate.now()) };

// inserting test data into H2 database
		for (Book book : testData) {
			entityManager.persist(book);
		}
		entityManager.flush();

	}

	@AfterEach
	public void clearDatabase() {
// removing test data into H2 database
		for (Book book : testData) {
			entityManager.remove(book);
		}
		entityManager.flush();
	}

	@Test
	public void findByTitleTest() {
		for (Book book : testData) {
			Assertions.assertEquals(book, bookRepository.findByTitle(book.getTitle()));
		}
	}

	@Test
	public void findByTitleTestWitnNonExistingTitle() {
		Assertions.assertNull(bookRepository.findByTitle("@#1234"));
	}

	@Test
	public void findAllByPublishedDateTest() {
		Book[] actualData = bookRepository.findAllByPublishedDate(LocalDate.now()).toArray(new Book[] {});
		for (int i = 0; i < actualData.length; i++) {
			Assertions.assertEquals(testData[i], actualData[i]);
		}
	}

	@Test
	public void findAllByPublishedDateTestWithNonExisitngDate() {
		List<Book> actualData = bookRepository.findAllByPublishedDate(LocalDate.now().plusDays(2));
		Assertions.assertEquals(0, actualData.size());
	}

	@Test
	public void getAllInPriceRangeTest() {
		Book[] actualData = bookRepository.getAllInPriceRange(2000, 5000).toArray(new Book[] {});
		Book[] expectedData = new Book[] { testData[1], testData[2] };

		for (int i = 0; i < actualData.length; i++) {
			Assertions.assertEquals(expectedData[i], actualData[i]);
		}

	}
}

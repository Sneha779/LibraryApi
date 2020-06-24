package com.dxc.lmsApi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.dxc.lmsapi.entity.Book;
import com.dxc.lmsapi.exception.LibraryException;
import com.dxc.lmsapi.repository.BookRepository;
import com.dxc.lmsapi.service.BookService;

@SpringJUnitConfig
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class BookServiceImplIntegrationTest {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private BookService bookService;

	private Book[] testData;

	@BeforeEach
	public void fillTestData() {
		testData = new Book[] { new Book(101, "Arthashastra", "Kautilya", 2000, LocalDate.now()),
				new Book(102, "Broken_Wings", "Sarojini_Naydu", 2000, LocalDate.now()),
				new Book(103, "Ram_Charit_Manas", "TulsiDas", 5000, LocalDate.now()),
				new Book(104, "Life_Of_Pie", "Yann_Martel", 4000, LocalDate.now()),
				new Book(105, "Half_Girlfriend","Chetan_bhagat", 3009, LocalDate.now()) };
		
		for (Book book : testData) {
			bookRepository.saveAndFlush(book);
		}
	}

	@AfterEach
	public void clearDatabase() {
		bookRepository.deleteAll();
		testData = null;
	}

	@Test
	public void addTest() {

		try {
			Book expected = new Book(106, "Gitanjali","Rabindranath", 500, LocalDate.now().minusYears(1));
			Book actual = bookService.add(testData[0]);
			Assertions.assertEquals(testData[0], actual);
		} catch (LibraryException e) {
			Assertions.fail(e.getMessage());
		}
	}

	@Test
	public void addExistingBookTest() {

		Assertions.assertThrows(LibraryException.class, () -> {
			bookService.add(testData[0]);
		});

	}

	@Test
	public void updateExistingBookTest() {

		try {
			Book actual = bookService.update(testData[0]);
			Assertions.assertEquals(testData[0], actual);
		} catch (LibraryException e) {
			Assertions.fail(e.getMessage());
		}
	}

	@Test
	public void updateNonExistingBookTest() {
		Book nonExistingBook = new Book(106, "Gitanjali","Rabindranath", 500, LocalDate.now().minusYears(1));
		Assertions.assertThrows(LibraryException.class, () -> {
			bookService.update(testData[0]);
		});

	}

	@Test
	public void deleteByIdExistingRecordTest() {

		try {
			Assertions.assertTrue(bookService.deleteBook(testData[0].getBcode()));
		} catch (LibraryException e) {
			Assertions.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void deleteByIdNonExistingRecordTest() {

		Assertions.assertThrows(LibraryException.class, () -> {
			bookService.deleteBook(testData[0].getBcode());
		});

	}

	@Test
	public void getByIdExisitngRecordTest() {
		Assertions.assertEquals(testData[0].getBcode(), 
				bookService.getByBcode(testData[0].getBcode()).getBcode());
	}

	@Test
	public void getByIdNonExisitngRecordTest() {
		Assertions.assertNull(bookService.getByBcode(testData[0].getBcode()));
	}

	@Test
	public void getAllBooksWhenDataExists() {
		List<Book> expected = Arrays.asList(testData);
		Assertions.assertIterableEquals(expected, bookService.getAllBooks());
	}

	@Test
	public void getAllBooksWhenNoDataExists() {
		List<Book> expected = new ArrayList<>();
		bookRepository.deleteAll();
		Assertions.assertEquals(expected, bookService.getAllBooks());
	}
}

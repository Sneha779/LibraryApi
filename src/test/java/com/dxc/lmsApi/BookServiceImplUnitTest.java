package com.dxc.lmsApi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.dxc.lmsapi.entity.Book;
import com.dxc.lmsapi.exception.LibraryException;
import com.dxc.lmsapi.repository.BookRepository;
import com.dxc.lmsapi.service.BookService;
import com.dxc.lmsapi.service.BookServiceImpl;

@SpringJUnitConfig
public class BookServiceImplUnitTest {

	@TestConfiguration
	static class BookServiceImplTestContextConfiguration {

		@Bean
		public BookService bookService() {
			return new BookServiceImpl();
		}
	}

	@MockBean
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
	}

	@AfterEach
	public void clearDatabase() {
		testData = null;
	}

	@Test
	public void addTest() {

		Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(null);

		Mockito.when(bookRepository.existsById(testData[0].getBcode())).thenReturn(false);

		try {
			Book actual = bookService.add(testData[0]);
			Assertions.assertEquals(testData[0], actual);
		} catch (LibraryException e) {
			Assertions.fail(e.getMessage());
		}
	}

	@Test
	public void addExistingBookTest() {
		Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(null);

		Mockito.when(bookRepository.existsById(testData[0].getBcode())).thenReturn(true);

		Assertions.assertThrows(LibraryException.class, () -> {
			bookService.add(testData[0]);
		});

	}

	@Test
	public void updateExistingBookTest() {

		Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(null);

		Mockito.when(bookRepository.existsById(testData[0].getBcode())).thenReturn(true);

		try {
			Book actual = bookService.update(testData[0]);
			Assertions.assertEquals(testData[0], actual);
		} catch (LibraryException e) {
			Assertions.fail(e.getMessage());
		}
	}

	@Test
	public void updateNonExistingBookTest() {
		Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(null);

		Mockito.when(bookRepository.existsById(testData[0].getBcode())).thenReturn(false);

		Assertions.assertThrows(LibraryException.class, () -> {
			bookService.update(testData[0]);
		});

	}

	@Test
	public void deleteByIdExistingRecordTest() {
		Mockito.when(bookRepository.existsById(Mockito.isA(Integer.class))).thenReturn(true);

		Mockito.doNothing().when(bookRepository).deleteById(Mockito.isA(Integer.class));

		try {
			Assertions.assertTrue(bookService.deleteBook(testData[0].getBcode()));
		} catch (LibraryException e) {
			Assertions.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void deleteByIdNonExistingRecordTest() {
		Mockito.when(bookRepository.existsById(Mockito.isA(Integer.class))).thenReturn(false);

		Mockito.doNothing().when(bookRepository).deleteById(Mockito.isA(Integer.class));

		Assertions.assertThrows(LibraryException.class, () -> {
			bookService.deleteBook(testData[0].getBcode());
		});

	}

	@Test
	public void getByIdExisitngRecordTest() {
		Mockito.when(bookRepository.findById(testData[0].getBcode())).thenReturn(Optional.of(testData[0]));
		Assertions.assertEquals(testData[0], bookService.getByBcode(testData[0].getBcode()));
	}

	@Test
	public void getByIdNonExisitngRecordTest() {
		Mockito.when(bookRepository.findById(testData[0].getBcode())).thenReturn(Optional.empty());
		Assertions.assertNull(bookService.getByBcode(testData[0].getBcode()));
	}

	@Test
	public void getAllBooksWhenDataExists() {
		List<Book> expected = Arrays.asList(testData);

		Mockito.when(bookRepository.findAll()).thenReturn(expected);

		Assertions.assertEquals(expected, bookService.getAllBooks());
	}

	@Test
	public void getAllBooksWhenNoDataExists() {
		List<Book> expected = new ArrayList<>();

		Mockito.when(bookRepository.findAll()).thenReturn(expected);

		Assertions.assertEquals(expected, bookService.getAllBooks());
	}
}

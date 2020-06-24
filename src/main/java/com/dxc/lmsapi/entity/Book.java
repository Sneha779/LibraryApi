package com.dxc.lmsapi.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name="books")
public class Book implements Serializable, Comparable<Book> {

	    @Id
	    @Column(name="bcode")
	    @NotNull(message = "bcode can not be null")
	    private Integer bcode;
	    
	    @Column(name="title", nullable=false)
	    @NotBlank(message = "title can not be blank")
	    @Size(min = 5,max=45,message = "title must be of 5 to 45 chars in length")
	    private String title;
	    
	    @Column(name="author", nullable=false)
	    @NotBlank(message = "aurhor can not be blank")
	    @Size(min = 5,max=45,message = "author must be of 5 to 45 chars in length")
	    private String author;
	    
	    @Column(name="price", nullable=false)
	    @NotNull(message = "price can not be blank")
	    @Min(value = 100,message = "price is expected to be not less than 100")
		@Max(value=25000,message="price is expected not more than 25000")
	    private double price;
	   
	    @Column(name="pdate",nullable=true)
		@NotNull(message = "date can not be blank")
		@PastOrPresent(message="published Date can not be a future date")
		@DateTimeFormat(iso = ISO.DATE)
	    private LocalDate publishedDate;
	    
	    public Book() {
	    	//Left unimplemented
	    }

		public Book(int bcode, String title, String author, double price, LocalDate publishedDate) {
			super();
			this.bcode = bcode;
			this.title = title;
			this.author = author;
			this.price = price;
			this.publishedDate=publishedDate;
		}
		
		public LocalDate getPublishedDate() {
			return publishedDate;
		}

		public void setPublishedDate(LocalDate publishedDate) {
			this.publishedDate = publishedDate;
		}

		public int getBcode() {
			return bcode;
		}

		public void setBcode(int bcode) {
			this.bcode = bcode;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getAuthor() {
			return author;
		}

		public void setAuthor(String author) {
			this.author = author;
		}

		public double getPrice() {
			return price;
		}

		public void setPrice(double price) {
			this.price = price;
		}

		@Override
		public int compareTo(Book arg0) {
			return bcode.compareTo(arg0.bcode);
			
		}
		@Override
		public boolean equals(Object obj) {
			return (obj instanceof Book) ? bcode.equals(((Book) obj).bcode) : false;
		}


		@Override
		public String toString() {
			return "Book [bcode=" + bcode + ", title=" + title + ", author=" + author + ", price=" + price
					+ ", publishedDate=" + publishedDate + "]";
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(bcode);
		}
	}

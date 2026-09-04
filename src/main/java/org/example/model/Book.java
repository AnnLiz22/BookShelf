package org.example.model;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Book {
  private static int nextId = 1;
  private int id;
  private String title;
  private Author author;
  private Genre genre;
  private ReadingStatus readingStatus;
  private int year;
  private String isbn;


  public Book() {
    this.readingStatus = ReadingStatus.WANT_TO_READ;
  }

  public Book(int id, String title, Author author, Genre genre, int year, String isbn) {
    this.id = id;
    this.title = title;
    this.author = author;
    this.genre = genre;
    this.readingStatus = ReadingStatus.WANT_TO_READ;
    this.year = year;
    this.isbn = isbn;
  }

  public Book(String title, Author author, Genre genre, int year, String isbn) {

    if (title == null || title.isBlank()) {
      throw new IllegalArgumentException("Title cannot be null or empty");
    }
    if(year > LocalDate.now().getYear()){
      throw new IllegalArgumentException("Wrong year");
    }
    if(!List.of(Genre.values()).contains(genre)){
      throw new IllegalArgumentException("Wrong genre");
    }
    this.id = nextId++;
    this.title = title;
    this.author = author;
    this.genre = genre;
    this.readingStatus = ReadingStatus.WANT_TO_READ;
    this.year = year;
    this.isbn = isbn;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Author getAuthor() {
    return author;
  }

  public void setAuthor(Author author) {
    this.author = author;
  }

  public Genre getGenre() {
    return genre;
  }

  public void setGenre(Genre genre) {
    this.genre = genre;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public ReadingStatus getReadingStatus() {
    return readingStatus;
  }

  public void setReadingStatus(ReadingStatus readingStatus) {
    this.readingStatus = readingStatus;
  }

  @Override
  public String toString() {
    return
        '\'' +   title + '\'' +
        ", author: " + author +
        ", genre: " + genre +
        ", readingStatus: " + readingStatus +
        ", year: " + year + " isbn: " + isbn + ".\n";
  }
}
package org.example.model;

public class Book {

  private int id;
  private String title;
  private Author author;
  private Genre genre;
  private ReadingStatus readingStatus;
  private int year;
  private String isbn;
  private boolean available;

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

  public Book(int id, String title, Author author, Genre genre, int year, String isbn, boolean available) {
    this.id = id;
    this.title = title;
    this.author = author;
    this.genre = genre;
    this.readingStatus = ReadingStatus.WANT_TO_READ;
    this.year = year;
    this.isbn = isbn;
    this.available = available;
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

  public boolean isAvailable() {
    return available;
  }

  public void setAvailable(boolean available) {
    this.available = available;
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
        ", year: " + year + ".\n";
  }
}
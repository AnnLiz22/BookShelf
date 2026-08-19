package org.example.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Genre;
import org.example.model.ReadingStatus;

public class LibraryService {
  // responsible for
  // adding books
  //removing books
  //searching
  //sorting
  //grouping
  //etc.
  private final List<Book> books = new ArrayList<>();

  public void addBook(Book book) {
    books.add(book);
  }

  public void removeBook(int id) {
    books.removeIf(book -> book.getId() == id);
  }

  public Book findById(int id) {
    Book book = new Book();
    for (Book book1 : books) {
      if (book1.getId() == id) book = book1;
    }
    return book;
  }

  public List<Book> getAllBooks() {

    List<Book> allBooks = new ArrayList<>();
    Iterator<Book> iterator = books.iterator();

    for(Book book : books){
      if(iterator.hasNext()){
        iterator.next();
        allBooks.add(book);
      }
    }
    return allBooks;
  }

  public Map<Author, List<Book>> getBooksByAuthor() {

    Map<Author, List<Book>> booksByAuthor = new HashMap<>();

    for(Book book : books){
    Author author = book.getAuthor();

    booksByAuthor.computeIfAbsent(author, k -> new ArrayList<>() )
        .add(book);
  }
    return booksByAuthor;
  }

  public Map<Genre, List<Book>> getBooksByGenre(){
    Map<Genre, List<Book>> booksByGenre = new HashMap<>();
    for(Book book : books){
      Genre genre = book.getGenre();
      booksByGenre.computeIfAbsent(genre, k -> new ArrayList<>())
          .add(book);
    }
    return booksByGenre;
  }

  public Map<ReadingStatus, List<Book>> getBooksByReadingStatus(){
    Map<ReadingStatus, List<Book>> booksByReadingStatus = new HashMap<>();

    for(Book book : books){
      ReadingStatus readingStatus = book.getReadingStatus();

      booksByReadingStatus.computeIfAbsent(readingStatus, k-> new ArrayList<>())
          .add(book);
    }
    return booksByReadingStatus;
  }

  public Map<Integer, List<Book>> getBooksSortedByYear(){

    return books
         .stream()
         .collect(Collectors.groupingBy(Book::getYear, TreeMap::new, Collectors.toList()));
  }

  public Map<Genre, Long> getNumberOfBooksForEachGenre(){
    return books.stream()
        .collect(Collectors.groupingBy(Book::getGenre, Collectors.counting()));
  }

  public Map<Genre, Long> getGenreWithBiggestNumberOfBooks() {

    Map<Genre, Long> genres = getNumberOfBooksForEachGenre();

    Genre mostPopularGenre = null;
    Long max = Long.MIN_VALUE;

    for (Map.Entry<Genre, Long> entry : genres.entrySet()) {

      if (entry.getValue() > max) {
        max = entry.getValue();
        mostPopularGenre = entry.getKey();
      }
    }
    assert mostPopularGenre != null;
    return Map.of(mostPopularGenre, max);
  }
}
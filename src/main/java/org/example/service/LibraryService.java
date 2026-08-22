package org.example.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Genre;
import org.example.model.ReadingStatus;

public class LibraryService {
  
  private final List<Book> books = new ArrayList<>();
  private final List<Author> authors = new ArrayList<>();

  public void addBook(Book book) {

    books.add(book);

    boolean isNotInCatalogue = authors.stream()
        .noneMatch(a -> a.getName().equalsIgnoreCase(book.getAuthor().getName()));
    if(isNotInCatalogue){
      authors.add(book.getAuthor());
    }
    if(!isNotInCatalogue){
     Optional <Book> book1 = books.stream()
         .filter(b->b.getAuthor().getName().equalsIgnoreCase(book.getAuthor().getName()))
         .findFirst();
     book.setAuthor(book1.get().getAuthor());
    }
  }

  public void addAuthor(Author author){

    boolean exists = authors.stream()
        .anyMatch(a -> a.getName().equalsIgnoreCase(author.getName()));

    if (exists) {
      throw new IllegalArgumentException("Author already exists: " + author.getName());
    }
    authors.add(author);
  }

  public boolean removeBook(int id) {
    return books.removeIf(book -> book.getId() == id);
  }

  public boolean removeBookByTitle(String title) {
    return books.removeIf(book -> book.getTitle().equalsIgnoreCase(title));
  }
  public boolean removeBookByTitleAndAuthor(String title, String authorName) {
    Book book = findBookByTitleAndAuthorName(title, authorName);
    return books.remove(book);
  }

  public Book findBookById(int id) {
    Book book = new Book();
    for (Book book1 : books) {
      if (book1.getId() == id) book = book1;
    }
    return book;
  }

  public Book findBookByTitle(String title) {

    Long booksWithGivenTitle = books
        .stream()
        .filter(book1 -> book1.getTitle()
            .equalsIgnoreCase(title)).count();

    if(booksWithGivenTitle>1) {
      throw new IllegalArgumentException("Duplicated");
    }
    if(booksWithGivenTitle<1){
      throw new NullPointerException("Title not found");
    }
    Optional<Book> book = books.stream()
        .filter(b -> b.getTitle().equalsIgnoreCase(title)).findFirst();
      return book.get();
    }

public Book findBookByTitleAndAuthorName(String title, String authorName){
    Optional<Book>book = books.stream()
        .filter(b -> b.getTitle().equalsIgnoreCase(title)
            &&b.getAuthor().getName().equalsIgnoreCase(authorName))
        .findFirst();
  return book.orElseThrow(()->new NullPointerException("Book not found"));
}

  public Book findBookByIsbn(String isbn){
    Optional<Book> book = books.stream()
        .filter(b -> b.getIsbn().equalsIgnoreCase(isbn)).findFirst();
    return book.get();
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

  public List<Author> getAllAuthors() {

    return authors;
  }

  public Author findAuthorByBookTitle(String title){
    Author author = new Author();
    for(Book book : books){
      if(book.getTitle().equalsIgnoreCase(title)){
        author = book.getAuthor();
      }
    }
    return author;
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

  public Map<Author, List<Book>> getBooksForGivenAuthor (String author) {

    return books
        .stream()
        .filter(book -> book.getAuthor().toString().equals(author))
        .collect(Collectors.groupingBy(Book::getAuthor));

  }

  public void setBookStatus(String title, ReadingStatus readingStatus){
    for(Book book : books){
      if(book.getTitle().equalsIgnoreCase(title)){
        book.setReadingStatus(readingStatus);
      }
    }
  }
}
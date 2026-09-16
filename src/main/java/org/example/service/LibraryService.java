package org.example.service;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Genre;
import org.example.model.ReadingStatus;

public class LibraryService {

  private final List<Book> books = new ArrayList<>();
  private final List<Author> authors = new ArrayList<>();

  public void addBook(Book book) {
    if (book == null || book.getTitle().isBlank() || book.getTitle() == null) {
      throw new NullPointerException();
    }
    Author author = authors.stream()
        .filter(a -> a.getName().equalsIgnoreCase(book.getAuthor().getName()))
        .findFirst().orElse(book.getAuthor());
    book.setAuthor(author);
    books.add(book);

    boolean isNotInCatalogue = authors.stream()
        .noneMatch(a -> a.getName().equalsIgnoreCase(book.getAuthor().getName()));
    if (isNotInCatalogue) {
      authors.add(book.getAuthor());
    }
  }

  public void addAuthor(Author author) {
    if(author==null || author.getName()==null){
      throw new IllegalArgumentException();
    }
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

  public void removeBookByTitle(String title) {
    if(title==null || title.isBlank()){
      throw new NullPointerException("Title is null");
    }
    books.removeIf(book -> book.getTitle().equalsIgnoreCase(title));
  }

  public boolean removeBookByTitleAndAuthor(String title, String authorName) {
    if(authorName==null || authorName.isBlank()){
      throw new NullPointerException("Author name is null");
    }
    Book book = findBookByTitleAndAuthorName(title, authorName);
   return books.remove(book);
  }

  public Book findBookById(int id) {
    Optional <Book> book = books.stream().filter(b->b.getId()==id).findFirst();
    return book.orElseThrow(NullPointerException::new);
  }

  public Book findBookByTitle(String title) {

    long booksWithGivenTitle = books
        .stream()
        .filter(book1 -> book1.getTitle()
            .equalsIgnoreCase(title.strip())).count();

    if (booksWithGivenTitle > 1) {
      throw new IllegalArgumentException("The selected title appears more then once.");
    }

    Optional<Book> book = books.stream()
        .filter(b -> b.getTitle().equalsIgnoreCase(title.strip())).findFirst();
    return book.orElseThrow(NullPointerException::new);
  }

  public Book findBookByTitleAndAuthorName(String title, String authorName) {
    Optional<Book> book = books.stream()
        .filter(b -> b.getTitle().equalsIgnoreCase(title.strip())
            && b.getAuthor().getName().equalsIgnoreCase(authorName.strip()))
        .findFirst();
    return book.orElseThrow(NullPointerException::new);
  }

  public Book findBookByIsbn(String isbn) {
    Optional<Book> book = books.stream()
        .filter(b -> b.getIsbn()
            .equalsIgnoreCase(isbn.strip().replace("-", ""))).findFirst();
    return book.orElseThrow(NullPointerException::new);
  }

  public List<Book> getAllBooks() {

    if(books.isEmpty()){
      throw new NullPointerException("No books added.");
    }
   return books.stream().sorted(Comparator.comparing(Book::getTitle)).toList();
  }

  public Map<Author, List<String>> getBooksByAuthor() {
  List<Author> list =  books.stream().map(Book::getAuthor).toList();
  if(list.isEmpty()){
    throw new NullPointerException("Nothing found.");
  }
        return books.stream()
            .collect(Collectors.groupingBy(Book::getAuthor, mapping(Book::getTitle, toList())));

  }

  public Map<Genre, List<String>> getBooksByGenre() {
    List<Genre>list = books.stream().map(Book::getGenre).toList();
    if(list.isEmpty()){
      throw new NullPointerException("Nothing found.");
    }
    return books
        .stream()
        .collect(Collectors.groupingBy(Book::getGenre, mapping(Book::getTitle, toList())));
  }

  public Map<ReadingStatus, List<String>> getBooksByReadingStatus() {
    return books.stream()
            .collect(Collectors.groupingBy(Book::getReadingStatus, mapping(Book::getTitle, toList())));

  }

  public List<Book> getBooksSortedByYear() {
    List<Book> sortedByYear = new ArrayList<>(books);
    sortedByYear.sort(Comparator.comparingInt(Book::getYear));
     return sortedByYear;

  }

  public List<Author> getAllAuthors() {
    return authors;
  }

  public Author findAuthor(String authorName) {
    return books.stream()
        .map(Book::getAuthor)
        .filter(author -> author.getName().equalsIgnoreCase(authorName.strip()))
        .findFirst()
        .orElseThrow(() ->
            new IllegalArgumentException("Author not found."));
  }

  public Author findPossibleMatchForAuthor(String authorName) {
    return books.stream()
        .map(Book::getAuthor)
        .filter(author -> author.getName()
            .toLowerCase()
            .contains(authorName.toLowerCase().strip()))
        .findFirst()
        .orElseThrow(() ->
            new IllegalArgumentException("Author not found."));
  }

  public Author findAuthorOfBookByTitle(String title) {
   Optional <Author> author = books.stream()
       .filter(b->b.getTitle().equalsIgnoreCase(title.strip()))
       .map(Book::getAuthor).findFirst();

    return author.orElseThrow(NullPointerException::new);
  }

  public Map<Genre, Long> getNumberOfBooksForEachGenre() {
    return books.stream()
        .collect(groupingBy(Book::getGenre, Collectors.counting()));
  }

  public Map<Genre, Long> getGenreWithBiggestNumberOfBooks() {
    Map<Genre, Long> genres = getNumberOfBooksForEachGenre();
    Optional<Map.Entry<Genre, Long>> mostPopularGenre =
        genres.entrySet().stream().max(Map.Entry.comparingByValue());

   return mostPopularGenre
       .map(entry ->
           Map.of(entry.getKey(), entry.getValue()))
       .orElseThrow(NullPointerException::new);
  }

  public Map<Author, List<Book>> getBooksForGivenAuthor(String authorName) {
    Author author = findAuthor(authorName);
     return books
          .stream()
          .filter(book -> book.getAuthor().equals(author))
          .collect(groupingBy(Book::getAuthor));
    }

  public void setBookStatus(String title, ReadingStatus readingStatus) {
    Book book = findBookByTitle(title);
    book.setReadingStatus(readingStatus);
  }

  public void setBookStatus(String title, String authorName, ReadingStatus readingStatus) {
   Book book = findBookByTitleAndAuthorName(title, authorName);
   book.setReadingStatus(readingStatus);
  }

  public void updateBook(String title, String authorName, int year, String isbn){
    Book book = findBookByTitleAndAuthorName(title, authorName);

    book.setYear(year);
    book.setIsbn(isbn);
  }
}
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

    books.add(book);

    boolean isNotInCatalogue = authors.stream()
        .noneMatch(a -> a.getName().equalsIgnoreCase(book.getAuthor().getName()));
    if (isNotInCatalogue) {
      authors.add(book.getAuthor());
    }
    if (!isNotInCatalogue) {
      Optional<Book> book1 = books.stream()
          .filter(b -> b.getAuthor().getName().equalsIgnoreCase(book.getAuthor().getName()))
          .findFirst();
      book.setAuthor(book1.orElseThrow().getAuthor());
    }
  }

  public void addAuthor(Author author) {
    if(author == null || author.getName().isBlank() || author.getName()==null){
      throw new NullPointerException("Author cannot be null");
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

  public void removeBookByTitleAndAuthor(String title, String authorName) {
    if(authorName==null || authorName.isBlank()){
      throw new NullPointerException("Author name is null");
    }
    Book book = findBookByTitleAndAuthorName(title, authorName);
    books.remove(book);
  }

  public Book findBookById(int id) {
    Optional <Book> book = books.stream().filter(b->b.getId()==id).findFirst();
    return book.orElseThrow(NullPointerException::new);
  }

  public Book findBookByTitle(String title) {

    Long booksWithGivenTitle = books
        .stream()
        .filter(book1 -> book1.getTitle()
            .equalsIgnoreCase(title)).count();

    if (booksWithGivenTitle > 1) {
      throw new IllegalArgumentException("Duplicated");
    }

    Optional<Book> book = books.stream()
        .filter(b -> b.getTitle().equalsIgnoreCase(title)).findFirst();
    return book.orElseThrow(NullPointerException::new);
  }

  public Book findBookByTitleAndAuthorName(String title, String authorName) {
    Optional<Book> book = books.stream()
        .filter(b -> b.getTitle().equalsIgnoreCase(title)
            && b.getAuthor().getName().equalsIgnoreCase(authorName))
        .findFirst();
    return book.orElseThrow(NullPointerException::new);
  }

  public Book findBookByIsbn(String isbn) {
    Optional<Book> book = books.stream()
        .filter(b -> b.getIsbn().equalsIgnoreCase(isbn)).findFirst();
    return book.orElseThrow(NullPointerException::new);
  }

  public List<Book> getAllBooks() {

    if(books.isEmpty()){
      throw new NullPointerException();
    }
   return books.stream().sorted(Comparator.comparing(Book::getTitle)).toList();
  }

  public Map<Author, List<String>> getBooksByAuthor() {
      Optional <Author> author = authors.stream().findFirst();
      if(author.isPresent()) {
        return books.stream()
            .collect(Collectors.groupingBy(Book::getAuthor, mapping(Book::getTitle, toList())));
      }
      throw new NullPointerException();
  }

  public Map<Genre, List<String>> getBooksByGenre() {
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

  public Author findAuthorOfBookByTitle(String title) {
   Optional <Author> author = books.stream()
       .filter(b->b.getTitle().equals(title))
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

    Optional<Book> author = books.stream().filter(b->b.getAuthor()
        .getName().equals(authorName)).findFirst();

    if(author.isPresent()) {
      return books
          .stream()
          .filter(book -> book.getAuthor().getName().equals(authorName))
          .collect(groupingBy(Book::getAuthor));
    }
throw new NullPointerException();
  }

  public void setBookStatus(String title, ReadingStatus readingStatus) {

    Long booksWithGivenTitle = books
        .stream()
        .filter(b -> b.getTitle()
            .equalsIgnoreCase(title)).count();

    if (booksWithGivenTitle > 1) {
      throw new IllegalArgumentException("Duplicated");
    }

    Optional<Book>book = books.stream()
        .filter(b -> b.getTitle().equalsIgnoreCase(title))
        .findFirst();
       book.ifPresent(value -> value.setReadingStatus(readingStatus));
  }
}
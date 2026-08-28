package org.example.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Genre;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;


public class LibraryServiceTest {
  private LibraryService libraryService;
  private List<Book> books;
  private List<Author> authors;

  @BeforeEach
  void setUp() {
    libraryService = new LibraryService();
    books = createBooks();
    authors = createBooks().stream().map(Book::getAuthor).toList();
    for(Book book : books){
      libraryService.addBook(book);
    }
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void shouldAddNewBookWithCorrectFieldsAndExistingAuthor() {
    Optional<Author> author = authors.stream().filter(b->b.getName().equalsIgnoreCase("Stephen King")).findFirst();
    Author author1 = null;
    if(author.isPresent()){
       author1 = author.get();
    }
    Book book = new Book("The Shining", author1, Genre.FICTION, 1977, "123454");
    libraryService.addBook(book);
    assertTrue(libraryService.getAllBooks().contains(book));
    assertTrue(libraryService.getAllBooks().stream().anyMatch(b->b.getIsbn().equalsIgnoreCase("123454")));

    Book addedBook = libraryService.getAllBooks().stream().filter(b->b.getIsbn().equalsIgnoreCase("123454"))
        .findFirst().orElseThrow();
    assertEquals("The Shining", addedBook.getTitle());
    assertEquals(Genre.FICTION, addedBook.getGenre());
    assertEquals("Stephen King", addedBook.getAuthor().getName());
  }

  @Test
  void shouldAddAuthorOfNewBookToAuthorsIfNotInCatalogue(){
    Author author = new Author("Marcel Proust");
    Book book = new Book("W poszukiwaniu straconego czasu", author, Genre.FICTION, 1913, "123454");
    libraryService.addBook(book);
    assertTrue(libraryService.getAllAuthors().contains(author));

  }
  @Test
  void shouldNotCreateNewAuthorIfAuthorExists(){
    Author author = new Author("Stephen King");
    Book book = new Book("The Shining", author, Genre.FICTION, 1977, "123454");
    libraryService.addBook(book);
    assertEquals(books.stream().map(Book::getAuthor).collect(Collectors.toSet()).size(),
        libraryService.getAllAuthors().size());
  }

  @Test
  void shouldAddAuthor() {
    Author author = new Author("Jean Paul Sartre");
    libraryService.addAuthor(author);
    assertTrue(libraryService.getAllAuthors().contains(author));
  }

  @Test
  void shouldThrowExceptionIfAuthorAlreadyExists(){
    Author author = new Author("Stephen King");
    assertThrows(IllegalArgumentException.class, ()-> libraryService.addAuthor(author));
  }

  @Test
  void shouldRemoveBookByTitle() {
    libraryService.removeBookByTitle("Clean Code");
    assertTrue(libraryService.getAllBooks()
        .stream()
        .noneMatch(book1 -> book1.getTitle()
            .equalsIgnoreCase("Clean Code")));

  }


  @Test
  void removeBookByTitleAndAuthor() {
    libraryService.removeBookByTitleAndAuthor("Misery", "Stephen King");

    assertTrue(libraryService.getAllBooks().stream().noneMatch(b -> b.getTitle().equalsIgnoreCase("Misery")
            && b.getAuthor().getName().equalsIgnoreCase("Stephen King")
        ));
  }

  @Test
  void findBookById() {
    Book book = new Book();
    book.setTitle("new book");
    book.setAuthor(new Author("Stephen"));
    libraryService.addBook(book);
    Book addedBook = libraryService.getAllBooks().stream()
        .filter(book1 -> book1
            .getTitle().equalsIgnoreCase("new book")).findFirst().orElseThrow();
    assertEquals(book.getTitle(), libraryService.findBookById(addedBook.getId()).getTitle());
  }

  @Test
  void findBookByTitle() {
  }

  @org.junit.jupiter.api.Test
  void findBookByTitleAndAuthorName() {
  }

  @org.junit.jupiter.api.Test
  void findBookByIsbn() {
  }

  @org.junit.jupiter.api.Test
  void getAllBooks() {
  }

  @org.junit.jupiter.api.Test
  void getBooksByAuthor() {
  }

  @org.junit.jupiter.api.Test
  void getBooksByGenre() {
  }

  @org.junit.jupiter.api.Test
  void getBooksByReadingStatus() {
  }

  @org.junit.jupiter.api.Test
  void getBooksSortedByYear() {
  }

  @org.junit.jupiter.api.Test
  void getAllAuthors() {
  }

  @Test
  void findAuthorOfBookByTitle() {
  }

  @Test
   void shouldCountBooksByGenre() {
  }

  @Test
  void getGenreWithBiggestNumberOfBooks() {
  }

  @org.junit.jupiter.api.Test
  void getBooksForGivenAuthor() {
  }

  @org.junit.jupiter.api.Test
  void setBookStatus() {
  }

  private static List<Book> createBooks(){

    Author author1 = new Author("Robert C. Martin");
    Author author2 = new Author("Joshua Bloch");
    Author author3 = new Author("J.K. Rowling");
    Author author4 = new Author("J.R.R. Tolkien");
    Author author5 = new Author("George Orwell");
    Author author6 = new Author("Fyodor Dostoevsky");
    Author author7 = new Author("Stephen King");

    Book book1 = new Book("Clean Code", author1, Genre.TECHNOLOGY, 2008 ,"9780132350884");
    Book book2 = new Book("The Clean Coder",
        author1,
        Genre.TECHNOLOGY,
        2011,
        "9780137081073");
    Book book3 = new Book("Effective Java",
        author2,
        Genre.TECHNOLOGY,
        2018,
        "9780134685991");
    Book book4 = new Book("Harry Potter and the Philosopher's Stone",
        author3,
        Genre.FANTASY,
        1997,
        "9780747532743"
    );
    Book book5 = new Book( "Harry Potter and the Chamber of Secrets",
        author3,
        Genre.FANTASY,
        1998,
        "9780747538493");

    Book book6 = new Book( "The Hobbit",
        author4,
        Genre.FANTASY,
        1937,
        "9780261102217");

    Book book7 = new Book(
        "1984",
        author5,
        Genre.FICTION,
        1949,
        "9780451524935"
    );

    Book book8 = new Book(
        "Animal Farm",
        author5,
        Genre.FICTION,
        1945,
        "9780451526342"
    );
    Book book9 = new Book(
        "Crime and Punishment",
        author6,
        Genre.FICTION,
        1866,
        "9780143058144"
    );

    Book book10 = new Book(
        "The Brothers Karamazov",
        author6,
        Genre.FICTION,
        1880,
        "9780374528379"
    );
    Book book11 = new Book(
        "Misery",
        author7,
        Genre.FICTION,
        1880,
        "9780374528378"
    );

    Book book12 = new Book(
        "Misery",
        author6,
        Genre.FICTION,
        1880,
        "9780374528300"
    );

    return new ArrayList<>(List.of(book1, book2, book3, book4, book5, book6, book7, book8, book9, book10, book11, book12));
  }
}
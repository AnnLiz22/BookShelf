package org.example.service;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Genre;
import org.example.model.ReadingStatus;
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
    authors = createBooks().stream().map(Book::getAuthor).distinct().toList();
    for (Book book : books) {
      libraryService.addBook(book);
    }
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void shouldAddNewBookWithCorrectFieldsAndExistingAuthor() {
    Optional<Author> author = authors
        .stream()
        .filter(b -> b.getName()
            .equalsIgnoreCase("Stephen King")).findFirst();
    Author author1 = null;
    if (author.isPresent()) {
      author1 = author.get();
    }

    Book book = new Book("The Shining", author1, Genre.FICTION, 1977, "123454");
    libraryService.addBook(book);
    assertTrue(libraryService.getAllBooks().contains(book));
    assertTrue(libraryService.getAllBooks().stream()
        .anyMatch(b -> b.getIsbn().equalsIgnoreCase("123454")));

    Book addedBook = libraryService.getAllBooks().stream()
        .filter(b -> b.getIsbn().equalsIgnoreCase("123454"))
        .findFirst().orElseThrow();
    assertEquals("The Shining", addedBook.getTitle());
    assertEquals(Genre.FICTION, addedBook.getGenre());
    assertEquals("Stephen King", addedBook.getAuthor().getName());
    assertTrue((addedBook.getYear() < (LocalDate.now().getYear())));
    assertTrue(addedBook.getAuthor().getName().length() >= 2);
  }

  @Test
  void shouldAddAuthorOfNewBookToAuthorsIfNotInCatalogue() {
    Author author = new Author("Marcel Proust");
    Book book = new Book("W poszukiwaniu straconego czasu", author, Genre.FICTION, 1913, "123454");
    libraryService.addBook(book);
    assertTrue(libraryService.getAllAuthors().contains(author));

  }

  @Test
  void shouldNotCreateNewAuthorIfAuthorExistsAndAddNewBook() {
    Author author = new Author("Stephen King");
    Book book = new Book("The Shining", author, Genre.FICTION, 1977, "123454");
    libraryService.addBook(book);
    assertTrue(libraryService.getAllBooks().contains(book));
    assertEquals(books.stream().map(Book::getAuthor).collect(Collectors.toSet()).size(),
        libraryService.getAllAuthors().size());
  }

  @Test
  void shouldThrowExceptionIfBookIsNullOrBookTitleIsEmptyOrNull() {
    assertThrows(NullPointerException.class, ()->libraryService.addBook(null));
    assertThrows(IllegalArgumentException.class,
        () -> libraryService.addBook(new Book("", new Author("author"), Genre.FICTION, 2000, "1233211234")
        ));

    assertThrows(IllegalArgumentException.class,
        () -> libraryService.addBook(new Book(null, new Author("author"), Genre.FICTION, 2000, "1233211234")));
    assertThrows(NullPointerException.class, () -> libraryService.addBook(null));
  }

  @Test
  void shouldAddAuthor() {
    Author author = new Author("Jean Paul Sartre");
    libraryService.addAuthor(author);
    assertTrue(libraryService.getAllAuthors().contains(author));
  }

  @Test
  void shouldThrowExceptionIfAuthorAlreadyExists() {
    Author author = new Author("Stephen King");
    assertThrows(IllegalArgumentException.class,
        () -> libraryService.addAuthor(author));
  }

  @Test
  void shouldThrowExceptionIfAuthorIsNull() {
    assertThrows(IllegalArgumentException.class,
        () -> libraryService.addAuthor(null));
  }

  @Test
  void shouldThrowExceptionIfAuthorNameIsEmpty() {
    assertThrows(IllegalArgumentException.class,
        () -> libraryService.addAuthor(new Author("")));
  }

  @Test
  void shouldThrowExceptionIfAuthorNameIsNull() {
    Author author = new Author();
    assertThrows(IllegalArgumentException.class,
        () -> libraryService.addAuthor(author));

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
  void removeByTitle_shouldThrowExceptionIfTitleIsNull() {
    assertThrows(NullPointerException.class,
        () -> libraryService.removeBookByTitle(null));
  }

  @Test
  void removeByTitle_shouldThrowExceptionIfTitleIsEmpty() {
    assertThrows(NullPointerException.class, () -> libraryService.removeBookByTitle(""));
    assertThrows(NullPointerException.class, () -> libraryService.removeBookByTitle(" "));

  }

  @Test
  void shouldRemoveBookByTitleAndAuthor() {
    assertTrue(libraryService.removeBookByTitleAndAuthor("Clean Code",
        "Robert C. Martin"));
  }

  @Test
  void removeByTitleAndAuthor_shouldThrowExceptionIfAuthorIsNull() {
    assertThrows(NullPointerException.class,
        () -> libraryService.removeBookByTitleAndAuthor("Misery", null));
  }

  @Test
  void removeByTitleAndAuthor_shouldThrowExceptionIfAuthorIsEmpty() {
    assertThrows(NullPointerException.class,
        () -> libraryService.removeBookByTitleAndAuthor("Misery", ""));
  }

  @Test
  void findBookById() {
    assertEquals("Clean Code", libraryService.findBookById(1).getTitle());
  }

  @Test
  void shouldThrowExceptionIfBookByIdNotFound() {
    assertThrows(NullPointerException.class, () -> libraryService.findBookById(20));
  }

  @Test
  void shouldFindBookByTitle() {
    assertEquals("Clean Code",
        libraryService.findBookByTitle("Clean Code").getTitle());
    assertEquals("Robert C. Martin",
        libraryService.findBookByTitle("Clean Code").getAuthor().getName());
  }

  @Test
  void shouldFindBookByTitleAndIgnoreCase(){
    assertEquals("Robert C. Martin",
        libraryService.findBookByTitle("clean Code").getAuthor().getName());
    assertEquals("Clean Code",
        libraryService.findBookByTitle("Clean CODE").getTitle());
  }
  @Test
  void shouldFindBookByTitleAndIgnoreEmptySpaces(){
    assertEquals("Robert C. Martin",
        libraryService.findBookByTitle(" Clean Code").getAuthor().getName());
    assertEquals("Clean Code",
        libraryService.findBookByTitle("Clean Code ").getTitle());
    assertEquals("Clean Code",
        libraryService.findBookByTitle("  Clean Code ").getTitle());
  }

  @Test
  void shouldThrowExceptionIfBookByTitleNotFound() {
    assertThrows(NullPointerException.class,
        () -> libraryService.findBookByTitle("Infinite Jest"));
  }

  @Test
  void shouldFindBookByTitleAndThrowExceptionIfTitleIsDuplicated() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> libraryService.findBookByTitle("Misery"));
    assertEquals("The selected title appears more then once.", exception.getMessage());
  }

  @Test
  void shouldFindBookByTitleAndAuthorName() {
    String title = "Misery";
    String authorName = "Stephen King";
    assertEquals(title, libraryService
        .findBookByTitleAndAuthorName("Misery", "Stephen King").getTitle());
    assertEquals(authorName, libraryService
        .findBookByTitleAndAuthorName("Misery", "Stephen King").getAuthor().getName());
  }

  @Test
  void shouldFindBookByTitleAndAuthorNameAndIgnoreCase() {
    assertEquals("Misery", libraryService
        .findBookByTitleAndAuthorName("misery", "Stephen King").getTitle());
    assertEquals("Stephen King", libraryService
        .findBookByTitleAndAuthorName("Misery", "stephen KING").getAuthor().getName());
  }

  @Test
  void shouldFindBookByTitleAndAuthorNameAndIgnoreEmptySpaces() {

    assertEquals("Misery", libraryService
        .findBookByTitleAndAuthorName("Misery  ", "Stephen King").getTitle());
    assertEquals("Stephen King", libraryService
        .findBookByTitleAndAuthorName("Misery", " Stephen King ").getAuthor().getName());
  }

  @Test
  void findBookByTitleAndAuthorName_shouldThrowExceptionIfNotFoundOrNull() {
    assertThrows(NullPointerException.class, () ->
        libraryService.findBookByTitleAndAuthorName("Misery", "No author"));
    assertThrows(NullPointerException.class, () ->
        libraryService.findBookByTitleAndAuthorName("Ulisses", "Stephen King"));
    assertThrows(NullPointerException.class, () ->
        libraryService.findBookByTitleAndAuthorName("", ""));
    assertThrows(NullPointerException.class, () ->
        libraryService.findBookByTitleAndAuthorName(null, null));

  }

  @Test
  void shouldFindBookByIsbn_ignoreCase_ignoreEmptySpaces_clean() {
    assertEquals("Clean Code",
        libraryService.findBookByIsbn("9780132350884").getTitle());
    assertEquals("Clean Code",
        libraryService.findBookByIsbn(" 9780132350884  ").getTitle());
    assertEquals("Clean Code",
        libraryService.findBookByIsbn("978-0132-350-884").getTitle());
    assertEquals("Clean Code",
        libraryService.findBookByIsbn("  978-0132-350-884").getTitle());
  }

  @Test
  void shouldThrowExceptionIfIsbnNotFound() {
    assertThrows(NullPointerException.class,
        () -> libraryService.findBookByIsbn("abcd"));
  }

  @Test
  void shouldPrintAllBooksSorted() {
        assertTrue(books.containsAll(libraryService.getAllBooks()));
        assertEquals("1984", libraryService.getAllBooks().get(0).getTitle());
        assertEquals("Animal Farm", libraryService.getAllBooks().get(1).getTitle());
  }

  @Test
  void shouldThrowExceptionIfBooksIsEmpty() {
    libraryService = new LibraryService();
    assertThrows(NullPointerException.class, () -> libraryService.getAllBooks());
  }

  @Test
  void getBooksByAuthor() {
    Map<Author, List<String>> expected = books.stream()
        .collect(Collectors.groupingBy(Book::getAuthor,
            Collectors.mapping(Book::getTitle, toList())));
    assertEquals(expected, libraryService.getBooksByAuthor());
  }

  @Test
  void shouldThrowExceptionIfAuthorsListIsEmpty() {
    libraryService = new LibraryService();
    assertThrows(NullPointerException.class,
        () -> libraryService.getBooksByAuthor());
  }

  @Test
  void getBooksByGenre() {
    Map<Genre, List<String>> result = libraryService.getBooksByGenre();
    assertEquals(List.of("Clean Code", "The Clean Coder", "Effective Java"),
        result.get(Genre.TECHNOLOGY));
  }

  @Test
  void shouldThrowExceptionIfBooksByGenreIsEmpty() {
    libraryService = new LibraryService();
    assertThrows(NullPointerException.class, () -> libraryService.getBooksByGenre());
  }

  @Test
  void getBooksByReadingStatus() {
    Map<ReadingStatus, List<String>> result = libraryService.getBooksByReadingStatus();
    assertNull(result.get(ReadingStatus.FINISHED));

    assertEquals((books.stream().map(Book::getTitle).toList()),
        result.get(ReadingStatus.WANT_TO_READ));
  }

  @Test
  void getBooksSortedByYear() {
    List<Book> result = libraryService.getBooksSortedByYear();
    List<Book> expected = new ArrayList<>(books);
    expected.sort(Comparator.comparingInt(Book::getYear));
    assertEquals(expected, result);
  }

  @Test
  void getAllAuthors() {
    String result = libraryService.getAllAuthors().toString();
    String expected = authors.toString();
    assertEquals(expected, result);
  }

  @Test
  void shouldFindAuthorWithIncompleteName_ignoreCase_ignoreEmptySpaces(){
    assertEquals("Stephen King",
        libraryService.findPossibleMatchForAuthor("King").getName());
    assertEquals("Stephen King",
        libraryService.findPossibleMatchForAuthor("Stephen").getName());
    assertEquals("Stephen King",
        libraryService.findPossibleMatchForAuthor("King  ").getName());
    assertEquals("Stephen King",
        libraryService.findPossibleMatchForAuthor("king").getName());
  }

  @Test
  void findAuthorOfBookByTitle() {
    assertEquals("Robert C. Martin",
        libraryService.findAuthorOfBookByTitle("Clean Code").getName());
    assertEquals("Robert C. Martin",
        libraryService.findAuthorOfBookByTitle("Clean Code ").getName());
    assertEquals("Robert C. Martin",
        libraryService.findAuthorOfBookByTitle("clean coDE").getName());
  }


  @Test
  void shouldCountBooksByGenre() {
    Map<Genre, Long> result = libraryService.getNumberOfBooksForEachGenre();
    Map<Genre, Long> expected = books.stream().collect(groupingBy(Book::getGenre, counting()));
    assertEquals(expected, result);
  }

  @Test
  void getGenreWithBiggestNumberOfBooks() {
    assertTrue(libraryService
        .getGenreWithBiggestNumberOfBooks().containsKey(Genre.FICTION));
    assertTrue(libraryService
        .getGenreWithBiggestNumberOfBooks().containsValue(6L));
  }

  @Test
  void getBooksForGivenAuthor() {
    Map<Author, List<String>> expected = new HashMap<>();
    Author author = new Author("Stephen King");
    List<String> booksOfAuthor = List.of("Misery");
    expected.put(author, booksOfAuthor);
    assertEquals(expected.keySet().toString(),
        libraryService.getBooksForGivenAuthor("Stephen King").keySet().toString());
    assertEquals(expected.size(), libraryService.getBooksForGivenAuthor("Stephen King").size());
    assertEquals(expected.size(), libraryService.getBooksForGivenAuthor("  Stephen King").size());
    assertEquals(expected.size(), libraryService.getBooksForGivenAuthor("Stephen KING").size());
  }

  @Test
  void setBookStatus() {
    assertEquals(ReadingStatus.WANT_TO_READ, books.get(0).getReadingStatus());
    libraryService.setBookStatus("Clean Code", ReadingStatus.READING);
    assertEquals(ReadingStatus.READING, books.get(0).getReadingStatus());
  }

  @Test
  void shouldThrowExceptionIfSetReadingStatusForDuplicatedTitle() {
    assertThrows(IllegalArgumentException.class,
        () -> libraryService.setBookStatus("Misery", ReadingStatus.READING));
  }


  private static List<Book> createBooks() {
    List<String> authorNames = List.of("Robert C. Martin", "Joshua Bloch", "J.K. Rowling",
        "J.R.R. Tolkien", "George Orwell", "Fyodor Dostoevsky", "Stephen King");
    List<Author> authors = new ArrayList<>(authorNames.stream().map(Author::new).toList());

    List<Book> books = List.of(new Book("Clean Code", authors.get(0), Genre.TECHNOLOGY,
        2008, "9780132350884"), new Book("The Clean Coder", authors.get(0),
        Genre.TECHNOLOGY, 2011, "9780137081073"), new Book("Effective Java",
        authors.get(1), Genre.TECHNOLOGY, 2018, "9780134685991"), new Book("Harry Potter and the Philosopher's Stone",
        authors.get(2), Genre.FANTASY, 1997, "9780747532743"), new Book("Harry Potter and the Chamber of Secrets",
        authors.get(2), Genre.FANTASY, 1998, "9780747538493"), new Book("The Hobbit",
        authors.get(3), Genre.FANTASY, 1937, "9780261102217"), new Book("1984",
        authors.get(4), Genre.FICTION, 1949, "9780451524935"), new Book("Animal Farm",
        authors.get(4), Genre.FICTION, 1945, "9780451526342"), new Book("Crime and Punishment",
        authors.get(5), Genre.FICTION, 1866, "9780143058144"), new Book("The Brothers Karamazov", authors.get(5),
        Genre.FICTION, 1880, "9780374528379"), new Book("Misery", authors.get(6),
        Genre.FICTION, 1880, "9780374528378"), new Book("Misery", authors.get(5), Genre.FICTION, 1880, "9780374528300"
    ));

    return books;
  }
}
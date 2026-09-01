package org.example.service;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;

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
    for(Book book : books){
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
        .filter(b->b.getName()
            .equalsIgnoreCase("Stephen King")).findFirst();
    Author author1 = null;
    if(author.isPresent()){
      author1 = author.get();
    }

    Book book = new Book("The Shining", author1, Genre.FICTION, 1977, "123454");
    libraryService.addBook(book);
    assertTrue(libraryService.getAllBooks().contains(book));
    assertTrue(libraryService.getAllBooks().stream()
        .anyMatch(b->b.getIsbn().equalsIgnoreCase("123454")));

    Book addedBook = libraryService.getAllBooks().stream()
        .filter(b->b.getIsbn().equalsIgnoreCase("123454"))
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
  void shouldNotCreateNewAuthorIfAuthorExistsAndAddNewBook(){
    Author author = new Author("Stephen King");
    Book book = new Book("The Shining", author, Genre.FICTION, 1977, "123454");
    libraryService.addBook(book);
    assertTrue(libraryService.getAllBooks().contains(book));
    assertEquals(books.stream().map(Book::getAuthor).collect(Collectors.toSet()).size(),
        libraryService.getAllAuthors().size());
  }

  @Test
  void shouldThrowExceptionIfBookIsNullOrBookTitleIsEmptyOrNull(){

    Book book1 = new Book();
    book1.setTitle("");
    Book book2 = new Book();
    book2.setTitle(null);
    book2.setAuthor(new Author("Jean Paul Sartre"));
    assertThrows(NullPointerException.class, ()->libraryService.addBook(book1));
    assertThrows(NullPointerException.class, ()->libraryService.addBook(book2));
    assertThrows(NullPointerException.class, ()->libraryService.addBook(null));
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
  void shouldThrowExceptionIfAuthorIsNull(){
    assertThrows(NullPointerException.class, ()->libraryService.addAuthor(null));
  }
@Test
void shouldThrowExceptionIfAuthorNameIsEmpty() {
    Author author = new Author();
    author.setName("");
  assertThrows(NullPointerException.class, ()->libraryService.addAuthor(author));
}
@Test
void shouldThrowExceptionIfAuthorNameIsNull() {
    Author author = new Author();
  assertThrows(NullPointerException.class, ()->libraryService.addAuthor(author));

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
void shouldThrowExceptionIfTitleIsNull() {
  assertThrows(NullPointerException.class, ()->libraryService.removeBookByTitle(null));
  }

 @Test
 void shouldThrowExceptionIfTitleIsEmpty() {
    assertThrows(NullPointerException.class, ()->libraryService.removeBookByTitle(""));
   assertThrows(NullPointerException.class, ()->libraryService.removeBookByTitle(" "));

 }

   @Test
  void removeBookByTitleAndAuthor() {
    assertTrue(libraryService.removeBookByTitleAndAuthor("Clean Code", "Robert C. Martin"));
  }

  @Test
  void shouldThrowExceptionIfAuthorForTitleIsNull(){
    assertThrows(NullPointerException.class,
        ()->libraryService.removeBookByTitleAndAuthor("Misery", null));
  }

  @Test
  void shouldThrowExceptionIfAuthorForTitleIsEmpty(){
    assertThrows(NullPointerException.class,
        ()->libraryService.removeBookByTitleAndAuthor("Misery", ""));
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
  void shouldThrowExceptionIfBookByIdNotFound(){
    assertThrows(NullPointerException.class, ()->libraryService.findBookById(20));
  }

  @Test
  void shouldFindBookByTitle() {
    Book book = new Book();
    book.setTitle("new book");
    book.setAuthor(new Author("Stephen"));
    libraryService.addBook(book);
    assertEquals(book.getTitle(), libraryService.findBookByTitle("new book").getTitle());
  }

  @Test
  void shouldThrowExceptionIfBookByTitleNotFound(){
    assertThrows(NullPointerException.class, ()-> libraryService.findBookByTitle("not on bookshelf"));
  }

  @Test
  void shouldFindBookByTitleAndThrowExceptionIfTitleIsDuplicated(){
    assertThrows(IllegalArgumentException.class, ()-> libraryService.findBookByTitle("Misery"));
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
  void shouldFindBookByTitleAndAuthorNameOrThrowException(){
    assertThrows(NullPointerException.class, ()->
        libraryService.findBookByTitleAndAuthorName("Misery", "No author"));
    assertThrows(NullPointerException.class, ()->
        libraryService.findBookByTitleAndAuthorName("", ""));
    assertThrows(NullPointerException.class, ()->
        libraryService.findBookByTitleAndAuthorName(null, null));

  }

  @Test
  void shouldFindBookByIsbn() {
    Author author = new Author("James Joyce");
    Book book = new Book("Ulisses", author, Genre.FICTION, 1234, "123454095" );
    libraryService.addBook(book);
    assertEquals(book.getIsbn(), libraryService.findBookByIsbn("123454095").getIsbn());
  }

  @Test
  void shouldThrowExceptionIfIsbnNotFound(){
    assertThrows(NullPointerException.class, ()->libraryService.findBookByIsbn("abcd"));
  }

  @Test
  void shouldPrintAllBooksSorted() {
    assertTrue(books.containsAll(libraryService.getAllBooks()));
  }

  @Test
  void shouldThrowExceptionIfBooksIsEmpty(){
    libraryService = new LibraryService();
    assertThrows(NullPointerException.class, () -> libraryService.getAllBooks());
  }

  @Test
  void getBooksByAuthor() {
  Map<Author, List<String>> booksByAuthor =  books.stream()
        .collect(Collectors.groupingBy(Book::getAuthor,
            Collectors.mapping(Book::getTitle, toList())));
   assertEquals(booksByAuthor, libraryService.getBooksByAuthor());
  }

  @Test
  void shouldThrowExceptionIfAuthorsListIsEmpty(){
    libraryService = new LibraryService();
    assertThrows(NullPointerException.class, ()->libraryService.getBooksByAuthor());
  }

  @Test
  void getBooksByGenre() {
    Map<Genre, List<String>> result = libraryService.getBooksByGenre();
    assertEquals(List.of("Clean Code", "The Clean Coder", "Effective Java"),
        result.get(Genre.TECHNOLOGY));
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
   List <Book> expected = new ArrayList<>(books);
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
  void findAuthorOfBookByTitle() {
    String title = "Clean Code";
    assertEquals("Robert C. Martin",
        libraryService.findAuthorOfBookByTitle(title).getName());
  }

  @Test
   void shouldCountBooksByGenre() {
    Map<Genre, Long> result = libraryService.getNumberOfBooksForEachGenre();
    Map<Genre, Long> expected = books.stream().collect(groupingBy(Book::getGenre, counting()));
    assertEquals(expected, result);
  }

  @Test
  void getGenreWithBiggestNumberOfBooks() {
    assertTrue(libraryService.getGenreWithBiggestNumberOfBooks().containsKey(Genre.FICTION));
    assertTrue(libraryService.getGenreWithBiggestNumberOfBooks().containsValue(6L));
  }

  @Test
  void getBooksForGivenAuthor() {
    Map<Author, List<String>> expected = new HashMap<>();
    Author author = new Author("Stephen King");
    List <String> booksOfAuthor = List.of("Misery");
    expected.put(author, booksOfAuthor );
    assertEquals(expected.keySet().toString(), libraryService.getBooksForGivenAuthor("Stephen King").keySet().toString());
    assertEquals(expected.size(), libraryService.getBooksForGivenAuthor("Stephen King").size());
  }

  @Test
  void setBookStatus() {
    Book book =
        libraryService.getAllBooks()
            .stream()
            .filter(b->b.getTitle().equalsIgnoreCase("Clean Code")).findFirst().orElseThrow();


    libraryService.setBookStatus("Clean Code", ReadingStatus.READING);
    assertEquals(ReadingStatus.READING, book.getReadingStatus());
  }

  @Test
  void shouldThrowExceptionIfSetReadingStatusForDuplicatedTitle(){
    assertThrows(IllegalArgumentException.class, ()->libraryService.setBookStatus("Misery", ReadingStatus.READING));
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
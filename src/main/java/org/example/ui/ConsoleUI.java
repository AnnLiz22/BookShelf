package org.example.ui;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Genre;
import org.example.model.ReadingStatus;
import org.example.service.LibraryService;

public class ConsoleUI {

  private final LibraryService libraryService;
  private final Scanner scanner;

  public ConsoleUI(LibraryService libraryService) {
    this.libraryService = libraryService;
    this.scanner = new Scanner(System.in);
  }

  public void start() {
   boolean isRunning = true;

   while (isRunning) {
    printMenu();

    int choice = readInt();

    switch (choice) {
     case 1 -> showAllBooks();
     case 2 -> showBooksByGenre();
     case 3 -> showBooksByAuthor();
     case 4 -> showAllAuthors();
     case 5 -> showBooksByReadingStatus();
     case 6 -> showBooksByYear();
     case 7 -> findBooksOfAuthor();
     case 8 -> showAuthorForGivenBookTitle();
     case 9 -> showGenreWithBiggestNumberOfBooks();
     case 10 -> addBook();
     case 11 -> addAuthor();
     case 12 -> setReadingStatusForBookFromLibrary();
     case 13 -> removeBookFromYourBookShelf();
     case 0 -> {
      System.out.println("Goodbye! 👋");
      isRunning = false;
     }
     default -> System.out.println("Invalid option.");
   }
  }}
 //helpers:
 private int readInt() {
  while (true) {
   System.out.print("Choose an option: ");

   try {
    return Integer.parseInt(scanner.nextLine());
   } catch (NumberFormatException e) {
    System.out.println("Please enter a number.");
   }
  }
 }


 private void printMenu() {

   System.out.println();
   System.out.println("====================================");
   System.out.println("          📚 BOOKS CATALOG");
   System.out.println("====================================");
   System.out.println("I. Show your library: ");
   System.out.println("1. Show all books");
   System.out.println("2. Show books by genre");
   System.out.println("3. Show books by author");
   System.out.println("4. Show all authors");
   System.out.println("5. Show books by reading status");
   System.out.println("6. Show books sorted by year");
   System.out.println("7. Find books of given author");
   System.out.println("8. Check author for given book title");
   System.out.println("9. Show top genre");
   System.out.println("====================================");
   System.out.println("II. Update your Book Shelf: ");
   System.out.println("10. Add a book");
   System.out.println("11. Add an Author");
   System.out.println("12. Set the reading status for your book");
   System.out.println("13. Remove a book from the Book Shelf");
   System.out.println("====================================");
   System.out.println("0. Exit");
   System.out.println("====================================");
  }

 private void showAllBooks(){
   List<Book> allBooks = libraryService.getAllBooks();
  System.out.println("========== All books on your book shelf ==========");
   System.out.println(allBooks);
 }

 private void showBooksByGenre() {

  Map<Genre, List<String>> books = libraryService.getBooksByGenre();
  if (books.isEmpty()) {
   System.out.println("No books found");
   return;
  }
  System.out.println("========== Books by genre ==========");
  System.out.println(books);
  }

  private void showAllAuthors(){
    List<Author> authors = libraryService.getAllAuthors();
    if (authors.isEmpty()) {
      System.out.println("No books found");
      return;
    }
   System.out.println("========== Authors ==========");
   System.out.println(authors);
  }

  private void showBooksByAuthor() {
    try {
      Map<Author, List<String>> booksByAuthors = libraryService.getBooksByAuthor();
      System.out.println("========== Books by Authors ==========");
      System.out.println(booksByAuthors);
    } catch (Exception e) {
      System.out.println("Author not found");
    }
  }
  private void showBooksByReadingStatus(){
   Map<ReadingStatus, List<String>> booksByReadingStatus = libraryService.getBooksByReadingStatus();
    if (booksByReadingStatus.isEmpty()) {
      System.out.println("No books found");
      return;
    }
   System.out.println("========== Books by Reading status ==========");
   System.out.println(booksByReadingStatus);

  }

  private void showBooksByYear(){
   Map<Integer, List<Book>> booksByYear = libraryService.getBooksSortedByYear();
    if (booksByYear.isEmpty()) {
      System.out.println("No books found");
      return;
    }
   System.out.println("========== Books by Year of Publication ==========");
   System.out.println(booksByYear);
  }

  private void showAuthorForGivenBookTitle(){
   System.out.println("Choose title: ");
   String title = scanner.nextLine();
   try {
     Author author = libraryService.findAuthorOfBookByTitle(title);
     System.out.println("Author of " + title + ": " + author);
   } catch (Exception e) {
     System.out.println("Title not found.");;
   }
  }

  private void findBooksOfAuthor(){
   System.out.println("Choose author: ");
   try {
     String author = scanner.nextLine();
     System.out.println("Books of " + author + " : "
         + libraryService.getBooksForGivenAuthor(author));
   }catch (NullPointerException e){
     System.out.println("Author not found.");
   }
  }

  private void showGenreWithBiggestNumberOfBooks(){
   Map<Genre, Long> genreWithBiggestNumOfBooks =
       libraryService.getGenreWithBiggestNumberOfBooks();
   System.out.println("Top genre on your book Shelf: " + genreWithBiggestNumOfBooks);
  }

  private void addBook() {
    try {
      System.out.println("Book title: ");
      String title = scanner.nextLine();

      System.out.println("Book author: ");
      String authorName = scanner.nextLine();

      System.out.println("Book genre:\n BIOGRAPHY, FICTION, FANTASY, HISTORY, SCIENCE, TECHNOLOGY, OTHER");
      Genre genre = Genre.valueOf(scanner.nextLine().toUpperCase());

      System.out.println("Book isbn: ");
      String isbn = scanner.nextLine();

      System.out.println("Book year: ");
      int year = scanner.nextInt();

      Author author = new Author(authorName);
      Book book = new Book(title, author, genre, year, isbn);
      libraryService.addBook(book);

      System.out.println("Book: " + book + "Added to your Book Shelf.");
    } catch (Exception e) {
      System.out.println("Book title and author must be set");
    }
  }

  private void addAuthor() {
    System.out.println("Author name: ");
    String authorName = scanner.nextLine();
    Author author;

    try {
      author = new Author(authorName);
    } catch (Exception e) {
      System.out.println("Incorrect author name");
      return;
    }
      System.out.println("Do you want to add book for this author? [YES/NO]");
      String response = scanner.nextLine().trim().toUpperCase();

    if(response.equalsIgnoreCase("NO")) {
      libraryService.addAuthor(author);
      System.out.println("Author: " + author + " added.");
      return;
    }

    while (response.equals("YES")) {
        System.out.println("Book title: ");
        String title = scanner.nextLine();
        System.out.println("Genre: \n BIOGRAPHY, FICTION, FANTASY, HISTORY, SCIENCE, TECHNOLOGY, OTHER");
        Genre genre = Genre.valueOf(scanner.nextLine().toUpperCase());

        System.out.println("isbn:");
        String isbn = scanner.nextLine();
        System.out.println("year:");
        int year = scanner.nextInt();
        scanner.nextLine();

        Book book = new Book(title, author, genre, year, isbn);
        libraryService.addBook(book);
        System.out.println("Author and book added. Do you want to add another book? [YES / NO]");
        response = scanner.nextLine().toUpperCase();
    }
  }

  private void setReadingStatusForBookFromLibrary(){
    System.out.println("Tape the book title: ");
    String title = scanner.nextLine();
    try {
     Book book = libraryService.findBookByTitle(title);
      System.out.println("Set the status to: \n [WANT_TO_READ, READING, FINISHED, ABANDONED]");
      ReadingStatus status = ReadingStatus
          .valueOf(scanner.nextLine().toUpperCase().replace(" ", "_"));
      libraryService.setBookStatus(book.getTitle(), status);
      System.out.println(book);

    } catch (Exception e) {
      System.out.println("The selected title not on the Book Shelf.");
    }
  }

  private void removeBookFromYourBookShelf() {

    System.out.println("Give the book title: ");
    String title = scanner.nextLine();
    Book book;

    try {
      book = libraryService.findBookByTitle(title);
      System.out.println("Are you sure you want to remove " + book + "[YES/NO]");

      String response = scanner.nextLine().toUpperCase();
      if (response.equals("YES")) {

        libraryService.removeBookByTitle(book.getTitle());
        System.out.println("Book removed.");
      }

    } catch (IllegalArgumentException e) {
      System.out.println("Title duplicated. Give the author name.");

      try{
      String authorName = scanner.nextLine();
      book = libraryService.findBookByTitleAndAuthorName(title, authorName);
      System.out.println("Are you sure you want to remove " + book  + "[YES/NO]");

      String response = scanner.nextLine().toUpperCase();
      if (response.equals("YES")) {
        libraryService.removeBookByTitleAndAuthor(title, authorName);
        System.out.println("Book removed.");
       }
      }catch (NullPointerException ex){
        System.out.println("Book with given title and author not found. Try again.");
      }
    } catch (NullPointerException e) {
      System.out.println("Title not found");
    }
  }
 }

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
   System.out.println("1. Show all books");
   System.out.println("2. Show books by genre");
   System.out.println("3. Show books by author");
   System.out.println("4. Show all authors");
   System.out.println("5. Show books by reading status");
   System.out.println("6. Show books sorted by year");
   System.out.println("7. Find books of given author");
   System.out.println("8. Check author for given book title");
   System.out.println("9. Show top genre");
   System.out.println("0. Exit");
   System.out.println("====================================");
  }

 private void showAllBooks(){
   List<Book> allBooks = libraryService.getAllBooks();
  System.out.println("========== All books on your book shelf ==========");
   System.out.println(allBooks);
 }

 private void showBooksByGenre() {

  Map<Genre, List<Book>> books = libraryService.getBooksByGenre();
  if (books.isEmpty()) {
   System.out.println("No books found");
   return;
  }
  System.out.println("========== Books by genre ==========");
  System.out.println(books);
  }

  private void showAllAuthors(){
   List<Author> authors = libraryService.getAllAuthors();
   System.out.println("========== Authors ==========");
   System.out.println(authors);
  }

  private void showBooksByAuthor(){
   Map<Author, List<Book>> booksByAuthors = libraryService.getBooksByAuthor();
   System.out.println("========== Books by Authors ==========");
   System.out.println(booksByAuthors);
  }

  private void showBooksByReadingStatus(){
   Map<ReadingStatus, List<Book>> booksByReadingStatus = libraryService.getBooksByReadingStatus();
   System.out.println("========== Books by Reading status ==========");
   System.out.println(booksByReadingStatus);

  }

  private void showBooksByYear(){
   Map<Integer, List<Book>> booksByYear = libraryService.getBooksSortedByYear();
   System.out.println("========== Books by Year of Publication ==========");
   System.out.println(booksByYear);
  }

  private void showAuthorForGivenBookTitle(){
   System.out.println("Choose title: ");
   String title = scanner.nextLine();
   Author author =  libraryService.findAuthorByBookTitle(title);
   System.out.println("Author of " + title + ": " + author);
  }

  private void findBooksOfAuthor(){
   System.out.println("Choose author: ");
   String author = scanner.nextLine();
   System.out.println("Books of " + author + " : "
       + libraryService.getBooksForGivenAuthor(author));

  }

  private void showGenreWithBiggestNumberOfBooks(){
   Map<Genre, Long> genreWithBiggestNumOfBooks =
       libraryService.getGenreWithBiggestNumberOfBooks();
   System.out.println("Top genre on your book Shelf: " + genreWithBiggestNumOfBooks);
  }
 }

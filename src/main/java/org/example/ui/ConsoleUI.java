package org.example.ui;

import java.util.Arrays;
import java.util.List;
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
    }
  }

  private void printMenu() {
    System.out.println();
    System.out.println("====================================");
    System.out.println("          📚 BOOKS CATALOG");
    System.out.println("====================================");
    Arrays.stream(MenuOption.values())
        .forEach(menuOption ->
            System.out.println(menuOption.getNumber() + ". " + menuOption.getDescription()));
  }

  private void showAllBooks() {
    try {
      List<Book> allBooks = libraryService.getAllBooks();
      System.out.println("========== All books on your book shelf ==========");
      System.out.println(allBooks);
    } catch (NullPointerException e) {
      System.out.println(e.getMessage());
    }
  }

  private void showBooksByGenre() {
    try {
      System.out.println("========== Books by genre ==========");

      System.out.println(libraryService.getBooksByGenre());
    } catch (NullPointerException e) {
      System.out.println(e.getMessage());
    }
  }

  private void showAllAuthors() {
    if (libraryService.getAllAuthors().isEmpty()) {
      System.out.println("No authors found");
      return;
    }
    System.out.println("========== Authors ==========");
    System.out.println(libraryService.getAllAuthors());
  }

  private void showBooksByAuthor() {
    try {
      System.out.println("========== Books by Authors ==========");
      System.out.println(libraryService.getBooksByAuthor());
    } catch (NullPointerException e) {
      System.out.println(e.getMessage());
    }
  }

  private void showBooksByReadingStatus() {
    if (libraryService.getBooksByReadingStatus().isEmpty()) {
      System.out.println("No books found");
      return;
    }
    System.out.println("========== Books by Reading status ==========");
    System.out.println(libraryService.getBooksByReadingStatus());
  }

  private void showBooksByYear() {
    if (libraryService.getBooksSortedByYear().isEmpty()) {
      System.out.println("No books found");
      return;
    }
    System.out.println("========== Books by Year of Publication ==========");
    System.out.println(libraryService.getBooksSortedByYear());
  }

  private void showAuthorForGivenBookTitle() {
    while (true) {
      System.out.println("Give the book title: \n[OR Go back to Main Menu - 0]");
      String title = scanner.nextLine().strip();
      if (title.equals("0")) {
        return;
      }
      try {
        Author author = libraryService.findAuthorOfBookByTitle(title);
        System.out.println("Author of " + title + ": " + author);
        return;
      } catch (Exception e) {
        System.out.println("Title not found.");
      }
    }
  }

  private void findBooksOfAuthor() {
    while (true) {
      System.out.println(
          "Give the author name:\n[OR Go back to the Main Menu - 0]");
      String author = scanner.nextLine().trim();
      if (author.equals("0")) {
        return;
      }
      try {
        Author foundAuthor = libraryService.findAuthor(author);
        System.out.println(
            "Books of " + foundAuthor.getName() + ": "
                + libraryService.getBooksForGivenAuthor(foundAuthor.getName()));
        return;
      } catch (IllegalArgumentException e) {
        try {
          Author possibleAuthor =
              libraryService.findPossibleMatchForAuthor(author);

          System.out.println(
              "Did you mean: " + possibleAuthor.getName()
                  + " ? [YES / NO]");
          String answer = scanner.nextLine().trim();
          if (answer.equalsIgnoreCase("YES")) {
            System.out.println(
                "Books of " + possibleAuthor.getName() + ": "
                    + libraryService.getBooksForGivenAuthor(
                    possibleAuthor.getName()));
            return;
          }
          if (answer.equalsIgnoreCase("NO")) {
            return;
          }
        } catch (IllegalArgumentException ex) {
          System.out.println("Author not found.");
        }
      }
    }
  }

  private void showGenreWithBiggestNumberOfBooks() {
    try {
      System.out.println("Top genre on your book Shelf: " + libraryService.getGenreWithBiggestNumberOfBooks());
    } catch (NullPointerException e) {
      System.out.println("Nothing found.");
    }
  }

  private void addBook() {
    Book book = new Book();
    try {
      System.out.println("Book title: ");
      String title = scanner.nextLine().trim();
      book.setTitle(title);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      return;
    }

    try {
      System.out.println("Book author: ");
      String authorName = scanner.nextLine();
      book.setAuthor(new Author(authorName));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      return;
    }
    book.setGenre(readGenre());
    try {
      System.out.println("Book isbn: ");
      String isbn = scanner.nextLine().trim().replace("-", "");
      book.setIsbn(isbn);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      return;
    }
    try {
      System.out.println("Book year: ");
      int year = scanner.nextInt();
      book.setYear(year);
      scanner.nextLine();
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
    libraryService.addBook(book);
    System.out.println("Book: " + book + "Added to your Book Shelf.");
  }

  private void addAuthor() {
    System.out.println("Author name: ");
    String authorName = scanner.nextLine().strip();
    Author author = new Author();

    try {
      author.setName(authorName);
      libraryService.addAuthor(author);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage() + " Enter author name: ");
      authorName = scanner.nextLine();
      if (authorName == null || authorName.isBlank()) {
        return;
      }
    }
    try{
      Author possibleAuthor = libraryService.findPossibleMatchForAuthor(authorName);
      System.out.println("Did you mean " + possibleAuthor + "? [YES / NO]");
      String response = scanner.nextLine().toUpperCase();
      if (response.equals("YES")) {
        System.out.println("Author "+ authorName + " already in the catalogue.");
        return;
      }}catch (IllegalArgumentException exception){
      System.out.println(exception.getMessage());
      return;
    }

    System.out.println("Do you want to add book for this author: "+ author +"? [YES/NO]");
    String response1 = scanner.nextLine().trim().toUpperCase();

    if (response1.equalsIgnoreCase("NO")) {
      System.out.println("Author: " + author + " added.");
      return;
    }

    while (response1.equals("YES")) {
      Book book = new Book();
      try {
        book.setAuthor(author);
        System.out.println("Book title: ");
        String title = scanner.nextLine();
        book.setTitle(title);
      }catch (IllegalArgumentException ex){
        System.out.println(ex.getMessage() + " Enter book title: ");
        String title = scanner.nextLine();
        if(title==null|| title.isBlank()){
          return;
        }
        book.setTitle(title);
      }
      try {
        System.out.println("Choose book genre: ");
        List.of(Genre.values()).forEach(System.out::println);
        String genre = scanner.nextLine().toUpperCase();
        book.setGenre(Genre.valueOf(genre));
      }catch (IllegalArgumentException exception) {
        System.out.println("Choose genre from the list.");
        String genre = scanner.nextLine().toUpperCase();
        try{
        book.setGenre(Genre.valueOf(genre));}
        catch (IllegalArgumentException ex){
          return;
        }
      } try {
        System.out.println("Enter isbn number: ");
        String isbn = scanner.nextLine();
        book.setIsbn(isbn);
      }catch(IllegalArgumentException isbnExc){
        System.out.println(isbnExc.getMessage());}
      try{
        System.out.println("Enter book year: ");
        int year = scanner.nextInt();
        scanner.nextLine();
        book.setYear(year);
      }catch (IllegalArgumentException yearExc){
        System.out.println(yearExc.getMessage());
      }
      libraryService.addBook(book);
      System.out.println("Author and book added. Do you want to add another book? [YES / NO]");
      response1 = scanner.nextLine().toUpperCase();
    }
  }

  private void setReadingStatusForBookFromLibrary() {
    System.out.println("Tape the book title: ");
    Book book;
    String title = scanner.nextLine();

    try {
      book = libraryService.findBookByTitle(title);
      System.out.println("Set the status to: \n [WANT_TO_READ, READING, FINISHED, ABANDONED]");
      ReadingStatus status = ReadingStatus
          .valueOf(scanner.nextLine().toUpperCase().replace(" ", "_"));
      libraryService.setBookStatus(book.getTitle(), status);
      System.out.println(book);

    }catch (NullPointerException e) {
      System.out.println("Book title not found.");
    }catch (IllegalArgumentException e) {
      System.out.println(e.getMessage() + " Give the author name.");
      try {
        String authorName = scanner.nextLine();
         try {
           libraryService.findAuthor(authorName);
         }catch (IllegalArgumentException ex){
           try {
             Author author = libraryService.findPossibleMatchForAuthor(authorName);
             System.out.println("Did you mean " + author + "? [YES / NO]");
             String response = scanner.nextLine().strip().toUpperCase();

             if(response.equals("YES")){
               authorName = author.getName();
             }if(response.equals("NO")) {
               System.out.println("No author found.");
               return;
             }
           }catch(IllegalArgumentException exception){
             System.out.println(exception.getMessage());
             return;
             }
         }
        book = libraryService.findBookByTitleAndAuthorName(title, authorName);
        System.out.println("Set the status to: \n [WANT_TO_READ, READING, FINISHED, ABANDONED]");
        ReadingStatus status = ReadingStatus
            .valueOf(scanner.nextLine().toUpperCase().replace(" ", "_"));
        libraryService.setBookStatus(book.getTitle(), authorName, status);
        System.out.println(book);
      } catch (NullPointerException ex) {
       System.out.println("Incorrect author name for given book title.");
      }
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
      System.out.println(e.getMessage());

      try {
        String authorName = scanner.nextLine();
        book = libraryService.findBookByTitleAndAuthorName(title, authorName);
        System.out.println("Are you sure you want to remove " + book + "[YES/NO]");

        String response = scanner.nextLine().toUpperCase();
        if (response.equals("YES")) {
          libraryService.removeBookByTitleAndAuthor(title, authorName);
          System.out.println("Book removed.");
        }
      } catch (NullPointerException ex) {
        System.out.println("Book with given title and author not found. Try again.");
      }
    } catch (NullPointerException e) {
      System.out.println("Title not found");
    }
  }

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


  private Genre readGenre() {
    while (true) {
      System.out.println("Enter genre: ");
      String genre = scanner.nextLine().trim();

      try {
        return Genre.valueOf(genre.toUpperCase());
      } catch (IllegalArgumentException e) {
        System.out.println("Invalid genre. Choose from the list.");
      }
    }
  }
}

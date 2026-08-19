package org.example;

import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Genre;
import org.example.service.LibraryService;
import org.example.ui.ConsoleUI;

public class Main {
  public static void main(String[] args) {

    Author author1 = new Author(1, "Robert C. Martin");
    Author author2 = new Author(2, "Joshua Bloch");
    Author author3 = new Author(3, "J.K. Rowling");
    Author author4 = new Author(4, "J.R.R. Tolkien");
    Author author5 = new Author(5, "George Orwell");
    Author author6 = new Author(6, "Fyodor Dostoevsky");

    Book book1 = new Book(
        1,
        "Clean Code",
        author1,
        Genre.TECHNOLOGY,
        2008,
        "9780132350884"
    );

    Book book2 = new Book(
        2,
        "The Clean Coder",
        author1,
        Genre.TECHNOLOGY,
        2011,
        "9780137081073"
    );

    Book book3 = new Book(
        3,
        "Effective Java",
        author2,
        Genre.TECHNOLOGY,
        2018,
        "9780134685991"
    );

    Book book4 = new Book(
        4,
        "Harry Potter and the Philosopher's Stone",
        author3,
        Genre.FANTASY,
        1997,
        "9780747532743"
    );

    Book book5 = new Book(
        5,
        "Harry Potter and the Chamber of Secrets",
        author3,
        Genre.FANTASY,
        1998,
        "9780747538493"
    );

    Book book6 = new Book(
        6,
        "The Hobbit",
        author4,
        Genre.FANTASY,
        1937,
        "9780261102217"
    );

    Book book7 = new Book(
        7,
        "1984",
        author5,
        Genre.FICTION,
        1949,
        "9780451524935"
    );

    Book book8 = new Book(
        8,
        "Animal Farm",
        author5,
        Genre.FICTION,
        1945,
        "9780451526342"
    );

    Book book9 = new Book(
        9,
        "Crime and Punishment",
        author6,
        Genre.FICTION,
        1866,
        "9780143058144"
    );

    Book book10 = new Book(
        10,
        "The Brothers Karamazov",
        author6,
        Genre.FICTION,
        1880,
        "9780374528379"
    );

    Book book11 = new Book(
        11,
        "Misery",
        author6,
        Genre.FICTION,
        1880,
        "9780374528378"
    );

    LibraryService libraryService = new LibraryService();
    libraryService.addBook(book1);
    libraryService.addBook(book2);
    libraryService.addBook(book3);
    libraryService.addBook(book4);
    libraryService.addBook(book5);
    libraryService.addBook(book6);
    libraryService.addBook(book7);
    libraryService.addBook(book8);
    libraryService.addBook(book9);
    libraryService.addBook(book10);
    libraryService.addBook(book11);

    System.out.println(libraryService.findById(1) );
    System.out.println("All books: " + libraryService.getAllBooks());
    System.out.println("Books by genre: " +  libraryService.getBooksByGenre());
    System.out.println("Books by reading status: " + libraryService.getBooksByReadingStatus());
    System.out.println("Books by author: " + libraryService.getBooksByAuthor());
    System.out.println("Books sorted by the year of publication: " + libraryService.getBooksSortedByYear());
    System.out.println("Number of books for a given genre: " + libraryService.getNumberOfBooksForEachGenre());
    System.out.println("Top genre: " + libraryService.getGenreWithBiggestNumberOfBooks());

    ConsoleUI consoleUI = new ConsoleUI(libraryService);
    consoleUI.start();

  }
}
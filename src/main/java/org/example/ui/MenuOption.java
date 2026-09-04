package org.example.ui;

public enum MenuOption {

  SHOW_ALL_BOOKS(1, "Show all books"),
  SHOW_BOOKS_BY_GENRE(2, "Show books by genre"),
  SHOW_BOOKS_BY_AUTHOR(3, "Show books by author"),
  SHOW_ALL_AUTHORS(4, "Show all authors"),
  SHOW_BOOKS_BY_READING_STATUS(5, "Show books by reading status"),
  SHOW_BOOKS_BY_YEAR(6, "Show books sorted by year"),
  FIND_ALL_BOOKS_OF_GIVEN_AUTHOR(7, "Find books of given author"),
  FIND_AUTHOR_OF_BOOK(8, "Check author for given book title"),
  FIND_GENRE_WITH_BIGGER_NO_OF_BOOKS(9, "Show top genre"),
  ADD_BOOK(10, " Add a book"),
  ADD_AUTHOR(11, "Add an Author"),
  SET_READING_STATUS(12, "Set the reading status for your book"),
  REMOVE_BOOK(13, "Remove a book from the Book Shelf"),
  EXIT(0, "Exit");


  private final int number;
  private final String description;

  MenuOption(int number, String description) {
    this.number = number;
    this.description = description;
  }

  public int getNumber() {
    return number;
  }

  public String getDescription() {
    return description;
  }
}

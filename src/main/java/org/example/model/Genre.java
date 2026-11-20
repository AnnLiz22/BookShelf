package org.example.model;

import java.util.Arrays;
import java.util.List;

public enum Genre {

  FICTION(1, "Fiction"),
  FANTASY(2, "Fantasy"),
  SCIENCE(3, "Science"),
  HISTORY(4, "History"),
  BIOGRAPHY(5, "Biography"),
  TECHNOLOGY(6, "Technology"),
  OTHER(7, "Other books");

  private final int number;
  private final String description;

  Genre(int number, String description) {
    this.number = number;
    this.description = description;
  }

  public int getNumber() {
    return number;
  }

  public String getDescription() {
    return description;
  }

  public static List<String> getGenres() {
  return Arrays.stream(Genre.values())
        .map(genre ->
            (genre.getNumber() + ". " + genre.getDescription()))
       .toList();
  }
}

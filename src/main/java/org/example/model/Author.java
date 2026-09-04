package org.example.model;

public class Author {

  private static int nextId = 1;
  private int id;
  private String name;

  public Author() {
  }

  public Author(String name) {
    if(name == null || name.isBlank() || name.length() < 2){
      throw new IllegalArgumentException();
    }
    this.id = nextId;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public static int getNextId() {
    return nextId;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return
        name ;
  }
}
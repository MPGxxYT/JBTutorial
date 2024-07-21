package me.mortaldev.jbtutorial.modules.profile;

import me.mortaldev.crudapi.CRUD;
import me.mortaldev.jbtutorial.modules.book.Book;
import me.mortaldev.jbtutorial.modules.book.BookManager;

import java.util.*;
import java.util.stream.Collectors;

public class Profile implements CRUD.Identifiable {

  private final UUID uuid;
  private final Set<String> completedBooks;
  private final Set<String> startedBooks;
  private Integer currentStep;
  private String activeBook;
  private String bookPlan;
  private Integer dataTracked;

  public Profile(UUID uuid) {
    this.uuid = uuid;
    defaultData();
    completedBooks = new HashSet<>();
    startedBooks = new HashSet<>();
  }

  @Override
  public String getID() {
    return uuid.toString();
  }

  public void defaultData() {
    currentStep = 0;
    activeBook = null;
    bookPlan = null;
    dataTracked = 0;
  }

  public UUID getUUID() {
    return uuid;
  }

  public Integer getDataTracked() {
    return dataTracked;
  }

  public void setDataTracked(Integer dataTracked) {
    this.dataTracked = dataTracked;
  }

  public List<Book> getStartedBooks() {
    return startedBooks.stream()
        .map(book -> BookManager.getInstance().getBook(book))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  public void addStartedBook(Book book) {
    startedBooks.add(book.getId());
  }

  public List<Book> getCompletedBooks() {
    return completedBooks.stream()
        .map(book -> BookManager.getInstance().getBook(book))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  public void addCompletedBook(Book book) {
    completedBooks.add(book.getId());
  }

  public Integer getCurrentStep() {
    return currentStep;
  }

  public void setCurrentStep(Integer currentStep) {
    this.currentStep = currentStep;
  }

  public Book getActiveBook() {
    return BookManager.getInstance().getBook(activeBook);
  }

  public void setActiveBook(Book activeBook) {
    if (activeBook == null) {
      this.activeBook = null;
    } else {
      this.activeBook = activeBook.getId();
    }
  }

  public void save() {
    ProfileCRUD.getInstance().saveData(this);
  }

  public void delete() {
    ProfileCRUD.getInstance().deleteData(this);
  }
}

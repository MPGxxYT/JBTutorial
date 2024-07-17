package me.mortaldev.jbtutorial.modules;

import me.mortaldev.crud.AbstractCRUD;

public class Person implements AbstractCRUD.Identifiable {
  String firstName;
  String lastName;
  Integer age;

  public Person(String firstName, String lastName, Integer age) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.age = age;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public Integer getAge() {
    return age;
  }

  @Override
  public String getID() {
    return firstName.toLowerCase() + "_" + lastName.toLowerCase();
  }
}

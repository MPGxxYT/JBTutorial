package me.mortaldev.jbtutorial.modules;

import me.mortaldev.crud.AbstractCRUD;
import me.mortaldev.jbtutorial.Main;

public class PersonCRUD extends AbstractCRUD<Person> {

  @Override
  public String getPath() {
    return Main.getInstance().getDataFolder().getPath() + "/people/";
  }

  public Person getData(String id) {
    return super.getData(id, Person.class).orElse(new Person("default", "name", 55));
  }
}

package me.mortaldev.jbtutorial.modules;

import me.mortaldev.jbtutorial.modules.crud.*;

public class PersonCRUD extends AbstractCRUD<Person> {

  public PersonCRUD() {
    iDelete = new NormalDelete();
    iSave = new GsonSave();
    iGet = new GsonGet();
  }

  @Override
  public String getPath() {
    return super.getPath() + "/people/";
  }

  public Person getData(String id) {
    return super.getData(id, Person.class).orElse(new Person("default", "name", 55));
  }
}

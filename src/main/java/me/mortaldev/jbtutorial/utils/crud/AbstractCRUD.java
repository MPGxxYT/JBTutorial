package me.mortaldev.jbtutorial.utils.crud;

import me.mortaldev.jbtutorial.Main;

import java.util.Optional;

public abstract class AbstractCRUD<T extends AbstractCRUD.Identifiable> {
  protected IDelete iDelete;
  protected ISave iSave;
  protected IGet iGet;

  public String getPath() {
    return Main.getInstance().getDataFolder().getPath() + "/";
  }

  protected Optional<T> getData(String id, Class<T> clazz) {
    return iGet.get(id, getPath(), clazz);
  }

  public void deleteData(T object) {
    iDelete.delete(object, getPath());
  }

  public void saveData(T object) {
    Main.log(getPath());
    iSave.save(object, getPath());
  }

  public interface Identifiable {
    String getID();
  }
}

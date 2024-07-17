package me.mortaldev.jbtutorial.utils.crud;

import java.io.File;
import me.mortaldev.crud.AbstractCRUD;
import me.mortaldev.crud.ISave;
import me.mortaldev.jbtutorial.modules.crud.AbstractCRUD;
import me.mortaldev.jbtutorial.modules.crud.ISave;
import me.mortaldev.jbtutorial.utils.GSON;

public class GsonSave implements ISave {
  @Override
  public <T extends AbstractCRUD.Identifiable> void save(T object, String path) {
    File filePath = new File(path + object.getID() + ".json");
    GSON.saveJsonObject(filePath, object);
  }
}

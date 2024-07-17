package me.mortaldev.jbtutorial.utils.crud;

import me.mortaldev.crud.AbstractCRUD;

public interface ISave {
  <T extends AbstractCRUD.Identifiable> void save(T object, String path);
}

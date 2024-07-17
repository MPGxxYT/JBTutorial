package me.mortaldev.jbtutorial.utils.crud;

import me.mortaldev.crud.AbstractCRUD;

public interface IDelete {
  <T extends AbstractCRUD.Identifiable> void delete(T object, String path);
}

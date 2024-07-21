package me.mortaldev.jbtutorial.modules.profile;

import me.mortaldev.crudapi.CRUD;
import me.mortaldev.jbtutorial.Main;

public class ProfileCRUD extends CRUD<Profile> {

  public static ProfileCRUD getInstance() {
    return SingletonHolder.profileCRUD;
  }

  @Override
  public String getPath() {
    return Main.getInstance().getDataFolder().getAbsolutePath() + "/profiles/";
  }

  /**
   * Used to get the profile data of a player.
   *
   * @param id - UUID of the player.
   * @return Profile of the player.
   */
  public Profile getData(String id) {
    return super.getData(id, Profile.class).orElse(null);
  }

  private static class SingletonHolder {
    private static final ProfileCRUD profileCRUD = new ProfileCRUD();
  }
}

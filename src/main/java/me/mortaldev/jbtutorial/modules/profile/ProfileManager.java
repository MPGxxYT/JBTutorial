package me.mortaldev.jbtutorial.modules.profile;

import java.util.HashMap;
import java.util.UUID;

public class ProfileManager {

  HashMap<UUID, Profile> loadedProfiles = new HashMap<>();

  public static ProfileManager getInstance() {
    return ProfileManager.SingletonHolder.profileManager;
  }

  public Profile getProfile(UUID uuid) {
    if (loadedProfiles.containsKey(uuid)) {
      return loadedProfiles.get(uuid);
    }
    return loadProfile(uuid);
  }

  public boolean updateProfile(Profile profile) {
    if (loadedProfiles.containsKey(profile.getUUID())) {
      loadedProfiles.put(profile.getUUID(), profile);
      return true;
    }
    return false;
  }

  public void saveProfile(UUID uuid) {
    getProfile(uuid).save();
  }

  public void saveAllProfiles() {
    loadedProfiles.values().forEach(Profile::save);
  }

  private Profile loadProfile(UUID uuid) {
    Profile profile = ProfileCRUD.getInstance().getData(uuid.toString());
    if (profile == null) {
      profile = new Profile(uuid);
    }
    loadedProfiles.put(uuid, profile);
    return profile;
  }

  private static class SingletonHolder {
    private static final ProfileManager profileManager = new ProfileManager();
  }
}

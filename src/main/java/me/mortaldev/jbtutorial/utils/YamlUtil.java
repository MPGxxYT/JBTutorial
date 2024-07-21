package me.mortaldev.jbtutorial.utils;

import java.io.*;
import me.mortaldev.jbtutorial.Main;

public class YamlUtil {
  public static final String RESOURCE_LOAD_ERROR = "Failed to load resource: ";
  private static final Main MAIN = Main.getInstance();

  public static void loadResource(String path, String name) {
    if (!name.contains(".yml")) {
      name = name.concat(".yml");
    }
    InputStream stream = MAIN.getResource(name);
    if (stream == null) {
      MAIN.getLogger().warning(RESOURCE_LOAD_ERROR + name);
      return;
    }
    File file = new File(path, name);
    try {
      if (!file.exists()) {
        file.createNewFile();
      }
      OutputStream outputStream = new FileOutputStream(file);
      outputStream.write(stream.readAllBytes());
      outputStream.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void loadResource(String name) {
    loadResource(MAIN.getDataFolder().getPath(), name);
  }
}

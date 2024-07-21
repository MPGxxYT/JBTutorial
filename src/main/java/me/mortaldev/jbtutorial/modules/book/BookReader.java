package me.mortaldev.jbtutorial.modules.book;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import me.mortaldev.jbtutorial.Main;
import me.mortaldev.jbtutorial.modules.book.types.ActionType;
import me.mortaldev.jbtutorial.modules.book.types.StartType;
import me.mortaldev.jbtutorial.records.Pair;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class BookReader {

  private static String ID;

  public static Book readBook(File file) {
    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
    String id = config.getString("id");
    if (id == null) {
      Bukkit.getLogger().warning("BOOK MISSING ID: " + file.getName());
      return null;
    }
    ID = id;
    String title = config.getString("title");
    if (title == null) {
      Bukkit.getLogger().warning("BOOK MISSING TITLE IN: " + ID + ".yml");
      return null;
    }
    String description = config.getString("description");
    if (description == null) {
      Bukkit.getLogger().warning("BOOK MISSING DESCRIPTION IN: " + ID + ".yml");
      return null;
    }
    boolean crucial = config.getBoolean("crucial");
    Book book = new Book(id, title, description, crucial);
    List<String> rewards = config.getStringList("rewards");
    if (!rewards.isEmpty()) {
      book.setSerializedRewards(rewards);
    }
    List<BookStep> bookSteps = readSteps(config);
    if (!bookSteps.isEmpty()) {
      book.setSteps(bookSteps);
    }
    return book;
  }

  private static List<BookStep> readSteps(YamlConfiguration config) {
    ConfigurationSection stepsSection = config.getConfigurationSection("steps");
    Set<String> stepKeysSet = stepsSection != null ? stepsSection.getKeys(false) : null;
    if (stepKeysSet == null || stepKeysSet.isEmpty()) {
      return new ArrayList<>();
    }
    List<BookStep> bookSteps = new ArrayList<>();
    for (String stepKey : stepKeysSet) {
      String text = stepsSection.getString(stepKey + ".text");
      BookStep bookStep = new BookStep(text);
      //
      ConfigurationSection startSection = stepsSection.getConfigurationSection(stepKey + ".start");
      List<Pair<StartType, String>> startPairsList = readEnumSection(StartType.class, startSection);
      if (!startPairsList.isEmpty()) {
        bookStep.setStartActions(startPairsList);
      }
      boolean info = stepsSection.getBoolean(stepKey + ".info", false);
      bookStep.setInfo(info);
      if (info) {
        int delay = stepsSection.getInt(stepKey + ".delay", 3);
        bookStep.setDelay(delay);
      } else {
        ConfigurationSection actionsSection =
            stepsSection.getConfigurationSection(stepKey + ".actions");
        List<Pair<ActionType, String>> actionPairsList =
            readEnumSection(ActionType.class, actionsSection);
        if (!actionPairsList.isEmpty()) {
          bookStep.setActions(actionPairsList);
        }
      }
      bookSteps.add(bookStep);
    }
    return bookSteps;
  }

  private static <T extends Enum<T>> List<Pair<T, String>> readEnumSection(
      Class<T> enumClazz, ConfigurationSection section) {
    if (section == null) {
      return new ArrayList<>();
    }
    Set<String> keys = section.getKeys(false);
    List<Pair<T, String>> pairs = new ArrayList<>();
    for (String key : keys) {
      ConfigurationSection keySection = section.getConfigurationSection(key);
      Pair<T, String> pair = readPair(enumClazz, keySection);
      if (pair != null) {
        pairs.add(pair);
      }
    }
    return pairs;
  }

  private static <T extends Enum<T>> Pair<T, String> readPair(
      Class<T> enumClazz, ConfigurationSection section) {
    if (section == null) {
      return null;
    }
    String type = section.getString("type");
    if (type == null || type.isEmpty()) {
      return null;
    }
    String data = section.getString("data");
    if (data == null || data.isEmpty()) {
      return null;
    }
    T typeEnum;
    try {
      typeEnum = Enum.valueOf(enumClazz, type.toUpperCase());
      if (typeEnum.equals(ActionType.CALL)) {
        if (!Main.getEnabledDependencies().contains("Skript")) {
          Bukkit.getLogger()
              .warning("UNABLE TO USE TYPE: '" + typeEnum + "'. Dependency 'Skript' is required.");
        }
      }
      if (typeEnum.equals(ActionType.SELL)) {
        if (!Main.getEnabledDependencies().contains("ShopGUIPlus")) {
          Bukkit.getLogger()
              .warning(
                  "UNABLE TO USE TYPE: '" + typeEnum + "'. Dependency 'ShopGUIPlus' is required.");
        }
      }
    } catch (IllegalArgumentException e) {
      Bukkit.getLogger().warning("TYPE NOT FOUND: '" + type + "' in " + ID + ".yml");
      return null;
    }
    return new Pair<>(typeEnum, data);
  }
}

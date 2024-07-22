package me.mortaldev.jbtutorial.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import me.mortaldev.jbtutorial.Main;
import me.mortaldev.jbtutorial.modules.book.Plan;
import me.mortaldev.jbtutorial.utils.YamlUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

public class TutorialConfig {
  File path;
  YamlConfiguration config;
  List<String> fullTutorial = new ArrayList<>();
  List<String> partialTutorial = new ArrayList<>();

  public TutorialConfig() {
    load();
  }

  public void load() {
    this.path = new File((Main.getInstance().getDataFolder().getPath() + "/tutorials.yml"));
    if (!path.exists()) {
      YamlUtil.loadResource("tutorials.yml");
    }
    this.config = YamlConfiguration.loadConfiguration(path);
    fullTutorial = config.getStringList("full-tutorial");
    partialTutorial = config.getStringList("partial-tutorial");
    Main.log(fullTutorial.toString());
    Main.log(partialTutorial.toString());
  }

  public List<String> getTutorial(Plan plan) {
    switch (plan) {
      case FULL_TUTORIAL -> {
        return fullTutorial;
      }
      case PARTIAL_TUTORIAL -> {
        return partialTutorial;
      }
    }
    return null;
  }

  public List<String> getFullTutorial() {
    return fullTutorial;
  }

  public void setFullTutorial(List<String> fullTutorial) {
    config.set("full-tutorial", fullTutorial);
    try {
      config.save(path);
    } catch (IOException e) {
      Bukkit.getLogger().warning("Error trying to save tutorials.yml config.");
    }
  }

  public List<String> getPartialTutorial() {
    return partialTutorial;
  }

  public void setPartialTutorial(List<String> partialTutorial) {
    config.set("partial-tutorial", partialTutorial);
    try {
      config.save(path);
    } catch (IOException e) {
      Bukkit.getLogger().warning("Error trying to save tutorials.yml config.");
    }
  }
}

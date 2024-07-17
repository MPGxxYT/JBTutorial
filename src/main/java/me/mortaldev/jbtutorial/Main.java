package me.mortaldev.jbtutorial;

import co.aikar.commands.PaperCommandManager;
import java.util.HashSet;

import me.mortaldev.menuapi.GUIListener;
import me.mortaldev.menuapi.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

  private static final String LABEL = "JBTutorial";
  static Main instance;
  static HashSet<String> dependencies =
      new HashSet<>() {
        {
          add("Skript");
        }
      };
  static PaperCommandManager commandManager;
  static GUIManager guiManager;

  public static Main getInstance() {
    return instance;
  }

  public static String getLabel() {
    return LABEL;
  }

  public static GUIManager getGuiManager() {
    return guiManager;
  }

  public static void log(String message) {
    Bukkit.getLogger().info("[" + getLabel() + "] " + message);
  }

  @Override
  public void onEnable() {
    instance = this;
    commandManager = new PaperCommandManager(this);

    // DATA FOLDER

    if (!getDataFolder().exists()) {
      getDataFolder().mkdir();
    }

    // DEPENDENCIES

    for (String plugin : dependencies) {
      if (Bukkit.getPluginManager().getPlugin(plugin) == null) {
        getLogger().warning("Could not find " + plugin + "! This plugin is required.");
        Bukkit.getPluginManager().disablePlugin(this);
        return;
      }
    }

    // CONFIGS
    //    mainConfig = new MainConfig();

    // Managers (Loading data)
    //    GangManager.loadGangDataList();

    // GUI Manager
    guiManager = new GUIManager();
    GUIListener guiListener = new GUIListener(guiManager);
    Bukkit.getPluginManager().registerEvents(guiListener, this);

    // Events

    //    getServer().getPluginManager().registerEvents(new OnGangCommand(), this);

    // COMMANDS

    //    commandManager.registerCommand(new LoreCommand());

    getLogger().info(LABEL + " Enabled");
  }

  @Override
  public void onDisable() {
    getLogger().info(LABEL + " Disabled");
  }
}

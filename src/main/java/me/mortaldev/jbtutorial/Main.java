package me.mortaldev.jbtutorial;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import co.aikar.commands.PaperCommandManager;
import java.io.IOException;
import java.util.HashSet;
import me.mortaldev.jbtutorial.commands.TutorialCommand;
import me.mortaldev.jbtutorial.listeners.*;
import me.mortaldev.jbtutorial.modules.book.BookManager;
import me.mortaldev.menuapi.GUIListener;
import me.mortaldev.menuapi.GUIManager;
import net.brcdev.shopgui.ShopGuiPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

  private static final String LABEL = "JBTutorial";
  static Main instance;
  static HashSet<String> dependencies = new HashSet<>();
  static HashSet<String> softDepend =
      new HashSet<>() {
        {
          add("Skript");
          add("ShopGUIPlus");
        }
      };
  static HashSet<String> enabledDependencies = new HashSet<>();
  static PaperCommandManager commandManager;
  static GUIManager guiManager;
  private ShopGuiPlugin shopGuiPlugin;

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

  public static HashSet<String> getEnabledDependencies() {
    return enabledDependencies;
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
    for (String plugin : softDepend) {
      if (Bukkit.getPluginManager().getPlugin(plugin) != null) {
        enabledDependencies.add(plugin);
      }
    }

    // CONFIGS
    // mainConfig = new MainConfig();

    // Managers (Loading data)
    BookManager.getInstance().loadBooks();
    // GangManager.loadGangDataList();

    // GUI Manager
    guiManager = new GUIManager();
    GUIListener guiListener = new GUIListener(guiManager);
    Bukkit.getPluginManager().registerEvents(guiListener, this);

    // Events

    if (enabledDependencies.contains("ShopGUIPlus")) {
      getServer().getPluginManager().registerEvents(new ShopGuiStartup(), this);
      getServer().getPluginManager().registerEvents(new ShopGuiListener(), this);
    }

    getServer().getPluginManager().registerEvents(new CommandListener(), this);
    getServer().getPluginManager().registerEvents(new MineListener(), this);
    getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);

    // COMMANDS

    commandManager.registerCommand(new TutorialCommand());

    SkriptAddon addon = Skript.registerAddon(this);
    try {
      addon.loadClasses("me.mortaldev.jbtutorial.register", "effects");
    } catch (IOException e) {
      e.printStackTrace();
    }

    getLogger().info(LABEL + " Enabled");
  }

  public ShopGuiPlugin getShopGuiPlugin() {
    return shopGuiPlugin;
  }

  public void setShopGuiPlugin(ShopGuiPlugin shopGuiPlugin) {
    this.shopGuiPlugin = shopGuiPlugin;
  }

  @Override
  public void onDisable() {
    getLogger().info(LABEL + " Disabled");
  }
}

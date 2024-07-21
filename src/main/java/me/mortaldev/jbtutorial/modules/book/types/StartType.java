package me.mortaldev.jbtutorial.modules.book.types;

import me.mortaldev.jbtutorial.Main;
import me.mortaldev.jbtutorial.modules.book.Book;
import me.mortaldev.jbtutorial.modules.profile.Profile;
import me.mortaldev.jbtutorial.modules.profile.ProfileManager;
import me.mortaldev.jbtutorial.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public enum StartType {
  COMMAND {
    @Override
    public void run(Player player, String data) {
      executeCommand(player, data, false);
    }
  },
  FIRST_COMMAND {
    @Override
    public void run(Player player, String data) {
      Profile profile = ProfileManager.getInstance().getProfile(player.getUniqueId());
      Book activeBook = profile.getActiveBook();
      if (!profile.getStartedBooks().contains(activeBook)) {
        executeCommand(player, data, false);
      }
    }
  },
  PLAYER_COMMAND {
    @Override
    public void run(Player player, String data) {
      executeCommand(player, data, true);
    }
  },
  FIRST_PLAYER_COMMAND {
    @Override
    public void run(Player player, String data) {
      Profile profile = ProfileManager.getInstance().getProfile(player.getUniqueId());
      Book activeBook = profile.getActiveBook();
      if (!profile.getStartedBooks().contains(activeBook)) {
        executeCommand(player, data, true);
      }
    }
  },
  GIVE {
    @Override
    public void run(Player player, String data) {
      String[] split = data.split(" of ");
      int amount = Integer.parseInt(split[0]);
      String blockType = split[1];
      giveItem(player, blockType, amount);
    }
  },
  FIRST_GIVE {
    @Override
    public void run(Player player, String data) {
      Profile profile = ProfileManager.getInstance().getProfile(player.getUniqueId());
      Book activeBook = profile.getActiveBook();
      if (!profile.getStartedBooks().contains(activeBook)) {
        String[] split = data.split(" of ");
        int amount = Integer.parseInt(split[0]);
        String blockType = split[1];
        giveItem(player, blockType, amount);
      }
    }
  };

  private static void executeCommand(Player player, String data, boolean asPlayer) {
    data = data.replaceAll("/", "");
    String updatedData = data.replaceAll("%player%", player.getName());
    Main.log("Executing command: " + data + " to " + updatedData + " " + asPlayer);
    if (asPlayer) {
      Bukkit.getServer().dispatchCommand(player, updatedData);
    } else {
      Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), updatedData);
    }
  }

  private static void giveItem(Player player, String blockType, int amount) {
    try {
      ItemStack item = new ItemStack(Material.valueOf(blockType));
      item.setAmount(amount);
      if (Utils.canPlayerHold(item, player)) {
        player.getInventory().addItem(item);
      } else {
        player.getWorld().dropItem(player.getLocation(), item);
      }
    } catch (IllegalArgumentException e) {
      Main.log("FAILED TO GIVE ITEM: " + amount + " of " + blockType);
    }
  }

  public abstract void run(Player player, String data);
}

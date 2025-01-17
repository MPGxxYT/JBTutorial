package me.mortaldev.jbtutorial.utils;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Utils {

  public static String itemName(ItemStack itemStack){
    String name = itemStack.getType().getKey().getKey().replaceAll("_", " ").toLowerCase();

    String[] strings = name.split(" ");
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < strings.length; i++) {
      String string = strings[i];
      if (string.length() > 1) {
        string = string.substring(0, 1).toUpperCase() + string.substring(1);
        if (i+1 < strings.length) {
          stringBuilder.append(string).append(" ");
        } else {
          stringBuilder.append(string);
        }
      }
    }
    return stringBuilder.toString();
  }

  public static boolean canPlayerHold(ItemStack item, Player player) {
    int i = amountCanHold(item, player);
    return i > 0;
  }

  public static int amountCanHold(ItemStack item, Player player) {
    ItemStack[] storageContents = player.getInventory().getStorageContents();
    int airAmount = 36 - storageContents.length;
    if (item.getType() == Material.AIR) {
      return airAmount;
    }
    int maxStackSize = item.getType().getMaxStackSize();
    int itemTotal = airAmount * maxStackSize;
    for (ItemStack inventoryItem : storageContents) {
      if (inventoryItem == null || !inventoryItem.asOne().equals(item.asOne())
          || inventoryItem.getAmount() >= maxStackSize) {
        continue;
      }
      int adjustAmount = maxStackSize - inventoryItem.getAmount();
      itemTotal += adjustAmount;
    }
    return itemTotal;
  }

  public static List<String> splitStringByWordLength(String string, int length) {
    String[] wordArray = string.split(" ");
    int currentLength = 0;
    StringBuilder currentString = new StringBuilder();
    List<String> returnList = new ArrayList<>();
    for (String word : wordArray) {
      currentLength += word.length();
      currentString.append(word).append(" ");
      if (currentLength >= length) {
        returnList.add(currentString.toString());
        currentString = new StringBuilder();
        currentLength = 0;
      }
    }
    if (currentLength > 0) {
      returnList.add(currentString.toString());
    }
    return returnList;
  }
}

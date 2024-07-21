package me.mortaldev.jbtutorial.listeners;

import me.mortaldev.jbtutorial.modules.book.BookManager;
import me.mortaldev.jbtutorial.modules.book.types.ActionType;
import me.mortaldev.jbtutorial.utils.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {

  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    ItemStack currentItem = event.getCurrentItem();
    if (currentItem == null) {
      return;
    }
    String dataString;
    if (!currentItem.hasItemMeta() || !currentItem.getItemMeta().hasDisplayName()) {
      dataString = currentItem.getType().toString();
    } else {
      String name = TextUtil.deformat(currentItem.getItemMeta().displayName());
      dataString = currentItem.getType() + " named \"" + name + "\"";
    }
    BookManager.getInstance()
        .performAction(ActionType.INVENTORY_CLICK, dataString, (Player) event.getWhoClicked());
  }
}

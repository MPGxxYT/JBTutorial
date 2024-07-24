package me.mortaldev.jbtutorial.listeners;

import me.mortaldev.jbtutorial.Main;
import me.mortaldev.jbtutorial.modules.book.BookManager;
import me.mortaldev.jbtutorial.modules.book.types.ActionType;
import net.brcdev.shopgui.event.ShopPreTransactionEvent;
import net.brcdev.shopgui.shop.ShopManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ShopGuiListener implements Listener {

  @EventHandler
  public void onSell(ShopPreTransactionEvent event) {
    if (Main.getInstance().getShopGuiPlugin() == null) {
      return;
    }
    if (event.isCancelled()) {
      return;
    }
    if (event.getShopAction().equals(ShopManager.ShopAction.BUY)) {
      return;
    }
    String dataString = event.getAmount() + " of " + event.getShopItem().getItem().getType();
    BookManager.getInstance().performAction(ActionType.SELL, dataString, event.getPlayer());
  }
}

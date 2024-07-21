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
    Main.log(event.getAmount() + " | " + event.getShopItem().getItem());
    if (Main.getInstance().getShopGuiPlugin() == null) {
      return;
    }
    Main.log("1");
    if (event.isCancelled()) {
      return;
    }
    Main.log("2" + " " + event.getShopAction());
    if (event.getShopAction().equals(ShopManager.ShopAction.BUY)) {
      return;
    }
    Main.log("3");
    String dataString = event.getAmount() + " of " + event.getShopItem().getItem().getType();
    BookManager.getInstance().performAction(ActionType.SELL, dataString, event.getPlayer());
  }
}

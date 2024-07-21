package me.mortaldev.jbtutorial.listeners;

import me.mortaldev.jbtutorial.Main;
import net.brcdev.shopgui.ShopGuiPlusApi;
import net.brcdev.shopgui.event.ShopGUIPlusPostEnableEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ShopGuiStartup implements Listener {

  @EventHandler
  public void onShopGuiEnable(ShopGUIPlusPostEnableEvent event) {
    Main.log("Connected to ShopGUIPlus");
    Main.getInstance().setShopGuiPlugin(ShopGuiPlusApi.getPlugin());
  }
}

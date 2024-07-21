package me.mortaldev.jbtutorial.listeners;

import me.mortaldev.jbtutorial.modules.book.BookManager;
import me.mortaldev.jbtutorial.modules.book.types.ActionType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class MineListener implements Listener {

  @EventHandler
  public void onMine(BlockBreakEvent event) {
    if (event.isCancelled()) {
      return;
    }
    String string = event.getBlock().getType().toString();
    BookManager.getInstance().performAction(ActionType.MINE, string, event.getPlayer());
  }
}

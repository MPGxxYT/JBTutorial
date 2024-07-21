package me.mortaldev.jbtutorial.listeners;

import me.mortaldev.jbtutorial.modules.book.BookManager;
import me.mortaldev.jbtutorial.modules.book.types.ActionType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements Listener {

  @EventHandler
  public void onCommand(PlayerCommandPreprocessEvent event) {
    BookManager.getInstance()
        .performAction(ActionType.COMMAND, event.getMessage(), event.getPlayer());
  }
}

package me.mortaldev.jbtutorial.listeners;

import me.mortaldev.jbtutorial.modules.book.Book;
import me.mortaldev.jbtutorial.modules.book.BookManager;
import me.mortaldev.jbtutorial.modules.profile.ProfileManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    Book activeBook = ProfileManager.getInstance().getProfile(player.getUniqueId()).getActiveBook();
    if (activeBook != null) {
      BookManager.getInstance().cancelBook(player);
    }
  }
}

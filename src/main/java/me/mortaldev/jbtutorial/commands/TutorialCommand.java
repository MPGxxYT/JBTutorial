package me.mortaldev.jbtutorial.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import me.mortaldev.jbtutorial.Main;
import me.mortaldev.jbtutorial.menus.MainTutorialMenu;
import me.mortaldev.jbtutorial.menus.manage.ViewBooksMenu;
import me.mortaldev.jbtutorial.modules.book.BookManager;
import me.mortaldev.jbtutorial.utils.TextUtil;
import org.bukkit.entity.Player;

@CommandAlias("tutorial")
public class TutorialCommand extends BaseCommand {

  @Default
  public void openMenu(Player player) {
    Main.getGuiManager().openGUI(new MainTutorialMenu(), player);
  }

  @Subcommand("cancel")
  public void cancelBook(Player player) {
    BookManager.getInstance().cancelBook(player);
  }

  @Subcommand("skip")
  public void skipBook(Player player) {
    BookManager.getInstance().skipBook(player);
  }

  @Subcommand("reload")
  @CommandPermission("jbtutorial.admin")
  public void reload(Player player) {
    Main.log("&7Reloading books & config...");
    player.sendMessage(TextUtil.format("&7Reloading books & config."));
    Main.getTutorialConfig().load();
    BookManager.getInstance().loadBooks();
  }

  @Subcommand("manage")
  @CommandPermission("jbtutorial.admin")
  public void manageBooks(Player player) {
    Main.getGuiManager().openGUI(new ViewBooksMenu(), player);
  }
}

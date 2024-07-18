package me.mortaldev.jbtutorial.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import me.mortaldev.jbtutorial.Main;
import me.mortaldev.jbtutorial.menus.MainTutorialMenu;
import org.bukkit.entity.Player;

@CommandAlias("tutorial")
public class TutorialCommand extends BaseCommand {

  @Default
  public void openMenu(Player player) {
    Main.getGuiManager().openGUI(new MainTutorialMenu(), player);
  }

  @Subcommand("reload")
  @CommandPermission("jbtutorial.admin")
  public void reload(Player player) {}
}
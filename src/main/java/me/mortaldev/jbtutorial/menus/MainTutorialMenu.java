package me.mortaldev.jbtutorial.menus;

import me.mortaldev.jbtutorial.modules.book.Book;
import me.mortaldev.jbtutorial.utils.ItemStackHelper;
import me.mortaldev.jbtutorial.utils.TextUtil;
import me.mortaldev.menuapi.InventoryButton;
import me.mortaldev.menuapi.InventoryGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MainTutorialMenu extends InventoryGUI {

  private static final ItemStackHelper.Builder FULL_TUTORIAL_ITEM_STACK =
      ItemStackHelper.builder(Material.WRITABLE_BOOK)
          .name("&6&lFull Tutorial")
          .addLore("&7This tutorial will guide you through")
          .addLore("&7all the features of our server!")
          .addLore()
          .addLore("&eClick to begin!");

  private static final ItemStackHelper.Builder PARTIAL_TUTORIAL_ITEM_STACK =
      ItemStackHelper.builder(Material.WRITABLE_BOOK)
          .name("&6&lPartial Tutorial")
          .addLore("&7This tutorial will only go through")
          .addLore("&7the crucial parts of our server.")
          .addLore()
          .addLore("&eClick to begin!");

  private static final ItemStackHelper.Builder BOOK_ITEM_STACK =
      ItemStackHelper.builder(Material.BOOK)
          .name("&6&l<title>")
          .addLore("&7<description>")
          //      .addLore()
          //      .addLore("&e&lRewards:")
          //      .addLore("&7- &e<reward>")
          .addLore()
          .addLore("&7(Click to start)");

  @Override
  protected Inventory createInventory() {
    return Bukkit.createInventory(null, 6 * 9, TextUtil.format("New Tutorial"));
  }

  @Override
  public void decorate(Player player) {
    ItemStack orangePane =
        ItemStackHelper.builder(Material.ORANGE_STAINED_GLASS_PANE).name().build();
    for (int i = 0; i < 27; i++) {
      getInventory().setItem(i, orangePane);
    }
    ItemStack redPane = ItemStackHelper.builder(Material.RED_STAINED_GLASS_PANE).name().build();
    for (int i = 27; i < 36; i++) {
      getInventory().setItem(i, redPane);
    }
    ItemStack whitePane = ItemStackHelper.builder(Material.WHITE_STAINED_GLASS_PANE).name().build();
    for (int i = 36; i < 54; i++) {
      getInventory().setItem(i, whitePane);
    }
    addButton(12, FullTutorialButton());
    addButton(14, PartialTutorialButton());
    // Loop through books, add crucial ones with alternating direction from center (31);
    // Then loop through the non-crucial ones, following the same pattern but on both rows (49, 49)
    super.decorate(player);
  }

  private InventoryButton FullTutorialButton() {
    return new InventoryButton()
        .creator(player -> FULL_TUTORIAL_ITEM_STACK.build())
        .consumer(
            event -> {
              Player player = (Player) event.getWhoClicked();
              // initialize tutorial plan
            });
  }

  private InventoryButton PartialTutorialButton() {
    return new InventoryButton()
        .creator(player -> PARTIAL_TUTORIAL_ITEM_STACK.build())
        .consumer(
            event -> {
              Player player = (Player) event.getWhoClicked();
              // initialize tutorial plan
            });
  }

  private InventoryButton BookButton(Book book) {
    return new InventoryButton()
        .creator(player -> ItemStackHelper.builder(Material.BOOK).build())
        .consumer(
            event -> {
              Player player = (Player) event.getWhoClicked();
            });
  }
}

package me.mortaldev.jbtutorial.menus.manage;

import me.mortaldev.jbtutorial.Main;
import me.mortaldev.jbtutorial.modules.book.Book;
import me.mortaldev.jbtutorial.modules.book.BookManager;
import me.mortaldev.jbtutorial.utils.ItemStackHelper;
import me.mortaldev.jbtutorial.utils.TextUtil;
import me.mortaldev.menuapi.InventoryButton;
import me.mortaldev.menuapi.InventoryGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ViewBooksMenu extends InventoryGUI {

  @Override
  protected Inventory createInventory() {
    return Bukkit.createInventory(null, 9 * 6, TextUtil.format("Manage Books"));
  }

  @Override
  public void decorate(Player player) {
    ItemStack whiteStainedGlassPane = ItemStackHelper.builder(Material.WHITE_STAINED_GLASS_PANE).name("&7").build();
    for(int i = 45; i < 54; i++) {
      getInventory().setItem(i, whiteStainedGlassPane);
    }
    List<Book> books = BookManager.getInstance().getBooks();
    int i = 0;
    for (Book book : books) {
      addButton(i, BookButton(book));
      i++;
    }
    super.decorate(player);
  }

  private InventoryButton BookButton(Book book) {
    return new InventoryButton()
        .creator(
            player -> {
              ItemStackHelper.Builder bookItem = ItemStackHelper.builder(book.getBookDisplay());
              bookItem.addLore().addLore("&7[Click to edit rewards]");
              return bookItem.build();
            })
        .consumer(
            event -> {
              Player player = (Player) event.getWhoClicked();
              Main.getGuiManager().openGUI(new EditBookRewardsMenu(book), player);
            });
  }

}

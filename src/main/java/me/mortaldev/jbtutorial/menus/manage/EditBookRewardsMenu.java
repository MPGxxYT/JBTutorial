package me.mortaldev.jbtutorial.menus.manage;

import me.mortaldev.jbtutorial.Main;
import me.mortaldev.jbtutorial.modules.book.Book;
import me.mortaldev.jbtutorial.modules.book.BookReader;
import me.mortaldev.jbtutorial.utils.ItemStackHelper;
import me.mortaldev.jbtutorial.utils.NBTUtil;
import me.mortaldev.jbtutorial.utils.TextUtil;
import me.mortaldev.menuapi.InventoryButton;
import me.mortaldev.menuapi.InventoryGUI;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class EditBookRewardsMenu extends InventoryGUI {

  private final Book book;

  @Override
  protected Inventory createInventory() {
    return Bukkit.createInventory(null, 9 * 6, TextUtil.format("Edit Book Rewards"));
  }

  public EditBookRewardsMenu(Book book) {
    this.book = book;
    allowBottomInventoryClick(true);
  }

  @Override
  public void decorate(Player player) {
    ItemStack whiteStainedGlassPane = ItemStackHelper.builder(Material.WHITE_STAINED_GLASS_PANE).name("&7").build();
    for(int i = 45; i < 54; i++) {
      getInventory().setItem(i, whiteStainedGlassPane);
    }
    addButton(45, BackButton());
    addButton(49, AddReward());
    int i = 0;
    for (ItemStack reward : book.getRewards()) {
      addButton(i, RewardButton(reward));
      i++;
    }
    super.decorate(player);
  }

  private InventoryButton RewardButton(ItemStack itemStack) {
    return new InventoryButton()
        .creator(
            player -> {
              if (NBTUtil.hasNBT(itemStack, "commandItem")) {
                return ItemStackHelper.builder(itemStack.clone()).addLore().addLore("&7Command: "+NBTUtil.getNBT(itemStack, "commandItem")).addLore().addLore("&7[Left-Click to convert to normal item]").addLore("&7[Right-Click to remove reward.").build();
              }
              return ItemStackHelper.builder(itemStack.clone()).addLore().addLore("&7[Left-Click to convert to command item]").addLore("&7[Right-Click to remove reward.").build();
            })
        .consumer(
            event -> {
              Player player = (Player) event.getWhoClicked();
              if (event.getClick().equals(ClickType.LEFT)) {
                if (NBTUtil.hasNBT(itemStack, "commandItem")) {
                  ItemStack clonedItemStack = itemStack.clone();
                  NBTUtil.removeNBT(clonedItemStack, "commandItem");
                  book.updateReward(itemStack, clonedItemStack);
                  BookReader.saveRewards(book);
                  Main.getGuiManager().openGUI(new EditBookRewardsMenu(book), player);
                } else {
                  new AnvilGUI.Builder()
                      .plugin(Main.getInstance())
                      .title("Enter Command")
                      .itemLeft(ItemStackHelper.builder(Material.BOOK).name().build())
                      .onClick(
                          (slot, snapshot) -> {
                            String command = snapshot.getText();
                            ItemStack clonedItemStack = itemStack.clone();
                            NBTUtil.addNBT(clonedItemStack, "commandItem", command);
                            book.updateReward(itemStack, clonedItemStack);
                            BookReader.saveRewards(book);
                            Main.getGuiManager().openGUI(new EditBookRewardsMenu(book), player);
                            return Collections.emptyList();
                          }
                      )
                      .open(player);
                }
              } else if (event.getClick().equals(ClickType.RIGHT)) {
                book.removeReward(itemStack);
                BookReader.saveRewards(book);
                Main.getGuiManager().openGUI(new EditBookRewardsMenu(book), player);
              }
            });
  }

  private InventoryButton BackButton() {
    return new InventoryButton()
        .creator(
            player ->
                ItemStackHelper.builder(Material.ARROW).name("&c&lBack").addLore("&7Click to return to previous page.").build())
        .consumer(
            event -> {
              Player player = (Player) event.getWhoClicked();
              Main.getGuiManager().openGUI(new ViewBooksMenu(), player);
            });
  }

  private InventoryButton AddReward() {
    return new InventoryButton()
        .creator(
            player ->
                ItemStackHelper.builder(Material.BUCKET)
                    .name("&6&lAdd Reward")
                    .addLore("&7Click with an item in your")
                    .addLore("&7cursor to add a reward.")
                    .build())
        .consumer(
            event -> {
              Player player = (Player) event.getWhoClicked();
              ItemStack itemOnCursor = event.getCursor();
              if (itemOnCursor == null || itemOnCursor.getType() == Material.AIR) {
                return;
              }
              book.addReward(itemOnCursor);
              BookReader.saveRewards(book);
              Main.getGuiManager().openGUI(new EditBookRewardsMenu(book), player);
            });
  }

}

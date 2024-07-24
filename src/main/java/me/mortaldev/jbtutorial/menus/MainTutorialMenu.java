package me.mortaldev.jbtutorial.menus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import me.mortaldev.jbtutorial.modules.book.Book;
import me.mortaldev.jbtutorial.modules.book.BookManager;
import me.mortaldev.jbtutorial.modules.book.Plan;
import me.mortaldev.jbtutorial.modules.profile.ProfileManager;
import me.mortaldev.jbtutorial.records.Pair;
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

  private static final int CRUCIAL_CENTER_SLOT = 31;
  private static final int NORMAL_CENTER_SLOT_1 = 40;
  private static final int NORMAL_CENTER_SLOT_2 = 49;
  // This is used to determine the amount of books on each row. <F is row 1, S is row 2>
  private static final List<Pair<Integer, Integer>> NORMAL_BOOK_ORDER =
      new ArrayList<>() {
        {
          add(new Pair<>(1, 0));
          add(new Pair<>(1, 1));
          add(new Pair<>(3, 0));
          add(new Pair<>(3, 1));
          add(new Pair<>(5, 0));
          add(new Pair<>(3, 3));
          add(new Pair<>(7, 0));
          add(new Pair<>(5, 3));
          add(new Pair<>(9, 0));
          add(new Pair<>(5, 5));
          add(new Pair<>(9, 2));
          add(new Pair<>(7, 5));
          add(new Pair<>(9, 4));
          add(new Pair<>(7, 7));
          add(new Pair<>(9, 6));
          add(new Pair<>(9, 7));
          add(new Pair<>(9, 8));
          add(new Pair<>(9, 9));
        }
      };

  @Override
  protected Inventory createInventory() {
    return Bukkit.createInventory(null, 6 * 9, TextUtil.format("New Tutorial"));
  }

  @Override
  public void decorate(Player player) {
    paintBackground();
    addButton(12, FullTutorialButton());
    addButton(14, PartialTutorialButton());

    List<Book> crucialBooks = new ArrayList<>();
    List<Book> normalBooks = new ArrayList<>();
    for (Book book : BookManager.getInstance().getBooks()) {
      if (book.isCrucial()) {
        crucialBooks.add(book);
      } else {
        normalBooks.add(book);
      }
    }
    Pair<Integer, Integer> bookOrderPair;
    addBookButtons(crucialBooks, CRUCIAL_CENTER_SLOT);
    if (NORMAL_BOOK_ORDER.size() < normalBooks.size()) {
      bookOrderPair = NORMAL_BOOK_ORDER.get(NORMAL_BOOK_ORDER.size() - 1);
    } else {
      bookOrderPair = NORMAL_BOOK_ORDER.get(normalBooks.size() - 1);
    }
    addNormalBooks(normalBooks, bookOrderPair);
    super.decorate(player);
  }

  private void addNormalBooks(List<Book> books, Pair<Integer, Integer> bookOrderPair) {
    List<Book> firstRowBooks = new ArrayList<>();
    List<Book> secondRowBooks = new ArrayList<>();
    for (int i = 0; i < books.size(); i++) {
      if (i < bookOrderPair.first()) {
        firstRowBooks.add(books.get(i));
      } else {
        secondRowBooks.add(books.get(i));
      }
    }
    addBookButtons(firstRowBooks, NORMAL_CENTER_SLOT_1);
    addBookButtons(secondRowBooks, NORMAL_CENTER_SLOT_2);
  }

  private void addBookButtons(List<Book> books, int centerSlot) {
    books.sort(Comparator.comparing(Book::getTitle));
    int adjust = (int) Math.floor((double) books.size() / 2);
    int slot = centerSlot - adjust;
    for (Book book : books) {
      addButton(slot, BookButton(book));
      slot++;
    }
  }

  private void paintBackground() {
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
  }

  private InventoryButton FullTutorialButton() {
    return new InventoryButton()
        .creator(player -> FULL_TUTORIAL_ITEM_STACK.build())
        .consumer(
            event -> {
              Player player = (Player) event.getWhoClicked();
              BookManager.getInstance().startPlan(player, Plan.FULL_TUTORIAL);
            });
  }

  private InventoryButton PartialTutorialButton() {
    return new InventoryButton()
        .creator(player -> PARTIAL_TUTORIAL_ITEM_STACK.build())
        .consumer(
            event -> {
              Player player = (Player) event.getWhoClicked();
              BookManager.getInstance().startPlan(player, Plan.PARTIAL_TUTORIAL);
            });
  }

  private InventoryButton BookButton(Book book) {
    return new InventoryButton()
        .creator(
            player -> {
              ItemStackHelper.Builder bookItem = ItemStackHelper.builder(book.getBookDisplay());
              bookItem.addLore().addLore("&7(Click to start)");
              return bookItem.build();
            })
        .consumer(
            event -> {
              Player player = (Player) event.getWhoClicked();
              ProfileManager.getInstance().getProfile(player.getUniqueId()).setBookPlan(null);
              BookManager.getInstance().startBook(player, book);
              getInventory().close();
            });
  }
}

package me.mortaldev.jbtutorial.menus;

import java.util.ArrayList;
import java.util.List;
import me.mortaldev.jbtutorial.modules.book.Book;
import me.mortaldev.jbtutorial.modules.book.BookManager;
import me.mortaldev.jbtutorial.records.Pair;
import me.mortaldev.jbtutorial.utils.ItemStackHelper;
import me.mortaldev.jbtutorial.utils.TextUtil;
import me.mortaldev.jbtutorial.utils.Utils;
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
          .name("<title>")
          .addLore("<description>")
          //      .addLore()
          //      .addLore("&e&lRewards:")
          //      .addLore("&7- &e<reward>")
          .addLore()
          .addLore("&7(Click to start)");

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
    int slot = centerSlot;
    boolean alternate = true;
    int alternateCount = 1;
    for (Book book : books) {
      addButton(slot, BookButton(book));
      if (alternate) {
        slot += alternateCount;
        alternate = false;
      } else {
        slot -= alternateCount;
        alternate = true;
      }
      alternateCount++;
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
        .creator(
            player -> { // Add the rewards part as well.
              List<String> descriptionStrings =
                  Utils.splitStringByWordLength(book.getDescription(), 25);
              ItemStackHelper.Builder bookCopy = BOOK_ITEM_STACK.clone();
              bookCopy.name("&6&l" + book.getTitle()).removeLore(0);
              for (int i = 0; i < descriptionStrings.size(); i++) {
                bookCopy.insertLore(TextUtil.format("&7" + descriptionStrings.get(i)), i);
              }
              return bookCopy.build();
            })
        .consumer(
            event -> {
              Player player = (Player) event.getWhoClicked();
              BookManager.getInstance().startBook(player, book);
              getInventory().close();
            });
  }
}

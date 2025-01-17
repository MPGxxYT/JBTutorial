package me.mortaldev.jbtutorial.modules.book;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import me.mortaldev.jbtutorial.Main;
import me.mortaldev.jbtutorial.modules.book.types.ActionType;
import me.mortaldev.jbtutorial.modules.book.types.StartType;
import me.mortaldev.jbtutorial.modules.profile.Profile;
import me.mortaldev.jbtutorial.modules.profile.ProfileManager;
import me.mortaldev.jbtutorial.records.Pair;
import me.mortaldev.jbtutorial.utils.NBTUtil;
import me.mortaldev.jbtutorial.utils.TextUtil;
import me.mortaldev.jbtutorial.utils.Utils;
import me.mortaldev.jbtutorial.utils.YamlUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BookManager {
  private static final String PATH =
      Main.getInstance().getDataFolder().getAbsolutePath() + "/books/";
  private final HashMap<String, Integer> tasks = new HashMap<>();
  private List<Book> books = new ArrayList<>();

  public static BookManager getInstance() {
    return SingletonHolder.bookManager;
  }

  public void loadBooks() {
    books = new ArrayList<>();
    File dirPath = new File(PATH);
    if (!dirPath.exists()) {
      dirPath.mkdirs();
      YamlUtil.loadResource(PATH, "exampleBook.yml");
    }
    File[] files = dirPath.listFiles();
    if (files == null) {
      Main.log("Loaded 0 book(s)");
      return;
    }
    for (File file : files) {
      if (file.isFile()) {
        books.add(BookReader.readBook(file));
      }
    }
    Main.log("Loaded " + books.size() + " book(s)");
  }

  public static String getPATH() {
    return PATH;
  }

  public List<Book> getBooks() {
    return books;
  }

  public Book getBook(String bookID) {
    for (Book book : books) {
      if (book.getId().equals(bookID)) {
        return book;
      }
    }
    return null;
  }

  public void performAction(ActionType actionType, String performData, Player player) {
//    Main.log("action = " + actionType + " | " + performData.trim());
    Profile profile = ProfileManager.getInstance().getProfile(player.getUniqueId());
    Book activeBook = profile.getActiveBook();
    if (activeBook == null) {
      return;
    }
    BookStep step = activeBook.getStepAtIndex(profile.getCurrentStep() - 1);
    if (confirmAction(actionType, performData, step.getActions(), profile)) {
      nextStep(player);
    }
  }

  private boolean confirmAction(
      ActionType actionType,
      String performData,
      List<Pair<ActionType, String>> actions,
      Profile profile) {
    boolean stop = true;
    for (Pair<ActionType, String> pair : actions) {
      if (pair.first().equals(actionType)) {
        stop = false;
        break;
      }
    }
    if (stop) {
      return false;
    }
    switch (actionType) {
      case MINE -> {
        return confirmMine(
            performData,
            actions.stream()
                .filter(pair -> pair.first() == ActionType.MINE)
                .map(Pair::second)
                .collect(Collectors.toList()),
            profile);
      }
      case COMMAND -> {
        return confirmCommand(
            performData,
            actions.stream()
                .filter(pair -> pair.first() == ActionType.COMMAND)
                .map(Pair::second)
                .collect(Collectors.toList()));
      }
      case INVENTORY_CLICK -> {
        return confirmInventoryClick(
            performData,
            actions.stream()
                .filter(pair -> pair.first() == ActionType.INVENTORY_CLICK)
                .map(Pair::second)
                .collect(Collectors.toList()));
      }
      case CALL -> {
        return confirmCall(
            performData,
            actions.stream()
                .filter(pair -> pair.first() == ActionType.CALL)
                .map(Pair::second)
                .collect(Collectors.toList()));
      }
      case SELL -> {
        return confirmSell(
            performData,
            actions.stream()
                .filter(pair -> pair.first() == ActionType.SELL)
                .map(Pair::second)
                .collect(Collectors.toList()),
            profile);
      }
    }
    return false;
  }

  private boolean confirmSell(String performData, List<String> pairData, Profile profile) {
    String[] performSplit = performData.split(" of ");
    int amountProgressed = Integer.parseInt(performSplit[0]);
    int dataTracked = profile.getDataTracked();
    for (String data : pairData) {
      String[] dataSplit = data.split(" of ");
      if (dataSplit[0].equalsIgnoreCase("any")) {
        return true;
      }
      int amountToComplete = Integer.parseInt(dataSplit[0]);
      if (dataSplit.length > 1) {
        String blockType = dataSplit[1].toUpperCase();
        if (!performSplit[1].equals(blockType) && !dataSplit[0].equalsIgnoreCase("any")) {
          return false;
        }
      }
      if ((dataTracked + amountProgressed) >= amountToComplete) {
        return true;
      }
    }
    dataTracked += amountProgressed;
    profile.setDataTracked(dataTracked);
    return false;
  }

  private boolean confirmCall(String performData, List<String> pairData) {
    for (String data : pairData) {
      if (data.equals(performData)) {
        return true;
      }
    }
    return false;
  }

  private boolean confirmMine(String performData, List<String> pairData, Profile profile) {
    int dataTracked = profile.getDataTracked();
    for (String data : pairData) {
      String[] split = data.split(" of ");
      int amountToComplete = Integer.parseInt(split[0]);
      if (split.length > 1) {
        String blockType = split[1].toUpperCase();
        if (!performData.equals(blockType)) {
          return false;
        }
      }
      if (dataTracked + 1 >= amountToComplete) {
        return true;
      }
    }
    dataTracked++;
    profile.setDataTracked(dataTracked);
    return false;
  }

  private boolean confirmInventoryClick(String performData, List<String> pairData) {
    for (String data : pairData) {
      if (performData.equals(data)) {
        return true;
      }
    }
    return false;
  }

  private boolean confirmCommand(String performData, List<String> pairData) {
    for (String data : pairData) {
      if (data.trim().equals(performData.trim())) {
        return true;
      }
    }
    return false;
  }

  public void showBook(Book book, Player player) {
    Component bookTitle = TextUtil.format("&6&l" + book.getTitle());
    List<String> descriptionStrings = Utils.splitStringByWordLength(book.getDescription(), 25);
    Component subtitle = TextUtil.format("&7" + descriptionStrings.get(0) + "...");
    player.showTitle(Title.title(TextUtil.format("&6Started ").append(bookTitle), subtitle));
    player.sendMessage("");
    player.sendMessage(TextUtil.format("&6Started tutorial: ").append(bookTitle));
    for (String descriptionString : descriptionStrings) {
      player.sendMessage(TextUtil.format("&7" + descriptionString));
    }
    player.sendMessage("");
  }

  public void showStep(BookStep step, Player player) {
    player.sendMessage("");
    if (step.isInfo()) {
      player.sendMessage(TextUtil.format("&f        [&6&lTutorial Info&f]"));
      List<String> strings = Utils.splitStringByWordLength(step.getText(), 18);
      for (String text : strings) {
        player.sendMessage(TextUtil.format("&7   " + text));
      }
    } else {
      Component text = TextUtil.format("&f" + step.getText());
      player.sendMessage(TextUtil.format("&f[&6&lTutorial&f] ").append(text));
    }
    player.sendMessage("");
    if (!tasks.containsKey(player.getUniqueId().toString())) {
      tasks.put(
          player.getUniqueId().toString(),
          Bukkit.getScheduler()
              .scheduleSyncRepeatingTask(
                  Main.getInstance(), () -> showStep(step, player), 30 * 20, 30 * 20));
    }
  }

  public void startPlan(Player player, Plan plan) {
    Plan bookPlan = ProfileManager.getInstance().getProfile(player.getUniqueId()).getBookPlan();
    if (bookPlan == plan) {
      player.sendMessage(TextUtil.format("&cYou are already doing a plan, do /tutorial cancel to cancel it."));
      return;
    }
    switch (plan) {
      case FULL_TUTORIAL -> {
        String bookID = Main.getTutorialConfig().getTutorial(Plan.FULL_TUTORIAL).get(0);
        Book book = getBook(bookID);
        ProfileManager.getInstance().getProfile(player.getUniqueId()).setBookPlan(plan);
        startBook(player, book);
      }
      case PARTIAL_TUTORIAL -> {
        String bookID = Main.getTutorialConfig().getTutorial(Plan.PARTIAL_TUTORIAL).get(0);
        Book book = getBook(bookID);
        ProfileManager.getInstance().getProfile(player.getUniqueId()).setBookPlan(plan);
        startBook(player, book);
      }
    }
  }

  public void startBook(Player player, Book book) {
    Profile profile = ProfileManager.getInstance().getProfile(player.getUniqueId());
    profile.setActiveBook(book);
    profile.setCurrentStep(1);
    profile.setDataTracked(0);
    profile.save();
    showBook(book, player);
    Bukkit.getScheduler()
        .scheduleSyncDelayedTask(
            Main.getInstance(),
            () -> {
              BookStep step = book.getStepAtIndex(0);
              showStep(step, player);
              stopRepeatingShow(player);
              for (Pair<StartType, String> action : step.getStartActions()) {
                action.first().run(player, action.second());
              }
              if (step.isInfo()) {
                int delay = step.getDelay() * 20;
                Bukkit.getScheduler()
                    .scheduleSyncDelayedTask(Main.getInstance(), () -> nextStep(player), delay);
              }
            },
            20);
  }

  public void completeBook(Player player, Book book) {
    Profile profile = ProfileManager.getInstance().getProfile(player.getUniqueId());
    profile.defaultData();
    Component completionText = TextUtil.format("&6Tutorial &l" + book.getTitle() + "&6 completed!");
    player.sendMessage(completionText);
    player.showTitle(Title.title(completionText, TextUtil.format("&fCongrats!")));
    profile.addStartedBook(book);
    if (profile.getCompletedBooks().contains(book)) {
      profile.save();
      return;
    } else {
      profile.addCompletedBook(book);
      profile.save();
    }
    List<ItemStack> rewards = book.getRewards();
    if (rewards == null || rewards.isEmpty()) {
      return;
    }
    player.sendMessage("");
    player.sendMessage(TextUtil.format("&e&lRewards:"));
    for (ItemStack reward : rewards) {
      Component displayName;
      if (reward.getItemMeta().hasDisplayName()) {
        displayName = TextUtil.format("x" + reward.getAmount() + " ").append(reward.getItemMeta().displayName());
      } else {
        displayName = TextUtil.format("x" + reward.getAmount() + " " + Utils.itemName(reward));
      }
      player.sendMessage(TextUtil.format("&f- &e").append(displayName));
      if (NBTUtil.hasNBT(reward, "commandItem")) {
        String command = NBTUtil.getNBT(reward, "commandItem");
        if (command == null) {
          continue;
        }
        command = command.replaceFirst("/", "");
        command = command.replaceAll("%player%", player.getName());
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
      } else {
        if (Utils.canPlayerHold(reward, player)) {
          player.getInventory().addItem(reward);
        } else {
          player.getWorld().dropItem(player.getLocation(), reward);
        }
      }
    }
    player.sendMessage("");
  }

  public void nextStep(Player player) {
    Profile profile = ProfileManager.getInstance().getProfile(player.getUniqueId());
    Book activeBook = profile.getActiveBook();
    Integer currentStep = profile.getCurrentStep();
    if (activeBook == null) {
      return;
    }
    stopRepeatingShow(player);
    BookStep nextStep = activeBook.getStepAtIndex(currentStep);
    if (nextStep == null) {
      completeBook(player, activeBook);
      if (profile.getBookPlan() != null) {
        continuePlan(player, profile, activeBook);
      }
      return;
    }
    profile.setCurrentStep(currentStep + 1);
    profile.setDataTracked(0);
    for (Pair<StartType, String> action : nextStep.getStartActions()) {
      action.first().run(player, action.second());
    }
    showStep(nextStep, player);
    profile.save();
    if (nextStep.isInfo()) {
      int delay = nextStep.getDelay() * 20;
      Bukkit.getScheduler()
          .scheduleSyncDelayedTask(Main.getInstance(), () -> nextStep(player), delay);
    }
  }

  private void continuePlan(Player player, Profile profile, Book activeBook) {
    List<String> tutorial = Main.getTutorialConfig().getTutorial(profile.getBookPlan());
    int nextIndex = tutorial.indexOf(activeBook.getId()) + 1;
    if (tutorial.size() >= nextIndex + 1) {
      Bukkit.getScheduler()
          .scheduleSyncDelayedTask(
              Main.getInstance(),
              () -> startBook(player, BookManager.getInstance().getBook(tutorial.get(nextIndex))),
              3 * 20);
    } else {
      Component message =
          TextUtil.format(
              "&6Tutorial Plan &l" + profile.getBookPlan().getDisplay() + "&6 completed!.");
      player.sendMessage(message);
      player.showTitle(Title.title(message, TextUtil.format("&fYIPPIE!")));
      profile.setBookPlan(null);
      profile.save();
    }
  }

  public void skipBook(Player player) {
    Profile profile = ProfileManager.getInstance().getProfile(player.getUniqueId());
    Book activeBook = profile.getActiveBook();
    if (activeBook == null) {
      return;
    }
    if (profile.getBookPlan() != null) {
      Component message =
          TextUtil.format("&fSkipping &6" + activeBook.getTitle() + "&f tutorial.");
      player.sendMessage(message);
      continuePlan(player, profile, activeBook);
    } else {
      Component message =
          TextUtil.format("&fYou cancelled &6" + activeBook.getTitle() + "&f tutorial.");
      player.sendMessage(message);
    }
    stopRepeatingShow(player);
    profile.addStartedBook(activeBook);
    profile.defaultData();
    profile.save();
  }

  public void cancelBook(Player player) {
    Profile profile = ProfileManager.getInstance().getProfile(player.getUniqueId());
    Book activeBook = profile.getActiveBook();
    if (activeBook == null) {
      return;
    }
    if (profile.getBookPlan() != null) {
      if (player.isOnline()) {
        player.sendMessage(
            TextUtil.format(
                "&fYou cancelled your tutorial plan. &6(" + profile.getBookPlan() + ")"));
      }
      profile.setBookPlan(null);
    } else {
      if (player.isOnline()) {
        player.sendMessage(
            TextUtil.format("&fYou cancelled &6" + activeBook.getTitle() + "&f tutorial."));
      }
    }
    stopRepeatingShow(player);
    profile.addStartedBook(activeBook);
    profile.defaultData();
    profile.save();
  }

  public void stopRepeatingShow(Player player) {
    if (tasks.containsKey(player.getUniqueId().toString())) {
      Bukkit.getScheduler().cancelTask(tasks.remove(player.getUniqueId().toString()));
    }
  }

  private static class SingletonHolder {
    private static final BookManager bookManager = new BookManager();
  }
}

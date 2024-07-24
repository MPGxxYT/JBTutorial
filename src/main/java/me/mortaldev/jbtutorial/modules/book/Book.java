package me.mortaldev.jbtutorial.modules.book;

import java.util.ArrayList;
import java.util.List;

import me.mortaldev.jbtutorial.utils.ItemStackHelper;
import me.mortaldev.jbtutorial.utils.TextUtil;
import me.mortaldev.jbtutorial.utils.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Book {
  private final String id;
  private final String title;
  private final String description;
  private final boolean isCrucial;
  private List<BookStep> steps;
  private List<String> rewards;

  public Book(String id, String title, String description, boolean isCrucial) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.isCrucial = isCrucial;
    this.rewards = new ArrayList<>();
    this.steps = new ArrayList<>();
  }

  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public boolean isCrucial() {
    return isCrucial;
  }

  public void addStep(BookStep step) {
    steps.add(step);
  }

  public List<BookStep> getSteps() {
    return steps;
  }

  public void setSteps(List<BookStep> steps) {
    this.steps = steps;
  }

  public BookStep getStepAtIndex(int index) {
    if (steps.size() < index + 1) {
      return null;
    }
    return steps.get(index);
  }

  public void setSerializedRewards(List<String> rewards) {
    this.rewards = rewards;
  }

  public void addReward(ItemStack itemStack) {
    String serializedReward = ItemStackHelper.serialize(itemStack);
    rewards.add(serializedReward);
  }

  public void removeReward(ItemStack itemStack) {
    String serializedReward = ItemStackHelper.serialize(itemStack);
    rewards.remove(serializedReward);
  }

  public List<ItemStack> getRewards() {
    return rewards.stream().map(ItemStackHelper::deserialize).toList();
  }

  public List<String> getRawRewards(){
    return rewards;
  }

  public Component[] getRewardsDisplay() {
    Component[] components = new Component[rewards.size()];
    int i = 0;
    for (ItemStack reward : getRewards()) {
      Component displayName;
      if (reward.getItemMeta().hasDisplayName()) {
        displayName = TextUtil.format("x" + reward.getAmount() + " ").append(reward.getItemMeta().displayName());
      } else {
        displayName = TextUtil.format("x" + reward.getAmount() + " " + Utils.itemName(reward));
      }
      components[i] = displayName;
      i++;
    }
    return components;
  }

  public ItemStack getBookDisplay() {
    List<String> descriptionStrings = Utils.splitStringByWordLength(getDescription(), 25);
    ItemStackHelper.Builder bookItem = ItemStackHelper.builder(Material.BOOK).name("&6&l" + getTitle());
    for (String string : descriptionStrings) {
      bookItem.addLore("&7"+string);
    }
    Component[] rewardsDisplay = getRewardsDisplay();
    if (rewardsDisplay.length > 0) {
      bookItem.addLore().addLore("&e&lRewards:");
      for (Component component : rewardsDisplay) {
        Component append = TextUtil.format("&7 ").append(component);
        bookItem.addLore(append);
      }
    }
    return bookItem.build();
  }

  public void setRewards(List<ItemStack> rewards) {
    List<String> list = rewards.stream().map(ItemStackHelper::serialize).toList();
    setSerializedRewards(list);
  }

  public void updateReward(ItemStack original, ItemStack replacement){
    addReward(replacement);
    removeReward(original);
  }
}

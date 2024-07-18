package me.mortaldev.jbtutorial.modules.book;

import me.mortaldev.jbtutorial.utils.ItemStackHelper;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

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
    return steps.get(index);
  }

  public void setRewards(List<String> rewards) {
    this.rewards = rewards;
  }

  public void addReward(ItemStack itemStack){
    String serializedReward = ItemStackHelper.serialize(itemStack);
    rewards.add(serializedReward);
  }

  public void removeReward(ItemStack itemStack){
    String serializedReward = ItemStackHelper.serialize(itemStack);
    rewards.remove(serializedReward);
  }
}

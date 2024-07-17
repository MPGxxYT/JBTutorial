package me.mortaldev.jbtutorial.modules.menu;

import java.util.function.Consumer;
import java.util.function.Function;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryButton {

  private Function<Player, ItemStack> iconCreator;
  private Consumer<InventoryClickEvent> eventConsumer;

  public InventoryButton creator(Function<Player, ItemStack> iconCreator) {
    this.iconCreator = iconCreator;
    return this;
  }

  public InventoryButton consumer(Consumer<InventoryClickEvent> eventConsumer) {
    this.eventConsumer = eventConsumer;
    return this;
  }

  public Consumer<InventoryClickEvent> getEventConsumer() {
    return this.eventConsumer;
  }

  public Function<Player, ItemStack> getIconCreator() {
    return this.iconCreator;
  }
}

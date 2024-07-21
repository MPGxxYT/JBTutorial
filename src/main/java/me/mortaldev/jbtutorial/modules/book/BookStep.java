package me.mortaldev.jbtutorial.modules.book;

import java.util.ArrayList;
import java.util.List;
import me.mortaldev.jbtutorial.modules.book.types.ActionType;
import me.mortaldev.jbtutorial.modules.book.types.StartType;
import me.mortaldev.jbtutorial.records.Pair;

public class BookStep {
  private final String text;
  private boolean info;
  private int delay;
  private List<Pair<StartType, String>> startActions;
  private List<Pair<ActionType, String>> actions;

  public BookStep(String text) {
    this.text = text;
    this.info = false;
    this.delay = 3;
    this.startActions = new ArrayList<>();
    this.actions = new ArrayList<>();
  }

  public String getText() {
    return text;
  }

  public boolean isInfo() {
    return info;
  }

  public void setInfo(boolean info) {
    this.info = info;
  }

  public int getDelay() {
    return delay;
  }

  public void setDelay(int delay) {
    this.delay = delay;
  }

  public void addAction(ActionType actionType, String data) {
    actions.add(new Pair<>(actionType, data));
  }

  public Pair<ActionType, String> getActionAtIndex(int index) {
    if (actions.size() < index + 1) {
      return null;
    }
    return actions.get(index);
  }

  public List<Pair<ActionType, String>> getActions() {
    return actions;
  }

  public void setActions(List<Pair<ActionType, String>> actions) {
    this.actions = actions;
  }

  public void addStartAction(StartType startType, String data) {
    startActions.add(new Pair<>(startType, data));
  }

  public Pair<StartType, String> getStartActionAtIndex(int index) {
    if (startActions.size() < index + 1) {
      return null;
    }
    return startActions.get(index);
  }

  public List<Pair<StartType, String>> getStartActions() {
    return startActions;
  }

  public void setStartActions(List<Pair<StartType, String>> startActions) {
    this.startActions = startActions;
  }
}

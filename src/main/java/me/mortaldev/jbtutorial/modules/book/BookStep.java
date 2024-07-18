package me.mortaldev.jbtutorial.modules.book;

import me.mortaldev.jbtutorial.modules.book.types.ActionType;
import me.mortaldev.jbtutorial.modules.book.types.StartType;
import me.mortaldev.jbtutorial.records.Pair;

import java.util.ArrayList;
import java.util.List;

public class BookStep {
  private final String text;
  private List<Pair<StartType, String>> startActions;
  private List<Pair<ActionType, String>> actions;

  public BookStep(String text) {
    this.text = text;
    this.startActions = new ArrayList<>();
    this.actions = new ArrayList<>();
  }

  public String getText() {
    return text;
  }

  public void addAction(ActionType actionType, String data) {
    actions.add(new Pair<>(actionType, data));
  }

  public Pair<ActionType, String> getActionAtIndex(int index) {
    if (actions.size() < index+1) {
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
    if (startActions.size() < index+1) {
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

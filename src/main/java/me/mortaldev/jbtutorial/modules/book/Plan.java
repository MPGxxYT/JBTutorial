package me.mortaldev.jbtutorial.modules.book;

public enum Plan {
  FULL_TUTORIAL("Full Tutorial"),
  PARTIAL_TUTORIAL("Partial Tutorial"),
  ;

  final String display;

  Plan(String display) {
    this.display = display;
  }

  public String getDisplay() {
    return display;
  }
}

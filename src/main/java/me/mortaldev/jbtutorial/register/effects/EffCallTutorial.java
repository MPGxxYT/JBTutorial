package me.mortaldev.jbtutorial.register.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.mortaldev.jbtutorial.modules.book.BookManager;
import me.mortaldev.jbtutorial.modules.book.types.ActionType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

@SuppressWarnings("NullableProblems")
public class EffCallTutorial extends Effect {

  static {
    Skript.registerEffect(EffCallTutorial.class, "call tutorial %strings% on %player%");
  }

  private Expression<String> calls;
  private Expression<Player> player;

  @Override
  protected void execute(Event event) {
    for (String s : calls.getArray(event)) {
      Player playerSingle = player.getSingle(event);
      if (playerSingle == null) {
        return;
      }
      BookManager.getInstance().performAction(ActionType.CALL, s, playerSingle);
    }
  }

  @Override
  public String toString(Event event, boolean b) {
    return "tutorial call " + calls.toString(event, b);
  }

  @Override
  public boolean init(
      Expression<?>[] expressions,
      int matchedPattern,
      Kleenean isDelayed,
      SkriptParser.ParseResult parseResult) {
    this.calls = (Expression<String>) expressions[0];
    this.player = (Expression<Player>) expressions[1];
    return true;
  }
}

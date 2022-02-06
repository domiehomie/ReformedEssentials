package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SudoCommand extends CommandListener {

  @Inject
  private Messaging messaging;

  public SudoCommand() {
    super(new CommandBuilder()
       .setName("sudo")
       .setDescription("Execute any command from another player. Append with c: for chat.")
       .setUsage("/sudo <player> <command|c:message>")
       .setAliases("doas")
       .setPermissions()
       .createCommand()
    );
  }

  @Override
  public boolean exec(CommandSender sender, Command cmd, String label, String[] args) {
    if (args.length < 2) {
      sender.sendMessage(messaging.errorMessage("You must provide a player and a command/message."));
      return true;
    }

    Player target = Bukkit.getPlayer(args[0]);
    if (target == null) {
      sender.sendMessage(messaging.errorMessage("Player not found."));
      return true;
    }

    StringBuilder messageBuilder = new StringBuilder();
    Arrays.stream(args).skip(1).forEach(arg -> messageBuilder.append(arg).append(" "));
    String message = messageBuilder.toString();

    if (message.startsWith("c:")) {
      message = message.replace("c:", "");
      target.chat(message);
      return true;
    }

    Bukkit.dispatchCommand(target, message);
    
    return true;
  }
}


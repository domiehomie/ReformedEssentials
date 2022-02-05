package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ExtinguishCommand extends CommandListener {

  @Inject
  private Messaging messaging;

  public ExtinguishCommand() {
    super(new CommandBuilder()
       .setName("extinguish")
       .setDescription("Extinguishes a player.")
       .setUsage("/extinguish [player]")
       .setAliases("ext")
       .setPermissions()
       .createCommand()
    );
  }

  @Override
  public boolean exec(CommandSender sender, Command cmd, String label, String[] args) {
    if (!(sender instanceof Player player)) {
      sender.sendMessage(messaging.errorMessage("Only players can execute this command."));
      return true;
    }

    Player target = args.length == 0 ? player : Bukkit.getPlayer(args[0]);
    if (target == null) {
      player.sendMessage(messaging.errorMessage("Player not found."));
      return true;
    }

    target.setFireTicks(0);

    player.sendMessage(messaging.successMessage("The player has been successfully extinguished."));

    return true;
  }
}


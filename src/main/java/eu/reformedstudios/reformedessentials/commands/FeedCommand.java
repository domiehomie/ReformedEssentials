package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FeedCommand extends CommandListener {

  @Inject
  private Messaging messaging;

  public FeedCommand() {
    super(new CommandBuilder()
       .setName("feed")
       .setDescription("Restores your hunger.")
       .setUsage("/feed <player>")
       .setAliases()
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
    if (args.length == 0) {
      player.setFoodLevel(20);
      player.setSaturation(20);
      player.sendMessage(messaging.successMessage("You fed yourself."));
      return true;
    }

    Player target = Bukkit.getPlayer(args[0]);
    if (target == null) {
      player.sendMessage(messaging.errorMessage("Player not found."));
      return true;
    }
    target.setFoodLevel(20);
    target.setSaturation(20);
    player.sendMessage(messaging.successMessage("You fed " + target.getName() + "."));

    return true;
  }
}


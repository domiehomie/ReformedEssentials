package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CompassCommand extends CommandListener {

  @Inject
  private Messaging messaging;

  public CompassCommand() {
    super(new CommandBuilder()
       .setName("compass")
       .setDescription("Shows you where you are looking.")
       .setUsage("/compass")
       .setAliases("direction")
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


    player.sendMessage(
       messaging.normalMessage("Your direction: ")
          .append(messaging.simpleGradient(String.valueOf(player.getLocation().getPitch())))
          .append(Component.newline())
          .append(messaging.normalMessage("Which is "))
          .append(messaging.simpleGradient(player.getFacing().name().toLowerCase()))
    );
    
    return true;
  }
}


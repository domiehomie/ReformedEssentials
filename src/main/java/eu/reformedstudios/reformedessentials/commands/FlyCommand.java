package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand extends CommandListener {

  @Inject
  private Messaging messaging;

  public FlyCommand() {
    super(new CommandBuilder()
       .setName("fly")
       .setDescription("Enables you to fly.")
       .setUsage("/fly")
       .setAliases("flight")
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


    player.setAllowFlight(!player.getAllowFlight());
    player.sendMessage(messaging.successMessage("Toggled flight."));

    return true;
  }
}


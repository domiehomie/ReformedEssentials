package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WeatherCommand extends CommandListener {

  @Inject
  private Messaging messaging;

  public WeatherCommand() {
    super(new CommandBuilder()
       .setName("weather")
       .setDescription("Changes the weather.")
       .setAliases()
       .setUsage("/weather <storm|stormy|sun|sunny>")
       .createCommand()
    );
  }

  @Override
  public boolean exec(CommandSender sender, Command cmd, String label, String[] args) {
    if (!(sender instanceof Player player)) {
      sender.sendMessage(messaging.errorMessage("Only players can execute this command."));
      return true;
    }

    if (args.length < 1) {
      sender.sendMessage(messaging.errorMessage("You must provide a type of weather."));
      return true;
    }

    switch (args[0]) {
      case "sun", "sunny" -> {
        player.getWorld().setStorm(false);
        player.sendMessage(messaging.successMessage("Set weather to sunny."));
      }
      case "storm", "stormy" -> {
        player.getWorld().setStorm(true);
        player.sendMessage(messaging.successMessage("Set weather to stormy."));
      }
    }

    return true;
  }
}

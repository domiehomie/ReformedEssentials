package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingCommand extends CommandListener {

   @Inject
   private Messaging messaging;

   public PingCommand() {
      super(new CommandBuilder()
         .setName("ping")
         .setDescription("Displays yours, or someone else's ping.")
         .setUsage("/ping [player]")
         .setAliases()
         .setPermissions("re.ping")
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
         player.sendMessage(
            messaging.normalMessage("Ping: ")
               .append(messaging.simpleGradient(String.valueOf(player.getPing())))
         );
         return true;
      }

      Player target = Bukkit.getPlayer(args[0]);
      if (target == null) {
         player.sendMessage(messaging.errorMessage("Player not found."));
         return true;
      }

      player.sendMessage(
         messaging.normalMessage("Ping: ")
            .append(messaging.simpleGradient(String.valueOf(target.getPing())))
      );

      return true;
   }
}


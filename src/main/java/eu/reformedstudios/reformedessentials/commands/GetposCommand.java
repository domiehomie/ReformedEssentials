package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import org.apache.commons.math3.util.Precision;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetposCommand extends CommandListener {

   @Inject
   private Messaging messaging;

   public GetposCommand() {
      super(new CommandBuilder()
         .setName("getpos")
         .setDescription("Gives you yours or someone else's position.")
         .setUsage("/getpos [player]")
         .setAliases("pos")
         .setPermissions("re.getpos")
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
            messaging.normalMessage("Your current position: ")
               .append(messaging.normalMessageNoPrefix("X: "))
               .append(messaging.simpleGradient(String.valueOf(Precision.round(player.getLocation().getX(), 1))))
               .append(messaging.normalMessageNoPrefix(" Y: "))
               .append(messaging.simpleGradient(String.valueOf(Precision.round(player.getLocation().getY(), 1))))
               .append(messaging.normalMessageNoPrefix(" Z: "))
               .append(messaging.simpleGradient(String.valueOf(Precision.round(player.getLocation().getZ(), 1))))
         );
         return true;
      }

      Player target = Bukkit.getPlayer(args[0]);
      if (target == null) {
         player.sendMessage(messaging.errorMessage("Player not found."));
         return true;
      }

      player.sendMessage(
         messaging.normalMessage("Current position of " + target.getName() + ": ")
            .append(messaging.normalMessageNoPrefix("X: "))
            .append(messaging.simpleGradient(String.valueOf(Precision.round(target.getLocation().getX(), 1))))
            .append(messaging.normalMessageNoPrefix(" Y: "))
            .append(messaging.simpleGradient(String.valueOf(Precision.round(target.getLocation().getY(), 1))))
            .append(messaging.normalMessageNoPrefix(" Z: "))
            .append(messaging.simpleGradient(String.valueOf(Precision.round(target.getLocation().getZ(), 1))))
      );

      return true;
   }
}


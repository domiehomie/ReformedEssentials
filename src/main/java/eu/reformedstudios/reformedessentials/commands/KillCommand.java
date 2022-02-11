package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KillCommand extends CommandListener {

   @Inject
   private Messaging messaging;

   public KillCommand() {
      super(new CommandBuilder()
         .setName("kill")
         .setDescription("Kills a player.")
         .setUsage("/kill [player]")
         .setAliases()
         .setPermissions("re.kill")
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
         player.setHealth(0);
         player.sendMessage(messaging.successMessage("Successfully killed.. yourself?"));
         return true;
      }

      Player target = Bukkit.getPlayer(args[0]);
      if (target == null) {
         player.sendMessage(messaging.errorMessage("Player not found."));
         return true;
      }

      target.setHealth(0);
      player.sendMessage(messaging.successMessage("Successfully killed " + target.getName() + "."));
      return true;
   }
}


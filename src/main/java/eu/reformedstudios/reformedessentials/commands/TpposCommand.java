package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpposCommand extends CommandListener {

   @Inject
   private Messaging messaging;

   public TpposCommand() {
      super(new CommandBuilder()
         .setName("tppos")
         .setDescription("Teleports you to a specific position.")
         .setUsage("/tppos <x> <y> <z>")
         .setAliases()
         .setPermissions("re.tppos")
         .createCommand());
   }

   @Override
   public boolean exec(CommandSender sender, Command cmd, String label, String[] args) {
      if (!(sender instanceof Player player)) {
         sender.sendMessage(messaging.errorMessage("Only players can execute this command."));
         return true;
      }

      if (args.length < 3) {
         sender.sendMessage(messaging.errorMessage("Incorrect usage."));
         return true;
      }

      try {
         int x = Integer.parseInt(args[0]);
         int y = Integer.parseInt(args[1]);
         int z = Integer.parseInt(args[2]);

         Location location = new Location(player.getWorld(), x, y, z);
         player.teleport(location);
      } catch (NumberFormatException exception) {
         sender.sendMessage(messaging.errorMessage("You must provide numbers as coordinates."));
      }
      return true;
   }
}

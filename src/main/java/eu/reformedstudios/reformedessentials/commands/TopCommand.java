package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TopCommand extends CommandListener {

   @Inject
   private Messaging messaging;

   public TopCommand() {
      super(new CommandBuilder()
         .setName("top")
         .setDescription("Teleports you to the highest point.")
         .setUsage("/top")
         .setAliases()
         .setPermissions("re.top")
         .createCommand());
   }

   @Override
   public boolean exec(CommandSender sender, Command cmd, String label, String[] args) {
      if (!(sender instanceof Player player)) {
         sender.sendMessage(messaging.errorMessage("Only players can execute this command."));
         return true;
      }

      Block highestBlock = player.getWorld().getHighestBlockAt(player.getLocation());
      Location targetLocation = highestBlock.getLocation().add(0, 1, 0).toCenterLocation();
      targetLocation.setPitch(player.getLocation().getPitch());
      targetLocation.setYaw(player.getLocation().getYaw());

      player.teleport(targetLocation);
      player.sendMessage(messaging.successMessage("Woosh!"));

      return true;
   }
}
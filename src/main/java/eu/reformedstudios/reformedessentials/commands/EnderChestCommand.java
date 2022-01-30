package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import eu.reformedstudios.reformedcoreapi.database.IDatabase;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnderChestCommand extends CommandListener {

   @Inject
   private Messaging messaging;
   @Inject
   private IDatabase database;

   public EnderChestCommand() {
      super(new CommandBuilder()
         .setName("enderchest")
         .setDescription("Let's you see your own or someone else's enderchest.")
         .setUsage("/enderchest [player]")
         .setAliases("endersee", "openender", "echest", "ec")
         .setPermissions()
         .createCommand());
   }

   @Override
   public boolean exec(CommandSender sender, Command cmd, String label, String[] args) {
      if (!(sender instanceof Player player)) {
         sender.sendMessage(messaging.errorMessage("Only players can execute this command."));
         return true;
      }

      if (args.length == 0) {
         player.openInventory(player.getEnderChest());
      } else {
         var p = Bukkit.getPlayer(args[0]);
         if (p == null) {
            sender.sendMessage(messaging.errorMessage("That player is not online."));
            return true;
         }
         player.openInventory(p.getEnderChest());
      }


      return true;
   }
}

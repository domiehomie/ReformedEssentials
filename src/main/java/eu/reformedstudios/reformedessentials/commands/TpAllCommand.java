package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpAllCommand extends CommandListener {

   @Inject
   private Messaging messaging;

   public TpAllCommand() {
      super(new CommandBuilder()
         .setName("tpall")
         .setDescription("Teleports all players to you or another player.")
         .setUsage("/tpall [player]")
         .setAliases()
         .setPermissions("re.tpall")
         .createCommand());
   }

   @Override
   public boolean exec(CommandSender sender, Command cmd, String label, String[] args) {
      if (!(sender instanceof Player player)) {
         sender.sendMessage(messaging.errorMessage("Only players can execute this command."));
         return true;
      }

      if (args.length == 0) {
         player.sendMessage(messaging.successMessage("Teleporting..."));
         Bukkit.getOnlinePlayers()
            .stream()
            .filter(target -> !target.getUniqueId().equals(player.getUniqueId()))
            .forEach(target -> target.teleport(player));
         player.sendMessage(messaging.successMessage("Finished!"));
         return true;
      }
      Player target = Bukkit.getPlayer(args[0]);

      if (target == null) {
         player.sendMessage(messaging.errorMessage("Invalid player."));
         return true;
      }
      player.sendMessage(messaging.successMessage("Teleporting..."));
      Bukkit.getOnlinePlayers()
         .stream()
         .filter(p -> !p.getUniqueId().equals(target.getUniqueId()))
         .forEach(p -> p.teleport(target));
      player.sendMessage(messaging.successMessage("Finished!"));


      return true;
   }
}


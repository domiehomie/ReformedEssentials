package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCommand extends CommandListener {

   @Inject
   private Messaging messaging;

   public HealCommand() {
      super(new CommandBuilder()
         .setName("heal")
         .setDescription("Heals you.")
         .setUsage("/heal [player]")
         .setAliases("regen", "cure", "regenerate")
         .setPermissions("re.heal")
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
         player.setFireTicks(0);
         player.setHealth(20);
         player.setFoodLevel(20);
         player.setSaturation(20);
         player.sendMessage(messaging.successMessage("You healed yourself."));
         return true;
      }

      Player target = Bukkit.getPlayer(args[0]);
      if (target == null) {
         player.sendMessage(messaging.errorMessage("Player not found."));
         return true;
      }

      target.setFireTicks(0);
      target.setHealth(20);
      target.setFoodLevel(20);
      target.setSaturation(20);
      player.sendMessage(messaging.successMessage("You healed " + target.getName() + "."));

      return true;
   }
}
package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SuicideCommand extends CommandListener {

   @Inject
   private Messaging messaging;

   public SuicideCommand() {
      super(new CommandBuilder()
         .setName("suicide")
         .setDescription("Well...")
         .setUsage("/suicide")
         .setAliases("killself")
         .setPermissions("re.suicide")
         .createCommand());
   }

   @Override
   public boolean exec(CommandSender sender, Command cmd, String label, String[] args) {
      if (!(sender instanceof Player player)) {
         sender.sendMessage(messaging.errorMessage("Only players can execute this command."));
         return true;
      }

      player.setHealth(0);
      player.sendMessage(messaging.gradientMessage("You took the easy way out."));
      return true;
   }
}

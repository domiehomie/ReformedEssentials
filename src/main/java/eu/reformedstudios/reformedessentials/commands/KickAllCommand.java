package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class KickAllCommand extends CommandListener {

   @Inject
   private Messaging messaging;

   public KickAllCommand() {
      super(new CommandBuilder()
         .setName("kickall")
         .setDescription("Kicks all players from the game.")
         .setUsage("/kickall [reason]")
         .setAliases()
         .setPermissions()
         .createCommand());
   }

   @Override
   public boolean exec(CommandSender sender, Command cmd, String label, String[] args) {
      if (args.length == 0) {
         Bukkit.getOnlinePlayers().forEach(p -> p.kick(messaging.simpleGradient("You have been kicked.")));
         return true;
      }

      StringBuilder b = new StringBuilder();
      Arrays.stream(args).forEach(a -> b.append(a).append(" "));
      Bukkit.getOnlinePlayers().forEach(p -> p.kick(messaging.simpleGradient(b.toString())));

      return true;
   }


}

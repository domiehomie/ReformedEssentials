package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import dev.morphia.query.experimental.filters.Filters;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import eu.reformedstudios.reformedcoreapi.database.IDatabase;
import eu.reformedstudios.reformedessentials.ReformedEssentials;
import eu.reformedstudios.reformedessentials.entities.DbHome;
import eu.reformedstudios.reformedessentials.entities.DbPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class SetHomeCommand extends CommandListener {

   private ReformedEssentials pl;
   @Inject
   private Messaging messaging;
   @Inject
   private IDatabase database;

   public SetHomeCommand(ReformedEssentials essentials) {
      super(new CommandBuilder()
         .setName("sethome")
         .setDescription("Sets your home.")
         .setUsage("/sethome [home]")
         .setAliases()
         .setPermissions()
         .createCommand());

      this.pl = essentials;
   }

   @Override
   public boolean exec(CommandSender sender, Command cmd, String label, String[] args) {
      if (!(sender instanceof Player player)) {
         sender.sendMessage(messaging.errorMessage("Only players can execute this command."));
         return true;
      }

      DbHome home = new DbHome(player.getLocation(), args.length == 0 ? "home" : args[0]);


      Bukkit.getScheduler().runTaskAsynchronously(pl, () -> {
         database.createQuery(DbPlayer.class).filter(Filters.eq("uuid", player.getUniqueId().toString()))
            .stream()
            .findFirst()
            .ifPresent(
               p -> {
                  if (p.getHomes() == null) p.setHomes(new ArrayList<>());
                  p.getHomes().stream().filter(h -> h.getName().equals(home.getName()))
                     .findFirst().ifPresentOrElse(
                        h -> {
                           p.getHomes().remove(h);
                           p.getHomes().add(home);
                           database.save(p);
                           player.sendMessage(messaging.successMessage("Set new home at your current location."));
                        },
                        () -> {
                           p.getHomes().add(home);
                           database.save(p);
                           player.sendMessage(messaging.successMessage("Set new home at your current location."));
                        }
                     );
               });

      });

      return true;
   }
}

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
import java.util.concurrent.atomic.AtomicReference;

public class SetHomeCommand extends CommandListener {

   private final ReformedEssentials plugin;
   @Inject
   private Messaging messaging;
   @Inject
   private IDatabase database;

   public SetHomeCommand(ReformedEssentials plugin) {
      super(new CommandBuilder()
         .setName("sethome")
         .setDescription("Sets your home.")
         .setUsage("/sethome [home]")
         .setAliases()
         .setPermissions("re.sethome")
         .createCommand());

      this.plugin = plugin;
   }

   @Override
   public boolean exec(CommandSender sender, Command cmd, String label, String[] args) {
      if (!(sender instanceof Player player)) {
         sender.sendMessage(messaging.errorMessage("Only players can execute this command."));
         return true;
      }

      DbHome home = new DbHome(player.getLocation(), args.length == 0 ? "home" : args[0]);


      Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
         database.createQuery(DbPlayer.class).filter(Filters.eq("uuid", player.getUniqueId().toString()))
            .stream()
            .findFirst()
            .ifPresent(
               dbPlayer -> {
                  if (dbPlayer.getHomes() == null) dbPlayer.setHomes(new ArrayList<>());

                  AtomicReference<Boolean> isAllowed = new AtomicReference<>();

                  player.getEffectivePermissions()
                     .stream()
                     .filter(perm -> perm.getPermission().startsWith("re.home."))
                     .findFirst()
                     .ifPresent(perm -> {
                        String number = perm.getPermission().replace("re.home.", "");

                        isAllowed.set(Integer.parseInt(number) > dbPlayer.getHomes().size());
                     });


                  dbPlayer.getHomes().stream().filter(h -> h.getName().equals(home.getName()))
                     .findFirst().ifPresentOrElse(
                        currentHome -> {
                           dbPlayer.getHomes().remove(currentHome);
                           dbPlayer.getHomes().add(home);
                           database.save(dbPlayer);
                           player.sendMessage(messaging.successMessage("Set new home at your current location."));
                        },
                        () -> {
                           if (!isAllowed.get()) {
                              if (!player.hasPermission("re.home.inf")) {
                                 player.sendMessage(messaging.errorMessage("You have reached the max amount of homes."));
                                 return;
                              }
                           }

                           dbPlayer.getHomes().add(home);
                           database.save(dbPlayer);
                           player.sendMessage(messaging.successMessage("Set new home at your current location."));
                        }
                     );
               });

      });

      return true;
   }
}

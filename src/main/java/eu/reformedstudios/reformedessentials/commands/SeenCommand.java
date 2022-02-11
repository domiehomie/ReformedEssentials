package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import dev.morphia.query.experimental.filters.Filters;
import eu.reformedstudios.reformedcore.util.Common;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import eu.reformedstudios.reformedcoreapi.database.IDatabase;
import eu.reformedstudios.reformedessentials.ReformedEssentials;
import eu.reformedstudios.reformedessentials.entities.DbPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class SeenCommand extends CommandListener {

   private final ReformedEssentials plugin;
   @Inject
   private Messaging messaging;
   @Inject
   private Common common;
   @Inject
   private IDatabase database;

   public SeenCommand(ReformedEssentials plugin) {
      super(new CommandBuilder()
         .setName("seen")
         .setDescription("Shows when a player was last online.")
         .setUsage("/seen <player>")
         .setAliases("lastseen")
         .setPermissions("re.seen")
         .createCommand()
      );
      this.plugin = plugin;
   }

   @Override
   public boolean exec(CommandSender sender, Command cmd, String label, String[] args) {
      if (args.length < 1) {
         sender.sendMessage(messaging.errorMessage("You must provide a player."));
         return true;
      }

      Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
         common.uuidFromIgn(args[0])
            .ifPresentOrElse(uuid -> {

               if (Bukkit.getPlayer(uuid) != null) {
                  sender.sendMessage(messaging.successMessage("That player is currently online."));
                  return;
               }

               this.database.createQuery(DbPlayer.class)
                  .filter(Filters.eq("uuid", uuid.toString()))
                  .stream()
                  .findFirst()
                  .ifPresentOrElse(dbPlayer -> {
                     Instant time = Instant.ofEpochSecond(dbPlayer.getLastJoined().getValue());
                     DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                        .withLocale(Locale.ROOT)
                        .withZone(ZoneId.systemDefault());

                     sender.sendMessage(messaging.normalMessage("That player was last seen at ")
                        .append(messaging.simpleGradient(formatter.format(time)))
                     );

                  }, () -> {
                     sender.sendMessage(messaging.errorMessage("That player has never joined before."));
                  });
            }, () -> {
               sender.sendMessage(messaging.errorMessage("No player by that name was found."));
            });

      });
      return true;
   }
}


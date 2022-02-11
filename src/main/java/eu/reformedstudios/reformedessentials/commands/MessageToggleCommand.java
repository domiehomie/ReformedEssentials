package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import dev.morphia.query.experimental.filters.Filters;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import eu.reformedstudios.reformedcoreapi.database.IDatabase;
import eu.reformedstudios.reformedessentials.ReformedEssentials;
import eu.reformedstudios.reformedessentials.entities.DbPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageToggleCommand extends CommandListener {

   private final ReformedEssentials plugin;
   @Inject
   private Messaging messaging;
   @Inject
   private IDatabase database;

   public MessageToggleCommand(ReformedEssentials plugin) {
      super(new CommandBuilder()
         .setName("msgtoggle")
         .setDescription("Toggles the ability for people to send you messages.")
         .setUsage("/msgtoggle")
         .setAliases("messagetoggle", "whispertoggle", "telltoggle", "wtoggle",
            "pmtoggle", "privatemessagetoggle", "dmtoggle", "directmessagetoggle")
         .setPermissions("re.msgtoggle")
         .createCommand()
      );

      this.plugin = plugin;
   }

   @Override
   public boolean exec(CommandSender sender, Command cmd, String label, String[] args) {
      if (!(sender instanceof Player player)) {
         sender.sendMessage(messaging.errorMessage("Only players can execute this command."));
         return true;
      }

      Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
         database.createQuery(DbPlayer.class)
            .filter(Filters.eq("uuid", player.getUniqueId().toString()))
            .stream()
            .findFirst()
            .ifPresent(dbPlayer -> {
               dbPlayer.setAcceptingMessages(!dbPlayer.isAcceptingMessages());
               database.save(dbPlayer);

               if (dbPlayer.isAcceptingMessages()) {
                  player.sendMessage(messaging.successMessage("You can now receive private messages!"));
               } else {
                  player.sendMessage(messaging.successMessage("You will now no longer receive private messages!"));
               }
            });
      });
      return true;
   }
}


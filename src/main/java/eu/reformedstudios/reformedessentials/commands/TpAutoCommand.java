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

public class TpAutoCommand extends CommandListener {

   private final ReformedEssentials plugin;
   @Inject
   private Messaging messaging;
   @Inject
   private IDatabase database;

   public TpAutoCommand(ReformedEssentials plugin) {
      super(new CommandBuilder()
         .setName("tpauto")
         .setDescription("Toggles automatic accepting of /tpa requests.")
         .setUsage("/tpauto")
         .setAliases()
         .setPermissions("re.tpauto")
         .createCommand());
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
               dbPlayer.setAutoAcceptingTpa(!dbPlayer.isAutoAcceptingTpa());
               database.save(dbPlayer);
               player.sendMessage(messaging.normalMessage("Automatically accept TPAs: ")
                  .append(messaging.success(String.valueOf(dbPlayer.isAutoAcceptingTpa())))
                  .append(messaging.normalMessageNoPrefix(".")));
            });
      });

      return true;
   }
}

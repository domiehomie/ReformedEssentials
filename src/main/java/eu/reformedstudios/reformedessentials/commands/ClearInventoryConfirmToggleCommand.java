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

public class ClearInventoryConfirmToggleCommand extends CommandListener {

   @Inject
   private Messaging messaging;
   @Inject
   private IDatabase database;
   private ReformedEssentials plugin;

   public ClearInventoryConfirmToggleCommand(ReformedEssentials plugin) {
      super(new CommandBuilder()
         .setName("clearinventoryconfirmtoggle")
         .setDescription("Toggles having to confirm the clear inventory command.")
         .setAliases("clearconfirm")
         .setUsage("/clearinventoryconfirmtoggle [on|off|enabled|disabled]")
         .setPermissions("re.clearinventoryconfirmtoggle")
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
         DbPlayer dbPlayer = database.createQuery(DbPlayer.class).filter(Filters.eq("uuid", player.getUniqueId().toString())).first();
         if (dbPlayer == null) {
            Bukkit.getScheduler().runTask(plugin, () -> sender.sendMessage(messaging.errorMessage("You.. dont exist?")));
            return;
         }

         if (args.length < 1) {
            dbPlayer.setClearInventoryConfirm(!dbPlayer.isClearInventoryConfirm());
            database.save(dbPlayer);
            Bukkit.getScheduler().runTask(plugin, () -> player.sendMessage(messaging.successMessage("Toggled clear inventory confirm.")));
            return;
         }

         switch (args[0]) {
            case "on", "enable" -> dbPlayer.setClearInventoryConfirm(true);
            case "off", "disable" -> dbPlayer.setClearInventoryConfirm(false);
         }
         database.save(dbPlayer);

         Bukkit.getScheduler().runTask(plugin, () -> player.sendMessage(messaging.successMessage("Set clear inventory confirm.")));
      });
      return true;
   }

}

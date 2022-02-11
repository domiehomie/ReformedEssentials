package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import dev.morphia.query.experimental.filters.Filters;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import eu.reformedstudios.reformedcoreapi.database.IDatabase;
import eu.reformedstudios.reformedessentials.ReformedEssentials;
import eu.reformedstudios.reformedessentials.entities.DbPlayer;
import eu.reformedstudios.reformedessentials.entities.TpaRequest;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpaCommand extends CommandListener {

   private final ReformedEssentials plugin;
   @Inject
   private Messaging messaging;
   @Inject
   private IDatabase database;


   public TpaCommand(ReformedEssentials plugin) {
      super(new CommandBuilder()
         .setName("tpa")
         .setDescription("Sends a teleportation request to a player.")
         .setUsage("/tpa <player>")
         .setAliases()
         .setPermissions("re.tpa")
         .createCommand());
      this.plugin = plugin;
   }

   @Override
   public boolean exec(CommandSender sender, Command cmd, String label, String[] args) {
      if (!(sender instanceof Player player)) {
         sender.sendMessage(messaging.errorMessage("Only players can execute this command."));
         return true;
      }

      if (args.length == 0) {
         player.sendMessage(messaging.errorMessage("You must provide a player."));
         return true;
      }

      Player target = Bukkit.getPlayer(args[0]);
      if (target == null) {
         player.sendMessage(messaging.errorMessage("Player not found."));
         return true;
      }

      if (target.getUniqueId().equals(player.getUniqueId())) {
         player.sendMessage(messaging.errorMessage("You cannot send a TPA request to yourself."));
         return true;
      }

      Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
         if (database.createQuery(TpaRequest.class)
            .filter(
               Filters.eq("sender", player.getUniqueId().toString()),
               Filters.eq("target", target.getUniqueId().toString()),
               Filters.eq("active", true)
            ).stream().findFirst().isPresent()) {
            player.sendMessage(messaging.errorMessage("You already have a request sent."));
            return;
         }


         database.createQuery(DbPlayer.class)
            .filter(Filters.eq("uuid", target.getUniqueId().toString()))
            .stream()
            .findFirst()
            .ifPresent(dbPlayer -> {
               if (dbPlayer.isAutoAcceptingTpa()) {
                  Bukkit.getScheduler().runTask(plugin, () -> {
                     player.teleport(target);
                     player.sendMessage(messaging.successMessage("Teleport commencing..."));
                     target.sendMessage(messaging.successMessage("Teleport commencing..."));
                  });
                  database.save(new TpaRequest(player, target, false));
                  return;
               }

               database.save(new TpaRequest(player, target));
               player.sendMessage(messaging.successMessage("Teleport request sent successfully."));

               target.sendMessage(messaging.gradientMessage(player.getName())
                  .append(messaging.normalMessageNoPrefix(" sent you a teleport request!"))
                  .append(Component.newline())
                  .append(messaging.errorMessage("DENY")
                     .hoverEvent(HoverEvent.showText(messaging.normalMessage("Deny the teleport request.")))
                     .clickEvent(ClickEvent.runCommand("/tpdeny " + player.getName()))
                  )
                  .append(Component.text(" "))
                  .append(messaging.success("ACCEPT")
                     .hoverEvent(HoverEvent
                        .showText(messaging.normalMessage("Accept the teleport request and be teleported immediately.")))
                     .clickEvent(ClickEvent.runCommand("/tpaccept " + player.getName()))
                  )

               );
            });


      });


      return true;
   }
}

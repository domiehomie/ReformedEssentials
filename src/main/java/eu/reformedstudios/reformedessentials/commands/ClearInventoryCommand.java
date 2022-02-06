package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import dev.morphia.query.experimental.filters.Filters;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import eu.reformedstudios.reformedcoreapi.database.IDatabase;
import eu.reformedstudios.reformedessentials.ReformedEssentials;
import eu.reformedstudios.reformedessentials.entities.DbPlayer;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearInventoryCommand extends CommandListener {

  private final ReformedEssentials plugin;
  @Inject
  private Messaging messaging;
  @Inject
  private IDatabase database;

  public ClearInventoryCommand(ReformedEssentials plugin) {
    super(new CommandBuilder()
       .setName("clearinventory")
       .setDescription("Clears your inventory.")
       .setUsage("/clearinventory [force]")
       .setAliases("ci")
       .setPermissions()
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

    if (args.length != 0 && args[0].equals("force")) {
      player.getInventory().clear();
      player.sendMessage(messaging.successMessage("Your inventory has been cleared successfully."));
      return true;
    }

    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
      this.database.createQuery(DbPlayer.class)
         .filter(Filters.eq("uuid", player.getUniqueId().toString()))
         .stream()
         .findFirst()
         .ifPresent(dbPlayer -> {
           if (dbPlayer.isClearInventoryConfirm()) {
             player.sendMessage(
                messaging.gradientMessage("Are you sure? ")
                   .append(messaging.success("[YES]"))
                   .clickEvent(ClickEvent.runCommand("/ci force"))
                   .hoverEvent(HoverEvent.showText(messaging.gradientMessage("Click to confirm.")))
             );
             return;
           }
           Bukkit.getScheduler().runTask(plugin, () -> Bukkit.dispatchCommand(player, "ci force"));
         });
    });
    return true;
  }
}


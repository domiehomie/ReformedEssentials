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
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCommand extends CommandListener {

  private final ReformedEssentials plugin;
  @Inject
  private Messaging messaging;
  @Inject
  private IDatabase database;

  public HomeCommand(ReformedEssentials plugin) {
    super(new CommandBuilder()
       .setName("home")
       .setDescription("Teleports you to your home.")
       .setAliases()
       .setUsage("/home [home]")
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
      database.createQuery(DbPlayer.class).filter(Filters.eq("uuid", player.getUniqueId().toString()))
         .stream().findFirst()
         .ifPresent(p -> {

           p.getHomes().stream().filter(home -> home.getName().equalsIgnoreCase(args.length == 0 ? "home" : args[0])).findFirst()
              .ifPresentOrElse(home -> {
                Bukkit.getScheduler().runTask(plugin, () -> player.teleport(new Location(Bukkit.getWorld(home.getWorld()), home.getX(), home.getY(), home.getZ(), (float) home.getYaw(), (float) home.getPitch())));
                player.sendMessage(
                   messaging.normalMessage("Teleported you to ")
                      .append(messaging.success(home.getName()))
                      .append(messaging.normalMessageNoPrefix("."))
                );
              }, () -> player.sendMessage(messaging.errorMessage("Couldn't find a home by that name.")));
         });
    });
    return true;
  }
}

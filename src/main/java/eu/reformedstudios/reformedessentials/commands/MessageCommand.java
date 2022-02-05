package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import dev.morphia.query.experimental.filters.Filters;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import eu.reformedstudios.reformedcoreapi.database.IDatabase;
import eu.reformedstudios.reformedessentials.ReformedEssentials;
import eu.reformedstudios.reformedessentials.cache.ICache;
import eu.reformedstudios.reformedessentials.entities.DbPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

public class MessageCommand extends CommandListener {

  private final ReformedEssentials plugin;

  @Inject
  private Messaging messaging;

  @Inject
  private IDatabase database;

  @Inject
  @Named("message-cache")
  private ICache<String> cache;


  public MessageCommand(ReformedEssentials plugin) {
    super(new CommandBuilder()
       .setName("msg")
       .setDescription("Sends a direct message to a player.")
       .setUsage("/msg <player> <message>")
       .setAliases("message", "w", "whisper", "tell", "pm", "privatemessage", "dm", "directmessage")
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

    if (args.length < 2) {
      player.sendMessage(messaging.errorMessage("You must provide a target player and a message."));
      return true;
    }

    StringBuilder msgBuilder = new StringBuilder();
    Arrays.stream(args).skip(1).forEach(argument -> msgBuilder.append(argument).append(" "));

    Player target = Bukkit.getPlayer(args[0]);
    String msg = msgBuilder.toString();

    if (target == null) {
      player.sendMessage(messaging.errorMessage("That player wasn't found."));
      return true;
    }


    Component finalMessage =
       messaging.anyMessage()
          .append(messaging.simpleGradient(player.getName()))
          .append(messaging.normalMessageNoPrefix(" » "))
          .append(messaging.simpleGradient(target.getName()))
          .append(messaging.normalMessageNoPrefix(": "))
          .append(messaging.lightMessageNoPrefix(msg));


    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> sendMessage(player, target, finalMessage));

    return true;
  }

  private void sendMessage(Player sender, Player target, Component message) {
    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
      this.database.createQuery(DbPlayer.class)
         .filter(Filters.eq("uuid", target.getUniqueId().toString()))
         .stream()
         .findFirst()
         .ifPresent(dbPlayer -> {
           if (!dbPlayer.isAcceptingMessages()) {
             sender.sendMessage(messaging.errorMessage("That player is not accepting private messages."));
             return;
           }

           sender.sendMessage(message);
           target.sendMessage(message);
           socialspy(message, target.getUniqueId().toString(), sender.getUniqueId().toString());

           this.cache.store(sender.getUniqueId().toString(), target.getUniqueId().toString());
         });
    });
  }

  private void socialspy(Component message, String target, String sender) {
    database.createQuery(DbPlayer.class)
       .filter(Filters.eq("socialspy", true))
       .stream()
       .forEach(dbPlayer -> {
         Player player = Bukkit.getPlayer(UUID.fromString(dbPlayer.getUuid()));
         if (player != null && dbPlayer.isSocialspy() && !dbPlayer.getUuid().equals(target) && !dbPlayer.getUuid().equals(sender)) {
           player.sendMessage(message);
         }
       });
  }

}


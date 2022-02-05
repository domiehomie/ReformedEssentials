package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
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

import javax.inject.Named;
import java.util.Arrays;
import java.util.UUID;

public class ReplyCommand extends CommandListener {

  private final ReformedEssentials plugin;
  @Inject
  private Messaging messaging;
  @Inject
  @Named("message-cache")
  private ICache<String> cache;
  @Inject
  private IDatabase database;


  public ReplyCommand(ReformedEssentials plugin) {
    super(new CommandBuilder()
       .setName("reply")
       .setDescription("Replies to the last message that was sent.")
       .setUsage("/reply <message>")
       .setAliases("r")
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

    String targetuuid = cache.get(player.getUniqueId().toString());
    if (targetuuid == null) {
      player.sendMessage(messaging.errorMessage("No one has messaged you yet."));
      return true;
    }

    Player target = Bukkit.getPlayer(UUID.fromString(targetuuid));
    if (target == null) {
      player.sendMessage(messaging.errorMessage("That player wasn't found. Perhaps they went offline?"));
      return true;
    }

    StringBuilder msgBuilder = new StringBuilder();
    Arrays.stream(args).forEach(argument -> msgBuilder.append(argument).append(" "));
    String msg = msgBuilder.toString();

    Component finalMessage =
       messaging.anyMessage()
          .append(messaging.simpleGradient(player.getName()))
          .append(messaging.normalMessageNoPrefix(" » "))
          .append(messaging.simpleGradient(target.getName()))
          .append(messaging.normalMessageNoPrefix(": "))
          .append(messaging.lightMessageNoPrefix(msg));

    sendMessage(player, target, finalMessage);

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


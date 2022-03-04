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
import net.kyori.adventure.text.Component;
import org.apache.commons.math3.util.Precision;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public class WhoIsCommand extends CommandListener {

  private final ReformedEssentials plugin;
  @Inject
  private Messaging messaging;
  @Inject
  private IDatabase database;
  @Inject
  private Common common;

  public WhoIsCommand(ReformedEssentials plugin) {
    super(new CommandBuilder()
       .setName("whois")
       .setDescription("Gives all the information known about a player.")
       .setUsage("/whois <player>")
       .setAliases()
       .setPermissions("re.whois")
       .createCommand()
    );
    this.plugin = plugin;
  }

  @Override
  public boolean exec(CommandSender sender, Command cmd, String label, String[] args) {
    if (args.length == 0) {
      sender.sendMessage(messaging.errorMessage("You must provide a player."));
      return true;
    }

    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
      this.common.uuidFromIgn(args[0])
         .ifPresentOrElse(uuid -> {
           this.database.createQuery(DbPlayer.class)
              .filter(Filters.eq("uuid", uuid.toString()))
              .stream()
              .findFirst()
              .ifPresentOrElse(dbPlayer -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                   .withLocale(Locale.ROOT)
                   .withZone(ZoneId.systemDefault());

                String time = formatter.format(Instant.ofEpochSecond(dbPlayer.getLastJoined().getValue()));

                Component dataMsg = messaging.normalMessage("Confirm clear inventory: ")
                   .append(messaging.simpleGradient(String.valueOf(dbPlayer.isClearInventoryConfirm())))
                   .append(Component.newline())
                   .append(messaging.normalMessage("Socialspy: "))
                   .append(messaging.simpleGradient(String.valueOf(dbPlayer.isSocialspy())))
                   .append(Component.newline())
                   .append(messaging.normalMessage("Last joined: "))
                   .append(messaging.simpleGradient(time))
                   .append(Component.newline())
                   .append(messaging.normalMessage("Accepting messages: "))
                   .append(messaging.simpleGradient(String.valueOf(dbPlayer.isAcceptingMessages())))
                   .append(Component.newline())
                   .append(messaging.normalMessage("Allowing teleports: "))
                   .append(messaging.simpleGradient(String.valueOf(dbPlayer.isAllowingTeleports())))
                   .append(Component.newline())
                   .append(messaging.normalMessage("Auto-accepting tpa: "))
                   .append(messaging.simpleGradient(String.valueOf(dbPlayer.isAutoAcceptingTpa())))
                   .append(Component.newline());
                AtomicReference<Component> homes = new AtomicReference<>(messaging.normalMessage("Homes: "));
                if (dbPlayer.getHomes() != null)
                  dbPlayer.getHomes().forEach(home -> {
                    homes.set(homes.get().append(
                       Component.newline()
                          .append(messaging.normalMessage("Name: "))
                          .append(messaging.simpleGradient(home.getName()))
                          .append(messaging.normalMessageNoPrefix(" World: "))
                          .append(messaging.simpleGradient(home.getWorld()))
                          .append(messaging.normalMessageNoPrefix(" X: "))
                          .append(messaging.simpleGradient(String.valueOf(Precision.round(home.getX(), 2))))
                          .append(messaging.normalMessageNoPrefix(" Y: "))
                          .append(messaging.simpleGradient(String.valueOf(Precision.round(home.getY(), 2))))
                          .append(messaging.normalMessageNoPrefix(" Z: "))
                          .append(messaging.simpleGradient(String.valueOf(Precision.round(home.getZ(), 2))))
                          .append(messaging.normalMessageNoPrefix(" Pitch: "))
                          .append(messaging.simpleGradient(String.valueOf(Precision.round(home.getPitch(), 2))))
                          .append(messaging.normalMessageNoPrefix(" Yaw: "))
                          .append(messaging.simpleGradient(String.valueOf(Precision.round(home.getPitch(), 2))))
                    ));
                  });
                else
                  homes.set(homes.get().append(
                     messaging.simpleGradient("none.")
                  ));

                sender.sendMessage(
                   messaging.splitterMessage()
                      .append(Component.newline())
                      .append(dataMsg)
                      .append(homes.get())
                      .append(Component.newline())
                      .append(messaging.splitterMessage())
                );

              }, () -> {
                sender.sendMessage(messaging.errorMessage("That player has never joined before."));
              });
         }, () -> {
           sender.sendMessage(messaging.errorMessage("A player by that name doesn't exist."));
         });
    });


    return true;
  }
}

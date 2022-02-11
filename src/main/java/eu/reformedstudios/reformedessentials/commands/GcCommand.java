package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import net.kyori.adventure.text.Component;
import org.apache.commons.math3.util.Precision;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.concurrent.TimeUnit;

public class GcCommand extends CommandListener {

   private final long uptime;
   @Inject
   private Messaging messaging;

   public GcCommand() {
      super(new CommandBuilder()
         .setName("gc")
         .setDescription("Shows current tps, memory usage and uptime.")
         .setAliases("uptime", "tps", "memory", "performance", "lag", "mem")
         .setUsage("/gc")
         .setPermissions("re.gc")
         .createCommand()
      );
      this.uptime = System.currentTimeMillis();
   }


   @Override
   public boolean exec(CommandSender sender, Command cmd, String label, String[] args) {
      long millis = System.currentTimeMillis() - this.uptime;
      String time = String.format("%02d:%02d:%02d",
         TimeUnit.MILLISECONDS.toHours(millis),
         TimeUnit.MILLISECONDS.toMinutes(millis) -
            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
         TimeUnit.MILLISECONDS.toSeconds(millis) -
            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

      double tps = Precision.round(Bukkit.getTPS()[0], 2);
      double tickTime = Precision.round(Bukkit.getAverageTickTime(), 2);


      double totalMemory = Precision.round(Runtime.getRuntime().totalMemory() / 1073741824d, 2);
      double freeMemory = Precision.round(Runtime.getRuntime().freeMemory() / 1073741824d, 2);
      double usedMemory = totalMemory - freeMemory;

      Component response = messaging.splitterMessage()
         .append(Component.newline())
         .append(messaging.gradientMessage("Server status:"))
         .append(Component.newline())
         .append(messaging.normalMessage("Uptime: "))
         .append(messaging.success(time))
         .append(Component.newline())
         .append(messaging.normalMessage("TPS: "))
         .append(messaging.success(String.valueOf(tps)))
         .append(Component.newline())
         .append(messaging.normalMessage("Average tick duration: "))
         .append(messaging.success(tickTime + "ms"))
         .append(Component.newline())
         .append(messaging.normalMessage("Memory: (used, free, total)"))
         .append(Component.newline())
         .append(messaging.successMessage(usedMemory + "G " + freeMemory + "G " + totalMemory + "G"))
         .append(Component.newline())
         .append(messaging.splitterMessage());

      sender.sendMessage(response);

      return true;
   }
}
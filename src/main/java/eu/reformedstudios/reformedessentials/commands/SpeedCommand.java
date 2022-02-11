package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpeedCommand extends CommandListener {

   @Inject
   private Messaging messaging;

   public SpeedCommand() {
      super(new CommandBuilder()
         .setName("speed")
         .setDescription("Sets your speed.")
         .setUsage("/speed <amount> [walk(ing)|fly(ing)]")
         .setAliases("setspeed")
         .setPermissions("re.speed")
         .createCommand());
   }

   @Override
   public boolean exec(CommandSender sender, Command cmd, String label, String[] args) {
      if (!(sender instanceof Player player)) {
         sender.sendMessage(messaging.errorMessage("Only players can execute this command."));
         return true;
      }

      if (args.length < 1) {
         player.sendMessage(messaging.errorMessage("Incorrect usage. See /re speed for the usage."));
         return true;
      }

      try {
         float speed = Integer.parseInt(args[0]) / 10f;

         if (speed < -10 || speed > 10) {
            player.sendMessage(messaging.errorMessage("The minimum and maximum speed are -10 and 10 respectively."));
            return true;
         }


         if (args.length == 2) {
            switch (args[1]) {
               case "walk", "walking" -> {
                  player.setWalkSpeed(speed);
                  player.sendMessage(speedMsg("walking", Integer.parseInt(args[0])));
               }
               case "fly", "flying" -> {
                  player.setFlySpeed(speed);
                  player.sendMessage(speedMsg("flying", Integer.parseInt(args[0])));
               }
               default -> player.sendMessage(messaging.errorMessage("Invalid speed type."));
            }
            return true;
         }

         if (player.isFlying()) {
            player.setFlySpeed(speed);
            player.sendMessage(speedMsg("flying", Integer.parseInt(args[0])));
         } else {
            player.setWalkSpeed(speed);
            player.sendMessage(speedMsg("walking", Integer.parseInt(args[0])));
         }
      } catch (NumberFormatException exception) {
         player.sendMessage(messaging.errorMessage("Speed must be a number."));
      }

      return true;
   }


   Component speedMsg(String type, int speed) {
      return messaging
         .normalMessage("Set your ")
         .append(messaging.success(type))
         .append(messaging.normalMessageNoPrefix(" speed to "))
         .append(messaging.success(String.valueOf(speed)))
         .append(messaging.normalMessageNoPrefix("."));

   }
}

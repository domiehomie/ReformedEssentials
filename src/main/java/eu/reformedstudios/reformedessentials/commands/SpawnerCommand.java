package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SpawnerCommand extends CommandListener {

   @Inject
   private Messaging messaging;

   public SpawnerCommand() {
      super(new CommandBuilder()
         .setName("spawner")
         .setDescription("Changes the mob type of a spawner.")
         .setAliases("changems", "mobspawner")
         .setUsage("/spawner <mob> [delay]")
         .createCommand()
      );
   }


   @Override
   public boolean exec(CommandSender sender, Command cmd, String label, String[] args) {
      if (!(sender instanceof Player player)) {
         sender.sendMessage(messaging.errorMessage("Only players can execute this command."));
         return true;
      }

      Block b = player.getTargetBlock(10);

      if (b == null || b.getType() != Material.SPAWNER) {
         player.sendMessage(messaging.errorMessage("You must be looking at a spawner!"));
         return true;
      }

      CreatureSpawner spawner = (CreatureSpawner) b.getState();
      if (args.length >= 1) {
         try {
            EntityType et = EntityType.valueOf(args[0].toUpperCase());

            spawner.setSpawnedType(et);
            player.sendMessage(messaging.successMessage("Successfully changed spawner type."));
         } catch (IllegalArgumentException e) {
            player.sendMessage(messaging.errorMessage("Invalid entity. You can find entities ")
               .append(messaging.errorMessage("here.")
                  .decorate(TextDecoration.UNDERLINED)
                  .clickEvent(ClickEvent.openUrl("https://purpurmc.org/javadoc/org/bukkit/entity/EntityType.html"))
               )
            );
         }
      }

      if (args.length >= 2) {
         try {
            int delay = Integer.parseInt(args[1]);
            spawner.setDelay(delay);
            player.sendMessage(messaging.successMessage("Successfully changed spawner delay."));
         } catch (NumberFormatException e) {
            player.sendMessage(messaging.errorMessage("You must provide a number as delay."));
         }
      }


      return true;
   }
}

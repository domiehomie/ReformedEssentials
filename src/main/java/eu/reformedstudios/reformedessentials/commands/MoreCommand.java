package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MoreCommand extends CommandListener {

   @Inject
   private Messaging messaging;

   public MoreCommand() {
      super(new CommandBuilder()
         .setName("more")
         .setDescription("Multiplies the item you're currently holding.")
         .setUsage("/more <amount>")
         .setAliases("multiply", "dupe")
         .setPermissions("re.more")
         .createCommand()
      );
   }

   @Override
   public boolean exec(CommandSender sender, Command cmd, String label, String[] args) {
      if (!(sender instanceof Player player)) {
         sender.sendMessage(messaging.errorMessage("Only players can execute this command."));
         return true;
      }

      ItemStack currentItem = player.getInventory().getItemInMainHand();
      if (currentItem.getType() == Material.AIR) {
         player.sendMessage(messaging.errorMessage("You must hold an item."));
         return true;
      }

      if (args.length == 0) {
         player.sendMessage(messaging.errorMessage("You must provide an amount."));
         return true;
      }

      try {

         int amount = Integer.parseInt(args[0]);

         if (amount < 0 || amount > 127) {
            player.sendMessage(messaging.errorMessage("Amount must be a number between 0-127"));
            return true;
         }

         currentItem.setAmount(amount);
         player.getInventory().setItemInMainHand(currentItem);
         player.sendMessage(messaging.successMessage("Item has been successfully replaced."));

      } catch (NumberFormatException e) {
         player.sendMessage(messaging.errorMessage("Amount must be a number between 0-127"));
      }

      return true;
   }
}

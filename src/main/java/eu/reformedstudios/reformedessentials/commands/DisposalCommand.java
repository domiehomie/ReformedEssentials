package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class DisposalCommand extends CommandListener {

   @Inject
   private Messaging messaging;

   public DisposalCommand() {
      super(new CommandBuilder()
         .setName("disposal")
         .setDescription("Opens a temporary inventory.")
         .setUsage("/disposal")
         .setAliases("trash")
         .setPermissions("re.disposal")
         .createCommand());
   }

   @Override
   public boolean exec(CommandSender sender, Command cmd, String label, String[] args) {
      if (!(sender instanceof Player player)) {
         sender.sendMessage(messaging.errorMessage("Only players can execute this command."));
         return true;
      }

      Inventory disposalInventory = Bukkit.createInventory(player, 4 * 9);
      player.openInventory(disposalInventory);
      player.sendMessage(messaging.errorMessage("Items you put in here will be DELETED when you close the menu.")
         .decorate(TextDecoration.BOLD)
      );

      return true;
   }
}
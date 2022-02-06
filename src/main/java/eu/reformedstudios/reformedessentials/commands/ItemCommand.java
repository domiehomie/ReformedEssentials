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

public class ItemCommand extends CommandListener {

  @Inject
  private Messaging messaging;

  public ItemCommand() {
    super(new CommandBuilder()
       .setName("item")
       .setDescription("Gives you any item.")
       .setUsage("/item <item> [amount]")
       .setAliases("i", "give")
       .setPermissions()
       .createCommand()
    );
  }

  @Override
  public boolean exec(CommandSender sender, Command cmd, String label, String[] args) {
    if (!(sender instanceof Player player)) {
      sender.sendMessage(messaging.errorMessage("Only players can execute this command."));
      return true;
    }

    if (args.length < 1) {
      player.sendMessage(messaging.errorMessage("You must provide an item."));
      return true;
    }
    try {

      int amount = args.length < 2 ? 1 : Integer.parseInt(args[1]);
      Material material = Material.valueOf(args[0].toUpperCase());

      player.getInventory().addItem(new ItemStack(material, amount));

      sender.sendMessage(messaging.successMessage(
         "Successfully gave you " + amount + " of " + material.name().toLowerCase().replaceAll("_", " ") + ".")
      );
    } catch (IllegalArgumentException exception) {
      sender.sendMessage(messaging.errorMessage("The item or amount was invalid."));
    }

    return true;
  }
}


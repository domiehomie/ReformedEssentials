package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;


public class HatCommand extends CommandListener {

  @Inject
  private Messaging messaging;

  public HatCommand() {
    super(new CommandBuilder()
       .setName("hat")
       .setDescription("Wears whatever you're holding on your head.")
       .setUsage("/hat")
       .setAliases("head", "wear")
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

    ItemStack handItem = player.getInventory().getItemInMainHand();
    ItemStack hatItem = player.getInventory().getItem(EquipmentSlot.HEAD);

    player.getInventory().setItemInMainHand(hatItem);
    player.getInventory().setItem(EquipmentSlot.HEAD, handItem);

    player.sendMessage(messaging.successMessage("Fasionable!"));

    return true;
  }
}


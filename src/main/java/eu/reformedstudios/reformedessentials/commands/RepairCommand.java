package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;

public class RepairCommand extends CommandListener {

	@Inject
	private Messaging messaging;

	public RepairCommand() {
		super(new CommandBuilder()
						.setName("repair")
						.setDescription("Repairs the durability of your items.")
						.setAliases("fix")
						.setUsage("/repair [hand|all]")
						.createCommand()
		);
	}

	@Override
	public boolean exec(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player player)) {
			sender.sendMessage(messaging.errorMessage("Only players can execute this command."));
			return true;
		}

		if(args.length < 1) {
			var item = player.getInventory().getItemInMainHand();
			if(item.getItemMeta() == null) {
				player.sendMessage(messaging.errorMessage("Please hold a repairable item in your hand."));
				return true;
			}
			item.setDamage(0);
			player.sendMessage(messaging.gradientMessage("Repaired the item in your main hand."));
			return true;
		}

		switch(args[0]) {
			case "hand" -> {
				var item = player.getInventory().getItemInMainHand();
				if(item.getItemMeta() == null) {
					player.sendMessage(messaging.errorMessage("Please hold a repairable item in your hand."));
					return true;
				}
				item.setDamage(0);
				player.sendMessage(messaging.successMessage("Repaired the item in your main hand."));
			}
			case "all" -> {
				Arrays.stream(player.getInventory().getContents()).filter(Objects::nonNull).filter(i -> i.getItemMeta() != null).forEach(i -> i.setDamage(0));
				player.sendMessage(messaging.successMessage("Repaired all items in your inventory."));
			}
		}
		return true;
	}
}
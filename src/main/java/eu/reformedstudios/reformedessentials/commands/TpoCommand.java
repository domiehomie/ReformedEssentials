package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpoCommand extends CommandListener {

	@Inject
	private Messaging messaging;

	public TpoCommand() {
		super(new CommandBuilder()
			.setName("tpo")
			.setDescription("Teleports to a player. Bypasses /tptoggle.")
			.setUsage("/tpo <player> [player]")
			.setAliases()
			.setPermissions()
			.createCommand());
	}

	@Override
	public boolean exec(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player player)) {
			sender.sendMessage(messaging.errorMessage("Only players can execute this command."));
			return true;
		}

		if (args.length == 0) {
			player.sendMessage(messaging.errorMessage("You must enter a player."));
		}

		switch (args.length) {
			case 0 -> player.sendMessage(messaging.errorMessage("You must enter a player."));
			case 1 -> {
				Player target = Bukkit.getPlayer(args[0]);
				if (target == null) {
					player.sendMessage(messaging.errorMessage("Player not found."));
					break;
				}
				player.sendMessage(messaging.normalMessage("You were teleported to ")
					.append(messaging.simpleGradient(target.getName()))
					.append(messaging.normalMessageNoPrefix("."))
				);
				player.teleport(target);
			}
			case 2 -> {

				Player teleported = Bukkit.getPlayer(args[0]);
				Player target = Bukkit.getPlayer(args[1]);

				if (teleported == null || target == null) {
					player.sendMessage(messaging.errorMessage("Player not found."));
					break;
				}
            
				teleported.sendMessage(messaging.normalMessage("You were teleported to ")
					.append(messaging.simpleGradient(target.getName()))
					.append(messaging.normalMessageNoPrefix("."))
				);
				teleported.teleport(target);
			}
		}
		return true;
	}
}

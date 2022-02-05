package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class TpoHereCommand extends CommandListener {

	@Inject
	private Messaging messaging;

	public TpoHereCommand() {
		super(new CommandBuilder()
			.setName("tpohere")
			.setDescription("Teleports a player to you. Bypasses /tptoggle.")
			.setUsage("/tpohere <player>")
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

		if (args.length == 0 || Bukkit.getPlayer(args[0]) == null) {
			sender.sendMessage(messaging.errorMessage("Invalid player."));
			return true;
		}

		Player target = Objects.requireNonNull(Bukkit.getPlayer(args[0]));
		target.teleport(player);

		player.sendMessage(messaging.normalMessage("Teleported ")
			.append(messaging.simpleGradient(target.getName()))
			.append(messaging.normalMessageNoPrefix(" to you.")));

		return true;
	}
}
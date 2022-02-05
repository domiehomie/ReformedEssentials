package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class GamemodeCommand extends CommandListener {

	@Inject
	private Messaging messaging;

	public GamemodeCommand() {
		super(new CommandBuilder()
			.setName("gamemode")
			.setAliases("gm")
			.setDescription("Lets you change your minecraft gamemode.")
			.setUsage("/gamemode <gamemode>")
			.setPermissions("re.gamemode")
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
			sender.sendMessage(messaging.errorMessage("Must provide 1 argument."));
			return true;
		}

		GameMode gameMode;

		switch (args[0]) {
			case "0", "s", "survival" -> gameMode = GameMode.SURVIVAL;
			case "1", "c", "creative" -> gameMode = GameMode.CREATIVE;
			case "2", "a", "adventure" -> gameMode = GameMode.ADVENTURE;
			case "3", "sp", "spectator" -> gameMode = GameMode.SPECTATOR;
			default -> {
				sender.sendMessage(messaging.errorMessage("Invalid gamemode."));
				return true;
			}
		}

		if (args.length == 1) {

			player.setGameMode(gameMode);
			player.sendMessage(messaging.normalMessage("Set gamemode to ")
				.append(
					messaging.success(gameMode.name().toLowerCase(Locale.US))
				)
				.append(
					messaging.normalMessageNoPrefix(".")
				)
			);
		} else {
			Player target = Bukkit.getPlayer(args[1]);
			if (target == null) {
				sender.sendMessage(messaging.errorMessage("That player isn't online."));
				return true;
			}
			target.setGameMode(gameMode);

			sender.sendMessage(
				messaging.normalMessage("Set gamemode of ")
					.append(messaging.success(target.getName()))
					.append(messaging.normalMessageNoPrefix(" to "))
					.append(
						messaging.success(gameMode.name().toLowerCase(Locale.US))
					)
					.append(
						messaging.normalMessageNoPrefix(".")
					));
		}


		return true;
	}
}

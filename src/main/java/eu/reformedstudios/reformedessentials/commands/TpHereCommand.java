package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import dev.morphia.query.experimental.filters.Filters;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import eu.reformedstudios.reformedcoreapi.database.IDatabase;
import eu.reformedstudios.reformedessentials.ReformedEssentials;
import eu.reformedstudios.reformedessentials.entities.DbPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class TpHereCommand extends CommandListener {

	private final ReformedEssentials pl;
	@Inject
	private Messaging messaging;
	@Inject
	private IDatabase database;

	public TpHereCommand(ReformedEssentials es) {
		super(new CommandBuilder()
			.setName("tphere")
			.setDescription("Teleports a player to you.")
			.setUsage("/tphere <player>")
			.setAliases()
			.setPermissions()
			.createCommand());
		this.pl = es;
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

		Bukkit.getScheduler().runTaskAsynchronously(pl, () -> {
			database.createQuery(DbPlayer.class)
				.filter(Filters.eq("uuid", target.getUniqueId().toString()))
				.stream()
				.findFirst()
				.ifPresent(p -> {
					if (!p.isAllowingTeleports()) {
						player.sendMessage(messaging.errorMessage("That player has teleports disabled. Use /tpohere to bypass."));
						return;
					}

					Bukkit.getScheduler().runTask(pl, () -> target.teleport(player));
					
					player.sendMessage(messaging.normalMessage("Teleported ")
						.append(messaging.simpleGradient(target.getName()))
						.append(messaging.normalMessageNoPrefix(" to you."))
					);
				});
		});
		return true;
	}
}

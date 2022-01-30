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

public class TpCommand extends CommandListener {

	private final ReformedEssentials pl;
	@Inject
	private Messaging messaging;
	@Inject
	private IDatabase database;

	public TpCommand(ReformedEssentials es) {
		super(new CommandBuilder()
			.setName("tp")
			.setDescription("Teleports to a player.")
			.setUsage("/tp <player> [otherPlayer]")
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

				Bukkit.getScheduler().runTaskAsynchronously(pl, () -> {
					database.createQuery(DbPlayer.class)
						.filter(Filters.eq("uuid", teleported.getUniqueId().toString()))
						.stream()
						.findFirst()
						.ifPresent(p -> {
							if (!p.isAllowingTeleports()) {
								player.sendMessage(messaging.errorMessage("That player has teleports disabled. Use /tpo to bypass."));
								return;
							}


							teleported.sendMessage(messaging.normalMessage("You were teleported to ")
								.append(messaging.simpleGradient(target.getName()))
								.append(messaging.normalMessageNoPrefix("."))
							);

							Bukkit.getScheduler().runTask(pl, () -> teleported.teleport(target));

						});
				});


			}
		}
		return true;
	}
}
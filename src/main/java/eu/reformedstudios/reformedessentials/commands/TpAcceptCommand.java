package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import dev.morphia.query.experimental.filters.Filters;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import eu.reformedstudios.reformedcoreapi.database.IDatabase;
import eu.reformedstudios.reformedessentials.ReformedEssentials;
import eu.reformedstudios.reformedessentials.entities.TpaRequest;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpAcceptCommand extends CommandListener {

	private final ReformedEssentials pl;
	@Inject
	private Messaging messaging;
	@Inject
	private IDatabase database;

	public TpAcceptCommand(ReformedEssentials es) {
		super(new CommandBuilder()
			.setName("tpaccept")
			.setDescription("Accepts a pending teleport request.")
			.setUsage("/tpaccept <player>")
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

		if (args.length < 1) {
			player.sendMessage("You must provide a player.");
		}

		var target = Bukkit.getPlayer(args[0]);
		if (target == null) {
			player.sendMessage(messaging.errorMessage("That player is currently offline."));
			return true;
		}
		Bukkit.getScheduler().runTaskAsynchronously(pl, () -> {
			database.createQuery(TpaRequest.class)
				.filter(
					Filters.eq("target", player.getUniqueId().toString()),
					Filters.eq("sender", target.getUniqueId().toString()),
					Filters.eq("active", true)
				)
				.stream()
				.findFirst()
				.ifPresentOrElse(r -> {

						r.setActive(false);
						database.save(r);
						Bukkit.getScheduler().runTask(pl, () -> target.teleport(player));
						player.sendMessage(messaging.successMessage("Teleport commencing..."));
						target.sendMessage(messaging.successMessage("Teleport commencing..."));
					}, () ->
						player.sendMessage(messaging.errorMessage("You don't have an active incoming request from that player."))
				);
		});
		return true;
	}
}
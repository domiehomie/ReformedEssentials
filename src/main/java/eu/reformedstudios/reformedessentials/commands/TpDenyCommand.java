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

public class TpDenyCommand extends CommandListener {

	private final ReformedEssentials pl;
	@Inject
	private Messaging messaging;
	@Inject
	private IDatabase database;

	public TpDenyCommand(ReformedEssentials es) {
		super(new CommandBuilder()
			.setName("tpdeny")
			.setDescription("Denies a pending teleport request.")
			.setUsage("/tpdeny <player>")
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
					target.sendMessage(messaging.errorMessage("Teleport request denied."));
					player.sendMessage(messaging.errorMessage("Teleport request denied."));
					database.save(r);
				}, () -> player.sendMessage(messaging.errorMessage("You do not have an active incoming teleport request from that player.")));
		});
		return true;
	}
}

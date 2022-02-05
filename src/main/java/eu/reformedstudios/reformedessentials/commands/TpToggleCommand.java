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

public class TpToggleCommand extends CommandListener {


	private final ReformedEssentials plugin;
	@Inject
	private Messaging messaging;
	@Inject
	private IDatabase database;

	public TpToggleCommand(ReformedEssentials plugin) {
		super(new CommandBuilder()
			.setName("tptoggle")
			.setDescription("Toggles ")
			.setUsage("/tptoggle")
			.setAliases()
			.setPermissions()
			.createCommand());

		this.plugin = plugin;
	}

	@Override
	public boolean exec(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player player)) {
			sender.sendMessage(messaging.errorMessage("Only players can execute this command."));
			return true;
		}

		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> database.createQuery(DbPlayer.class)
			.filter(Filters.eq("uuid", player.getUniqueId().toString()))
			.stream()
			.findFirst()
			.ifPresent(dbPlayer -> {
				dbPlayer.setAllowingTeleports(!dbPlayer.isAllowingTeleports());
				database.save(dbPlayer);

				player.sendMessage(messaging.normalMessage("Allow teleports: ")
					.append(messaging.success(String.valueOf(dbPlayer.isAllowingTeleports())))
					.append(messaging.normalMessageNoPrefix("."))
				);
			}));


		return true;
	}
}

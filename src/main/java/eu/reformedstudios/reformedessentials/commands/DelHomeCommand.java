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

public class DelHomeCommand extends CommandListener {

	@Inject
	private Messaging messaging;
	@Inject
	private IDatabase database;
	private ReformedEssentials pl;

	public DelHomeCommand(ReformedEssentials e) {
		super(new CommandBuilder()
						.setName("delhome")
						.setDescription("Deletes one of your homes.")
						.setAliases("deletehome", "rmhome", "removehome")
						.setUsage("/delhome")
						.createCommand()
		);
		this.pl = e;
	}


	@Override
	public boolean exec(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player player)) {
			sender.sendMessage(messaging.errorMessage("Only players can execute this command."));
			return true;
		}

		Bukkit.getScheduler().runTaskAsynchronously(pl, () -> {
			database.createQuery(DbPlayer.class).filter(Filters.eq("uuid", player.getUniqueId().toString()))
							.stream().findFirst().ifPresent(p -> {
								p.getHomes().stream().filter(h -> h.getName().equals(args.length == 0 ? "home" : args[0])).findFirst()
												.ifPresentOrElse(home -> {
													p.getHomes().remove(home);
													database.save(p);
													player.sendMessage(messaging.normalMessage("Deleted home ")
																	.append(messaging.success(home.getName()))
																	.append(messaging.normalMessageNoPrefix("."))
													);
												}, () -> player.sendMessage(messaging.errorMessage("That home doesn't exist.")));
							}
			);
		});
		return true;
	}
}

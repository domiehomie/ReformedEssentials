package eu.reformedstudios.reformedessentials.events.bukkit;

import dev.morphia.query.experimental.filters.Filters;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.database.IDatabase;
import eu.reformedstudios.reformedessentials.ReformedEssentials;
import eu.reformedstudios.reformedessentials.entities.DbPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class DbPlayerCreate implements Listener {

	private final IDatabase database;
	private final Messaging messaging;

	private final ReformedEssentials pl;

	public DbPlayerCreate(ReformedEssentials reformedEssentials, Messaging messaging, IDatabase database) {
		this.pl = reformedEssentials;
		this.database = database;
		this.messaging = messaging;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Bukkit.getScheduler().runTaskAsynchronously(pl, () -> {
			var player = this.database.createQuery(DbPlayer.class).filter(Filters.eq("uuid", e.getPlayer().getUniqueId().toString())).first();
			if(player == null) {
				this.database.save(new DbPlayer(e.getPlayer().getUniqueId()));
				Bukkit.getScheduler().runTask(pl,
								() -> Bukkit.getConsoleSender().sendMessage(this.messaging.normalMessage("Saved player ")
								.append(messaging.simpleGradient(e.getPlayer().getName()))
								.append(messaging.normalMessageNoPrefix("."))));
			}
		});
	}

}

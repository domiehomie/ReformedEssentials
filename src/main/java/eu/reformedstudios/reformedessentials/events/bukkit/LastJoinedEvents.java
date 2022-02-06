package eu.reformedstudios.reformedessentials.events.bukkit;

import dev.morphia.query.experimental.filters.Filters;
import eu.reformedstudios.reformedcoreapi.database.IDatabase;
import eu.reformedstudios.reformedessentials.ReformedEssentials;
import eu.reformedstudios.reformedessentials.entities.DbPlayer;
import org.bson.BsonTimestamp;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.Instant;

public class LastJoinedEvents implements Listener {

  private final IDatabase database;

  private final ReformedEssentials plugin;

  public LastJoinedEvents(ReformedEssentials reformedEssentials, IDatabase database) {
    this.plugin = reformedEssentials;
    this.database = database;
  }

  @EventHandler
  public void onJoin(PlayerJoinEvent event) {

    if (!event.getPlayer().hasPlayedBefore()) return;
    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
      this.database.createQuery(DbPlayer.class)
         .filter(Filters.eq("uuid", event.getPlayer().getUniqueId().toString()))
         .stream()
         .findFirst()
         .ifPresent(dbPlayer -> {
           dbPlayer.setLastJoined(new BsonTimestamp(Instant.now().getEpochSecond()));
           database.save(dbPlayer);
         });
    });
  }

  @EventHandler
  public void onLeave(PlayerQuitEvent event) {
    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
      this.database.createQuery(DbPlayer.class)
         .filter(Filters.eq("uuid", event.getPlayer().getUniqueId().toString()))
         .stream()
         .findFirst()
         .ifPresent(dbPlayer -> {
           dbPlayer.setLastJoined(new BsonTimestamp(Instant.now().getEpochSecond()));
           database.save(dbPlayer);
         });
    });
  }
}

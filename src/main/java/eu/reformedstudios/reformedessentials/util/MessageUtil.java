package eu.reformedstudios.reformedessentials.util;

import dev.morphia.query.experimental.filters.Filters;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.database.IDatabase;
import eu.reformedstudios.reformedessentials.cache.ICache;
import eu.reformedstudios.reformedessentials.entities.DbPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MessageUtil implements IMessageUtil {

   public void socialspy(Component message, String target, String sender, IDatabase database) {
      database.createQuery(DbPlayer.class)
         .filter(Filters.eq("socialspy", true))
         .stream()
         .forEach(dbPlayer -> {
            Player player = Bukkit.getPlayer(UUID.fromString(dbPlayer.getUuid()));
            if (player != null && dbPlayer.isSocialspy() && !dbPlayer.getUuid().equals(target) && !dbPlayer.getUuid().equals(sender)) {
               if (player.hasPermission("re.socialspy")) {
                  player.sendMessage(message);
                  return;
               }
               dbPlayer.setSocialspy(false);
               database.save(dbPlayer);
            }
         });
   }

   @Override
   public void sendMessage(Component message, Player sender, Player target, IDatabase database, Messaging messaging, ICache<String> cache) {
      database.createQuery(DbPlayer.class)
         .filter(Filters.eq("uuid", target.getUniqueId().toString()))
         .stream()
         .findFirst()
         .ifPresent(dbPlayer -> {
            if (!dbPlayer.isAcceptingMessages()) {
               sender.sendMessage(messaging.errorMessage("That player is not accepting private messages."));
               return;
            }

            sender.sendMessage(message);
            target.sendMessage(message);
            socialspy(message, target.getUniqueId().toString(), sender.getUniqueId().toString(), database);

            cache.store(sender.getUniqueId().toString(), target.getUniqueId().toString());
         });
   }
}

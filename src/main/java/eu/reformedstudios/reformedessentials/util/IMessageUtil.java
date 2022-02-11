package eu.reformedstudios.reformedessentials.util;

import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.database.IDatabase;
import eu.reformedstudios.reformedessentials.cache.ICache;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public interface IMessageUtil {


   void sendMessage(Component message, Player sender, Player target, IDatabase database, Messaging messaging, ICache<String> cache);
}

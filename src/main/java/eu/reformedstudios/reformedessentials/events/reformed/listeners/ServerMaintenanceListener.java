package eu.reformedstudios.reformedessentials.events.reformed.listeners;

import com.google.inject.Inject;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.events.EventContext;
import eu.reformedstudios.reformedcoreapi.events.IEventListener;
import eu.reformedstudios.reformedessentials.events.reformed.Events;
import org.bukkit.Bukkit;

public class ServerMaintenanceListener implements IEventListener {

	@Inject
	private Messaging messaging;

	@Override
	public void onEvent(EventContext eventContext) {
		if(eventContext.getName().equals(Events.SERVER_MAINTENANCE)) {


			messaging.errorMessage((String) eventContext.getData().get("reason"));

			Bukkit.getConsoleSender().sendMessage(messaging.errorMessage((String) eventContext.getData().get("reason")));


		}
	}

}

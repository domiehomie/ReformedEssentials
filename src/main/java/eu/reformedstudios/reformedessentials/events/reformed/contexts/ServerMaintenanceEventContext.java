package eu.reformedstudios.reformedessentials.events.reformed.contexts;

import eu.reformedstudios.reformedcoreapi.events.EventContext;
import eu.reformedstudios.reformedessentials.events.reformed.Events;

import java.util.Map;

public class ServerMaintenanceEventContext extends EventContext {

	public ServerMaintenanceEventContext(Map<String, Object> context) {
		super(Events.SERVER_MAINTENANCE, context);
	}

	public ServerMaintenanceEventContext() {
		super(Events.SERVER_MAINTENANCE, null);
	}

}

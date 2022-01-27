package eu.reformedstudios.reformedessentials;

import com.google.inject.Inject;
import eu.reformedstudios.reformedcoreapi.modules.IEventManager;
import eu.reformedstudios.reformedcoreapi.modules.ReformedModule;
import eu.reformedstudios.reformedcoreapi.modules.ReformedModuleBuilder;
import eu.reformedstudios.reformedessentials.events.contexts.ServerMaintenanceEventContext;
import eu.reformedstudios.reformedessentials.events.listeners.ServerMaintenanceListener;
import org.bukkit.plugin.java.JavaPlugin;

public class ReformedEssentials extends JavaPlugin {

	@Inject
	private IEventManager manager;

	@Override
	public void onEnable() {
		manager.execute(new ServerMaintenanceEventContext());
	}

	@Override
	public void onDisable() {

	}

	@Override
	public void onLoad() {
		ReformedModule mod = ReformedModuleBuilder.builder()
						.withName("ReformedEssentials")
						.withMainClass(this)
						.withListeners(new ServerMaintenanceListener())
						.build();



	}
}

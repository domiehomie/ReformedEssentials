package eu.reformedstudios.reformedessentials;

import com.google.inject.Inject;
import eu.reformedstudios.reformedcoreapi.commands.ICommandManager;
import eu.reformedstudios.reformedcoreapi.modules.IEventManager;
import eu.reformedstudios.reformedcoreapi.modules.ReformedModule;
import eu.reformedstudios.reformedcoreapi.modules.ReformedModuleBuilder;
import eu.reformedstudios.reformedessentials.commands.GamemodeCommand;
import eu.reformedstudios.reformedessentials.commands.HelpCommand;
import eu.reformedstudios.reformedessentials.commands.RepairCommand;
import eu.reformedstudios.reformedessentials.events.contexts.ServerMaintenanceEventContext;
import eu.reformedstudios.reformedessentials.events.listeners.ServerMaintenanceListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ReformedEssentials extends JavaPlugin {

	@Inject
	private IEventManager manager;

	private ReformedModule mod;

	@Override
	public void onEnable() {
		manager.execute(new ServerMaintenanceEventContext());

		ICommandManager manager = mod.getCommandManager();
		manager.registerCommand(new GamemodeCommand());
		manager.registerCommand(new RepairCommand());
		manager.registerCommand(new HelpCommand(mod.getCommandManager()));

	}

	@Override
	public void onDisable() {

	}

	@Override
	public void onLoad() {
		mod = ReformedModuleBuilder.builder()
						.withName("ReformedEssentials")
						.withMainClass(this)
						.withListeners(new ServerMaintenanceListener())
						.build();



	}
}

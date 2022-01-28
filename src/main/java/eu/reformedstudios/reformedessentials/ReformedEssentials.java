package eu.reformedstudios.reformedessentials;

import com.google.inject.Inject;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.ICommandManager;
import eu.reformedstudios.reformedcoreapi.database.IDatabase;
import eu.reformedstudios.reformedcoreapi.modules.IEventManager;
import eu.reformedstudios.reformedcoreapi.modules.ReformedModule;
import eu.reformedstudios.reformedcoreapi.modules.ReformedModuleBuilder;
import eu.reformedstudios.reformedessentials.commands.*;
import eu.reformedstudios.reformedessentials.entities.DbPlayer;
import eu.reformedstudios.reformedessentials.events.bukkit.DbPlayerCreate;
import eu.reformedstudios.reformedessentials.events.reformed.contexts.*;
import eu.reformedstudios.reformedessentials.events.reformed.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ReformedEssentials extends JavaPlugin {

	@Inject
	private IEventManager manager;
	@Inject
	private Messaging messaging;
	@Inject
	private IDatabase database;

	private ReformedModule mod;

	@Override
	public void onEnable() {
		manager.execute(new ServerMaintenanceEventContext());

		ICommandManager manager = mod.getCommandManager();
		manager.registerCommand(new GamemodeCommand());
		manager.registerCommand(new RepairCommand());
		manager.registerCommand(new ClearInventoryConfirmToggleCommand(this));
		manager.registerCommand(new SpawnerCommand());
		manager.registerCommand(new WeatherCommand());
		manager.registerCommand(new GcCommand());
		manager.registerCommand(new HelpCommand(mod.getCommandManager()));


		Bukkit.getPluginManager().registerEvents(new DbPlayerCreate(this, messaging, database), this);

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
						.withEntities(DbPlayer.class)
						.build();



	}
}

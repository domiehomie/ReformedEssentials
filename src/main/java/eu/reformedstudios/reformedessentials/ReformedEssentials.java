package eu.reformedstudios.reformedessentials;

import com.google.inject.Inject;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.ICommandManager;
import eu.reformedstudios.reformedcoreapi.database.IDatabase;
import eu.reformedstudios.reformedcoreapi.modules.IEventManager;
import eu.reformedstudios.reformedcoreapi.modules.ReformedModule;
import eu.reformedstudios.reformedcoreapi.modules.ReformedModuleBuilder;
import eu.reformedstudios.reformedessentials.commands.*;
import eu.reformedstudios.reformedessentials.entities.DbHome;
import eu.reformedstudios.reformedessentials.entities.DbPlayer;
import eu.reformedstudios.reformedessentials.entities.TpaRequest;
import eu.reformedstudios.reformedessentials.events.bukkit.DbPlayerCreate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ReformedEssentials extends JavaPlugin {

  @Inject
  private IEventManager manager;
  @Inject
  private Messaging messaging;
  @Inject
  private IDatabase database;

  private ReformedModule module;

  @Override
  public void onEnable() {

    ICommandManager manager = module.getCommandManager();
    manager.registerCommand(new GamemodeCommand());
    manager.registerCommand(new RepairCommand());
    manager.registerCommand(new ClearInventoryConfirmToggleCommand(this));
    manager.registerCommand(new SpawnerCommand());
    manager.registerCommand(new WeatherCommand());
    manager.registerCommand(new GcCommand());
    manager.registerCommand(new SetHomeCommand(this));
    manager.registerCommand(new HomeCommand(this));
    manager.registerCommand(new DelHomeCommand(this));
    manager.registerCommand(new EnderChestCommand());
    manager.registerCommand(new KickAllCommand());
    manager.registerCommand(new SpeedCommand());
    manager.registerCommand(new TpAllCommand());
    manager.registerCommand(new SuicideCommand());
    manager.registerCommand(new TpToggleCommand(this));
    manager.registerCommand(new DisposalCommand());
    manager.registerCommand(new TopCommand());
    manager.registerCommand(new TpCommand(this));
    manager.registerCommand(new TpHereCommand(this));
    manager.registerCommand(new TpoHereCommand());
    manager.registerCommand(new TpoCommand());
    manager.registerCommand(new TpposCommand());
    manager.registerCommand(new TpAutoCommand(this));
    manager.registerCommand(new TpaCommand(this));
    manager.registerCommand(new TpAcceptCommand(this));
    manager.registerCommand(new TpDenyCommand(this));
    manager.registerCommand(new HatCommand());
    manager.registerCommand(new MoreCommand());
    manager.registerCommand(new HelpCommand(module.getCommandManager()));


    Bukkit.getPluginManager().registerEvents(new DbPlayerCreate(this, messaging, database), this);

  }

  @Override
  public void onDisable() {

  }

  @Override
  public void onLoad() {
    module = ReformedModuleBuilder.builder()
       .withName("ReformedEssentials")
       .withMainClass(this)
       .withEntities(DbPlayer.class, DbHome.class, TpaRequest.class)
       .build();


  }
}

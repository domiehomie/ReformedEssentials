package eu.reformedstudios.reformedessentials;

import com.google.inject.Inject;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.ICommandManager;
import eu.reformedstudios.reformedcoreapi.config.IConfigurationManager;
import eu.reformedstudios.reformedcoreapi.database.IDatabase;
import eu.reformedstudios.reformedcoreapi.modules.IEventManager;
import eu.reformedstudios.reformedcoreapi.modules.ReformedModule;
import eu.reformedstudios.reformedcoreapi.modules.ReformedModuleBuilder;
import eu.reformedstudios.reformedessentials.commands.*;
import eu.reformedstudios.reformedessentials.entities.DbHome;
import eu.reformedstudios.reformedessentials.entities.DbPlayer;
import eu.reformedstudios.reformedessentials.entities.TpaRequest;
import eu.reformedstudios.reformedessentials.events.bukkit.DbPlayerCreate;
import eu.reformedstudios.reformedessentials.events.bukkit.LastJoinedEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ReformedEssentials extends JavaPlugin {

  @Inject
  private IEventManager manager;
  @Inject
  private Messaging messaging;
  @Inject
  private IDatabase database;
  @Inject
  private IConfigurationManager cfgManager;

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
    manager.registerCommand(new MessageToggleCommand(this));
    manager.registerCommand(new MessageCommand(this));
    manager.registerCommand(new SocialSpyCommand(this));
    manager.registerCommand(new ReplyCommand(this));
    manager.registerCommand(new CompassCommand());
    manager.registerCommand(new ExtinguishCommand());
    manager.registerCommand(new ItemCommand());
    manager.registerCommand(new KillCommand());
    manager.registerCommand(new SudoCommand());
    manager.registerCommand(new FlyCommand());
    manager.registerCommand(new SeenCommand(this));
    manager.registerCommand(new PingCommand());
    manager.registerCommand(new GetposCommand());
    manager.registerCommand(new ClearInventoryCommand(this));
    manager.registerCommand(new FeedCommand());
    manager.registerCommand(new HealCommand());
    manager.registerCommand(new WhoIsCommand(this));
    manager.registerCommand(new HelpCommand(module.getCommandManager()));

    PluginManager pm = Bukkit.getPluginManager();
    pm.registerEvents(new DbPlayerCreate(this, messaging, database), this);
    pm.registerEvents(new LastJoinedEvents(this, database), this);


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
       .withModule(new ReformedEssentialsModule())
       .build();


  }
}

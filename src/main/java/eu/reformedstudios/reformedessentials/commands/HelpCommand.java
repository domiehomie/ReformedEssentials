package eu.reformedstudios.reformedessentials.commands;

import com.google.inject.Inject;
import eu.reformedstudios.reformedcore.commands.CommandManager;
import eu.reformedstudios.reformedcore.util.Messaging;
import eu.reformedstudios.reformedcoreapi.commands.CommandBuilder;
import eu.reformedstudios.reformedcoreapi.commands.CommandListener;
import eu.reformedstudios.reformedcoreapi.commands.ICommandManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class HelpCommand extends CommandListener {

	private final ICommandManager manager;

	public HelpCommand(ICommandManager manager) {
		super(new CommandBuilder()
						.setName("reformedessentials")
						.setAliases("re")
						.setDescription("Help command for ReformedEssentials")
						.setUsage("/reformedessentials [command]")
						.createCommand()
		);
		this.manager = manager;
	}


	@Override
	public boolean exec(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length < 1) {
			sender.sendMessage(manager.getHelpMenu(sender));
		} else {
			sender.sendMessage(manager.getHelpMenu(sender, args[0]));
		}

		return true;
	}
}

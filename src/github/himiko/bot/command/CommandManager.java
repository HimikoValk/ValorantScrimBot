package github.himiko.bot.command;

import github.himiko.bot.command.commands.scrim.CreateScrimCommand;
import github.himiko.bot.command.commands.scrim.MapSelectionCommand;
import github.himiko.bot.command.commands.stats.StatsCommand;
import github.himiko.bot.command.commands.utils.DeleteCommand;
import github.himiko.bot.command.commands.utils.NukeCommand;

import java.util.ArrayList;

public class CommandManager {
    private ArrayList<Command> commands = new ArrayList<>();

    public CommandManager()
    {
        //Scrim Commands
        commands.add(new CreateScrimCommand());
        commands.add(new MapSelectionCommand());

        //Stats Commands
        commands.add(new StatsCommand());

        //Util Commands
        commands.add(new NukeCommand());
        commands.add(new DeleteCommand());
    }

    public Command getCommandByName(String name)
    {
        for(Command command : commands)
        {
            if(command.getCommandName()  == name || name.contains(command.getCommandName()))
            {
                return command;
            }
        }
        return null;
    }

    public ArrayList<Command> getCommands()
    {
        return this.commands;
    }
}

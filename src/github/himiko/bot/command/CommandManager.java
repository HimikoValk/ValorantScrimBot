package github.himiko.bot.command;

import github.himiko.bot.command.commands.scrim.CreateEmbedCommand;
import github.himiko.bot.command.commands.utils.NukeCommand;

import java.util.ArrayList;

public class CommandManager {
    private ArrayList<Command> commands = new ArrayList<>();

    public CommandManager()
    {
        //Scrim Commands
        commands.add(new CreateEmbedCommand());

        //Util Commands
        commands.add(new NukeCommand());
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

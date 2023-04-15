package github.himiko.bot.events.listeners;

import github.himiko.bot.BotBuilder;
import github.himiko.bot.command.Command;
import github.himiko.bot.command.CommandManager;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SlashCommandListeners extends ListenerAdapter {

    private Command slashCommand = null;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        int counter = 0;
        String[] message = event.getCommandString().split(" ");
        String[] arguments = new String[10];
        String commandName = message[0];
        User user = event.getMember().getUser();

        this.slashCommand = BotBuilder.commandManager.getCommandByName(commandName);

        if(slashCommand.hasArguments())
        {
            for(String arg : slashCommand.getArgs())
            {
                arguments[counter] = event.getOption(arg).getAsString();
                counter++;
            }
        }

        if(slashCommand == null)
        {
            event.reply("Command not found!").queue();
        }else if(!slashCommand.hasArguments())
        {
            slashCommand.onActivity(message, event,true, user);
        }else {
            slashCommand.onActivity(arguments, event, true, user);
        }

    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {

        List<CommandData> commandDataList = new ArrayList<CommandData>();

        for(Command sCommand : BotBuilder.commandManager.getCommands())
        {
            if(sCommand.hasArguments())
            {
                SlashCommandData commandData = Commands.slash(sCommand.getCommandName().toLowerCase(), sCommand.getCommandDescription());

                for(String args : sCommand.getArgs())
                {
                    commandData.addOption(sCommand.getOptionType(), args, sCommand.getCommandDescription());
                }

                commandDataList.add(commandData);

            }else
            {
                commandDataList.add(Commands.slash(sCommand.getCommandName().toLowerCase(), sCommand.getCommandDescription()));
            }
        }

        event.getGuild().updateCommands().addCommands(commandDataList).queue();
    }
}

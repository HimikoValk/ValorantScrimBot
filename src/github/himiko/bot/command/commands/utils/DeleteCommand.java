package github.himiko.bot.command.commands.utils;

import github.himiko.bot.command.Command;
import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.List;

public class DeleteCommand extends Command {

    public DeleteCommand()
    {
        super("clear", "CLears all");
    }

    @Override
    public void onActivity(String[] args, SlashCommandInteractionEvent event, boolean hasPermission, User author)
    {
        List<GuildChannel> channelList = event.getGuild().getChannels();

        for(GuildChannel c : channelList)
        {
            c.delete().queue();
        }

    }
}

package github.himiko.bot.command.commands.utils;

import github.himiko.bot.command.Command;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.List;

public class NukeCommand extends Command {

    public NukeCommand() {
        super("nuke", "Clears a Channels Mesages", OptionType.STRING);
    }

    @Override
    public void onActivity(String[] args, SlashCommandInteractionEvent event, boolean hasPermission, User author) {

        TextChannel channel = (TextChannel) event.getChannel();
        MessageHistory history = new MessageHistory(channel);
        List<Message> mgs;

        for(int i = 0; i < 5; i++)
        {
            mgs = history.retrievePast(100).complete();
            if(mgs.size() > 1)
            {
                channel.deleteMessages(mgs).queue(); //5 * 100 = 500 Messages!
            }else
            {
                break;
            }
        }

    }
}

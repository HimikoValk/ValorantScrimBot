package github.himiko.bot.events.listeners;

import github.himiko.Main;
import github.himiko.bot.BotBuilder;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ChannelListener extends ListenerAdapter {

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        AudioChannel audioChannel = BotBuilder.channelManager.getTeamChanelByName(event.getChannelLeft().getName());
        if(audioChannel != null) {
            if (audioChannel.getMembers().size() < 1) {
                audioChannel.delete().queue();
                BotBuilder.channelManager.removeTeamChannel(BotBuilder.channelManager.getTeamChanelByName(audioChannel.getName()));
            }
        }
    }

}

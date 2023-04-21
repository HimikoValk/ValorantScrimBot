package github.himiko.bot.button.buttons;

import github.himiko.Main;
import github.himiko.bot.BotBuilder;
import github.himiko.bot.button.Button;
import github.himiko.system.scrim.ScrimManager;
import github.himiko.system.scrim.channel.ChannelManager;
import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class QueButton extends Button {

    public QueButton() {
        super( net.dv8tion.jda.api.interactions.components.buttons.Button.success("que-button", "Queue"));
    }

    @Override
    public void onInteraction(User author, ButtonInteractionEvent event) {
        VoiceChannel queChannel = event.getGuild().getVoiceChannelById(BotBuilder.channelManager.getChannelIDByName(event.getChannel().getName() + "-waiting-queue"));

        if(doesUserExist(author, queChannel))
        {
            if(Main.scrimManager.addUserToList(author,event.getChannel())) {
                //event.reply("<@" + author.getId() + ">" + " Has been Successfully Added to Queue!" + "\n" + Main.scrimManager.getUserCounter(event.getChannel()) + "/" + Main.scrimManager.getSizeOfLobby(event.getChannel())).queue();
                event.editMessage( "Players in Queue: " + Main.scrimManager.getUserCounter(event.getChannel()) + "/" + Main.scrimManager.getSizeOfLobby(event.getChannel())).queue();
            }else
            {
                //event.reply("<@" + author.getId() + ">" + " You are already in Queue or The Queue is already Full!").queue();
            }
            Main.scrimManager.printList(event.getChannel());
        }else
        {
            //event.reply("<@" + author.getId() +"> You need to be in the Right Waiting Channel before getting in the Queue!").queue();
        }

    }

    private boolean doesUserExist(User author, VoiceChannel channel)
    {
        for(Member m : channel.getMembers())
        {
            if(m.getUser().getId().contains(author.getId()))
            {
                return true;
            }
        }
        return false;
    }
}

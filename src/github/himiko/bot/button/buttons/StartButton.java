package github.himiko.bot.button.buttons;

import github.himiko.Main;
import github.himiko.bot.button.Button;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class StartButton extends Button {

    public StartButton() {
        super(net.dv8tion.jda.api.interactions.components.buttons.Button.primary("start-button", "Start"));
    }

    @Override
    public void onInteraction(User author, ButtonInteractionEvent event) {
        if(Main.scrimManager.isUserInQueue(author, event.getChannel()))
        {
            if(Main.scrimManager.startScrim(event.getChannel(), event))
            {
                event.reply("<#" + event.getChannel().getId() + "> Started Successfully!").queue();
            }else
            {
                event.reply("Sorry but you need to be 10 Players before Starting!").queue();
            }
        }else
        {
            event.reply("<@" + author.getId() + "> You are not a Member of the Lobby!").queue();
        }
    }
}

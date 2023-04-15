package github.himiko.bot.button.buttons;

import github.himiko.Main;
import github.himiko.bot.button.Button;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class CancelButton extends Button {

    public CancelButton() {
        super(net.dv8tion.jda.api.interactions.components.buttons.Button.danger("cancel-button", "Cancel"));
    }

    @Override
    public void onInteraction(User author, ButtonInteractionEvent event) {

        if(Main.scrimManager.removeFromQue(author, event.getChannel()))
        {
            event.reply( "<@" + author.getId() + ">"+ " Has been Successfully Removed from Queue!").queue();
        }else
        {
            event.reply( "<@" + author.getId() + ">"+ " You are not in Queue!").queue();
        }
        Main.scrimManager.printList(event.getChannel());
    }
}

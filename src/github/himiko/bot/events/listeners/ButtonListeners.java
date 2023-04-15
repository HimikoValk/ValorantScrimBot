package github.himiko.bot.events.listeners;

import github.himiko.Main;
import github.himiko.bot.BotBuilder;
import github.himiko.bot.button.Button;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ButtonListeners extends ListenerAdapter {
    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event)
    {

        Button button = BotBuilder.buttonManager.getButtonByID(event.getButton().getId());

        if(button == null)
        {
            System.out.println("Button is null");
            return;
        }

        button.onInteraction(event.getUser(), event);
    }
}

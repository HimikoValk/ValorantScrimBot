package github.himiko.bot.button;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public abstract class Button {
    private net.dv8tion.jda.api.interactions.components.buttons.Button button;
    private String id;

    public Button(net.dv8tion.jda.api.interactions.components.buttons.Button button)
    {
        this.button = button;
        this.id = button.getId();
    }

    public abstract void onInteraction(User author, ButtonInteractionEvent event);

    public net.dv8tion.jda.api.interactions.components.buttons.Button getButton() {
        return button;
    }

    public void setButton(net.dv8tion.jda.api.interactions.components.buttons.Button button) {
        this.button = button;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

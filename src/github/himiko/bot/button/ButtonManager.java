package github.himiko.bot.button;

import github.himiko.bot.button.buttons.CancelButton;
import github.himiko.bot.button.buttons.QueButton;
import github.himiko.bot.button.buttons.StartButton;

import java.util.ArrayList;

public class ButtonManager {
    private static ArrayList<Button> buttons = new ArrayList<>();

    public ButtonManager()
    {
        buttons.add(new QueButton());
        buttons.add(new StartButton());
        buttons.add(new CancelButton());
    }

    public void addButton(Button button)
    {
        buttons.add(button);
    }

    public Button getButtonByID(String id)
    {
        for(Button b : buttons)
        {
            if(b.getButton().getId() == id || b.getId().contains(id))
            {
                return b;
            }
        }
        return null;
    }

}

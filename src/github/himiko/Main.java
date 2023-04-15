package github.himiko;

import github.himiko.bot.BotBuilder;
import github.himiko.system.scrim.ScrimManager;

public class Main {

    public static BotBuilder bot;
    public static ScrimManager scrimManager;

    public static void main(String[] args) {

        try
        {
            bot = new BotBuilder();
            scrimManager = new ScrimManager();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}

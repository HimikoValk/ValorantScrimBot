package github.himiko;

import github.himiko.bot.BotBuilder;
import github.himiko.system.scrim.ScrimManager;

public class Main {

    public static BotBuilder bot;
    public static ScrimManager scrimManager;

    public static void main(String[] args) {

        try
        {
            //System.out.println(args.length);
            if(args.length > 0)
            {
                bot = BotBuilder.createForYourBot(args[0]);
            }else
            {
                bot = BotBuilder.createDefault();
            }

            scrimManager = new ScrimManager();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}

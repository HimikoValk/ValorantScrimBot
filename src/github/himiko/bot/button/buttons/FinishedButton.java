package github.himiko.bot.button.buttons;

import github.himiko.Main;
import github.himiko.bot.BotBuilder;
import github.himiko.bot.button.Button;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class FinishedButton extends Button {
    public FinishedButton() {
        super(net.dv8tion.jda.api.interactions.components.buttons.Button.success("finished-button", "Finished"));
    }

    @Override
    public void onInteraction(User author, ButtonInteractionEvent event) {

        Button team1 = new Button(net.dv8tion.jda.api.interactions.components.buttons.Button.primary("team1-button", "Team 1")) {
            @Override
            public void onInteraction(User author, ButtonInteractionEvent event) {
                for(User user : Main.scrimManager.getTeam1(event.getChannel()))
                {
                    Main.scrimManager.statisticManager.addWinToStatistic(user);
                }
            }
        };
        Button team2 = new Button(net.dv8tion.jda.api.interactions.components.buttons.Button.primary("team1-button", "Team 2 ")) {
            @Override
            public void onInteraction(User author, ButtonInteractionEvent event) {
                for(User user : Main.scrimManager.getTeam2(event.getChannel()))
                {
                    Main.scrimManager.statisticManager.addWinToStatistic(user);
                }
            }
        };

        BotBuilder.buttonManager.addButton(team1);
        BotBuilder.buttonManager.addButton(team2);

        net.dv8tion.jda.api.interactions.components.buttons.Button button1 = team1.getButton();
        net.dv8tion.jda.api.interactions.components.buttons.Button button2 = team2.getButton();

        if(Main.scrimManager.isUserInTeam(author, event.getChannel()))
        {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder
                    .setTitle("RESULTS")
                    .setFooter("Bitte wählt aus wer Gewonnen hat");
            event.replyEmbeds(embedBuilder.build())
                    .addActionRow(button1, button2)
                    .queue();

        }else
        {
            event.reply("Sorry but you are not a Member of one Team!").queue();
        }
    }
}

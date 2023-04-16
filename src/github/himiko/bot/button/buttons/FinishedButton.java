package github.himiko.bot.button.buttons;

import github.himiko.Main;
import github.himiko.bot.BotBuilder;
import github.himiko.bot.button.Button;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class FinishedButton extends Button {
    public FinishedButton() {
        super(net.dv8tion.jda.api.interactions.components.buttons.Button.success("finish-button", "Finished"));
    }

    @Override
    public void onInteraction(User author, ButtonInteractionEvent event) {

        Button team1 = new Button(net.dv8tion.jda.api.interactions.components.buttons.Button.primary("team1-button", "Team 1")) {
            @Override
            public void onInteraction(User author, ButtonInteractionEvent event) {
                String pingIDS = "";
                if(Main.scrimManager.isUserInTeam(author, event.getChannel()))
                {
                    for(User user : Main.scrimManager.getTeam1(event.getChannel()))
                     {
                        Main.scrimManager.statisticManager.addWinToStatistic(user);
                        pingIDS += "<@" + user.getId() + ">";
                    }

                    for(User user : Main.scrimManager.getTeam2(event.getChannel()))
                    {
                        Main.scrimManager.statisticManager.addLoosToStatistic(user);
                    }

                    event.reply("Congrats to " + pingIDS + " for the Win! \nFor watching your Stats make /stats").queue();
                }else
                {
                    event.reply("Sorry but you are not one of the Team Members!").queue();
                }


            }
        };
        Button team2 = new Button(net.dv8tion.jda.api.interactions.components.buttons.Button.primary("team2-button", "Team 2 ")) {
            String pingIDS = "";
            @Override
            public void onInteraction(User author, ButtonInteractionEvent event) {
                if(Main.scrimManager.isUserInTeam(author, event.getChannel())) {
                    for (User user : Main.scrimManager.getTeam2(event.getChannel())) {
                        Main.scrimManager.statisticManager.addWinToStatistic(user);
                        pingIDS += "<@" + user.getId() + ">";
                    }

                    for (User user : Main.scrimManager.getTeam1(event.getChannel())) {
                        Main.scrimManager.statisticManager.addLoosToStatistic(user);
                    }
                    event.reply("Congrats to " + pingIDS + " for the Win! \nFor watching your Stats make /stats").queue();
                }else
                {
                    event.reply("Sorry but you are not one of the Team Members!").queue();
                }
            }
        };

        BotBuilder.buttonManager.addButton(team1);
        BotBuilder.buttonManager.addButton(team2);

        net.dv8tion.jda.api.interactions.components.buttons.Button button1 = team1.getButton();
        net.dv8tion.jda.api.interactions.components.buttons.Button button2 = team2.getButton();

        if(Main.scrimManager.isLobbyRunning(event.getChannel()))
        {

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
        }else
        {
            event.reply("Sorry but the Game is not running jet!").queue();
        }

    }
}

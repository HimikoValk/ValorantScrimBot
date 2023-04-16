package github.himiko.bot.command.commands.stats;

import github.himiko.Main;
import github.himiko.bot.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.awt.*;

public class StatsCommand extends Command {
    public StatsCommand() {
        super("stats", "Shows the statistic from the player", OptionType.STRING);
    }

    @Override
    public void onActivity(String[] args, SlashCommandInteractionEvent event, boolean hasPermission, User author) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle("STATS " + author.getName())
                        .setFooter(Main.scrimManager.statisticManager.getStatisticsAsString(author))
                .setColor(Color.RED);
        event.replyEmbeds(embedBuilder.build()).queue();
    }
}

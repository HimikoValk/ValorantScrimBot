package github.himiko.bot.command.commands.scrim;

import github.himiko.Main;
import github.himiko.bot.BotBuilder;
import github.himiko.bot.command.Command;
import github.himiko.system.logger.LogCategory;
import github.himiko.system.scrim.ScrimManager;
import github.himiko.system.scrim.channel.ChannelManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;

import java.awt.*;
import java.util.concurrent.ExecutionException;

public class CreateEmbedCommand extends Command {
    public CreateEmbedCommand() {
        super("create", "creates embed for starting scrims", OptionType.STRING, "size");
    }

    @Override
    public void onActivity(String[] args, SlashCommandInteractionEvent event, boolean hasPermission, User author)  {

        // No need to do this I was drunk or something
        int size = Integer.parseInt(args[0]);

        if(size == 0 ||  args[0] == null)
        {
            size = 10; // default size of an lobby
        }

        Main.scrimManager.createScrim(event.getChannel(), size);

        Button queButton = BotBuilder.buttonManager.getButtonByID("que-button").getButton();
        Button cancelButton = BotBuilder.buttonManager.getButtonByID("cancel-button").getButton();
        Button startButton = BotBuilder.buttonManager.getButtonByID("start-button").getButton();
        Button finishButton = BotBuilder.buttonManager.getButtonByID("finish-button").getButton();
        Category category = null;
        try
        {
            category = event.getGuild().getCategoriesByName("Valorant Scrims", true).get(0);
        }catch (Exception e)
        {
            Main.bot.scrimLogger.trace(this.getClass(), LogCategory.WARNING, e.getMessage());
        }


        if(category == null)
        {
            category = event.getGuild().createCategory("Valorant Scrims").complete();
        }

        BotBuilder.channelManager.addCategory(category);

        ChannelAction<VoiceChannel> channel = category.createVoiceChannel(event.getChannel().getName() + "-waiting-queue");

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(author.getName());
        embedBuilder.setColor(Color.BLACK);
        embedBuilder.setTitle("VALORANT SCRIMS");
        embedBuilder.setFooter("Moin, das ist erstmal, ein Test Bot für, ein Valorant Scrims System.\nUnten sieht man Drei Buttons, die dafür da sind um zu, das Spiel zu Starten, zu Queuen oder seine Queue abzubrechen.\nEs gibt bestimmte channels (Lobbys) wo ihr rein Joinen könnt\n\nWie Spiele Ich?\nBevor ihr auf den Queue Button Klickt, Joint ihr erstmal den dazu gehörigen Lobby Waiting Channel.\nDann könnt ihr auf Queue drücken und wartet bis weitere Spieler Joinen.\nNach dem alle 10 Spieler da sind drückt einer von euch auf Start.\nIhr werdet in Random Teams eingeteilt.\nViel Spaß!");

        event.replyEmbeds(embedBuilder.build())
                .addActionRow(startButton,queButton,cancelButton, finishButton)
                .queue();

        try {

            BotBuilder.channelManager.addChannel(channel.submit().get());
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

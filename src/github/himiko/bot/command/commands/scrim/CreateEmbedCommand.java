package github.himiko.bot.command.commands.scrim;

import github.himiko.Main;
import github.himiko.bot.BotBuilder;
import github.himiko.bot.command.Command;
import github.himiko.system.scrim.channel.ChannelManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;

import java.awt.*;
import java.util.concurrent.ExecutionException;

public class CreateEmbedCommand extends Command {
    public CreateEmbedCommand() {
        super("create", "creates embed for starting scrims", OptionType.STRING);
    }

    @Override
    public void onActivity(String[] args, SlashCommandInteractionEvent event, boolean hasPermission, User author)  {
        Button queButton = BotBuilder.buttonManager.getButtonByID("que-button").getButton();
        Button cancelButton = BotBuilder.buttonManager.getButtonByID("cancel-button").getButton();
        Button startButton = BotBuilder.buttonManager.getButtonByID("start-button").getButton();

        ChannelAction<VoiceChannel> channel = event.getGuild().createVoiceChannel(event.getChannel().getName() + "-waiting-queue");

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(author.getName());
        embedBuilder.setColor(Color.BLACK);
        embedBuilder.setTitle("VALORANT SCRIMS");
        embedBuilder.setFooter("Moin, das ist erstmal, ein Test Bot für, ein Valorant Scrims System.\nUnten sieht man Drei Buttons, die dafür da sind um zu, das Spiel zu Starten, zu Queuen oder seine Queue abzubrechen.\nEs gibt bestimmte channels (Lobbys) wo ihr rein Joinen könnt\n\nWie Spiele Ich?\nBevor ihr auf den Queue Button Klickt, Joint ihr erstmal den dazu gehörigen Lobby Waiting Channel.\nDann könnt ihr auf Queue drücken und wartet bis weitere Spieler Joinen.\nNach dem alle 10 Spieler da sind drückt einer von euch auf Start.\nIhr werdet in Random Teams eingeteilt.\nViel Spaß!");

        event.replyEmbeds(embedBuilder.build())
                .addActionRow(startButton,queButton, cancelButton)
                .queue();

        try {

            BotBuilder.channelManager.addChannel(channel.submit().get());
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

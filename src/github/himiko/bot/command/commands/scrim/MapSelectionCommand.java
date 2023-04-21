package github.himiko.bot.command.commands.scrim;

import github.himiko.Main;
import github.himiko.bot.BotBuilder;
import github.himiko.bot.button.Button;
import github.himiko.bot.command.Command;
import github.himiko.system.logger.LogCategory;
import github.himiko.system.map.ValorantMap;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MapSelectionCommand extends Command {

    public MapSelectionCommand() {
        super("mapvote", "Creates a Embed with the Map List as Buttons that will Ban theme for the Match", OptionType.STRING);
    }

    @Override
    public void onActivity(String[] args, SlashCommandInteractionEvent event, boolean hasPermission, User author) {

        int counter = 0;
        final boolean[] mapSelected = {false};

        ArrayList<Button> buttons = new ArrayList<>();
        ArrayList<net.dv8tion.jda.api.interactions.components.buttons.Button> jdaButtons  = new ArrayList<>();

        ArrayList<ValorantMap> bannedMaps = new ArrayList<>();
        ArrayList<ValorantMap> notBannedList = new ArrayList<>();

        for(ValorantMap valorantMap : BotBuilder.mapManager.getMapList())
        {
            buttons.add(new github.himiko.bot.button.Button(net.dv8tion.jda.api.interactions.components.buttons.Button.primary(valorantMap.getMapName().toLowerCase() +"-button", valorantMap.getMapName())){
                @Override
                public void onInteraction(User author, ButtonInteractionEvent event) {
                    if(Main.scrimManager.isTeamCaptain(author, event.getChannel()) && Main.scrimManager.isOnTurn(event.getChannel(), author) && !mapSelected[0])
                    {
                        if(bannedMaps.size() < 5)
                        {
                            if(!valorantMap.isBanned())
                            {
                                valorantMap.setBanned(true);
                                event.editMessage(valorantMap.getMapName() + " Banned!").queue();
                                Main.scrimManager.switchTurn(event.getChannel());
                                bannedMaps.add(valorantMap);
                            }
                            else
                            {
                                event.editMessage("Map is already Banned! Please Select a other Map!").queue();
                            }
                        }else
                        {
                            for(ValorantMap map : BotBuilder.mapManager.getMapList())
                            {
                                if(!map.isBanned())
                                {
                                    notBannedList.add(map);
                                }
                            }
                            Collections.shuffle(notBannedList);
                            event.reply("You are Playing Map:" + notBannedList.get(0).getMapName() + "!").queue();
                            mapSelected[0] = true;
                        }
                    }else
                    {
                        Main.bot.scrimLogger.trace(this.getClass(), LogCategory.WARNING, "User is not a Team Captain or on move!");
                    }
                }
            });
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("MAP SELECTION");
        embedBuilder.setColor(Color.CYAN);
        embedBuilder.setFooter("Here the Team captains can ban the Maps you don't want to play:\nTeam Captains:\nTeam 1:" + Main.scrimManager.getTeam1Captain(event.getChannel()).getName() + "\nTeam 2:" +  Main.scrimManager.getTeam2Captain(event.getChannel()).getName());

        ReplyCallbackAction callbackAction = addButtonsToReplyCallbackFunction(embedBuilder, event, buttons, jdaButtons);

        callbackAction.queue();
    }

    private static TextChannel getTextChannelByName(String name, ButtonInteractionEvent event)
    {
        for(TextChannel tx : event.getGuild().getTextChannels())
        {
            if(tx.getName().contains(name))
            {
                return tx;
            }
        }

        return null;
    }

    private static ReplyCallbackAction addButtonsToReplyCallbackFunction(EmbedBuilder embedBuilder, SlashCommandInteractionEvent event, ArrayList<Button> buttons, ArrayList<net.dv8tion.jda.api.interactions.components.buttons.Button> jdaButtons)
    {
        int counter = 0;

        ReplyCallbackAction callbackAction = event.replyEmbeds(embedBuilder.build());

        counter = buttons.size() / 2 ;
        for(int i = 0; i < 2; i++)
        {
            for(int j = 0; j < buttons.size() - counter; j++)
            {
                BotBuilder.buttonManager.addButton(buttons.get(j));
                jdaButtons.add(buttons.get(j).getButton());
            }

            callbackAction.addActionRow(jdaButtons);
            buttons.removeIf(button -> {
                for(net.dv8tion.jda.api.interactions.components.buttons.Button button1 : jdaButtons)
                {
                    if(button1.getId().contains(button.getButton().getId()))
                    {
                        return true;
                    }
                }

                return false;
            });
            jdaButtons.clear();

            counter = 0;
        }
        return callbackAction;
    }
}

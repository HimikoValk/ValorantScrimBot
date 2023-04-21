package github.himiko.system.scrim;

import com.iwebpp.crypto.TweetNaclFast;
import github.himiko.Main;
import github.himiko.bot.BotBuilder;
import github.himiko.system.logger.LogCategory;
import github.himiko.system.map.MapManager;
import github.himiko.system.map.ValorantMap;
import github.himiko.system.scrim.exceptions.IllegalInteractionException;
import github.himiko.system.statistics.StatisticManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import net.dv8tion.jda.internal.interactions.command.SlashCommandInteractionImpl;

import java.util.*;

public class ScrimManager {

    public StatisticManager statisticManager;

    //LOBBY, LIST WITH USERES
    private static HashMap<String,ArrayList<User>> queList = new HashMap<>();
    //LOBBY, TEAMLIST WITH USERES
    private static HashMap<String, ArrayList<User>> teamMap = new HashMap<>();
    //LOBBY, STATUS
    private static HashMap<String, Boolean> statusMap = new HashMap<>();
    //LOBBY, SIZE
    private static HashMap<String, Integer> lobbySizeMap = new HashMap<>();
    //LOBBY, MAP
    private static HashMap<String, ArrayList<ValorantMap>> valorantMapSlectionMap= new HashMap<>();
    //LOBBY, CAPTAINS
    private static HashMap<String, ArrayList<User>> teamCaptainsMap = new HashMap<>();
    //LOBBY onTurn
    private static HashMap<String, ArrayList<Boolean>> turnMap = new HashMap<>();

    private static Random RANDOM = new Random();

    public ScrimManager()
    {
        statisticManager = new StatisticManager();
    }

    /**
     * Starts The Scrims by sorting first the Teams
     * Creating then the Lobby-Channels for each Team
     * Them Moving the Players into it
     * @param lobbyChannel
     * @param event
     * @return boolean
     */
    public boolean startScrim(Channel lobbyChannel, ButtonInteractionEvent event)
    {

            if (queList.get(lobbyChannel.getName()).size() == lobbySizeMap.get(lobbyChannel.getName())) {

                Category category = null;

                try
                {
                    category = event.getGuild().getCategoriesByName( "Valorant Scrim " + lobbyChannel.getName(), true).get(0);
                }catch (Exception e)
                {
                    Main.bot.scrimLogger.trace(this.getClass(), LogCategory.WARNING, e.getMessage());
                }

                if(category == null)
                {
                    category = event.getGuild().createCategory("Valorant Scrim " + lobbyChannel.getName()).complete();
                }

                BotBuilder.channelManager.addCategory(category);

                EnumSet<Permission> permission = EnumSet.of(Permission.VIEW_CHANNEL, Permission.ADMINISTRATOR);

                HashMap<String, ArrayList<User>> teams = createRandomTeam(event.getChannel(), queList.get(lobbyChannel.getName()).size() / 2);

                ArrayList<User> team1 = teams.get("team1");
                ArrayList<User> team2 = teams.get("team2");

                Main.bot.scrimLogger.trace(this.getClass(), LogCategory.INFORMATION, "Successfully Created Teams!");

                teamMap.put(lobbyChannel.getName() + "-team1", team1);
                teamMap.put(lobbyChannel.getName() + "-team2", team2);

                ChannelAction<VoiceChannel> team1VoiceChannel = category.createVoiceChannel(lobbyChannel.getName() + "-team1");
                team1VoiceChannel
                        .addPermissionOverride(event.getGuild().getPublicRole(), null, permission);
                addPermissionForTeam(lobbyChannel,team1VoiceChannel, event.getGuild().getMembers(), permission);
                try {
                    BotBuilder.channelManager.addTeamChannel(team1VoiceChannel.complete());
                    for (User user : team1) {
                        event.getGuild().getGuildChannelById(BotBuilder.channelManager.getTeamChannelIDByName(lobbyChannel.getName() + "-team1")).getGuild().moveVoiceMember(event.getGuild().getMemberById(user.getId()), BotBuilder.channelManager.getTeamChanelByName(lobbyChannel.getName() + "-team1")).queue();
                    }

                    ChannelAction<VoiceChannel> team2VoiceChannel = category.createVoiceChannel(lobbyChannel.getName() + "-team2");
                    team2VoiceChannel
                            .addPermissionOverride(event.getGuild().getPublicRole(), null, permission);
                            //.addPermissionOverride(event.getGuild().getMember(teamMap.get(lobbyChannel.getName() + "-team2").get(0)), permission, null);
                    addPermissionForTeam(lobbyChannel,team2VoiceChannel, event.getGuild().getMembers(), permission);
                    BotBuilder.channelManager.addTeamChannel(team2VoiceChannel.complete());
                    Main.bot.scrimLogger.trace(this.getClass(), LogCategory.INFORMATION, "Successfully Created Voice-Channels!");

                    for (User user : team2) {
                        event.getGuild().getGuildChannelById(BotBuilder.channelManager.getTeamChannelIDByName(lobbyChannel.getName() + "-team2")).getGuild().moveVoiceMember(event.getGuild().getMemberById(user.getId()), BotBuilder.channelManager.getTeamChanelByName(lobbyChannel.getName() + "-team2")).queue();
                    }

                    Main.bot.scrimLogger.trace(this.getClass(), LogCategory.INFORMATION, "Successfully Moved Teams to Voice Channels!");

                    ChannelAction<TextChannel> lobbyTextChannel = category.createTextChannel(lobbyChannel.getName() + "-lobby-chat");

                    lobbyTextChannel.addPermissionOverride(event.getGuild().getPublicRole(), null, permission);
                    addPermissionForTeam(lobbyChannel,lobbyTextChannel, event.getGuild().getMembers(), permission);

                    TextChannel channel = lobbyTextChannel.complete();

                    //Turn Map
                    ArrayList<Boolean> booleans = new ArrayList<>();
                    booleans.add(true);
                    booleans.add(false);
                    turnMap.put(channel.getName(), booleans);

                    //TeamCaptains

                    HashMap<String, ArrayList<User>> teamCaptains = createRandomTeamCaptains(lobbyChannel);
                    teamCaptainsMap.put(channel.getName(), teamCaptains.get(lobbyChannel.getName() + "-captains"));


                    //addPermissionForTeam(lobbyTextChannel, team2VoiceChannel.getGuild().getVoiceChannelById(BotBuilder.channelManager.getTeamChannelIDByName(lobbyChannel.getName()) + "-team2").getMembers(), permission);



                    //entereMapSelection(channel);

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                statusMap.put(lobbyChannel.getName(), true);
                return true;
            }

        return false;
    }

    public void entereMapSelection(TextChannel lobbyTextChannel)
    {

    }

    /**
     *
     * @param lobbyChanel
     * @param size
     */
    public void createScrim(Channel lobbyChanel, int size)
    {
        if(!doesLobbyExist(lobbyChanel)) {
            queList.put(lobbyChanel.getName(), new ArrayList<>());
            lobbySizeMap.put(lobbyChanel.getName(), size);
        }else
        {
            Main.bot.scrimLogger.trace(this.getClass(), LogCategory.WARNING, "Lobby already Exist!");
        }
    }

    /**
     *
     * @param lobbyChannel
     */
    public void deleteScrim(Channel lobbyChannel)
    {
        if(doesLobbyExist(lobbyChannel)) {
            queList.remove(lobbyChannel.getName(), queList.get(lobbyChannel.getName()));
        }else
        {
            Main.bot.scrimLogger.trace(this.getClass(), LogCategory.WARNING, "Lobby dose not Exist!");
        }
    }

    /**
     * Adds Users to their Lobby by first using the Helper-Function {@link ScrimManager#doesUserExist(User, Channel)}
     * which checks if User is already in a Lobby/Queue
     * @param user
     * @param lobbyChannel
     * @return boolean
     */
    public boolean addUserToList(User user, Channel lobbyChannel)
    {
        if(!doesUserExist(user, lobbyChannel))
        {
            if(queList.get(lobbyChannel.getName()) == null||queList.get(lobbyChannel.getName()).size() < 10)
            {

                if(queList.get(lobbyChannel.getName()) != null)
                {
                    queList.get(lobbyChannel.getName()).add(user);
                }else
                {
                    queList.put(lobbyChannel.getName(), new ArrayList<User>());
                    queList.get(lobbyChannel.getName()).add(user);
                }

            }else
            {
                Main.bot.scrimLogger.trace(this.getClass(), LogCategory.WARNING, "Lobby is full!");
                return false;
            }
            Main.bot.scrimLogger.trace(this.getClass(), LogCategory.INFORMATION, "Successfully Added User to Queue List!");
            return true;

        }else {
            Main.bot.scrimLogger.trace(this.getClass(), LogCategory.ERROR, "User is already in Queue!");
            return false;
        }
    }

    /**
     * Creates a HashMap with Randomized teams where the key of the Map the Team name is and the Value the List with Users (Team Members)
     *
     * @param lobbyChannel
     * @param teamSize
     * @return {@code HashMap<String,ArrayList<User>>}
     */
    public HashMap<String,ArrayList<User>> createRandomTeam(Channel lobbyChannel, int teamSize)
    {
        HashMap<String ,ArrayList<User>> teams = new HashMap<>();
        ArrayList<User> players = queList.get(lobbyChannel.getName());

        Collections.shuffle(players);

        ArrayList<User> team = new ArrayList<>();

        int teamCounter = 1;

        for(int i = 0; i < players.size(); i++)
        {
            if(i % teamSize == 0)
            {
                String teamName = "team" + teamCounter;
                team = new ArrayList<>();
                teams.put(teamName, team);
                teamCounter++;
            }
            team.add(players.get(i));
        }

        return teams;
    }

    public HashMap<String, ArrayList<User>> createRandomTeamCaptains(Channel lobbyChannel)
    {
        HashMap<String, ArrayList<User>> teamCaptains = new HashMap<>();
        ArrayList<User> team1 = teamMap.get(lobbyChannel.getName() + "-team1");
        ArrayList<User> team2 = teamMap.get(lobbyChannel.getName() + "-team2");

        Collections.shuffle(team1);
        Collections.shuffle(team2);

        ArrayList<User> captains = new ArrayList<>();

        captains.add(team1.get(0));
        captains.add(team2.get(0));

        teamCaptains.put(lobbyChannel.getName() + "-captains",captains);

        return teamCaptains;
    }

    //public void startMapSelection(Channel lobby, )

    public void scrimFinished(Channel lobbyChannel)
    {
        teamMap.get(lobbyChannel.getName() + "-team1").clear();
        teamMap.get(lobbyChannel.getName() + "-team2").clear();
    }

    /**
     * Removes User by Using the Helper-Function {@link ScrimManager#doesUserExist(User, Channel)}
     * this function checks if the User exist in the Lobby/Queue
     * @param user
     * @param lobbyChannel
     * @return boolean
     */
    public boolean removeFromQue(User user, Channel lobbyChannel)
    {
        if(doesUserExist(user, lobbyChannel))
        {
            queList.get(lobbyChannel.getName()).remove(user);
            Main.bot.scrimLogger.trace(this.getClass(), LogCategory.INFORMATION, "Successfully removed User from Queue");
            return true;
        }else
        {
            Main.bot.scrimLogger.trace(this.getClass(), LogCategory.WARNING, "User is not in Queue!");
            return false;
        }

    }

    public void printList(Channel lobbyChannel)
    {
        System.out.println("Lobby:" + lobbyChannel.getName());
        for(User user : queList.get(lobbyChannel.getName()))
        {
            System.out.println("Username:" + user.getName());
        }
    }

    public <T extends GuildChannel> void addPermissionForTeam(Channel lobbyChannel,ChannelAction<T> channelChannelAction, List<Member> team, EnumSet<Permission> permission)
    {
        for(Member teamMember : team)
        {
            if(isUserInTeam(teamMember.getUser(), lobbyChannel))
            {
                channelChannelAction.addMemberPermissionOverride(Long.parseLong(teamMember.getId()), permission,null);
            }
        }
    }

    public boolean isOnTurn(Channel lobbyChannel, User user)
    {
        if(isTeamCaptain(user,lobbyChannel))
        {
            if(getTeam1Captain(lobbyChannel).getId().contains(user.getId()) && turnMap.get(lobbyChannel.getName()).get(0))
            {
                return true;
            }else if (getTeam2Captain(lobbyChannel).getId().contains(user.getId()) && turnMap.get(lobbyChannel.getName()).get(1))
            {
                return true;
            }
        }
        else
        {
            Main.bot.scrimLogger.trace(this.getClass(), LogCategory.WARNING, "User is not a Team Captain!");
        }
        return false;
    }

    public void switchTurn(Channel lobbyChannel)
    {
        if(turnMap.get(lobbyChannel.getName()).get(0))
        {
            turnMap.get(lobbyChannel.getName()).set(0, false);
            turnMap.get(lobbyChannel.getName()).set(1, true);
        }else
        {
            turnMap.get(lobbyChannel.getName()).set(1, false);
            turnMap.get(lobbyChannel.getName()).set(0, true);
        }
    }

    public boolean isLobbyRunning(Channel lobbychannel)
    {
        try
        {
            return statusMap.get(lobbychannel.getName());
        }catch (Exception e)
        {
            Main.bot.scrimLogger.trace(this.getClass(), LogCategory.WARNING, e.getMessage());
            return false;
        }
    }

    private boolean isUserAlreadyInTeam(User user, ArrayList<User> teamMembers)
    {
         for(User u : teamMembers)
         {
             if(u.getId().contains(user.getId()))
             {
                 return true;
             }
         }

         return false;
    }

    private boolean doesUserExist(User user, Channel channel)
    {
        if(doesLobbyExist(channel))
        {

            for(User users : queList.get(channel.getName())) {
                if (users.getId().contains(user.getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean doesLobbyExist(Channel lobbyChannel)
    {
        if(queList.get(lobbyChannel.getName()) == null)
        {
            return false;
        }

        return true;
    }

    public boolean isUserInTeam(User user, Channel lobbyChannel)
    {
        for(User u : teamMap.get(lobbyChannel.getName()+ "-team1"))
        {
            if(u.getId().contains(user.getId()))
            {
                return true;
            }
        }

        for(User us : teamMap.get(lobbyChannel.getName() + "-team2"))
        {
            if(us.getId().contains(user.getId()))
            {
                return true;
            }
        }

        return false;
    }

    public boolean isUserInTeam(User user, String lobbyName)
    {
        for(User u : teamMap.get(lobbyName))
        {
            if(u.getId().contains(user.getId()))
            {
                return true;
            }
        }

        for(User us : teamMap.get(lobbyName))
        {
            if(us.getId().contains(user.getId()))
            {
                return true;
            }
        }

        return false;
    }

    public boolean isTeamCaptain(User user, Channel lobbyChannel)
    {
        for(User u : teamCaptainsMap.get(lobbyChannel.getName()))
        {
            if(u.getId().contains(user.getId()))
            {
                return true;
            }
        }
        return false;
    }

    public User getTeam1Captain(Channel lobbyChannel)
    {
       return teamCaptainsMap.get(lobbyChannel.getName()).get(0);
    }

    public User getTeam2Captain(Channel lobbyChannel)
    {
        return teamCaptainsMap.get(lobbyChannel.getName()).get(1);
    }

    public boolean isOnMove(User user, Channel lobChannel)
    {
        if(valorantMapSlectionMap.isEmpty())
        {
            return true;
        }

       // for(valorantMapSlectionMap.get())
        return false;
    }

    /**
     * Checks if user is in Queue by Using the Helper-Function {@link ScrimManager#doesUserExist(User, Channel)}
     * Which Checks if User is in the Right Lobby/Queue
     * @param user
     * @param lobbyChannel
     * @return boolean
     */
    public boolean isUserInQueue(User user, Channel lobbyChannel)
    {
        if(doesUserExist(user, lobbyChannel))
        {
            return true;
        }
        return false;
    }

    public int getUserCounter(Channel lobbyChannel)
    {
        int counter = 0;

        for(User user : queList.get(lobbyChannel.getName()))
        {
            if(user != null)
            {
                counter++;
            }
        }
        return counter;
    }

    public String getChannelNameByUser(User user)
    {
        for(String lobbyName : teamMap.keySet())
        {
            if(isUserInTeam(user,lobbyName))
            {
                return lobbyName;
            }
        }
        return "";
    }

    public int getSizeOfLobby(Channel lobbyChannel)
    {
        return lobbySizeMap.get(lobbyChannel.getName());
    }

    public ArrayList<User> getTeam1(Channel lobbyChannel)
    {
        return teamMap.get(lobbyChannel.getName() + "-team1");
    }

    public ArrayList<User> getTeam2(Channel lobbyChannel)
    {
        return teamMap.get(lobbyChannel.getName() + "-team2");
    }

}

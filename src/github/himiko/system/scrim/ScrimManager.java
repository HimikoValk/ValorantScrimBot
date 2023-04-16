package github.himiko.system.scrim;

import com.iwebpp.crypto.TweetNaclFast;
import github.himiko.Main;
import github.himiko.bot.BotBuilder;
import github.himiko.system.logger.LogCategory;
import github.himiko.system.scrim.exceptions.IllegalInteractionException;
import github.himiko.system.statistics.StatisticManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ScrimManager {

    public StatisticManager statisticManager;

    //LOBBY, LIST WITH USERES
    private static HashMap<String,ArrayList<User>> queList = new HashMap<>();
    //LOBBY, TEAMLIST WITH USERES
    private static HashMap<String, ArrayList<User>> teamMap = new HashMap<>();
    //LOBBY, STATUS
    private static HashMap<String, Boolean> status = new HashMap<>();
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
        if(queList.get(lobbyChannel.getName()).size() == 2)
        {
            ArrayList<User> team1 = createRandomTeam(lobbyChannel);
            //Just for watching the Process of Creating the Teams (Only Visual for the Console)
            printList(lobbyChannel);
            ArrayList<User> team2 = createRandomTeam(lobbyChannel);

            Main.bot.scrimLogger.trace(this.getClass(), LogCategory.INFORMATION,"Successfully Created Teams!");

            teamMap.put(lobbyChannel.getName() + "-team1", team1);
            teamMap.put(lobbyChannel.getName() + "-team2", team2);

            ChannelAction<VoiceChannel> team1VoiceChannel = BotBuilder.channelManager.getCategoryByName("Valorant Scrims").createVoiceChannel(lobbyChannel.getName() + "-team1");
            try
            {

            BotBuilder.channelManager.addTeamChannel(team1VoiceChannel.complete());

            for(User user : team1)
            {
                System.out.println("Team1:" + user.getName());
                System.out.println(BotBuilder.channelManager.getTeamChannelIDByName(lobbyChannel.getName() + "-team1"));
                event.getGuild().getGuildChannelById(BotBuilder.channelManager.getTeamChannelIDByName(lobbyChannel.getName() + "-team1")).getGuild().moveVoiceMember(event.getGuild().getMemberById(user.getId()), BotBuilder.channelManager.getTeamChanelByName(lobbyChannel.getName() + "-team1")).queue();
            }

            ChannelAction<VoiceChannel> team2VoiceChannel =  BotBuilder.channelManager.getCategoryByName("Valorant Scrims").createVoiceChannel(lobbyChannel.getName() + "-team2");
            BotBuilder.channelManager.addTeamChannel(team2VoiceChannel.complete());

            Main.bot.scrimLogger.trace(this.getClass(), LogCategory.INFORMATION,"Successfully Created Voice-Channels!");

            for(User user : team2)
            {
                System.out.println("Team2:" + user.getName());
                event.getGuild().getGuildChannelById(BotBuilder.channelManager.getTeamChannelIDByName(lobbyChannel.getName() + "-team2")).getGuild().moveVoiceMember(event.getGuild().getMemberById(user.getId()), BotBuilder.channelManager.getTeamChanelByName(lobbyChannel.getName() + "-team2")).queue();
            }

            Main.bot.scrimLogger.trace(this.getClass(), LogCategory.INFORMATION,"Successfully  Moved Teams to Voice Channels!");

            }catch (Exception e)
            {
                e.printStackTrace();
                return false;
            }
            status.put(lobbyChannel.getName(), true);
            return true;
        }

        return false;
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
        }else
        {
            Main.bot.scrimLogger.trace(this.getClass(), LogCategory.WARNING, "Lobby already Exist");
        }
    }

    /**
     *
     * @param lobbyChannel
     */
    public void deleteScrim(Channel lobbyChannel)
    {
        queList.remove(lobbyChannel.getName(), queList.get(lobbyChannel.getName()));
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

    public ArrayList<User> getTeam1(Channel lobbyChannel)
    {
        return teamMap.get(lobbyChannel.getName() + "-team1");
    }

    public ArrayList<User> getTeam2(Channel lobbyChannel)
    {
        return teamMap.get(lobbyChannel.getName() + "-team2");
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

    public int getSizeOfLobby(Channel lobbyChannel)
    {
        return queList.get(lobbyChannel.getName()).size();
    }

    public void printList(Channel lobbyChannel)
    {
        System.out.println("Lobby:" + lobbyChannel.getName());
        for(User user : queList.get(lobbyChannel.getName()))
        {
            System.out.println("Username:" + user.getName());
        }
    }


    // Need to find a Fix for the 2Times Return (Ich brauche ja 2 Teams und deswegen muss ich eine Anzahl an Useren return)

    /**
     * Creates a Randomized Team by counting to 5 after a User gets Added to the first Team Array.
     * Using the Helper-Function {@link ScrimManager#removeFromQue(User, Channel)} to remove the User from the Hashmap.
     * Using the Helper-Function {@link ScrimManager#isUserAlreadyInTeam(User, ArrayList)} to check if the current Randomized User is already in a Team
     *
     * @param lobbyChannel
     * @return ArrayList<User>
     */
    public ArrayList<User> createRandomTeam(Channel lobbyChannel)
    {
        int counter = 0;
        ArrayList<User> queueMembers = queList.get(lobbyChannel.getName());
        ArrayList<User> teamMembers = new ArrayList<>();
        User addData = queueMembers.get(0);

        while(queueMembers.size() != 0)
        {
            if(counter < 1)
            {
                if(teamMembers.size() == 0)
                {
                    //System.out.println("Added" + addData.getName());
                    teamMembers.add(addData);
                    queueMembers.remove(addData);
                    removeFromQue(addData, lobbyChannel);
                }else
                {
                    while(isUserAlreadyInTeam(addData, teamMembers))
                    {
                        //System.out.println("Ok" + addData.getName());
                        for(User user : queueMembers)
                        {
                            if(!user.getId().contains(addData.getId()))
                            {
                                addData = user;
                            }
                        }
                    }
                    teamMembers.add(addData);
                    queueMembers.remove(addData);
                    removeFromQue(addData, lobbyChannel);
                }

            }else
            {
                break;
            }

            counter++;
        }

        return teamMembers;
    }

    public boolean isLobbyRunning(Channel lobbychannel)
    {
        try
        {
            return status.get(lobbychannel.getName());
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

}

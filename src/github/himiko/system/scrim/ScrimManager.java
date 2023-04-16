package github.himiko.system.scrim;

import github.himiko.Main;
import github.himiko.bot.BotBuilder;
import github.himiko.system.logger.LogCategory;
import github.himiko.system.scrim.exceptions.IllegalInteractionException;
import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ScrimManager {

    //LOBBY, LIST WITH USERES
    private static HashMap<String,ArrayList<User>> queList = new HashMap<>();
    private static Random RANDOM = new Random();

    public ScrimManager()
    {

    }

    /**
     * Starts The Scrims by sorting first the Teams
     * Creating then the Lobby-Channels for each Team
     * Them Moving the Players into it
     * @param lobbyChannel
     * @param event
     * @return
     */
    public boolean startScrim(Channel lobbyChannel, ButtonInteractionEvent event)
    {
        if(queList.get(lobbyChannel.getName()).size() == 10)
        {
            ArrayList<User> team1 = createRandomTeam(lobbyChannel);
            //Just for watching the Process of Creating the Teams (Only Visual for the Console)
            printList(lobbyChannel);
            ArrayList<User> team2 = createRandomTeam(lobbyChannel);

            ChannelAction<VoiceChannel> team1VoiceChannel = event.getGuild().createVoiceChannel(lobbyChannel.getName() + "-team1");
            team1VoiceChannel.submit();

            for(User user : team1)
            {
                System.out.println("Team1:" + user.getName());
                event.getGuild().getGuildChannelById(BotBuilder.channelManager.getChannelIDByName(lobbyChannel.getName() + "waiting-queue")).getGuild().moveVoiceMember(event.getGuild().getMemberById(user.getId()),team1VoiceChannel.complete()).queue();
            }

            ChannelAction<VoiceChannel> team2VoiceChannel = event.getGuild().createVoiceChannel(lobbyChannel.getName() + "-team2");
            team2VoiceChannel.submit();

            for(User user : team2)
            {
                System.out.println("Team2:" + user.getName());
                event.getGuild().getGuildChannelById(BotBuilder.channelManager.getChannelIDByName(lobbyChannel.getName() + "waiting-queue")).getGuild().moveVoiceMember(event.getGuild().getMemberById(user.getId()),team2VoiceChannel.complete()).queue();
            }

            return true;
        }

        return false;
    }

    /**
     * Adds Users to there Lobby by first using the Helper-Function {@link ScrimManager#doesUserExist(User, Channel)}
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
            Main.bot.scrimLogger.trace(this.getClass(), LogCategory.ERROR, "User is not in Queue!");
            return false;
        }

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
        User addData = queueMembers.get(RANDOM.nextInt(queueMembers.size()));

        while(queueMembers != null)
        {
            if(counter < 5)
            {
                if(teamMembers == null)
                {
                    teamMembers.add(addData);
                    queueMembers.remove(addData);
                    removeFromQue(addData, lobbyChannel);
                }else
                {
                    while(isUserAlreadyInTeam(addData, teamMembers))
                    {
                        addData = queueMembers.get(RANDOM.nextInt(queueMembers.size()));
                    }
                    teamMembers.add(addData);
                    queueMembers.remove(addData);
                    removeFromQue(addData, lobbyChannel);
                }

            }

            counter++;
        }

        return teamMembers;
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

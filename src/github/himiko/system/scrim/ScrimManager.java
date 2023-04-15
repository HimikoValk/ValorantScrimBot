package github.himiko.system.scrim;

import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.UserSnowflake;

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

    public boolean  addUserToList(User user, Channel lobbyChannel)
    {
        if(!doesUserExist(user, lobbyChannel))
        {
            if(queList.get(lobbyChannel.getName()) != null)
            {
                queList.get(lobbyChannel.getName()).add(user);
            }else
            {
                queList.put(lobbyChannel.getName(), new ArrayList<User>());
                queList.get(lobbyChannel.getName()).add(user);
            }

            System.out.println("Successfully Added User to Queue List!");
            return true;

        }else {
            System.out.println("User is already in Queue!");
            return false;
        }
    }

    public boolean removeFromQue(User user, Channel lobbyChannel)
    {
        if(doesUserExist(user, lobbyChannel))
        {
            queList.get(lobbyChannel.getName()).remove(user);
            System.out.println("Successfully removed User from Queue");
            return true;
        }else
        {
            System.out.println("User is not in Queue!");
            return false;
        }

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
    public ArrayList<User> createRandomTeam(Channel lobbyChannel)
    {
        int counter = 0;
        ArrayList<User> users = new ArrayList<>();

        return users;
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

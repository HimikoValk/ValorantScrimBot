package github.himiko.system.scrim.channel;

import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.ArrayList;

public class ChannelManager {
     private static ArrayList<VoiceChannel> channels = new ArrayList<>();
     private static ArrayList<VoiceChannel> teamChannels = new ArrayList<>();

     public void addChannel(VoiceChannel voiceChannel)
     {
         channels.add(voiceChannel);
         //System.out.println(voiceChannel.getId());
     }

     public void addTeamChannel(VoiceChannel voiceChannel)
     {
         teamChannels.add(voiceChannel);
     }

     public String getTeamChannelIDByName(String name)
     {
         for(VoiceChannel c : channels)
         {
             if(c.getName().contains(name))
             {
                 return c.getId();
             }
         }
         return "";
     }

    public String getChannelIDByName(String name)
    {
        for(VoiceChannel c : channels)
        {
            if(c.getName().contains(name))
            {
                return c.getId();
            }
        }

        return "";
    }
}

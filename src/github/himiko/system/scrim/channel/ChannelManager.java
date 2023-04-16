package github.himiko.system.scrim.channel;

import net.dv8tion.jda.api.entities.*;

import java.util.ArrayList;

public class ChannelManager {
     private static ArrayList<Category> categories = new ArrayList<>();
     private static ArrayList<VoiceChannel> channels = new ArrayList<>();
     private static ArrayList<VoiceChannel> teamChannels = new ArrayList<>();

     public void addCateogry(Category category)
     {
            categories.add(category);
     }

     public void addChannel(VoiceChannel voiceChannel)
     {
         channels.add(voiceChannel);
         //System.out.println(voiceChannel.getId());
     }

     public void addTeamChannel(VoiceChannel voiceChannel)
     {
         teamChannels.add(voiceChannel);
     }

     public Category getCategoryByName(String name)
     {
         for(Category c : categories)
         {
             if(c.getName().toLowerCase().contains(name.toLowerCase()))
             {
                 return c;
             }
         }

         return null;
     }

     public VoiceChannel getTeamChanelByName(String name)
     {
         for(VoiceChannel c : teamChannels)
         {
             if(c.getName().contains(name))
             {
                 return c;
             }
         }
         return null;
     }

     public String getTeamChannelIDByName(String name)
     {
         for(VoiceChannel c : teamChannels)
         {
             System.out.println(c.getName());
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

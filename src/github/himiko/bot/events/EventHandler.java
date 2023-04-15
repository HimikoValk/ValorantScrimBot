package github.himiko.bot.events;

import github.himiko.bot.events.listeners.ButtonListeners;
import github.himiko.bot.events.listeners.SlashCommandListeners;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.ShardManager;

import java.util.ArrayList;
import java.util.List;

public class EventHandler {

    private static List<ListenerAdapter> evListenerAdapterList = new ArrayList<>();

    public EventHandler(ShardManager shardManager)
    {
        evListenerAdapterList.add(new SlashCommandListeners());
        evListenerAdapterList.add(new ButtonListeners());

        for(ListenerAdapter ls : evListenerAdapterList)
        {
            shardManager.addEventListener(ls);
        }
    }
}

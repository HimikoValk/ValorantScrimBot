package github.himiko.system.statistics;

import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

public class StatisticManager {
    public static ArrayList<Stats> statisticList = new ArrayList<>();

    public void addStatistic(User user)
    {
        statisticList.add(new Stats(user, 0, 0));
    }

    public void addWinToStatistic(User user)
    {
        if(geStatisticByUser(user) == null)
        {
            addStatistic(user);
        }
        geStatisticByUser(user).setWins(geStatisticByUser(user).getWins() + 1);

    }

    public void addLoosToStatistic(User user)
    {
        if(geStatisticByUser(user) == null)
        {
            addStatistic(user);
        }

        geStatisticByUser(user).setLooses(geStatisticByUser(user).getLooses() + 1);
    }

    public Stats geStatisticByUser(User user)
    {
        for(Stats s : statisticList)
        {
            if(s.getUser().getId().contains(user.getId()))
            {
                return s;
            }
        }
        return null;
    }

    public String getStatisticsAsString(User user)
    {
        for(Stats s : statisticList)
        {
            if(s.getUser().getId().contains(user.getId()))
            {
                return "User:" + s.getUser().getName() +"\nWins:" + s.getWins() +"\nLooses:" + s.getLooses();
            }
        }
        return "";
    }

}

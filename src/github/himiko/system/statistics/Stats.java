package github.himiko.system.statistics;

import net.dv8tion.jda.api.entities.User;

public class Stats {
    private User user;
    private int wins;
    private int looses;

    public Stats(User user, int wins, int looses)
    {
        this.user = user;
        this.wins = wins;
        this.looses = looses;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLooses() {
        return looses;
    }

    public void setLooses(int looses) {
        this.looses = looses;
    }

}

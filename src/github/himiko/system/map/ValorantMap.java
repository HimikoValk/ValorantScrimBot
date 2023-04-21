package github.himiko.system.map;

public class ValorantMap {
    private final String mapName;

    private boolean isBanned = false;

    public ValorantMap(String mapName)
    {
        this.mapName = mapName;
    }

    public String getMapName() {
        return mapName;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }
}

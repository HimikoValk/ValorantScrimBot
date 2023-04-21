package github.himiko.system.map;

import github.himiko.Main;
import github.himiko.system.logger.LogCategory;

import java.util.ArrayList;

public class MapManager {

    private static ArrayList<ValorantMap> valorantMapList = new ArrayList<>();

    public MapManager()
    {
        valorantMapList.add(new ValorantMap("LOTUS"));
        valorantMapList.add(new ValorantMap("PEARL"));
        valorantMapList.add(new ValorantMap("FRACTURE"));
        valorantMapList.add(new ValorantMap("BREEZE"));
        valorantMapList.add(new ValorantMap("ICEBOX"));
        valorantMapList.add(new ValorantMap("BIND"));
        valorantMapList.add(new ValorantMap("HAVEN"));
        valorantMapList.add(new ValorantMap("SPLIT"));
        valorantMapList.add(new ValorantMap("ASCENT"));
    }

    public void addMap(ValorantMap valorantMap)
    {
        if(!doesMapExist(valorantMap))
            valorantMapList.add(valorantMap);
        else
            Main.bot.scrimLogger.trace(this.getClass(), LogCategory.WARNING, "Map already Exist!");
    }

    public boolean doesMapExist(ValorantMap valorantMap)
    {
        for(ValorantMap m : valorantMapList)
        {
            if(m.equals(valorantMap))
            {
                return true;
            }
        }

        return false;
    }


    public ArrayList<ValorantMap> getMapList()
    {
        return valorantMapList;
    }

}

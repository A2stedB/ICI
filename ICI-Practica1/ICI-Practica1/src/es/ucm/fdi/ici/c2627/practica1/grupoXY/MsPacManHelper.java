package es.ucm.fdi.ici.c2627.practica1.grupoXY;


import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

import java.util.EnumMap;
import es.ucm.fdi.ici.c2627.practica1.grupoXY.GhostData;

class MsPacManHelper
{

    static EnumMap<GHOST,GhostData> ghostDatas = new EnumMap<GHOST,GhostData>(GHOST.class);

    
    public static void init(Game game)
    {
        MsPacManHelper.initGhostData();
    }

    public static void tick(Game game)
    {
        
    }

    public static void initGhostData()
    {
        System.out.println("Inicializando los datos");
        for(GHOST ghost : GHOST.values())
        {
            GhostData ghostData = new GhostData();
            ghostData.type = ghost;
            ghostDatas.put(ghost, ghostData);
        }
    }

    public static void updateGhostData(Game game)
    {
        for(GHOST ghost : GHOST.values())
        {
            int node_index = game.getGhostCurrentNodeIndex(ghost); // Esto devuelve 1292 cuando esta en la carcel pero deberia devolver -1???
            MOVE last_move = game.getGhostLastMoveMade(ghost);
            boolean is_eatable = game.isGhostEdible(ghost);

            GhostData data = ghostDatas.get(ghost); // get() es por referencia

            data.node_index = node_index;
            data.last_move = last_move;
            data.is_eatable = is_eatable;
        }
    }
}

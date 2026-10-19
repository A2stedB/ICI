package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.actions;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class DefaultAction implements Action 
{

    public DefaultAction() 
    {

    }

    @Override
    public MOVE execute(Game game) {
        return MOVE.NEUTRAL;
    }

    @Override
    public String getActionId() {
        return "Default Action";
    }
}

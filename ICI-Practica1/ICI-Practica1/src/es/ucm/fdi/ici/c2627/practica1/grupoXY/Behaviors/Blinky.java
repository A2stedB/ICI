package es.ucm.fdi.ici.c2627.practica1.grupoXY.Behaviors;

import pacman.game.Game;
import pacman.game.Constants.MOVE;

public class Blinky extends GhostBehavior {
    @Override
    public MOVE Chase(Game game, int gNode, int pNode, MOVE lMove) {
    	return MOVE.DOWN;
    }
}
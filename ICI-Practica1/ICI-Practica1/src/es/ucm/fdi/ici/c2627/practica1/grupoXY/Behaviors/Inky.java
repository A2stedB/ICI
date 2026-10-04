package es.ucm.fdi.ici.c2627.practica1.grupoXY.Behaviors;

import es.ucm.fdi.ici.c2627.practica1.grupoXY.GhostsHelper;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.GHOST;

public class Inky extends GhostBehavior {
    @Override
    public MOVE Chase(Game game, int gNode, int pNode, MOVE lMove) {
    	//Encierra a pacman combinandose con Blinky
    	int targetNode = pNode;
    	MOVE pacDir = game.getPacmanLastMoveMade();
        
    	int blinkyIdx = game.getGhostCurrentNodeIndex(GHOST.BLINKY);
    	if (blinkyIdx != -1 && pacDir != null) {
            int distBlinkyPac = game.getShortestPathDistance(blinkyIdx, pNode);
            int[] pacAhead = game.getShortestPath(pNode, game.getPacManInitialNodeIndex(), pacDir);
            
            if (pacAhead != null && pacAhead.length > 0) {
                int pasosProyeccion = Math.max(1, (distBlinkyPac / 2) * 3);
                int indiceProyeccion = Math.min(pacAhead.length - 1, pasosProyeccion);
                
                int puntoFlanqueo = pacAhead[indiceProyeccion];
                
                if (game.getShortestPathDistance(gNode, pNode, lMove) <= indiceProyeccion) targetNode = pNode;
                else targetNode = puntoFlanqueo;
            }
        }  
        
        MOVE bestMove = game.getApproximateNextMoveTowardsTarget(gNode, targetNode, lMove, DM.PATH);
        // Si el movimiento óptimo nos metiera en un bucle circular sobre nosotros mismos, 
        // elegimos la segunda mejor opción para romperlo.
        int nextNode = game.getNeighbour(gNode, bestMove);
        if (nextNode != -1 && game.isJunction(nextNode)) {
            int[] jctInfo = GhostsHelper.getNextJuctionOnWay(game, gNode, game.getShortestPath(gNode, targetNode, bestMove));
            if (jctInfo[0] == gNode) {
                MOVE[] options = game.getPossibleMoves(gNode, lMove);
                for (MOVE m : options) {
                    if (m != bestMove) { bestMove = m; break; }
                }
            }
        }
        
        return bestMove;
    }
}
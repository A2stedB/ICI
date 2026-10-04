package es.ucm.fdi.ici.c2627.practica1.grupoXY.Behaviors;

import pacman.game.Game;
import es.ucm.fdi.ici.c2627.practica1.grupoXY.GhostsHelper;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;

public class Blinky extends GhostBehavior {
    @Override
    public MOVE Chase(Game game, int gNode, int pNode, MOVE lMove) {
    	int targetNode = pNode;
        MOVE pacDir = game.getPacmanLastMoveMade();
        
        if (pacDir != null) {
            //Buscamos cruce anticipandonos a ir a él de la forma mas rapida en lugar de seguirle como un perro
            int[] pacJctInfo = GhostsHelper.getNextJuctionOnWay(game, pNode, game.getShortestPath(pNode, game.getPacManInitialNodeIndex(), pacDir));
            int nextPacJct = pacJctInfo[0];
            int stepsToJct = pacJctInfo[1];
            
            //Si MsPac está a 12 nodos del tunel o es su proximo objetivo, vamos a acorralarle al cruce o al lado contrario del mapa
            if (nextPacJct == -1) {
            	int[] junctions = game.getJunctionIndices();
                targetNode = game.getFarthestNodeIndexFromNodeIndex(pNode, junctions, DM.PATH);
            }
            else if (stepsToJct > 0 && stepsToJct < 12) targetNode = nextPacJct;
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
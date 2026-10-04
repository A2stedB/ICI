package es.ucm.fdi.ici.c2627.practica1.grupoXY.Behaviors;

import es.ucm.fdi.ici.c2627.practica1.grupoXY.Helper;
import es.ucm.fdi.ici.c2627.practica1.grupoXY.GhostsHelper;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public abstract class GhostBehavior {
    public MOVE Avoid(Game game, int gNode, int pNode, MOVE lMove, GHOST type) {
        MOVE[] options = game.getPossibleMoves(gNode, lMove);
        if (options.length == 1) return options[0];

        int closest_PP = Helper.PACMAN_get_closest_power_pill(game);
        MOVE bestMove = options[0];
        int maxWeight = Integer.MIN_VALUE;
        
        int dest = closest_PP;
        if (closest_PP == -1) {
            int[] activePills = game.getActivePillsIndices();
            dest = (activePills.length > 0) ? game.getClosestNodeIndexFromNodeIndex(pNode, activePills, DM.PATH) : pNode;
        }
        
        int[] pacWay = game.getShortestPath(pNode, dest, game.getPacmanLastMoveMade());

        for (MOVE m : options) {
            int[] jctInfo = GhostsHelper.getNextJuctionOnWay(game, gNode, game.getShortestPath(gNode, pNode, m));
            int nextJct = jctInfo[0];
            if (nextJct == -1) continue;

            // Peso base: Distancia real en el futuro (queremos maximizarla)
            int weight = game.getShortestPathDistance(nextJct, pNode);

            //Para añadir dispersión de los fantasmas y evitar los combos en pocas lineas le daremos una dirección favorita a cada fantasma.
            if (m.ordinal() == type.ordinal()) weight += 5;
            
            //Penalizo masivamente para casi siempre cancelar las decisiones que tengan que ver con elegir un camino circular.
            if (nextJct == gNode) {
                weight -= 60.0;
            }
            
            //Si el nodo se encuentra en el camino de pacman, penalizo.
            if (pacWay != null) {
                for (int i = 0; i < pacWay.length; i++) {
                    if (pacWay[i] == nextJct) {
                        weight -= (40.0 / (i + 1)); 
                        break;
                    }
                }
            }
            
            //Si el nodo al que voy te acerca a una píldora, penalizo. Prefiero desviarme y hacer decidir a Pacman si yo o la píldora. Pero no ambas.
            if (closest_PP != -1) {
                int neighbour = game.getNeighbour(gNode, m);
                int currDis = game.getShortestPathDistance(gNode, closest_PP);
                int newDis = game.getShortestPathDistance(neighbour, closest_PP);
                
                if (newDis < currDis) {
                    weight -= (double) (currDis - newDis) * 3.0; 
                }
            }
            
            if (weight > maxWeight) {
                maxWeight = weight;
                bestMove = m;
            }
        }
        return bestMove;
    }
    
    public MOVE Scatter(Game game, int gNode, MOVE lMove, int corner) {
    	return game.getApproximateNextMoveTowardsTarget(gNode, game.getPowerPillIndices()[corner], lMove, DM.PATH);
    }
    
    public abstract MOVE Chase(Game game, int gNode, int pNode, MOVE lMove);
}
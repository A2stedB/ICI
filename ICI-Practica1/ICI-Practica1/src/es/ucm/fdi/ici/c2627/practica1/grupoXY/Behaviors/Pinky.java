package es.ucm.fdi.ici.c2627.practica1.grupoXY.Behaviors;

import es.ucm.fdi.ici.c2627.practica1.grupoXY.GhostsHelper;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;

public class Pinky extends GhostBehavior {
    @Override
    public MOVE Chase(Game game, int gNode, int pNode, MOVE lMove) {
    	//Embosca apuntando 4 pasos por delante del camino de Pac-Man
    	int targetNode = pNode;
    	MOVE pacDir = game.getPacmanLastMoveMade();
    	
        if (pacDir != null) {
            //Buscamos el primer cruce que tiene pacman delante
        	int[] pacPathToJct = game.getShortestPath(pNode, game.getPacManInitialNodeIndex(), pacDir);
        	int[] firstJctInfo = GhostsHelper.getNextJuctionOnWay(game, pNode, pacPathToJct);
            int firstJct = firstJctInfo[0];
            int stepsToJct = firstJctInfo[1];
            if (firstJct != -1) {
            	targetNode = firstJct;
            	//Calcular dirección de pacman cuando llegue al cruce, si es que no es el siguiente nodo justo
            	MOVE realDir = (stepsToJct <= 1 || pacPathToJct == null || pacPathToJct.length < stepsToJct) ? pacDir : game.getMoveToMakeToReachDirectNeighbour(pacPathToJct[stepsToJct - 2], firstJct);
                MOVE[] salidasCruce = game.getPossibleMoves(firstJct, realDir);
                MOVE mejorPasillo = MOVE.NEUTRAL;
                int maxPills = -1;
                
                for (MOVE salida : salidasCruce) {
                    //Buscamos el cruce tras este
                    int[] infoFinPasillo = GhostsHelper.getNextJuctionOnWay(game, firstJct, game.getShortestPath(firstJct, game.getPacManInitialNodeIndex(), salida));
                    if (infoFinPasillo[0] == -1) continue;
                    
                    // Contamos cuántas píldoras activas hay en este pasillo específico entre cruces
                    int[] pathNodes = game.getShortestPath(firstJct, infoFinPasillo[0], salida);
                    int pillsEnPasillo = 0;
                    for (int node : pathNodes) {
                        int pillIdx = game.getPillIndex(node);
                        // Si hay una píldora normal válida y sigue disponible en el mapa
                        if (pillIdx >= 0 && game.isPillStillAvailable(pillIdx)) {
                            pillsEnPasillo++;
                        }
                    }
                    
                    // Nos quedamos con el pasillo que tenga más comida
                    if (pillsEnPasillo > maxPills) {
                        maxPills = pillsEnPasillo;
                        mejorPasillo = salida;
                    }
                }
                
                // Si encontramos el pasillo con más píldoras, Pinky viaja al cruce del final de ese pasillo
                if (mejorPasillo != MOVE.NEUTRAL) {
                    int[] jctFinal = GhostsHelper.getNextJuctionOnWay(game, firstJct, game.getShortestPath(firstJct, game.getPacManInitialNodeIndex(), mejorPasillo));
                    if (jctFinal[0] != -1) targetNode = jctFinal[0];
                }
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
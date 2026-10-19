package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.ghosts.transitions;

import es.ucm.fdi.ici.fsm.Transition;
import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.ghosts.GhostsInput;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.ghosts.GhostsBrain;

import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;


public class attackToAmbushTransition implements Transition {
	private boolean _opposite;
	private GhostsBrain _brain;
	
    public attackToAmbushTransition(boolean opposite, GhostsBrain brain) {
    	_opposite = opposite;
    	_brain = brain;
    }

    @Override
    public boolean evaluate(Input in) {
        GhostsInput input = (GhostsInput) in;
        
        boolean tired = _brain.getGhostsAttacking() >= 1
                || catchMsPac(input);
        
        if (_opposite && !tired) _brain.increaseAttacking(1);
        else if (!_opposite && tired) _brain.increaseAttacking(-1);
        
        return _opposite? !tired : tired;
    }

    @Override
    public String toString() {
        return "Ya hay un fantasma atacando o si embosco puedo cazar a Pacman seguro.";
    }
    
    //Para motivar a cazar a todos los fantasmas, diré que evalúe a todos.
    //Si la situación es que hay fantasmas aunque seamos nosotros mismos que estan en caminos a los que se dirige pacman excepto 1,
    //digo que le atrapamos 100% si algún fantasma aunque seamos nosotros, puede tapar ese cruce antes de que llegue pacMan si lo toma.
    private boolean catchMsPac(GhostsInput in){
    	Game g = in.getGame();
    	MOVE lastMove = g.getPacmanLastMoveMade();
    	int pacmanNeighbour = g.getNeighbour(g.getPacmanCurrentNodeIndex(), lastMove);
    	if (pacmanNeighbour == -1) return false;
    	int[] pacPathToJct = g.getShortestPath(pacmanNeighbour, g.getPacManInitialNodeIndex(), lastMove);
    	int[] firstJctInfo = _brain.getNextJuctionOnWay(g, pacmanNeighbour, pacPathToJct);
        int firstJct = firstJctInfo[0];
        
        boolean canCatch = false;
        
        if (firstJct != -1) {
        	MOVE[] pacmanMoves = g.getPossibleMoves(firstJct, lastMove);
        	int cubiertos = 0;
        	//Para cada camino miramos cuantos siguientes cruces tienen fantasmas.
        	for (int i = 0; i < pacmanMoves.length; i++) {
        		int pNextNeighbour = g.getNeighbour(firstJct, pacmanMoves[i]);
        		int[] pacPathToNextJct = g.getShortestPath(pNextNeighbour, g.getPacManInitialNodeIndex(), pacmanMoves[i]);
            	
        		int j = 0;
        		boolean enc = false;
        		
        		while (j < pacPathToNextJct.length && !enc) {
        			if (pacPathToNextJct[j] != -1 && g.isJunction(pacPathToNextJct[j])) {
        				enc = true;
        			}
        			else {
        				int m = 0;
        				GHOST[] ghosts = GHOST.values();
        				boolean enc2 = false;
        				while(m < GHOST.values().length && !enc2) {
        					if (g.getGhostCurrentNodeIndex(ghosts[m]) == pacPathToNextJct[j]) {
        						enc2=true;
        						enc=true;
        						cubiertos++;
        					}
        					m++;
        				}
        			}
        			j++;
        		}
        	}
        	
        	canCatch = cubiertos >= pacmanMoves.length - 1;
        }
    	
    	return canCatch;
    }
}

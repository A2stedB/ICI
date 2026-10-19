package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.ghosts.actions;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game; 

public class searchPillsAction implements Action {

    private final GHOST ghost;      // qué fantasma, sí; el Game, nunca
    private int _porcentaje;
    public searchPillsAction(GHOST ghost, int por) {
        this.ghost = ghost;
        _porcentaje = por;
    }

    @Override
    public MOVE execute(Game game) {
    	return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), getTarget(game), game.getGhostLastMoveMade(ghost), DM.PATH);
    }

    @Override
    public String getActionId() {
        return ghost + " search Pills";
    }
    
    private int getTarget(Game game) {
    	int[] activePillsNodes = game.getActivePillsIndices();
        
        int minX = 0, maxX = 0;
        int minY = 0, maxY = 0;
        
        int numNodes = game.getNumberOfNodes();
        for (int i = 0; i < numNodes; i++) {
            int x = game.getNodeXCood(i), y = game.getNodeYCood(i);
            if (x > maxX) maxX = x;
            if (y > maxY) maxY = y;
        }
        
        int centroX = maxX / 2, centroY = maxY / 2;
        int[] pills = {0,0,0,0};
        int max = 0;
        
        for (int pillNodeIndex : activePillsNodes) {
            int x = game.getNodeXCood(pillNodeIndex);
            int y = game.getNodeYCood(pillNodeIndex);

            if (x <= centroX && y <= centroY) {
            	pills[0]++;
            	if (pills[0] > max) max = pills[0];
            }
            else if (x > centroX && y <= centroY) {
            	pills[1]++;
            	if (pills[1] > max) max = pills[1];
            }
            else if (x <= centroX && y > centroY) {
            	pills[2]++;
            	if (pills[2] > max) max = pills[2];
            }
            else {
            	pills[3]++;
            	if (pills[3] > max) max = pills[3];
            }
        }
        
        int[] corners=  {0,0,0,0};
        int corner = 0;
        
        if (pills[0] >= max * (_porcentaje/100.0)) {
        	corners[corner] = maxX * (maxY-1) + 1;
        	corner++;
        }
        if (pills[1] >= max * (_porcentaje/100.0)) {
        	corners[corner] = maxX * maxY;
        	corner++;
        }
        if (pills[2] >= max * (_porcentaje/100.0)) {
        	corners[corner] = 0;
        	corner++;
        }
        if (pills[3] >= max * (_porcentaje/100.0)) {
        	corners[corner] = maxX;
        	corner++;
        }

        return corners[ghost.ordinal()%corner];
    }
}
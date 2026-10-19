package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.ghosts.transitions;

import es.ucm.fdi.ici.fsm.Transition;
import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.ghosts.GhostsInput;

import pacman.game.Game;
import pacman.game.Constants.GHOST;

public class CornerWithoutPillsTransition implements Transition {
	private int _porcentaje;
	private boolean _opposite;
	
    public CornerWithoutPillsTransition(boolean opposite, int porcentaje) {
    	_porcentaje = porcentaje;
    	_opposite = opposite;
    }

    @Override
    public boolean evaluate(Input in) {
        GhostsInput input = (GhostsInput) in;
        return _opposite? !lessThan(in.getGame()) : lessThan(in.getGame());
    }

    @Override
    public String toString() {
        return "No todas las esquinas tienen píldoras >= 50% del número de píldoras en la esquina que más píldoras tiene.";
    }
    
    public boolean lessThan(Game game) {
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
        
        int i = 0;
        boolean enc = false;
        
        while (i < pills.length && !enc) {
        	enc = pills[i] < max * (_porcentaje/100.0);
        	i++;
        }
        
        return enc;
    }
}

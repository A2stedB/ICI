package es.ucm.fdi.ici.c2627.practica1.grupoXY;

import java.util.EnumMap;
import java.util.Map;

import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

import es.ucm.fdi.ici.c2627.practica1.grupoXY.Behaviors.GhostBehavior;
import es.ucm.fdi.ici.c2627.practica1.grupoXY.Behaviors.Inky;
import es.ucm.fdi.ici.c2627.practica1.grupoXY.Behaviors.Blinky;
import es.ucm.fdi.ici.c2627.practica1.grupoXY.Behaviors.Pinky;
import es.ucm.fdi.ici.c2627.practica1.grupoXY.Behaviors.Sue;

import pacman.game.Game;

public class Ghosts extends pacman.controllers.GhostController
{
    private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
    private final EnumMap<GHOST, GhostBehavior> behaviors = new EnumMap<>(Map.of(
    	    GHOST.BLINKY, new Blinky(),
    	    GHOST.PINKY,   new Pinky(),
    	    GHOST.INKY,     new Inky(),
    	    GHOST.SUE,       new Sue()
    	));
    
    //Contadores para el modo de scatter
    private final int scatterPhases = 2, scatterTime = 100, scatterCooldown = 400, scatterReduction = 45;
    private int scatterCurrPhases = scatterPhases, scatterCurrTime = scatterTime, scatterCurrCooldown = 0;
    
    private boolean scatterMode = true;
    
    //Como nada avisa cuando se han reseteado los fantasmas, usaremos el nivel en el que está para cuando cambie de nivel
    //Y usaremos las vidas para cuando muere y se reinicia el nivel, así reseteamos los valores del modo scatter/chase.
    private int lastFrameLives = 0, lastFrameLvlTime = 0; //Usado también para calcular el deltaTime
    
    public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
    	int currLvlTime = game.getCurrentLevelTime();
    	
    	if (game.getPacmanNumberOfLivesRemaining() < lastFrameLives || lastFrameLvlTime > currLvlTime) {
    		scatterCurrPhases = scatterPhases;
    		scatterCurrTime = scatterTime;
    		scatterCurrCooldown = 0;
    	}
    	
    	int pacman_node_index = game.getPacmanCurrentNodeIndex();
    	
        for (GHOST ghostType : GHOST.values()) {
        	
            //Si el fantasma no requiere de ninguna acción, se pasa a evaluar el siguiente fantasma.
            if (!game.doesGhostRequireAction(ghostType)) continue;
            
            MOVE last_move = game.getGhostLastMoveMade(ghostType);
            int ghost_node_index = game.getGhostCurrentNodeIndex(ghostType);
            MOVE next_move = MOVE.NEUTRAL;
            
            //Si requiere una acción, evaluaremos si el fantasma debe huir o perseguir. Dando prioridad a no darle puntos a pacman, es decir, huir si es necesario.
            //Para evaluarlo, usaremos la siguiente lista de preguntas:
            // - ¿Soy comestible y si voy a por MsPac me come con el tiempo de inmunidad que le queda?
            // - ¿MsPac está lo suficientemente cerca de una Power Pill como para que pueda comerla y alcanzarme?
            // - ¿Estoy en ScatterMode?
            if (game.isGhostEdible(ghostType) || GhostsHelper.pacman_dangerously_close_to_PP(game, ghost_node_index, pacman_node_index, last_move)) next_move = behaviors.get(ghostType).Avoid(game, ghost_node_index, pacman_node_index, last_move, ghostType);
            else if (scatterCurrTime > 0.0f) next_move = behaviors.get(ghostType).Scatter(game, ghost_node_index, last_move, ghostType.ordinal());
            else next_move = behaviors.get(ghostType).Chase(game, ghost_node_index, pacman_node_index, last_move);
            
            moves.put(ghostType, next_move);
        }
        
        //Para facilitar la dispersión de los fantasmas por el mapa, usaremos el scatter del pacman original, es decir,
        //No solo se dispersan al inicio, sino un total de 4 veces por ronda con una diferencia X de tiempo entre estas fases.
        //Lo que conseguimos con esto es que los fantasmas se dispersen mejor a lo largo de la partida.
        if (scatterCurrPhases > 0) {
        	if (scatterCurrTime > 0.0f) scatterCurrTime -= lastFrameLvlTime > currLvlTime ? currLvlTime : currLvlTime-lastFrameLvlTime;
            else if (scatterCurrCooldown > 0.0f) scatterCurrCooldown -= 1;
            else {
            	if (scatterMode) {
            		scatterCurrCooldown = scatterCooldown;
            		scatterCurrPhases--;
            	}
            	else scatterCurrTime = scatterTime - (scatterReduction * (scatterPhases-scatterCurrPhases));
            	scatterMode = !scatterMode;
            }
        }
        
        lastFrameLives = game.getPacmanNumberOfLivesRemaining();
        lastFrameLvlTime = currLvlTime;
        
        return moves;
    }
}
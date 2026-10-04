package es.ucm.fdi.ici.c2627.practica1.grupoXY;

import java.util.EnumMap;
import java.util.Map;

import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import static pacman.game.Constants.EDIBLE_TIME;
import static pacman.game.Constants.EDIBLE_TIME_REDUCTION;
import static pacman.game.Constants.LEVEL_RESET_REDUCTION;

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
    private final int scatterPhases = 4, scatterTime = 150, scatterCooldown = 400, scatterReduction = 45;
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
            
            //En caso contrario tendrémos que evaluar si el fantasma debe huir o debe hacer su papel de persecución.
            //Para evaluarlo, usaremos la siguiente lista de preguntas:
            // - ¿Soy comestible y si voy a por MsPac me come con el tiempo de inmunidad que le queda?
            // - ¿MsPac está lo suficientemente cerca de una Power Pill como para que pueda comerla y alcanzarme?
            // - ¿Estoy en ScatterMode?
            
            //Ponemos ese límite para comprobar la cercanía a una pildora porque sabemos que al moverse a mitad de velocidad
            //Si la distancia entre pacman y la pildora es menor que la diferencia entre pacman y el fantasma para que lo pille menos la diferencia real entre pacman y el fantasma, todo a la mitad, pacman puede pillar al fantasma si se come esa píldora.
            //(Dividirlo todo a la mitad es porque se que mientras pacman se acerca a la pildora, el fantasma se aleja, por lo que para alcanzar la diferencia de distancia, se tarda la mitad de tiempo.)
            int dangerousDistance = (int) ((((getEdibleTime(game)/2)+1)-game.getDistance(pacman_node_index, ghost_node_index, game.getPacmanLastMoveMade(), DM.PATH))/2);
            if (game.isGhostEdible(ghostType) || pacman_close_to_power_pill(game, dangerousDistance)) {
                next_move = behaviors.get(ghostType).Avoid(game, ghost_node_index, pacman_node_index, last_move);
            }
            //Para facilitar la dispersión de los fantasmas por el mapa, usaremos el scatter del pacman original, es decir,
            //No solo se dispersan al inicio, sino un total de 4 veces por ronda con una diferencia X de tiempo entre estas fases.
            //Lo que conseguimos con esto es que los fantasmas se dispersen mejor a lo largo de la partida.
            else if (scatterCurrTime > 0.0f) {
            	next_move = behaviors.get(ghostType).Scatter(game, ghost_node_index, last_move, ghostType.ordinal());
            }
            else {
            	next_move = game.getApproximateNextMoveTowardsTarget(ghost_node_index, pacman_node_index, last_move, DM.PATH);
            }
            
            moves.put(ghostType, next_move);
        }
        
        //Lógica de contadores, nunca se da el caso en el que ambos son mayores de 0,
        //Por lo tanto el activo (>0) se resta con el tiempo y al llegar a 0 activa el otro contador (controlamos con bool)
        //Cada vez que cambia de nuevo a la fase de cooldown, restamos una fase, así cuando llegue a 0 no volverá a estar en scatterMode.
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
    
    private int getEdibleTime(Game game) {
    	
    	double reduccionExponencial = Math.pow(EDIBLE_TIME_REDUCTION, game.getCurrentLevel() % LEVEL_RESET_REDUCTION);
    	int edibleTime = (int) (EDIBLE_TIME * reduccionExponencial);
            
    	return edibleTime;
    }
    
    private boolean pacman_close_to_power_pill(Game game,int limit)
    {
        int pacman_node_index = game.getPacmanCurrentNodeIndex();
        int closest_power_pill_index = Helper.PACMAN_get_closest_power_pill(game);

        if(closest_power_pill_index != -1 && game.getDistance(pacman_node_index, closest_power_pill_index, game.getPacmanLastMoveMade(), DM.PATH) < limit)
        {
            return true;
        }
        return false;
    }
}

package es.ucm.fdi.ici.c2627.practica1.grupoXY;

import static pacman.game.Constants.EDIBLE_TIME;
import static pacman.game.Constants.EDIBLE_TIME_REDUCTION;
import static pacman.game.Constants.LEVEL_RESET_REDUCTION;

import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;

public class GhostsHelper {
	public static int getEdibleTime(Game game) {
    	double reduccionExponencial = Math.pow(EDIBLE_TIME_REDUCTION, game.getCurrentLevel() % LEVEL_RESET_REDUCTION);
    	int edibleTime = (int) (EDIBLE_TIME * reduccionExponencial);
            
    	return edibleTime;
    }
    
    public static int[] getNextJuctionOnWay(Game game, int g_index, int[] way) {
    	int index = 0;
    	boolean enc = false;
        
        while (index < way.length && !enc) {
        	enc = game.isJunction(way[index]);
        	if (!enc) index++;
        }
        
        if (enc) return new int[]{way[index], index+1};
        else return new int[] {-1,-1};
    }
    
    //Evaluaremos si pacman está cerca de una píldora que al comer pueda comerme luego a mi, pero no desde mi cruce, sino desde el siguiente cruce suponiendo
    // que voy a por pacman, si es peligroso, huyo, si no, sigo yendo por él.
    // La anticipación del cruce es porque si tomo la decisión en el mismo cruce, quizás sea demasiado tarde o incluso no esté en peligro hasta avanzar un poco más donde ya no podré tomar una decisión.
    public static boolean pacman_dangerously_close_to_PP(Game game, int gIdx, int pIdx, MOVE last_move) {
        int closest_power_pill_index = Helper.PACMAN_get_closest_power_pill(game);
        if (closest_power_pill_index == -1) return false;
        
        int stepsToPP = game.getShortestPathDistance(gIdx, closest_power_pill_index, last_move);
        int pacStepsToPP = game.getShortestPathDistance(pIdx, closest_power_pill_index, game.getPacmanLastMoveMade());
        //Si estoy mas cerca de la pildora, sigo con mi recorrido hasta pillarle (hasta que no le pille siempre estaré mas cerca de la pildora)
        if (stepsToPP <= pacStepsToPP) return false;
        
        
        int[] nextJuctionInfo = getNextJuctionOnWay(game, gIdx, game.getShortestPath(gIdx, pIdx, last_move));
        int nextJuction = nextJuctionInfo[0];
        int stepsToJunction = nextJuctionInfo[1];
        if (nextJuction == -1) return false;
        
        int[] msPacWay = game.getShortestPath(pIdx, closest_power_pill_index, game.getPacmanLastMoveMade());
        
        //Si yo tardo más en llegar a la esquina de lo que Pac-Man tarda 
        //en comerse la píldora, me volveré azul dentro del pasillo sin poder escapar. Huyo.
        if (stepsToJunction >= msPacWay.length) {
            return true;
        }
        
        int pacPos = (msPacWay.length == 0) ? closest_power_pill_index : msPacWay[stepsToJunction-1];
        
        int PPPDistance = (int) game.getDistance(pacPos, closest_power_pill_index, game.getPacmanLastMoveMade(), DM.PATH);
        int PGDistance = (int) game.getDistance(pacPos, nextJuction, game.getPacmanLastMoveMade(), DM.PATH);
        
        //Ponemos ese límite para comprobar la cercanía a una pildora porque sabemos que al moverse a mitad de velocidad
        //Si la distancia entre pacman y la pildora es menor que la diferencia entre pacman y el fantasma para que lo pille menos la diferencia real entre pacman y el fantasma, todo a la mitad, pacman puede pillar al fantasma si se come esa píldora.
        //(Dividirlo todo a la mitad es porque se que mientras pacman se acerca a la pildora, el fantasma se aleja, por lo que para alcanzar la diferencia de distancia, se tarda la mitad de tiempo.)
        int dangerousDistance = (int) ((((getEdibleTime(game)/2)+1)-PGDistance)/2);
        
        if(PPPDistance < dangerousDistance) return true;
        else return false;
    }
}
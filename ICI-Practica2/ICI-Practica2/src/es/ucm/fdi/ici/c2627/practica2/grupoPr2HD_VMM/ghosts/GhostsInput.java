package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.ghosts;

import static pacman.game.Constants.EDIBLE_TIME;
import static pacman.game.Constants.EDIBLE_TIME_REDUCTION;
import static pacman.game.Constants.LEVEL_RESET_REDUCTION;

import java.util.EnumMap;

import es.ucm.fdi.ici.Input;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.DM;
import pacman.game.Game;

public class GhostsInput extends Input {

    // Sin inicializar aquí: parseInput() se ejecuta dentro de super(game),
    // antes que los inicializadores de esta clase, y estos lo machacarían.
    private EnumMap<GHOST, Boolean> comestible;
    private EnumMap<GHOST, Boolean> encerrado;
    private double distanciaPacManAPildora;
    private final int scatterPhases = 4, scatterTime = 100, scatterCooldown = 400, scatterReduction = 12;
    private int scatterCurrPhases = 0, scatterNextTime, scatterLastTime;
    private int edibleTime;
    
    public GhostsInput(Game game, int lastScatter, int nextScatter) {
    	scatterNextTime = nextScatter;
    	scatterLastTime = lastScatter;
        super(game);
    }

    @Override
    public void parseInput() {
        comestible = new EnumMap<>(GHOST.class);
        encerrado = new EnumMap<>(GHOST.class);
        
        for (GHOST g : GHOST.values()) {
            comestible.put(g, game.isGhostEdible(g));
            encerrado.put(g, game.getGhostLairTime(g) > 0);
        }

        int pacman = game.getPacmanCurrentNodeIndex();
        distanciaPacManAPildora = Double.MAX_VALUE;           // sin píldoras: infinitamente lejos
        for (int pp : game.getActivePowerPillsIndices()) {    // las que quedan, no todas
            double d = game.getDistance(pacman, pp, DM.PATH);
            distanciaPacManAPildora = Math.min(d, distanciaPacManAPildora);
        }
        
        //Medida de distribución
        if (game.getCurrentLevelTime() >= scatterNextTime && scatterCurrPhases < scatterPhases) {
        	//Decimos que el siguiente scatter empieza cuando termine el scatter actual y pase el tiempo de espera
        	scatterLastTime = scatterNextTime;
        	scatterNextTime = scatterNextTime + (scatterTime - (scatterReduction*scatterCurrPhases)) + scatterCooldown;
        }
        
        //Calcular edible time total
        double reduccionExponencial = Math.pow(EDIBLE_TIME_REDUCTION, game.getCurrentLevel() % LEVEL_RESET_REDUCTION);
    	edibleTime = (int) (EDIBLE_TIME * reduccionExponencial);
    }

    public boolean esComestible(GHOST g)       { return comestible.get(g); }
    public boolean estaEncerrado(GHOST g)      { return encerrado.get(g); }
    public double getDistanciaPacManAPildora() { return distanciaPacManAPildora; }
    public int getScatterTimeLeft()            { return (scatterTime - (scatterReduction*scatterCurrPhases)) - (game.getCurrentLevelTime() - scatterLastTime);}
    public int getLastScatterTime()            { return scatterLastTime;}
    public int getNextScatterTime()            { return scatterNextTime;}
    public int getEdibleTime()                 { return edibleTime;}
}
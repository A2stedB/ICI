package es.ucm.fdi.ici.c2627.practica1.grupoXY;

import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

import java.util.ArrayList;

import org.apache.commons.lang.ObjectUtils.Null;
import org.graphstream.ui.graphicGraph.stylesheet.Color;

import net.sourceforge.jFuzzyLogic.fcl.FclParser.linguistic_term_return;

import java.util.EnumMap;

import es.ucm.fdi.ici.c2627.practica1.grupoXY.MsPacManHelper;

public class MsPacMan extends pacman.controllers.PacmanController
{
    private Integer limit = 30;
    private Boolean is_initialized;
    private MsPacManMap map = new MsPacManMap();
    private static Boolean DEBUG = true;

    public MsPacMan()
    {
        MsPacManHelper.initGhostData();
        is_initialized = false;
    }

    // La idea es tener en todo el momento los datos de los fantasmas, y tomar una decision en funcion de los datos
    // Coger la distancia euclidea y el ultimo movimiento, en funcion de esos dos datos mover el pacman

    // La idea seria intentar pillar todas las pildoras normales, y si una fantasma esta a cierta distancia euclidea, ir por una pildora (puede que se quede en bucle lol)
    // O otra idea es mantener siempre a una distancia euclidea de una pildora, marcar esos nodos como un zona, y desplazamos de zona en zona, pero esto es como complicarme la vida

    public MOVE getMove(Game game, long timeDue)
    {
        if(!is_initialized)
        {
            // inicializar las cosas por primera vez, los datos y los precalculos
            map.reservar(game);
        }

        int index = game.getPacmanCurrentNodeIndex();

        if(!game.isJunction(index))
        {
            // 
            // Lo que se puede hacer aqui es en vez devolver directamente, continuar haciendo calculos
            // Como por ejemplo calcular el mapa de influencia de los fantasmas a dos pasos, que es la idea original
            // 
            // La cosa aqui es el trade off de Prediccion vs Realidad. Que informacion me ofrece la Prediccion?
            //
            return MOVE.NEUTRAL; 
        }

        // if (DEBUG) {
        //     depurar(game);
        // }

        MsPacManHelper.updateGhostData(game);
        map.calculateMap(game);

        MOVE mejor = MOVE.NEUTRAL;
        double mejorValor = Double.NEGATIVE_INFINITY;
        for (MOVE m : game.getPossibleMoves(index, game.getPacmanLastMoveMade())) {
            double v = valorarTramo(game, index, m);
            if (v > mejorValor) {
                mejorValor = v;
                mejor = m;
            }
        }
        String mejoString = mejor.toString();
        return mejor;
    };
    

    private double utilidad(int n) 
    {
        return map.pill_weight * map.pillMap[n] 
                + map.pill_weight * map.pillMap[n] // hacer que sea dinamico
                - map.danger_ghost_weight * map.dangerMap[n] 
                + map.eatable_ghost_weight * map.eatableGhostMap[n];
    }

    // private void depurar(Game game) {
    //     double escala = Math.max(map.pill_weight + map.power_pill_weight+map.eatable_ghost_weight,map.danger_ghost_weight);
    //     for (int n = 0; n < map.pillMap.length; n++) {
    //         double u = utilidad(n) / escala;                  // entre -1 y 1
    //         int tramo = (int) Math.min(4, Math.abs(u) * 5);   // 0..4
    //         if (Math.abs(u) < 0.02) continue;                 // el cero no se pinta
    //         int alfa = 40 + 45 * tramo;
    //         Color c = (u > 0) ? new Color(245, 180, 0, alfa) : new Color(220, 40, 30, alfa);
    //         int[] array = new int[1];
    //         array[0] = n;
    //         GameView.addPoints(game, c, n);
    //     }
    // }

    private double valorarTramo(Game game, int desde, MOVE m) {
        int anterior = desde;
        int n = game.getNeighbour(desde, m);
        double peor = Double.POSITIVE_INFINITY;
        while (true) {
            peor = Math.min(peor, utilidad(n));
            if (game.isJunction(n)) {
                return peor;
            }
            // un nodo de pasillo tiene dos vecinos: el siguiente es el que no es el anterior
            int siguiente = (map.vecinos[n][0] == anterior) ? map.vecinos[n][1] : map.vecinos[n][0];
            anterior = n;
            n = siguiente;
        }
    }
}
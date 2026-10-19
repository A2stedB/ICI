package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.ArrayList;
import java.util.List;

public class CaminoBloqueadoTransition implements Transition 
{
    private int max_profundidad;
    private int margen_seguridad;

    public CaminoBloqueadoTransition(int max_profundidad, int margen_seguridad) {
        this.max_profundidad = max_profundidad;
        this.margen_seguridad = margen_seguridad;
    }

    @Override
    public boolean evaluate(Input in) {
        MsPacManInput input = (MsPacManInput) in;
        Game game = input.getGame();
        
        int pacman_nodo = input.pacman_nodo;
        MOVE pacman_last_move = input.pacman_last_move;

        MOVE[] posibles = game.getPossibleMoves(pacman_nodo, pacman_last_move);
        if (posibles.length == 0) return true;

        for (MOVE m : posibles) {
            if (is_ruta_segura(game, input, pacman_nodo, m, 0, 1, new ArrayList<Integer>())) 
            {
                return false; 
            }
        }
        return true; 
    }

    // Un DFS que mira a intersecciones futuras para comparar el tiempo que tardaria en llegar hasta alli el pacman y los fantasmas, devuelve true si ha encontrado un camino que llega antes que los 4 fantasmas, false en caso contrario
    // Gracias MARP xDs
    private boolean is_ruta_segura(Game game, MsPacManInput input, int nodo_inicio, MOVE movimiento, int dist_acumulada, int profundidad_actual, List<Integer> visitados) {
        int nodo_actual = game.getNeighbour(nodo_inicio, movimiento);
        int dist_tramo = 1;
        MOVE dir_actual = movimiento;

        while (nodo_actual != -1 && !game.isJunction(nodo_actual)) {
            MOVE[] siguientes_movs = game.getPossibleMoves(nodo_actual, dir_actual);
            if (siguientes_movs.length == 0) return false; 
            dir_actual = siguientes_movs[0];
            nodo_actual = game.getNeighbour(nodo_actual, dir_actual);
            dist_tramo++;
        }

        if (nodo_actual == -1) return false;

        if (visitados.contains(nodo_actual)) {
            return false; 
        }
        
        List<Integer> nuevos_visitados = new ArrayList<>(visitados);
        nuevos_visitados.add(nodo_actual);

        int distancia_llegada_pacman = dist_acumulada + dist_tramo;

        for (GHOST ghost : GHOST.values()) {
            if (!input.is_eatable(ghost) && game.getGhostLairTime(ghost) == 0) {
                int ghost_node = input.ghost_nodo_actual.get(ghost);
                MOVE ghost_move = input.ghost_last_move.get(ghost);
                
                int dist_fantasma = game.getShortestPathDistance(ghost_node, nodo_actual, ghost_move);
                int dist_fantasma_a_pacman = game.getShortestPathDistance(ghost_node, input.pacman_nodo, ghost_move);

                if (dist_fantasma_a_pacman != -1 && dist_fantasma == (dist_fantasma_a_pacman + distancia_llegada_pacman)) 
                {
                    continue; 
                }

                if (dist_fantasma != -1 && dist_fantasma <= distancia_llegada_pacman + margen_seguridad) 
                {
                    return false; 
                }
            }
        }

        if (profundidad_actual >= max_profundidad) return true;

        MOVE[] posibles_salidas = game.getPossibleMoves(nodo_actual, dir_actual);
        if (posibles_salidas.length == 0) return false;

        for (MOVE m_siguiente : posibles_salidas) {
            if (is_ruta_segura(game, input, nodo_actual, m_siguiente, distancia_llegada_pacman, profundidad_actual + 1, nuevos_visitados)) {
                return true;
            }
        }

        return false; 
    }

    @Override
    public String toString() {
        return "Camino Bloqueado (Profundidad " + max_profundidad + ")";
    }
}
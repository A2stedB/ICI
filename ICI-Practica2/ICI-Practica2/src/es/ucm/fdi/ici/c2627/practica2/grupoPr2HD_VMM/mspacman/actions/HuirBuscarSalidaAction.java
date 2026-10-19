package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.actions;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.MsPacManInput;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class HuirBuscarSalidaAction implements Action 
{

    private int max_profundidad;

    public HuirBuscarSalidaAction(int max_profundidad) 
    {
        this.max_profundidad = max_profundidad;
    }

    @Override
    public MOVE execute(Game game) 
    {
        int pacman_nodo = game.getPacmanCurrentNodeIndex();
        MOVE pacman_last_move = game.getPacmanLastMoveMade();
        
        if (!game.isJunction(pacman_nodo)) {
            return MOVE.NEUTRAL;
        }

        MsPacManInput input = new MsPacManInput(game);
        MOVE[] posibles_movimientos = game.getPossibleMoves(pacman_nodo, pacman_last_move);
        
        MOVE mejor_movimiento = MOVE.NEUTRAL;
        int max_supervivencia = -1;
        for (MOVE m : posibles_movimientos) {
            int score_supervivencia = DFS(game, input, pacman_nodo, m, 0, 1, new ArrayList<Integer>());
            
            if (score_supervivencia > max_supervivencia) {
                max_supervivencia = score_supervivencia;
                mejor_movimiento = m;
            }
        }

        return mejor_movimiento;
    }

    // Similar al DFS de CaminoBloqueadoTransition
    private int DFS(Game game, MsPacManInput input, int nodo_inicio, MOVE movimiento, int dist_acumulada, int profundidad_actual, List<Integer> visitados) {
        int nodo_actual = game.getNeighbour(nodo_inicio, movimiento);
        int dist_tramo = 1;
        MOVE dir_actual = movimiento;

        while (nodo_actual != -1 && !game.isJunction(nodo_actual)) {
            MOVE[] siguientes_movs = game.getPossibleMoves(nodo_actual, dir_actual);
            if (siguientes_movs.length == 0) return dist_acumulada + dist_tramo; 
            dir_actual = siguientes_movs[0];
            nodo_actual = game.getNeighbour(nodo_actual, dir_actual);
            dist_tramo++;
        }

        if (nodo_actual == -1) return dist_acumulada + dist_tramo;

        if (visitados.contains(nodo_actual)) {
            return dist_acumulada + dist_tramo; 
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

                if (dist_fantasma != -1 && dist_fantasma <= distancia_llegada_pacman) {
                    return distancia_llegada_pacman; 
                }
            }
        }

        if (profundidad_actual >= max_profundidad) 
        {
            return 10000 + distancia_llegada_pacman; // si ha llegado aqui significa que por aqui garantiza la seguridad
        }

        MOVE[] posibles_salidas = game.getPossibleMoves(nodo_actual, dir_actual);
        if (posibles_salidas.length == 0) return distancia_llegada_pacman;

        int max_supervivencia_hijos = -1;
        
        for (MOVE m_siguiente : posibles_salidas) {
            int supervivencia_rama = DFS(game, input, nodo_actual, m_siguiente, distancia_llegada_pacman, profundidad_actual + 1, nuevos_visitados);
            if (supervivencia_rama > max_supervivencia_hijos) {
                max_supervivencia_hijos = supervivencia_rama;
            }
        }

        return max_supervivencia_hijos;
    }

    @Override
    public String getActionId() 
    {
        return "Buscando Salida";
    }
}
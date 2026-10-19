package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM;

import es.ucm.fdi.ici.fsm.CompoundState;
import es.ucm.fdi.ici.fsm.FSM;
import es.ucm.fdi.ici.fsm.SimpleState;
import es.ucm.fdi.ici.fsm.State;
import es.ucm.fdi.ici.fsm.Transition;
import es.ucm.fdi.ici.fsm.observers.ConsoleFSMObserver;
import es.ucm.fdi.ici.fsm.observers.GraphFSMObserver;
import pacman.controllers.PacmanController;
import pacman.game.Constants;
import pacman.game.Constants.*;
import pacman.game.Game;
import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.*;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.actions.*;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.transitions.*;
import java.awt.*;

public class MsPacMan extends PacmanController
{
    private FSM fsm;
    private CompoundState comer_state;
    private CompoundState huir_state;
    // private final SimpleState huir_state;
    private CompoundState atacar_state;


    public MsPacMan()
    {
        setName("MsPacMan HD_VMM");
        fsm = new FSM("MsPacMan");
        GraphFSMObserver grafo = new GraphFSMObserver("MsPacMan");
        fsm.addObserver(grafo);
        fsm.addObserver(new ConsoleFSMObserver("MsPacMan"));

        // Transition fantasma_cerca = new FantasmaCercaTransition(50);
        // Transition no_fantasma_cerca = new NotTransition(new FantasmaCercaTransition(50));
        
        Transition fantasma_comestible_cerca = new FantasmaComestibleCercaTransition(50);
        // Transition comio_pp = new PPWasEaten();
        
        Transition poco_tiempo_edible = new SmallEatableTimeTransition(Constants.EDIBLE_TIME);
        Transition comestible_lejos = new ComestibleLejosTransition();
        
        Transition tiempo_o_lejos = new OrTransition(poco_tiempo_edible, comestible_lejos);

        
        Action accion_neutral = new DefaultAction();
        State movimiento_neutral = new SimpleState("Movimiento neutral", accion_neutral);

        setComerSubmachine();
        setAtacarSubmachine();
        setHuirSubmachine();



       

        // huir_state = new SimpleState(new DefaultAction());
        int cerca = 100;
        fsm.add(movimiento_neutral, new FantasmaCercaTransition(cerca), huir_state);
        fsm.add(movimiento_neutral, new NotTransition(new FantasmaCercaTransition(cerca + 10)), comer_state);

        fsm.add(comer_state, new FantasmaCercaTransition(cerca - 10), huir_state);
        fsm.add(comer_state, fantasma_comestible_cerca, atacar_state);

        fsm.add(huir_state, new NotTransition(new FantasmaCercaTransition(cerca + 15)), comer_state);
        fsm.add(huir_state, new PPWasEaten(), atacar_state);

        fsm.add(atacar_state, new FantasmaCercaTransition(cerca - 15), huir_state);
        fsm.add(atacar_state, tiempo_o_lejos, comer_state);
        fsm.add(comer_state, new PPWasEaten(), atacar_state);

        fsm.ready(movimiento_neutral);


        // SimpleState default_state = new SimpleState(new DefaultAction());
        // SimpleState basic = new SimpleState(new ComerTramoSinPP());
        // SimpleState basic2 = new SimpleState(new ComerTramoSinPP());



        // fsm.add(basic, new FantasmaComestibleCercaTransition(50), basic2);



        // fsm.ready(basic);
        grafo.showInFrame(new Dimension(320, 240));
    }

    public MOVE getMove(Game game, long timedue)
    {
        return fsm.run(new MsPacManInput(game));
        // return MOVE.NEUTRAL;
    }

    private void setComerSubmachine()
    {
        FSM comer_fsm = new FSM("Comer_FSM");
        // GraphFSMObserver graf_comer = new GraphFSMObserver("Comer_FSM");
        // fsm.addObserver(graf_comer);
        // fsm.addObserver(new ConsoleFSMObserver("Comer_FSM"));
        comer_state = new CompoundState("Comer", comer_fsm);

        Transition queda_pp = new PPLeftTransition();
        Transition no_queda_pp = new NotTransition(queda_pp);

        Transition pocas_pildoras = new PP_PocasTransition(10);

        Transition pp_muy_cerca = new PPCercaTransition(100); 
        Transition no_hay_pp_radio = new NotTransition(new PPCercaTransition(100));

        Transition ir_hacia_pp = new AndTransition(queda_pp, no_hay_pp_radio);
        Transition volver_a_comer = new AndTransition(queda_pp, pp_muy_cerca);
        Transition ir_a_limpiar = new OrTransition(pocas_pildoras, no_queda_pp);

        State estado_escoger_camino = new SimpleState("Comiendo Zonas Densas", new ComerTramoSinPPAction());
        State estado_ir_hacia_pp = new SimpleState("Buscando Zona Segura", new ComerBuscarPP());
        State estado_terminar_restos = new SimpleState("Limpiando Mapa", new ComerLimpiarNivelAction());

        comer_fsm.add(estado_escoger_camino, ir_hacia_pp, estado_ir_hacia_pp);
        comer_fsm.add(estado_ir_hacia_pp, volver_a_comer, estado_escoger_camino);
        comer_fsm.add(estado_escoger_camino, ir_a_limpiar, estado_terminar_restos);
        comer_fsm.add(estado_ir_hacia_pp, ir_a_limpiar, estado_terminar_restos);

        comer_fsm.ready(estado_escoger_camino);
    }

    private void setAtacarSubmachine()
    {
         FSM atacar_fsm = new FSM("Atacar_FSM");
        // GraphFSMObserver graf_atacar = new GraphFSMObserver("Atacar_FSM");
        // fsm.addObserver(graf_atacar);
        // fsm.addObserver(new ConsoleFSMObserver("Atacar_FSM"));
        atacar_state = new CompoundState("Atacar", atacar_fsm);

        Action atacar_cercano_action = new AtacarClosestAction();
        Action atacar_grupo_action = new AtacarGrupoAction();

        State estado_atacar_cercano = new SimpleState("Atacar unico", atacar_cercano_action);
        State estado_atacar_grupo = new SimpleState("Atacar a grupo", atacar_grupo_action);

        Transition grupo_detectado = new FantasmasAgrupadosTransition(100, 2); 
        
        Transition grupo_disperso = new NotTransition(new FantasmasAgrupadosTransition(60, 2));

        atacar_fsm.add(estado_atacar_cercano, grupo_detectado, estado_atacar_grupo);
        atacar_fsm.add(estado_atacar_grupo, grupo_disperso, estado_atacar_cercano);

        atacar_fsm.ready(estado_atacar_cercano);
    }

    private void setHuirSubmachine()
    {
        FSM huir_fsm_interna = new FSM("Huir_FSM");
        // GraphFSMObserver graf_huir = new GraphFSMObserver("Huir_FSM");
        // fsm.addObserver(graf_huir);
        // fsm.addObserver(new ConsoleFSMObserver("Huir_FSM"));
        huir_state = new CompoundState("Huir", huir_fsm_interna);

        // Action huir_comiendo_action = new HuirPerseguirPildoraAction_v0();
        Action huir_comiendo_action = new HuirPerseguirPildoraAction_v1();
        Action buscar_salida_action = new HuirBuscarSalidaAction(3);

        State estado_huir_comiendo = new SimpleState("Huyendo y Comiendo", huir_comiendo_action);
        State estado_buscar_salida = new SimpleState("Rompiendo Encerramiento", buscar_salida_action);

        Transition camino_bloqueado = new CaminoBloqueadoTransition(2, 1); 
        Transition camino_seguro = new NotTransition(camino_bloqueado);
        huir_fsm_interna.add(estado_huir_comiendo, camino_bloqueado, estado_buscar_salida);
        huir_fsm_interna.add(estado_buscar_salida, camino_seguro, estado_huir_comiendo);
        huir_fsm_interna.ready(estado_huir_comiendo);
    }
}
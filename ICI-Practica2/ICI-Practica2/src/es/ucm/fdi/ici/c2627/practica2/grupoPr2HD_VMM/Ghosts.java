package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM;

import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;

import es.ucm.fdi.ici.fsm.SimpleState;
import es.ucm.fdi.ici.fsm.CompoundState;
import es.ucm.fdi.ici.fsm.Transition;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.ghosts.transitions.*;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.ghosts.actions.*;
import es.ucm.fdi.ici.fsm.FSM;
import es.ucm.fdi.ici.fsm.observers.GraphFSMObserver;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.ghosts.GhostsBrain;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.ghosts.GhostsInput;

import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.internal.Ghost;
import pacman.game.Game;

public class Ghosts extends GhostController {

    private static final boolean DEBUG = false;     // a false antes de entregar

    private final EnumMap<GHOST, FSM> fsms = new EnumMap<>(GHOST.class);
    
    private int lastScatter = 0, nextScatter = 0;
    
    private GhostsBrain brain;
    
    public Ghosts() {
        this(30, 40);
    }

    public Ghosts(double cerca, double lejos) {
        setName("Ghosts HD_VMM");
        
        brain = new GhostsBrain();
        
        for (GHOST ghost : GHOST.values()) {        // una máquina por fantasma
            FSM fsm = new FSM(ghost.name());
            
            //Máquinas de estado para los estados compuestos.
            FSM chaseFSM = new FSM(ghost.name()+"_CHASE");
            FSM avoidFSM = new FSM(ghost.name()+"_AVOID");
            FSM scatterFSM = new FSM(ghost.name()+"_SCATTER");

            GraphFSMObserver grafo = null;
            if (DEBUG) {                            // el observador, antes de añadir nada
                grafo = new GraphFSMObserver(ghost.name());
                fsm.addObserver(grafo);
            }

            CompoundState chase = new CompoundState("CHASE", chaseFSM);
            CompoundState avoid = new CompoundState("AVOID", avoidFSM);
            CompoundState scatter = new CompoundState("SCATTER", scatterFSM);
            
            fsm.add(chase, new OnDangerTransition(false, ghost), avoid);
            fsm.add(chase, new TiredOfAttackingTransition(false), scatter);
            fsm.add(avoid, new OnDangerTransition(true, ghost), chase);
            fsm.add(avoid, new ScatterWithoutDangerTransition(false, ghost), scatter);
            fsm.add(scatter, new ScatterWithoutDangerTransition(true, ghost), avoid);
            fsm.add(scatter, new TiredOfAttackingTransition(true), chase);
            
            //Ahora formo la máquina de estados de los estads compuestos.
            //Para el estado de CHASE:
            SimpleState attack = new SimpleState(null);
            SimpleState ambush = new SimpleState(null);
            SimpleState help = new SimpleState(null);
            
            chaseFSM.add(attack, new helpTransition(false), help);
            chaseFSM.add(attack, new attackToAmbushTransition(false, brain), ambush);
            chaseFSM.add(ambush, new helpTransition(false), help);
            chaseFSM.add(ambush, new attackToAmbushTransition(true, brain), attack);
            chaseFSM.add(help, new helpTransition(true), attack);
            chaseFSM.add(help, new helpTransition(true), ambush);
            
            //Para el estado de AVOID
            SimpleState runAway = new SimpleState(null);
            SimpleState seekHelp = new SimpleState(null);
            
            avoidFSM.add(runAway, new SOSTransition(false), seekHelp);
            avoidFSM.add(seekHelp, new SOSTransition(true), runAway);
            
            //Para el estado de SCATTER
            SimpleState searchCorners = new SimpleState(null);
            SimpleState searchPills = new SimpleState(null);
            
            scatterFSM.add(searchCorners, new CornerWithoutPillsTransition(false, 50), searchPills);
            scatterFSM.add(searchPills, new CornerWithoutPillsTransition(true, 50), searchCorners);
            
            fsm.ready(scatter);  
            chaseFSM.ready(attack);
            avoidFSM.ready(runAway);
            scatterFSM.ready(searchCorners);

            fsms.put(ghost, fsm);
        }
    }

    @Override
    public void preCompute(String opponent) {
        for (FSM fsm : fsms.values())
            fsm.reset();                            // cada partida empieza en el estado inicial
    }

    @Override
    public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
        GhostsInput in = new GhostsInput(game, lastScatter, nextScatter);     // una vez por turno, para los cuatro
        lastScatter = in.getLastScatterTime();
        nextScatter = in.getNextScatterTime();
        
        EnumMap<GHOST, MOVE> result = new EnumMap<>(GHOST.class);

        for (GHOST ghost : GHOST.values()) {
            MOVE move = fsms.get(ghost).run(in);    // la máquina corre en todos los turnos...
            if (game.doesGhostRequireAction(ghost)) // ...pero el movimiento sólo cuenta en un cruce
                result.put(ghost, move);
        }
        return result;
    }
}
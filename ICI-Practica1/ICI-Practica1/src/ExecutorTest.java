import es.ucm.fdi.ici.c2627.practica1.grupoXY.Ghosts;
import es.ucm.fdi.ici.c2627.practica1.grupoXY.MsPacMan;
import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.HumanController;
import pacman.controllers.KeyBoardInput;
import pacman.controllers.PacmanController;
import pacman.game.util.Stats;

public class ExecutorTest {

    public static void main(String[] args) {
        Executor executor = new Executor.Builder()
                .setTickLimit(4000)
                .setVisual(true)
                .setScaleFactor(3)
                .build();

        // PacmanController pacMan = new HumanController(new KeyBoardInput());
        PacmanController pacMan = new MsPacMan();
        GhostController ghosts = new Ghosts();
        
        System.out.println( 
            executor.runGame(pacMan, ghosts, 30) //last parameter defines speed
            // Stats[] stat = executor.runExperiment(pacMan, ghosts, 2,"Testing stats"); //last parameter defines speed
        );     
    }
	
}

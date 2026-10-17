import es.ucm.fdi.ici.c2627.practica1.grupoPr1HD_VMM.Ghosts;
import es.ucm.fdi.ici.c2627.practica1.grupoPr1HD_VMM.MsPacMan;
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
        
        int runs = 10;
        int[] points = new int[runs];
        
        for (int i = 0; i < runs; i++) {
        		points[i] = executor.runGame(pacMan, ghosts, 1); //last parameter defines speed
            //Stats[] stat = executor.runExperiment(pacMan, ghosts, 2,"Testing stats"); //last parameter defines speed
        }
        
        int media = 0;
        for (int i = 0; i < runs; i++) {
        		System.out.println("Partida " + (i+1) + ": " + points[i]);
        		media+=points[i];
        }
        if (runs != 0) media/=runs;
        
        System.out.println("Partidas totales: " + runs);
        System.out.println("Media: " + media);
    }
	
}

import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.Ghosts;
//import es.ucm.fdi.ici.c2627.practica1.grupoPr1HD_VMM.MsPacMan;
import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.PacmanController;
import pacman.game.util.Stats;
import es.ucm.fdi.ici.rivales.*;

public class ExecutorTest {

    public static void main(String[] args) {
        Executor executor = new Executor.Builder()
                .setTickLimit(4000)
                .setVisual(true)
                .setScaleFactor(3)
                .build();

        // PacmanController pacMan = new HumanController(new KeyBoardInput());
        PacmanController pacMan = new MsPacManBasico();
        GhostController ghosts = new Ghosts();
        
        int runs = 100;
        // int[] points = new int[runs];

        // Para que se vea graficamente
        //executor.runGame(pacMan, ghosts, 20); //last parameter defines speed

        // Para los test masivos para la media, el stddev y el error tipico;
        Stats[] stat = executor.runExperiment(pacMan, ghosts, runs,"v0"); //last parameter defines speed
        System.out.println("Media: " + stat[0].getAverage());
        System.out.println("Desviacion tipica: " + stat[0].getStandardDeviation());
        System.out.println("Error tipico: " + stat[0].getStandardError());


        // for (int i = 0; i < runs; i++) {
        //     points[i] = executor.runGame(pacMan, ghosts, 1); //last parameter defines speed
        // }
         
    }
	
}

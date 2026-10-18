package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.ghosts;

import pacman.game.Game;

public class GhostsBrain {
	
	private int attacking;
	
	public GhostsBrain() {
		attacking = 0;
	}
	
	public int getGhostsAttacking() {return attacking;}
	public void increaseAttacking(int num) { attacking += num;}
}

package es.ucm.fdi.ici.c2627.practica1.grupoXY;

import pacman.game.Constants.MOVE;

import pacman.game.Constants.GHOST;

class GhostData{
    public int node_index;
    public GHOST type;
    public MOVE last_move;
    public boolean is_eatable = false;

    public GhostData(){};

    // public int compareTo(GhostData other){
    //     if(path_distance == -1){
    //         return 1;
    //     }
    //     if(path_distance < other.path_distance){
    //         return -1;
    //     }
    //     if(path_distance == other.path_distance){
    //         return 0;
    //     }
    //     else{
    //         return 1;
    //     }
    // }
}
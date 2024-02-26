package assign3;
//import assign3.HighScoreTable;
//import javax.swing.Timer;
import java.util.Random;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author lenovo
 */
/**
 * The Model class represents the game logic for a maze-based game.
 * It handles the game's mechanics, such as player movement, interaction with enemies,
 * and level progression. The class extends JPanel and implements ActionListener
 * to manage game rendering and events.
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.sql.*;

public class Model extends JPanel implements ActionListener {
    // Member variables
    public String pj ;
    Database dbd = new Database();
    private Dimension d;
    private final Font smallFont = new Font("Arial", Font.BOLD, 14);
    private boolean inGame = false;
    private boolean dying = false;
  
    private boolean waitingForSpaceBar = false;

    private final int BLOCK_SIZE = 24;
    private final int N_BLOCKS = 15;
    private final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;
    private final int Max_dragon = 12;
    private final int Player_SPEED = 6;
    private int levelsCompleted = 0;

    private int N_Dragons = 6;
    private int lives, score;
    private int[] dx, dy;
    private int[] dragon_x, dragon_y, dragon_dx, dragon_dy, ghostSpeed;

    private Image heart, ghost;
    private Image up, down, left, right;

    private int player_x, player_y, player_dx, player_dy;
    private int req_dx, req_dy;
    private boolean showFinalMessage = false;
    
    private int currentLevel = 1;
    /**
     * Constructs a Model instance with a player name.
     * Initializes the game environment and sets up necessary configurations.
     * 
     * @param pj The name of the player.
     */
    public Model(String pj) {
        //this.dbs = pj.db;
         this.pj = pj;
         System.out.println("Player Name: " + (pj != null ? pj : "null"));
        loadImages();
        initVariables();
        addKeyListener(new TAdapter());
        setFocusable(true);
        initGame();
    }

    private final short levelData[] = {
    	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,


  /*  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17,
18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 16

*/


    };
    
    short[] level2Data = {
   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0,
0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 0,
0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0,
0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0,
0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0,
0, 1, 1, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0,
0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 0,
0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0,
0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0,
0, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0,
0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,

    
            
    // ... maze data for level 2 ...
};
    short[] level3Data = {
   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0,
0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0,
0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0,
0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 1, 0,
0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0,
0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,

};
    short[] level4Data = {
  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0,
0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0,
0, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0,
0, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0,
0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 0,
0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0,
0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0,
0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0,
0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 0,
0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0,
0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,

};
    private final short[] level5Data = {
   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0,
0, 1, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0,
0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 0,
0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0,
0, 1, 0, 1, 1, 1, 0, 0, 1, 0, 1, 1, 1, 0, 0,
0, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0,
0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0,
0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0,
0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0,
0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0,
0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,

};
    short level6Data[] = {
 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0,
0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0,
0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0,
0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0,
0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0,
0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0,
0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0,
0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0,
0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0,
0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0,
0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0

};
short level7Data[] = {
   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0,
0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0,
0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0,
0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0,
0, 1, 1, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0,
0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0,
0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0,
0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0,
0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0,
0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0,
0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0,
0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0,
0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0

};
short[] level8Data = {
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
    0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0,
    0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0,
    0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0,
    0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0,
    0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0,
    0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0,
    0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0,
    0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0,
    0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0,
    0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0,
    0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0,
    0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
};
short[] level9Data = {
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0,
0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0,
0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0,
0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0,
0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0,
0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0,
0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0,
0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0,
0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0,
0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0,
0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 0,
0, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0,
0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
};
short[] level10Data = {
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0,
0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0,
0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0,
0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0,
0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0,
0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0,
0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0,
0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0,
0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0,
0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0,
0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0,
0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        

};

   
    
/**
     * Initializes game variables and starts the game.
     * This method sets up the initial game state.
     */

     public void StartGame() {
     
        currentLevel = 1;
        score = 0;
        lives = 3;
        levelsCompleted = 0;
        inGame = true;
        showFinalMessage = false;
        waitingForSpaceBar = false;
        gameWon = false;

    
        loadLevel(currentLevel);
        continueLevel();

        
    }

/**
     * Loads the level data based on the current level number.
     * 
     * @param levelNumber The number of the level to load.
     */
private void loadLevel(int levelNumber) {
    switch (levelNumber) {
        case 1:
            screenData = levelData.clone();
            break;
        case 2:
            screenData = level2Data.clone();//level2Data.clone();
            break;
        case 3:
            screenData = level3Data.clone();
            break;
        case 4 :
            screenData = level4Data.clone();
            break;
        case 5 :
            screenData = level5Data.clone();
            break;
        case 6 :
            screenData = level6Data.clone();
            break;
        case 7 :
            screenData = level7Data.clone();
            break;
        case 8 :
            screenData = level8Data.clone();
            break;
        case 9 :
            screenData = level9Data.clone();
            break;
        case 10 :
            screenData = level10Data.clone();
            break;
        default:
            // You might want to reset to level 1 or handle this case differently
            screenData = levelData.clone(); // For example, reset to level 1
            break;
    }
   // initLevel();
}
private final int MAX_LEVELS =10;
public boolean finished =false;
/**
     * Handles the logic to proceed to the next level in the game.
     */
private void goToNextLevel() {
     levelsCompleted++; 
     score++;
    currentLevel++;
    if (currentLevel > MAX_LEVELS) { // Assuming you define MAX_LEVELS
        showFinalMessage = true;
        inGame = false;
        finished=true;
        //repaint();
    }
    //else if (  currentLevel == MAX_LEVELS  ){
    //    levelsCompleted=0; 
//}
            else {
        loadLevel(currentLevel);
        initLevel();
    }
    repaint();
     
}


    private final int validSpeeds[] = {1, 2, 3, 4, 6, 8};
    private final int maxSpeed = 6;

    private int currentSpeed = 3;
    private short[] screenData;
    private Timer timer;

    
    
    private void loadImages() {
    	down = new ImageIcon("src/images/warrior.gif").getImage();
        //down = new ImageIcon(getClass().getResource("/images/down.gif")).getImage();
    	up = new ImageIcon("src/images/warrior.gif").getImage();
    	left = new ImageIcon("src/images/warrior.gif").getImage();
    	right = new ImageIcon("src/images/warrior.gif").getImage();
        ghost = new ImageIcon("src/images/ghost.gif").getImage();
        heart = new ImageIcon("src/images/heart.png").getImage();

    }
       private void initVariables() {

        screenData = new short[N_BLOCKS * N_BLOCKS];
        d = new Dimension(400, 400);
        dragon_x = new int[Max_dragon];
        dragon_dx = new int[Max_dragon];
        dragon_y = new int[Max_dragon];
        dragon_dy = new int[Max_dragon];
        ghostSpeed = new int[Max_dragon];
        dx = new int[4];
        dy = new int[4];
        
        timer = new Timer(40, this);
        timer.start();
    }
 /**
     * Handles the main game loop including player movement and game state checks.
     * 
     * @param g2d The Graphics2D object for rendering.
     */
    private void playGame(Graphics2D g2d) {

        if (dying) {

            death();

        } else {

            movePlayer();
            drawPlayer(g2d);
            moveDragons(g2d);
            checkMaze();
        }
    }

    private void showIntroScreen(Graphics2D g2d) {
 
    	String start = "Press SPACE to start";
        g2d.setColor(Color.yellow);
        g2d.drawString(start, (SCREEN_SIZE)/4, 150);
    }

    private void drawScore(Graphics2D g) {
        g.setFont(smallFont);
        g.setColor(new Color(5, 181, 79));
        String s = "Score: " + score;
        g.drawString(s, SCREEN_SIZE / 2 + 96, SCREEN_SIZE + 16);

        for (int i = 0; i < lives; i++) {
            g.drawImage(heart, i * 28 + 8, SCREEN_SIZE + 1, this);
        }
    }
private void drawLevel(Graphics2D g2d) {
    String levelStr = "Level: " + currentLevel;
    g2d.drawString(levelStr, 22, 22); // Choose appropriate x and y
}
private boolean gameWon = false; 
    private void checkMaze() {

      int topRightX = (N_BLOCKS - 2) * BLOCK_SIZE;
    int topRightY = 1* BLOCK_SIZE;
    System.out.println(player_x ); 
     System.out.println(player_y ); 

    if (player_x >= topRightX && player_y <= topRightY) {
        gameWon = true;
        waitingForSpaceBar = true; 
        stopMovement(); 
        repaint();
    }
       
    
    
    }
    private void stopMovement() {
         timer.stop(); 
    for (int i = 0; i < N_Dragons; i++) {
        dragon_dx[i] = 0;
        dragon_dy[i] = 0;
    }
    player_dx = 0;
    player_dy = 0;
    //allowMovement = false;
}



    private void death() {

    	lives--;

        if (lives == 0) {
            inGame = false;
            // showGameOverMessage = false;
              showFinalMessage = true;
              finished=true;
             repaint();
        }
        else{

        continueLevel();
        }
    }
    private void displayFinalMessage(Graphics g) {
    String message = "Congrats! You finished " + levelsCompleted + " levels!!!";
    Font messageFont = new Font("Arial", Font.BOLD, 24);
    g.setFont(messageFont);
    g.setColor(Color.WHITE);

    // Center the message on the screen
    int messageWidth = g.getFontMetrics().stringWidth(message);
    int messageX = (getWidth() - messageWidth) / 2;
    int messageY = getHeight() / 2;

    g.drawString(message, messageX, messageY);
    // levelsCompleted=0;
}
    
    

    private void moveDragons(Graphics2D g2d) {

         int pos;
    int count;
      int playerBlockX = player_x / BLOCK_SIZE;
    int playerBlockY = player_y / BLOCK_SIZE;
    
    for (int i = 0; i < N_Dragons; i++) {
                

        
        if (dragon_x[i] % BLOCK_SIZE == 0 && dragon_y[i] % BLOCK_SIZE == 0) {
            pos = dragon_x[i] / BLOCK_SIZE + N_BLOCKS * (int) (dragon_y[i] / BLOCK_SIZE);

            count = 0;

            // Check all directions for available moves
            if (!isWallAt(pos - 1) && dragon_dx[i] != 1) {
                dx[count] = -1;
                dy[count] = 0;
                count++;
            }

            if (!isWallAt(pos - N_BLOCKS) && dragon_dy[i] != 1) {
                dx[count] = 0;
                dy[count] = -1;
                count++;
            }

            if (!isWallAt(pos + 1) && dragon_dx[i] != -1) {
                dx[count] = 1;
                dy[count] = 0;
                count++;
            }

            if (!isWallAt(pos + N_BLOCKS) && dragon_dy[i] != -1) {
                dx[count] = 0;
                dy[count] = 1;
                count++;
            }

            if (count == 0) {
                // Ghost is trapped and needs to turn back
                dragon_dx[i] = -dragon_dx[i];
                dragon_dy[i] = -dragon_dy[i];
            } else {
                count = (int) (Math.random() * count);
                dragon_dx[i] = dx[count];
                dragon_dy[i] = dy[count];
            }
            
        }

        dragon_x[i] += (dragon_dx[i] * ghostSpeed[i]);
        dragon_y[i] += (dragon_dy[i] * ghostSpeed[i]);
        //(g2d, dragon_x[i], dragon_y[i]);
        int ghostBlockX = dragon_x[i] / BLOCK_SIZE;
        int ghostBlockY = dragon_y[i] / BLOCK_SIZE;
         if (Math.abs(ghostBlockX - playerBlockX) <= 3 && Math.abs(ghostBlockY - playerBlockY) <= 3) {
            drawDragon(g2d, dragon_x[i], dragon_y[i]);
        }

        
        if (player_x > (dragon_x[i] - 12) && player_x < (dragon_x[i] + 12)
                && player_y > (dragon_y[i] - 12) && player_y < (dragon_y[i] + 12)
                && inGame) {

            dying = true;
        }
        
    }
    }
    private boolean isWallAt(int pos) {
    return screenData[pos] == 0;
}

    private void drawDragon(Graphics2D g2d, int x, int y) {
    	g2d.drawImage(ghost, x, y, this);
        }

    private void movePlayer() {
         if (waitingForSpaceBar) {
        return; // Skip movement if waiting for space bar after winning
    }
        int pos;
    short ch;

    if (player_x % BLOCK_SIZE == 0 && player_y % BLOCK_SIZE == 0) {
        pos = player_x / BLOCK_SIZE + N_BLOCKS * (int) (player_y / BLOCK_SIZE);
        ch = screenData[pos];

        // Check for and consume a dot
        if ((ch & 16) != 0) {
            screenData[pos] = (short) (ch & 15);
            score++;
        }

        // Update Pac-Man's direction
        if (req_dx != 0 || req_dy != 0) {
            if (canMoveTo(player_x + req_dx * BLOCK_SIZE, player_y + req_dy * BLOCK_SIZE)) {
                player_dx = req_dx;
                player_dy = req_dy;
            }
        }

        // Check for standstill
        if (!canMoveTo(player_x + player_dx * BLOCK_SIZE, player_y + player_dy * BLOCK_SIZE)) {
            player_dx = 0;
            player_dy = 0;
        }
    }

    player_x += Player_SPEED * player_dx;
    player_y += Player_SPEED * player_dy;
}
    private boolean canMoveTo(int x, int y) {
    int pos = x / BLOCK_SIZE + N_BLOCKS * (y / BLOCK_SIZE);
    return screenData[pos] == 1; // Check if the position is walkable (not a wall)
}
    

    private void drawPlayer(Graphics2D g2d) {

        if (req_dx == -1) {
        	g2d.drawImage(left, player_x + 1, player_y + 1, this);
        } else if (req_dx == 1) {
        	g2d.drawImage(right, player_x + 1, player_y + 1, this);
        } else if (req_dy == -1) {
        	g2d.drawImage(up, player_x + 1, player_y + 1, this);
        } else {
        	g2d.drawImage(down, player_x + 1, player_y + 1, this);
        }
    }
     /**
     * Draws the maze environment on the screen.
     * 
     * @param g2d The Graphics2D object for rendering.
     */
private void drawMaze(Graphics2D g2d) {
    g2d.setColor(new Color(255,215,0)); // Red for walls
                g2d.fillRect(13*BLOCK_SIZE,  1*BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
    short i = 0;
    int x, y;
        int playerBlockX = player_x / BLOCK_SIZE;
    int playerBlockY = player_y / BLOCK_SIZE;

    for (y = 0; y < SCREEN_SIZE; y += BLOCK_SIZE) {
        for (x = 0; x < SCREEN_SIZE; x += BLOCK_SIZE) {
             int blockX = x / BLOCK_SIZE;
            int blockY = y / BLOCK_SIZE;

            // Determine the color based on the value in levelData
            if (Math.abs(blockX - playerBlockX) <= 3 && Math.abs(blockY - playerBlockY) <= 3) {
            if (screenData[i] == 0) { 
                g2d.setColor(new Color(255, 0, 0)); // Red for walls
                g2d.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
            } else if (screenData[i] == 1) {
                g2d.setColor(new Color(0, 0, 0)); // Black for paths
                g2d.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
            }
            }

            i++;
        }
    }
}

    private void initGame() {

    	 lives = 3;
    score = 0;
     levelsCompleted = 0;
    loadLevel(currentLevel); // Load current level data
    continueLevel();
    N_Dragons = 1;
    currentSpeed = 3;
    }
     private void initGame2() {

    	 lives = 3;
   // score = 0;
    // levelsCompleted = 0;
    loadLevel(currentLevel); // Load current level data
    continueLevel();
    N_Dragons = 1;
    currentSpeed = 3;
    }

    private void initLevel() {

        /*int i;
        for (i = 0; i < N_BLOCKS * N_BLOCKS; i++) {
            screenData[i] = levelData[i];
        }
*/

        continueLevel();
    }
    private Point getRandomNonWallPosition() {
        Random random = new Random();
        int x, y;
        do {
            x = random.nextInt(N_BLOCKS);
            y = random.nextInt(N_BLOCKS);
        } while (isWall(x, y));
        return new Point(x * BLOCK_SIZE, y * BLOCK_SIZE);
    }
private boolean isWall(int x, int y) 
{ return screenData[x + N_BLOCKS * y] == 0;
       // int pos = x + N_BLOCKS * y;
        //return (levelData[pos] & 1) != 0 || (levelData[pos] & 2) != 0 || (levelData[pos] & 4) != 0 || (levelData[pos] & 8) != 0;
    }

    private void continueLevel() {

    	int dx = 3;///should 1
        int random;

        for (int i = 0; i < N_Dragons; i++) {
        Point p = getRandomNonWallPosition();
            dragon_x[i] = p.x;
            dragon_y[i] = p.y;
            dragon_dx[i] = 0;
            dragon_dy[i] = 0;
            random = (int) (Math.random() * (currentSpeed + 1));

            if (random > currentSpeed) {
                random = currentSpeed;
            }

            ghostSpeed[i] = validSpeeds[random];
        }

          // Determine the bottom-left position
    int bottomLeftX = 1;
    int bottomLeftY = N_BLOCKS - 2;

    // Check if the bottom-left position is not a wall
    if (!isWall(bottomLeftX, bottomLeftY)) {
        player_x = bottomLeftX * BLOCK_SIZE;
        player_y = bottomLeftY * BLOCK_SIZE;
    } else {
        // If it's a wall, find a random non-wall position
        Point p = getRandomNonWallPosition();
        player_x = p.x;
        player_y = p.y;
    }
        player_dx = 0;	//reset direction move
        player_dy = 0;
        req_dx = 0;		// reset direction controls
        req_dy = 0;
        dying = false;
    }

 
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, d.width, d.height);

        

        if (inGame) {
            drawMaze(g2d);
            drawScore(g2d);
            playGame(g2d);
            
        
       // }
       // else if (showGameOverMessage) {
         //   displayGameOverMessage(g2d);
        } else if (showFinalMessage) {
        displayFinalMessage(g2d); 
// Display the final message when the flag is set
   
    }
       //     else {
         //   showIntroScreen(g2d);
        //}
        
      //  if (gameWon) {
      //  displayWinningMessage(g);
   // }

        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
        
    }
   
    
    private void displayWinningMessage(Graphics g) {
    String message = "Congratulations! You Won! Score: " + score;
    Font messageFont = new Font("Arial", Font.BOLD, 24);
    g.setFont(messageFont);
    g.setColor(Color.WHITE);

    // Center the message on the screen
    int messageWidth = g.getFontMetrics().stringWidth(message);
    int messageX = (getWidth() - messageWidth) / 2;
    int messageY = getHeight() / 2;

    g.drawString(message, messageX, messageY);
}
    
    private void displayGameOverMessage(Graphics g) {
      String message = "You lost! Score: " + score;
    Font messageFont = new Font("SansSerif", Font.BOLD, 24); // Choose an appropriate font style and size
    g.setFont(messageFont);
    g.setColor(Color.WHITE);
    // Center the message
    int messageWidth = g.getFontMetrics(messageFont).stringWidth(message);
    int messageX = (getWidth() - messageWidth) / 2;
    int messageY = getHeight() / 2;
    g.drawString(message, messageX, messageY);
    
    }

private Database dbs;
    //controls
    class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (inGame) {
                if (key == KeyEvent.VK_LEFT) {
                    req_dx = -1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_RIGHT) {
                    req_dx = 1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_UP) {
                    req_dx = 0;
                    req_dy = -1;
                } else if (key == KeyEvent.VK_DOWN) {
                    req_dx = 0;
                    req_dy = 1;
                } 
               
             else if(key == KeyEvent.VK_SPACE && finished) {
                 endGame();
   // highscoreTable.setVisible(true);
                restartGame();
            }
            }
             if (!finished&&key == KeyEvent.VK_SPACE) {
              
              
              if (waitingForSpaceBar) {
                waitingForSpaceBar = false;
                gameWon = false;
                goToNextLevel();
                //initGame2(); // Reset the game for the next level
            }
              else if(!inGame){
               inGame = true;
               initGame();
              }
            
            
               // initGame();
           
            
        } else if (finished) {
                 endGame();
             }
            
            
        }
        public void restartGame() {
    currentLevel = 1;
    levelsCompleted = 0;
    score=0;
    showFinalMessage = false;
    finished=false;
    inGame = true;
    initGame();
}
        // Method to be called when the game ends
        
private void endGame() {
     System.out.println("Player Name: " + (pj != null ? pj : "null"));
    System.out.println("Ending game and saving score"); 
    // Save score to the database
    dbd.insertHighScore(pj, levelsCompleted); // Assuming pj.playerName is the player's name

    // Open the HighScoreTable frame
    HighScoreTable highScoreTable = new HighScoreTable();
    highScoreTable.setVisible(true);
}


        
       
}

	
    @Override
    public void actionPerformed(ActionEvent e) {
       

        repaint();
    }
		
	}
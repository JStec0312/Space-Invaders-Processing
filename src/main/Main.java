package main;
import processing.core.PApplet;
import game.Game;
import processing.core.PFont;
import processing.core.PImage;
import sound_manager.SoundManager;

public class Main extends PApplet {
    boolean leftPressed = false;
    boolean rightPressed = false;
    PImage bg;
    Game game;
    SoundManager soundManager;
    public void settings() {
        size(800, 600);
    }

    public void setup() {
        SoundManager soundManager = new SoundManager(this);
        soundManager.playNewGameSound();

        soundManager.playBackgroundMusic();
       bg = loadImage("/images/bg_ai_generated_800x600.png") ;
       game = new Game(soundManager);
        PFont font;
        font = createFont("/fonts/PressStart2P-Regular.ttf", 24);
        textFont(font);

    }

    public void draw() {
        background(bg);
        fill(255, 0, 0);
        game.update(this);
        game.draw(this);
    }
    public void keyPressed() {
        game.handleKeyPressed(keyCode);
    }
    public void keyReleased() {
        game.handleKeyReleased(keyCode);
    }

    public void mousePressed() {
        game.handleMousePressed(mouseX, mouseY);
    }

    public static void main(String[] args) {
        PApplet.main("main.Main");
    }
}

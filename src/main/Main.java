package main;
import processing.core.PApplet;
import game.Game;

public class Main extends PApplet {
    boolean leftPressed = false;
    boolean rightPressed = false;
    Game game;
    public void settings() {
        size(800, 600); // szerokosc, wysokosc
    }

    public void setup() {
       background(0);  // czarne tlo
       game = new Game();

    }

    public void draw() {
        background(0);
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

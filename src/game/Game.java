package game;

import config.Settings;
import entities.Bullet;
import entities.Player;
import jdk.jshell.spi.ExecutionControl;
import processing.core.PApplet;
import java.util.ArrayList;
import java.util.List;

public class Game {
    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD
    }
    public enum GameState {
        START,
        PLAY,
        GAME_OVER
    }
;
    //Player and bullets
    private Player player;
    private List<Bullet> bullets;
    private int shootCooldownFrames = Settings.BULLET_COOLDOWN_FRAMES;
    private int shotTimer = 0;

    //enemies
    //@TODO dodać wrogów
    private float enemySpeed;
    //game settings
    private GameState gameState = GameState.START;
    private Difficulty difficulty;
    private int lives;
    private int score;
    private List<List<Float>> difficultyButtonPositions;


    //events
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean shotPressed;



    public void update(PApplet p) {
        if (gameState!=GameState.PLAY){
            return;
        }
        player.update(leftPressed, rightPressed);
        if (shotTimer > 0){
            shotTimer--;
        }
        if (shotPressed && shotTimer==0){
            spawnBullet();
            shotTimer = shootCooldownFrames;
        }
        if (bullets!=null){
            bullets.removeIf(b ->{
                b.update(p);
                return b.isOffScreen(Settings.HEIGHT);
            });
        }
    }

    public void draw(PApplet p ){
        p.background(Settings.BACKGROUND_COLOR);
        switch (gameState){
            case START:
                drawStartScreen(p);
                break;
            case PLAY:
                player.draw(p);
                drawBullets(p);
                drawGameScreen(p);
                break;
            case GAME_OVER:
                // rysowanie ekranu końcowego
                break;
        }
    }
    public void handleMousePressed(int x, int y){
        if (gameState == GameState.START){
            handleDifficultyPressed(x, y);
        }
    }
    public void handleKeyPressed(int keyCode){
        if (gameState == GameState.PLAY){
            if (keyCode == 'A' || keyCode=='a'){
                leftPressed = true;
            } else if (keyCode == 'D' || keyCode=='d'){
                rightPressed = true;
            } else if (keyCode == ' '){ //spacja
                shotPressed = true;
            }
        }
    }
    public void handleKeyReleased(int keyCode){
        if (gameState == GameState.PLAY){
            if (keyCode == 'A' || keyCode=='a'){
                leftPressed = false;
            }
            else if (keyCode == 'D' || keyCode=='d'){
                rightPressed = false;
            }
            else if (keyCode == ' '){ //spacja
                shotPressed = false;
            }
        }
    }

    //rysujemy ekran start
    private void drawStartScreen(PApplet p){
        difficultyButtonPositions  = new ArrayList<>();
        p.pushStyle();
        //przyciski wyboru trudnosci
        List<String>  difficultyOptions = List.of("EASY", "MEDIUM", "HARD");
        for (int i=0; i<difficultyOptions.size(); i++){
            float x = Settings.WIDTH / 2f;
            float y = Settings.HEIGHT / 2f + i * Settings.BUTTON_SPACING;
            difficultyButtonPositions.add(List.of(x, y));
            float w = Settings.BUTTON_WIDTH;
            float h = Settings.BUTTON_HEIGHT;

            //przycisk
            p.fill(100);
            p.rectMode(PApplet.CENTER);
            p.rect(x, y, w, h, 10);

            //tekst
            p.fill(255);
            p.textAlign(PApplet.CENTER, PApplet.CENTER);
            p.textSize(20);
            p.text(difficultyOptions.get(i), x, y);
        }
        p.popStyle();
    }
    private void handleDifficultyPressed(int x, int y){
        if (difficultyButtonPositions == null){;
            return;
        }
        for (int i=0; i<difficultyButtonPositions.size(); i++){
            float bx = difficultyButtonPositions.get(i).get(0);
            float by = difficultyButtonPositions.get(i).get(1);
            float bw = Settings.BUTTON_WIDTH;
            float bh = Settings.BUTTON_HEIGHT;

            if (x >= bx - bw / 2f && x <= bx + bw / 2f &&
                y >= by - bh / 2f && y <= by + bh / 2f){
                switch (i){
                    case 0:
                        difficulty = Difficulty.EASY;
                        lives = Settings.PLAYER_LIVES_EASY;
                        enemySpeed = Settings.ENEMY_SPEED_EASY;
                        break;
                    case 1:
                        difficulty = Difficulty.MEDIUM;
                        lives = Settings.PLAYER_LIVES_MEDIUM;
                        enemySpeed = Settings.ENEMY_SPEED_MEDIUM;
                        break;
                    case 2:
                        difficulty = Difficulty.HARD;
                        lives = Settings.PLAYER_LIVES_HARD;
                        enemySpeed = Settings.ENEMY_SPEED_HARD;
                        break;
                }
                startNewGame();
            }
        }
    }

    // rozpoczynamy nową grę
    private void startNewGame(){
        player = new Player(Settings.PLAYER_INITIAL_X, Settings.PLAYER_INITIAL_Y);
        bullets = new ArrayList<>();
        score = 0;
        gameState = GameState.PLAY;
    }
    // rysujemy ekran gry
    private void drawGameScreen(PApplet p){
        p.pushStyle();
        //wyświetlanie życia i wyniku
        p.fill(255);
        p.textSize(20);
        p.textAlign(PApplet.LEFT, PApplet.TOP);
        p.text("Lives: " + lives, 10, 10);
        p.text("Score: " + score, 10, 40);
        p.popStyle();

    }
    //spawnowanie nabojow
    private void spawnBullet(){
        Bullet bullet = new Bullet(player.getX(), player.getY() - Settings.PLAYER_HEIGHT / 2f, Settings.BULLET_SPEED);
        bullets.add(bullet);
    }
    //rysowanie nabojow
    private void drawBullets(PApplet p){
        if (bullets!=null){
            for (Bullet b : bullets){
                b.draw(p);
            }
        }
    }
}

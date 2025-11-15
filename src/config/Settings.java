package config;


public class Settings {
    // Frame
    public static final  int WIDTH = 800;
    public static final  int HEIGHT = 600;

    //HP
    public static final int PLAYER_LIVES_EASY = 9;
    public static final int PLAYER_LIVES_MEDIUM = 5;
    public static final int PLAYER_LIVES_HARD = 3;


    //ENEMY SPEED
    public static final float ENEMY_SPEED_EASY_BASE = 1.5f;
    public  static final float ENEMY_SPEED_MEDIUM_BASE = 3.0f;
    public static final float ENEMY_SPEED_HARD_BASE  = 3.0f;
    public static final float ENEMY_SPEED_INCREASE=0.1f; // zwiększenie prędkości po każdym pokonanym wrogu
    //PLAYER
    public static  final float PLAYER_SPEED = 7.0f;
    public static final float PLAYER_WIDTH = 30.0f;
    public static final float PLAYER_HEIGHT = 40.0f;
    public static final float PLAYER_ENGINE_WIDTH = 10.0f;
    public static final float PLAYER_ENGINE_HEIGHT = 10.0f;
    public static final float PLAYER_INITIAL_X = WIDTH / 2.0f;
    public static final float PLAYER_INITIAL_Y = HEIGHT - 60.0f;
    public static final float BACKGROUND_COLOR = 0;


    //BUTTON SIZE
    public static final float BUTTON_WIDTH = 200.0f;
    public static final float BUTTON_HEIGHT = 40.0f;
    public static final float BUTTON_SPACING = 50.0f;

    // BULLET
    public static final float PLAYER_BULLET_SPEED = -10.0f;
    public static final float PLAYER_BULLET_RADIUS = 5.0f;
    public static final int BULLET_COOLDOWN_FRAMES = 10; // liczba klatek między strzałami

    // WALL
    public static final float WALL_WIDTH = 88.0f;
    public static final float WALL_HEIGHT = 20.0f;
    public static final int WALL_HP = 10;
    public static final float FIRST_WALL_Y = HEIGHT - 200.0f;
    public static final float WALL_SPACING = 60.0f;
    public static final float FIRST_WALL_X = WALL_SPACING + WALL_WIDTH / 2.0f;
    public static final int WALL_COUNT = 5;
}

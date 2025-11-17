package game;

import config.Settings;
import entities.*;
import entities.enemies.*;
import processing.core.PApplet;
import java.util.ArrayList;
import processing.sound.*;
import sound_manager.SoundManager;

import java.util.List;
import java.util.Random;
import java.util.Iterator;


public class Game {
    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD
    }
    public enum GameState {
        START,
        PLAY,
        NEW_LEVEL,
        GAME_OVER
    }
    private Random rand = new Random();

    ;
    //Player and bullets
    private Player player;
    private List<PlayerBullet> bullets;
    private List<EnemyBullet> enemyBullets;
    private int shootCooldownFrames = Settings.BULLET_COOLDOWN_FRAMES;
    private float shotTimer = Settings.BULLET_COOLDOWN_FRAMES;
    private List<Wall> walls;
    boolean resetPoints = false;

    //enemies
    private List<List<Enemy>> enemies;
    private int enemiesRowCount;
    private float enemySpeed;
    //game settings
    private GameState gameState = GameState.START;
    private Difficulty difficulty;
    private int lives;
    private int score;
    private int welcomeTextIncr = 0;
    private int welcomeTextIncrTimer=0;
    private float enemyShootTimer =  rand.nextFloat() * (Settings.MAX_ENEMY_SHOT_INTERVAL - Settings.MIN_ENEMY_SHOT_INTERVAL) * Settings.MIN_ENEMY_SHOT_INTERVAL;
    private List<List<Float>> difficultyButtonPositions;
    private boolean enemiesMovingRight = true;
    private int level = 1;
    private boolean resetOnNextFrame = false;
    //events
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean shotPressed;
    private SoundManager soundManager;
    public Game(SoundManager soundManager){
        this.soundManager = soundManager;
    }




    public void update(PApplet p) {

        if(gameState == GameState.NEW_LEVEL){
            p.delay(Settings.NEXT_LEVEL_SCREEN_TIME);
            gameState = GameState.PLAY;
            startNewGame();
            return;
        }
        if (gameState!=GameState.PLAY){
            return;
        }
        player.update(leftPressed, rightPressed);
        if (shotTimer > 0){
            shotTimer--;
        }
        if (shotPressed && shotTimer==0){
            spawnPlayerBullet();
            shotTimer = shootCooldownFrames;
        }
        if (bullets!=null){
            bullets.removeIf(b ->{
                b.update(p);
                return b.isOffScreen(Settings.HEIGHT);
            });
        }
        if (enemyBullets!=null){
            enemyBullets.removeIf(eb ->{
                eb.update(p);
                return eb.isOffScreen(Settings.HEIGHT);
            });
        }
        detectPlayerBulletEnemyCollisions();
        detectPlayerBulletWallCollisions();
        detectEnemyBulletPlayerCollisions();
        detectEnemyWallCollisions();
        detectEnemyPlayerCollisions();
        moveEnemies();
        detectEnemyBulletWallCollisions();
        randomEnemyShoot();
        startNewLevelCheck();

    }

    public void draw(PApplet p ){
        switch (gameState){
            case START:
                drawStartScreen(p);
                break;
            case PLAY:
                player.draw(p);
                drawBullets(p);
                drawGameScreen(p);
                drawWalls(p);
                drawEnemies(p);
                drawEnemyBullets(p);
                break;
            case NEW_LEVEL:

                drawNewLevelScreen(p);
                break;

            case GAME_OVER:
                drawGameOverScreen(p);

        }
    }
    public void handleMousePressed(int x, int y){
        if (gameState == GameState.START){
            handleDifficultyPressed(x, y);
        }
        if (gameState == GameState.GAME_OVER){
            handleNewGamePressed(x, y);
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
            } else if (keyCode== 'p' || keyCode=='P'){ // GAME OVER
                resetPoints=true;
                gameState = GameState.GAME_OVER;
                lives=0;
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
        p.textAlign(PApplet.CENTER, PApplet.TOP);
        p.fill(255);
        welcomeTextIncrTimer++;
        int baseTextSize = 20;
        if(welcomeTextIncrTimer >= 30){
            welcomeTextIncr++;
            welcomeTextIncrTimer=0;
        }
        p.textSize(baseTextSize + welcomeTextIncr % 4);
        p.stroke(0);
        p.text("SPACE INVADERS", Settings.WIDTH/2f, Settings.HEIGHT/2f - 250);

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
                        enemySpeed = Settings.ENEMY_SPEED_EASY_BASE;
                        enemiesRowCount = Settings.ENEMIES_PER_WAVE_EASY;
                        break;
                    case 1:
                        difficulty = Difficulty.MEDIUM;
                        lives = Settings.PLAYER_LIVES_MEDIUM;
                        enemySpeed = Settings.ENEMY_SPEED_MEDIUM_BASE;
                        enemiesRowCount = Settings.ENEMIES_PER_WAVE_MEDIUM;
                        break;
                    case 2:
                        difficulty = Difficulty.HARD;
                        lives = Settings.PLAYER_LIVES_HARD;
                        enemySpeed = Settings.ENEMY_SPEED_HARD_BASE;
                        enemiesRowCount = Settings.ENEMIES_PER_WAVE_HARD;
                        break;
                }
                startNewGame();
            }
        }
    }


    // rozpoczynamy nową grę
    private void startNewGame(){
        player = new Player(Settings.PLAYER_INITIAL_X, Settings.PLAYER_INITIAL_Y, lives);
        bullets = new ArrayList<>();
        walls = new ArrayList<>();
        enemies = new ArrayList<>();
        enemyBullets = new ArrayList<>();
        float initialWallX = Settings.FIRST_WALL_X;
        float wallY = Settings.FIRST_WALL_Y;
        float wallSpacing = Settings.WALL_SPACING;
        float step = Settings.WALL_WIDTH + wallSpacing;

        for (int i=0; i<Settings.WALL_COUNT; i++){
            Wall wall = new Wall(initialWallX, wallY);
            walls.add(wall);
            initialWallX += step;
        }

        float enemiesWidth = Settings.ENEMEIES_COLS * (Settings.ENEMY_WIDTH + 20) ;
        float totalSpacingWidth = (Settings.ENEMEIES_COLS -1) * Settings.ENEMY_SPACING;
        float startX = (Settings.WIDTH - enemiesWidth) / 2f;
        float enemyWidth = Settings.ENEMY_WIDTH;
        float enemyHeight = Settings.ENEMY_HEIGHT;
        float enemySpacingX = Settings.ENEMY_SPACING;
        float enemySpacingY = Settings.ENEMY_VERTICAL_SPACING;

        int cols = Settings.ENEMEIES_COLS;
        int rows = enemiesRowCount;
        float formationWidth = cols * enemyWidth + (cols - 1) * enemySpacingX;
        float firstEnemyX = (Settings.WIDTH - formationWidth) / 2f + enemyWidth / 2f;
        float firstEnemyY = Settings.FIRST_ENEMY_Y;
        for (int i = 0; i < rows; i++) {
            int rowType = rand.nextInt(1, 5); // Wybierz losowy typ przeciwnika dla każdego wiersza
            for (int j = 0; j < cols; j++) {
                float enemyX = firstEnemyX + j * (enemyWidth + enemySpacingX);
                float enemyY = firstEnemyY + i * (enemyHeight + enemySpacingY);
                Enemy enemy = EnemyFactory.createEnemy(
                        rowType,
                        enemyX,
                        enemyY,
                        enemySpeed,
                        Settings.BASE_ENEMY_SCORE_VALUE,
                        Settings.BASE_ENEMY_DAMAGE,
                        Settings.BASE_ENEMY_HP,
                        Settings.ENEMY_VERTICAL_SPEED

                        );
                if (enemies.size() <= i) {
                    enemies.add(new ArrayList<>());
                }
                enemies.get(i).add(enemy);
            }
        }
        if(resetPoints){
            score = 0;
            level = 1;
            resetPoints = false;
        }

        gameState = GameState.PLAY;
        leftPressed = false;
        rightPressed = false;
        shotPressed = false;

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
        p.text("Level: " + level, 600, 10);
        p.popStyle();

    }
    //spawnowanie nabojow
    private void spawnPlayerBullet(){
        PlayerBullet bullet = new PlayerBullet(player.getX(), player.getY() - Settings.PLAYER_HEIGHT / 2f, Settings.PLAYER_BULLET_SPEED, Settings.PLAYER_BULLET_RADIUS, Settings.PLAYER_BULLET_DAMAGE);
        bullets.add(bullet);
        soundManager.playPlayerShotSound();
    }
    //rysowanie nabojow
    private void drawBullets(PApplet p){
        if (bullets!=null){
            for (PlayerBullet b : bullets){
                b.draw(p);
            }
        }
    }

    //ściany
    private void drawWalls(PApplet p){
        for (Wall w : walls){
            w.draw(p);
        }
    }
    private void detectPlayerBulletWallCollisions(){
        for (int i=bullets.size()-1; i>=0; i--){
            PlayerBullet pb = bullets.get(i);
            float pbx = pb.getX();
            float pby = pb.getY();
            for (int j = 0; j<walls.size(); j++){
                Wall w = walls.get(j);
                float wx = w.getX();
                float wy = w.getY();
                float halfW = Settings.WALL_WIDTH / 2f;
                float halfH = Settings.WALL_HEIGHT / 2f;
                if (pbx >= wx - halfW && pbx <= wx + halfW &&
                    pby >= wy - halfH && pby <= wy + halfH){
                    w.decreaseHP();
                    soundManager.playWallHitSound();
                    bullets.remove(i);
                    if (w.getHP() <= 0){
                        walls.remove(j);
                        soundManager.playWallExplosionSound();
                    }
                    break;
                }
            }
        }
    }
    private void detectPlayerBulletEnemyCollisions(){
        Iterator<PlayerBullet> bulletIterator = bullets.iterator();
        while(bulletIterator.hasNext()){
            PlayerBullet pb = bulletIterator.next();
            float pbx = pb.getX();
            float pby = pb.getY();
            boolean enemyHit = false;
            for (List<Enemy> enemyRow: enemies){
                Iterator<Enemy> enemyIterator = enemyRow.iterator();
                while(enemyIterator.hasNext()){
                    Enemy e = enemyIterator.next();
                    float ex = e.getX();
                    float ey = e.getY();
                    float halfW = e.getWidth()/2f;
                    float halfH = e.getHeight()/2f;
                    if (pbx >= ex - halfW && pbx <= ex + halfW && pby >= ey - halfH && pby <= ey + halfH){
                        e.takeDamage(pb.getDamage());
                        bulletIterator.remove();
                        soundManager.playEnemyHitSound();
                        if (e.isDestroyed()) {
                            score += e.getScoreValue();
                            enemyIterator.remove();
                            enemySpeed+= Settings.ENEMY_SPEED_INCREASE;
                        }
                        enemyHit = true;
                        break;
                    }
                }
                if (enemyHit){
                    break;
                    }
            }
        }


    }
    public void moveEnemies(){
        boolean mustChangeDirection = false;
        for (List<Enemy> enemyRow : enemies) {
            if (enemyRow.isEmpty()) {
                continue;
            }

            if (enemiesMovingRight) {
                Enemy rightMostEnemy = enemyRow.get(enemyRow.size() - 1);
                if (rightMostEnemy.getX() + rightMostEnemy.getWidth() / 2f >= Settings.WIDTH - 30) {
                    mustChangeDirection = true;
                }
            } else {
                Enemy leftMostEnemy = enemyRow.get(0);
                if (leftMostEnemy.getX() - leftMostEnemy.getWidth() / 2f <= 30) {
                    mustChangeDirection = true;
                    break;
                }
            }
        }

        if (mustChangeDirection) {
            enemiesMovingRight = !enemiesMovingRight;
            for (List<Enemy> enemyRow : enemies) {
                for (Enemy e : enemyRow) {
                    e.moveDown();
                }
            }
        }
        for (List<Enemy> enemyRow : enemies) {
            for (Enemy e : enemyRow) {
                e.update(enemiesMovingRight);
            }
        }
    }
    public void randomEnemyShoot(){
        float deltaTime = 1.0f/60.0f;
        enemyShootTimer -= deltaTime;
        if (enemyShootTimer > 0){
            return;
        }

        int lastRowIndex = enemies.size() - 1;
        if (lastRowIndex < 0) {
            return;
        }
        List<Enemy> lastRow = enemies.get(lastRowIndex);
        if (lastRow.isEmpty()) {
            return;
        }
        Enemy lastRowEnemy = lastRow.get(new Random().nextInt(lastRow.size()));
        float shotX = lastRowEnemy.getX();
        float shotY = lastRowEnemy.getY() + lastRowEnemy.getHeight() / 2f;
        EnemyBullet enemyBullet = new EnemyBullet(shotX, shotY, Settings.ENEMY_BULLET_SPEED,  Settings.BASE_ENEMY_DAMAGE);
        enemyBullets.add(enemyBullet);
        soundManager.playEnemyShotSound();
        enemyShootTimer = rand.nextFloat() * (Settings.MAX_ENEMY_SHOT_INTERVAL - Settings.MIN_ENEMY_SHOT_INTERVAL) + Settings.MIN_ENEMY_SHOT_INTERVAL;
    }
    private void drawEnemyBullets(PApplet p){
        if (enemyBullets!=null){
            for (EnemyBullet eb : enemyBullets){
                eb.draw(p);
            }
        }
    }

    public void detectEnemyBulletPlayerCollisions(){
        Iterator<EnemyBullet> enemyBulletIterator = enemyBullets.iterator();
        while(enemyBulletIterator.hasNext()){
            EnemyBullet eb = enemyBulletIterator.next();
            float ebx = eb.getX();
            float eby = eb.getY();
            float halfW = Settings.PLAYER_WIDTH / 2f;
            float halfH = Settings.PLAYER_HEIGHT / 2f;
            if (ebx >= player.getX() - halfW && ebx <= player.getX() + halfW &&
                eby >= player.getY() - halfH && eby <= player.getY() + halfH){
                lives--;
                enemyBulletIterator.remove();
                soundManager.playPlayerHitSound();
                if (lives <= 0){
                    resetPoints= true;
                    gameState = GameState.GAME_OVER;
                    soundManager.playGameOverSound();

                }
            }
        }
    }
    public void detectEnemyBulletWallCollisions(){
        Iterator<EnemyBullet> enemyBulletIterator = enemyBullets.iterator();
        while(enemyBulletIterator.hasNext()){
            EnemyBullet eb = enemyBulletIterator.next();
            float ebx = eb.getX();
            float eby = eb.getY();
            if (eby < Settings.FIRST_WALL_Y - Settings.WALL_HEIGHT / 2f){
                continue;
            }
            boolean wallHit = false;
            for (int j = 0; j<walls.size(); j++){
                Wall w = walls.get(j);
                float wx = w.getX();
                float wy = w.getY();
                float halfW = Settings.WALL_WIDTH / 2f;
                float halfH = Settings.WALL_HEIGHT / 2f;
                if (ebx >= wx - halfW && ebx <= wx + halfW &&
                    eby >= wy - halfH && eby <= wy + halfH){
                    w.decreaseHP();
                    soundManager.playWallHitSound();
                    enemyBulletIterator.remove();
                    if (w.getHP() <= 0){
                        walls.remove(j);
                        soundManager.playWallExplosionSound();
                    }
                    wallHit = true;
                    break;
                }
            }
            if (wallHit){
                break;
            }
        }
    }
    public void detectEnemyWallCollisions(){
        if (walls.isEmpty()){
            return;
        }
        Iterator<Enemy> lastRowIterator = enemies.get(enemies.size() - 1).iterator();
        if (!lastRowIterator.hasNext()){
            return;
        }
        Enemy enemy = lastRowIterator.next(); // Pobierz wroga TYLKO RAZ
        float enemyY = enemy.getY() + enemy.getHeight() / 2f; // Użyj tej samej zmienn
        if (enemyY < Settings.FIRST_WALL_Y - Settings.WALL_HEIGHT / 2f){
            return;
        }
        for (int i = walls.size() - 1; i >= 0; i--){
            Wall w = walls.get(i);
            float wy = w.getY() - Settings.WALL_HEIGHT / 2f;
            if (enemyY >= wy){
                walls.remove(i);
                soundManager.playWallHitSound();
            }
        }
    }

    public void detectEnemyPlayerCollisions() {

        List<Enemy> lowestRow = null;
        if (enemies.isEmpty()) {
            return; // Nie ma wrogów
        }
        for (int i = enemies.size() - 1; i >= 0; i--) {
            if (!enemies.get(i).isEmpty()) {
                lowestRow = enemies.get(i);
                break;
            }
        }
        if (lowestRow == null) {
            return;
        }
        Enemy sampleEnemy = lowestRow.get(0);
        float enemyBottomY = sampleEnemy.getY() + sampleEnemy.getHeight() / 2f;
        float playerTopY = player.getY() - Settings.PLAYER_HEIGHT / 2f;
        if (enemyBottomY < playerTopY) {
            return;
        }

        float pHalfW = Settings.PLAYER_WIDTH / 2f;
        float pHalfH = Settings.PLAYER_HEIGHT / 2f;
        float playerLeft = player.getX() - pHalfW;
        float playerRight = player.getX() + pHalfW;
        float playerBottom = player.getY() + pHalfH;
        for (Enemy e : lowestRow) {
            float ex = e.getX();
            float ey = e.getY();
            float halfW = e.getWidth() / 2f;
            float halfH = e.getHeight() / 2f;
            float enemyLeft = ex - halfW;
            float enemyRight = ex + halfW;
            float enemyTop = ey - halfH;

            if (enemyRight < playerLeft ||
                    enemyLeft > playerRight ||
                    enemyBottomY < playerTopY ||
                    enemyTop > playerBottom)
            {

                continue;
            }

            lives = 0;
            resetPoints = true;
            soundManager.playGameOverSound();
            gameState = GameState.GAME_OVER;
            break;
        }
    }

    public void drawEnemies(PApplet p) {
        for (List<Enemy> enemyRow : enemies) {
            for (Enemy e : enemyRow) {
                e.draw(p);
            }
        }
    }
    public void startNewLevelCheck(){
        if (resetOnNextFrame){
            resetOnNextFrame = false;
            level++;
            rightPressed = false;
            leftPressed = false;
            shotPressed = false;
            gameState = GameState.NEW_LEVEL;
            soundManager.playNextLevelSound();
            return;
        }
        boolean allEnemiesDestroyed = true;
        for (List<Enemy> enemyRow : enemies) {
            if (!enemyRow.isEmpty()) {
                allEnemiesDestroyed = false;
                break;
            }
        }
        if (allEnemiesDestroyed){
            resetOnNextFrame = true;

        }
    }
    private void drawNewLevelScreen(PApplet p){
        p.pushStyle();
        p.fill(255);
        p.textSize(30);
        p.textAlign(PApplet.CENTER, PApplet.CENTER);
        p.text("Level " + level, Settings.WIDTH / 2f, Settings.HEIGHT / 2f);
        p.popStyle();
    }

    private void drawGameOverScreen(PApplet p){
        p.pushStyle();
        p.fill(255, 0, 0);
        p.textSize(40);
        p.textAlign(PApplet.CENTER, PApplet.CENTER);
        p.text("GAME OVER", Settings.WIDTH / 2f, Settings.HEIGHT / 2f - 50);
        p.textSize(30);
        p.text("Final Score: " + score, Settings.WIDTH / 2f, Settings.HEIGHT / 2f + 10);
        p.rectMode(PApplet.CENTER);
        p.fill(100);
        p.rect(Settings.WIDTH / 2f, Settings.HEIGHT / 2f + 80, 400, 50, 10);
        p.fill(255);
        p.textSize(20);
        p.text("Click to Restart", Settings.WIDTH / 2f, Settings.HEIGHT / 2f + 80);
        p.popStyle();
    }
    private void handleNewGamePressed(int x, int y){
        float bx = Settings.WIDTH / 2f;
        float by = Settings.HEIGHT / 2f + 80;
        float bw = 400;
        float bh = 50;

        if (x >= bx - bw / 2f && x <= bx + bw / 2f &&
            y >= by - bh / 2f && y <= by + bh / 2f){
            gameState = GameState.START;
            welcomeTextIncr = 0;
            welcomeTextIncrTimer=0;
        }
    }
}

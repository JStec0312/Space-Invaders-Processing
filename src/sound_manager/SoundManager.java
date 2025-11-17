package sound_manager;

import processing.core.PApplet;
import processing.sound.SoundFile;

public class SoundManager {
    public PApplet p;
    private SoundFile newGameSound;
    private SoundFile backgroundMusic;
    private SoundFile playerShotSound;
    private SoundFile enemyShotSound;
    private SoundFile nextLevelSound;
    private SoundFile wallHitSound;
    private SoundFile gameOverSound;
    private SoundFile playerHitSound;
    private SoundFile enemyHitSound;
    private SoundFile wallExplosionSound;
    public SoundManager(PApplet p){
        this.p = p;
        try{
            newGameSound = new SoundFile(p, "/audio/new_game.mp3");
            backgroundMusic = new SoundFile(p, "/audio/bg_music.mp3");
            playerShotSound = new SoundFile(p, "/audio/player_shot.mp3");
            enemyShotSound = new SoundFile(p, "/audio/enemy_shot.mp3");
            nextLevelSound = new SoundFile(p, "/audio/next_level.mp3");
            wallHitSound = new SoundFile(p, "/audio/wall_hit.mp3");
            gameOverSound = new SoundFile(p, "/audio/game_over.mp3");
            playerHitSound = new SoundFile(p, "/audio/player_hit.mp3");
            enemyHitSound = new SoundFile(p, "/audio/enemy_hit.mp3");
            wallExplosionSound = new SoundFile(p, "/audio/wall_explosion.mp3");

        } catch (Exception e) {
            throw new RuntimeException("Error loading sound files", e);
        }
    }

    public  void playNewGameSound(){
        newGameSound.play();
    }
    public void playBackgroundMusic(){
        if(!backgroundMusic.isPlaying()){
            backgroundMusic.loop();
        }
    }
    public void playPlayerShotSound(){
        playerShotSound.play();
    }
    public void playEnemyShotSound(){
        enemyShotSound.play();
    }
    public void playNextLevelSound(){
        nextLevelSound.play();
    }
    public void playWallHitSound(){
        wallHitSound.play();
    }
    public void playGameOverSound(){
        gameOverSound.play();
    }
    public void playPlayerHitSound(){
        playerHitSound.play();
    }
    public void playEnemyHitSound(){
        enemyHitSound.play();
    }
    public void playWallExplosionSound(){
        wallExplosionSound.play();
    }


}

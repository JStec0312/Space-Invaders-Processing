package entities;

import config.Settings;
import processing.core.PApplet;

public abstract class Enemy {
    protected float x;
    protected float y;
    protected float speed;
    protected int scoreValue;
    protected int damage;
    protected float verticalSpeed;
    protected int health;
    protected int WIDTH = 40;
    protected int HEIGHT = 40;
    public Enemy(float x, float y, float speed, int scoreValue, int damage, int health, float verticalSpeed){
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.scoreValue = scoreValue;
        this.damage = damage;
        this.health = health;
        this.verticalSpeed = verticalSpeed;
    }
    float halfWidth = WIDTH / 2f;
    public void update(boolean movingRight){
        if(movingRight){
            x+= speed;
        } else{
            x-= speed;
        }
    }
    public void moveDown(){
        y += verticalSpeed;
    }
    public abstract void draw(PApplet p);
    public int getScoreValue(){
        return scoreValue;
    }
    public int getDamage(){
        return damage;
    }
    public void takeDamage(int damage){
        this.health -= damage;
    }
    public boolean isDestroyed(){
        return this.health <= 0;
    }
    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
    public float getWidth() {
        return WIDTH;
    }
    public float getHeight() {
        return HEIGHT;
    }


}

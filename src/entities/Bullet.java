package entities;

import config.Settings;
import processing.core.PApplet;

public abstract  class Bullet {
    protected float x, y, vy;
    protected int damage;
    public Bullet(float x, float y, float vy, int damage) {
        this.x = x;
        this.y = y;
        this.vy = vy; //prędkość wstrzykiwana bo może będzimy robić różne rodzaje pocisków
        this.damage = damage;
    }

    public void update(PApplet p){
        y += vy;
    }
    public abstract  void draw(PApplet p);
    public  boolean isOffScreen(int height){
        return y < 0 || y > height;
    }
    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
    public int getDamage() {
        return damage;
    }
}

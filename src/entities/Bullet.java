package entities;
import config.Settings;
import processing.core.PApplet;

public class Bullet {
    private float x, y, vy, radius;
    public Bullet(float x, float y, float vy) {
        this.x = x;
        this.y = y;
        this.vy = vy; //prędkość wstrzykiwana bo może będzimy robić różne rodzaje pocisków
        this.radius = Settings.BULLET_RADIUS;
    }
    public void update(PApplet p) {
        y += vy;
    }
    public void draw(PApplet p){
        p.pushStyle();
        p.fill(p.random(0,255), p.random(0,255), p.random(0,255));
        p.noStroke();
        p.ellipse(x, y, radius * 2, radius * 2);
        p.popStyle();
    }
    public boolean isOffScreen(int height) {
        return y+radius < -10;
    }
    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
}

package entities;
import config.Settings;
import processing.core.PApplet;

public class Bullet {
    private float x, y, vy, radius;
    public Bullet(float x, float y) {
        this.x = x;
        this.y = y;
        this.vy = Settings.BULLET_SPEED;
        this.radius = Settings.BULLET_RADIUS;
    }
    public void update(PApplet p) {
        y += vy;
    }
    public void draw(PApplet p){
        p.pushStyle();
        p.fill(255, 255, 0); // żółty
        p.noStroke();
        p.ellipse(x, y, radius * 2, radius * 2);
        p.popStyle();
    }
    boolean isOffScreen(int height) {
        return y+radius < -10 || y > height +10;
    }
    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
}

package entities;
import config.Settings;
import processing.core.PApplet;
import entities.Bullet;

public class PlayerBullet extends Bullet {
    private final float radius;

    public PlayerBullet(float x, float y, float vy, float radius, int damage) {
        super(x, y, vy, damage);
        this.radius = radius;
    }

    @Override
    public void draw(PApplet p) {
        p.pushStyle();
        p.fill(255, 0, 0);
        p.noStroke();
        p.ellipse(x, y, radius * 2, radius * 2);
        p.popStyle();
    }

}

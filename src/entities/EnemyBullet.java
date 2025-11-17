package entities;

import config.Settings;
import processing.core.PApplet;

public class EnemyBullet extends  Bullet{

    // Dodaj te pola do przechowywania wymiarów
    private float width;
    private float height;

    public EnemyBullet(float x, float y, float vy, int damage) {
        super(x, y, vy, damage);
        // Ustaw wymiary w konstruktorze
        this.width = Settings.ENEMY_BULLET_WIDTH;   // szerokość z ustawień
        this.height = Settings.ENEMY_BULLET_HEIGHT; // wysokość z ustawień
    }

    @Override
    public void draw(PApplet p) {
        p.pushStyle();

        p.fill(255, 255, 0); // Jasny żółty kolor
        p.noStroke(); // Bez obramowania

        // Rysuj prostokąt. Używamy trybu CENTER (jeśli ustawiony w PApplet)
        // lub rysujemy od rogu, dostosowując x i y.
        // Zakładam, że (x, y) to środek.
        p.rectMode(PApplet.CENTER); // Upewnij się, że rysujesz od środka
        p.rect(x, y, width, height);

        p.popStyle();
    }
}
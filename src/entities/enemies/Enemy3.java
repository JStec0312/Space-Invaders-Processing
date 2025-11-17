package entities.enemies;

import entities.Enemy;
import processing.core.PApplet;

public class Enemy3 extends Enemy {


    public Enemy3(float x, float y, float speed, int scoreValue, int damage, int health, float verticalSpeed) {
        super(x, y, speed, scoreValue, damage, health, verticalSpeed);
    }

    @Override
    public void draw(PApplet p) {
        p.pushStyle();
        p.rectMode(PApplet.CENTER);
        p.noStroke();

        // --- KOLORY OD health ---
        int r, g, b;
        if (health > 12) {          // Więcej zdrowia dla "ciężkiego" przeciwnika
            r = 150; g = 150; b = 180; // Szary/Niebieski (Zdrowy pancerz)
        } else if (health > 6) {
            r = 200; g = 180; b = 100; // Brązowo-złoty (Uszkodzony)
        } else {
            r = 255; g = 80; b = 80;   // Czerwony (Krytyczny)
        }

        // --- ZMIENNE ANIMACJI ---
        float time = p.frameCount * 0.05f;
        float pulse = p.sin(time * 2f) * 3f; // Powolne pulsowanie silnika
        float legOffset = p.sin(time * 1.5f) * 2f; // Delikatne "kroczenie" nóg

        // --- TŁO / PODSTAWA PANCERZA ---
        p.fill(r, g, b, 40);
        p.rect(x, y, WIDTH * 1.2f, HEIGHT * 1.2f, 15);

        // --- GŁÓWNY KORPUS (warstwy pancerza) ---
        p.fill(25); // Najciemniejsza warstwa
        p.rect(x, y, WIDTH, HEIGHT, 10);

        p.fill(40); // Średnia warstwa
        p.rect(x, y, WIDTH * 0.85f, HEIGHT * 0.85f, 8);

        p.fill(r - 30, g - 30, b - 30); // Warstwa kolorystyczna pancerza
        p.rect(x, y, WIDTH * 0.7f, HEIGHT * 0.7f, 6);

        // --- NOGI / STABILIZATORY ---
        float legWidth = WIDTH * 0.2f;
        float legHeight = HEIGHT * 0.3f;
        float legOffsetX = WIDTH * 0.4f;
        float legOffsetY = HEIGHT * 0.3f;

        p.fill(30); // Kolor nóg
        // Lewa noga (przednia)
        p.rect(x - legOffsetX, y + legOffsetY + legOffset, legWidth, legHeight, 5);
        // Prawa noga (tylna)
        p.rect(x + legOffsetX, y + legOffsetY - legOffset, legWidth, legHeight, 5);

        // --- CENTRALNY "SILNIK" / RDZEŃ ---
        p.fill(r, g, b);
        p.ellipse(x, y, WIDTH * 0.4f + pulse, HEIGHT * 0.4f + pulse);

        // --- WZMOCNIENIA NAROŻNE (opcjonalne detale) ---
        p.fill(60);
        float cornerSize = 8;
        p.rect(x - WIDTH * 0.45f, y - HEIGHT * 0.45f, cornerSize, cornerSize, 3);
        p.rect(x + WIDTH * 0.45f, y - HEIGHT * 0.45f, cornerSize, cornerSize, 3);
        p.rect(x - WIDTH * 0.45f, y + HEIGHT * 0.45f, cornerSize, cornerSize, 3);
        p.rect(x + WIDTH * 0.45f, y + HEIGHT * 0.45f, cornerSize, cornerSize, 3);


        p.popStyle();
    }
}

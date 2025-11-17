package entities.enemies;

import entities.Enemy;
import processing.core.PApplet;

public class Enemy4 extends Enemy {


    public Enemy4(float x, float y, float speed, int scoreValue, int damage, int health, float verticalSpeed) {
        super(x, y, speed, scoreValue, damage, health, verticalSpeed);
    }

    @Override
    public void draw(PApplet p) {
        p.pushStyle();
        p.ellipseMode(PApplet.CENTER);
        p.noStroke();

        // --- KOLORY OD health ---
        int r, g, b;
        if (health > 5) {
            r = 150; g = 100; b = 255; // Fioletowy/Niebieski (Zdrowy)
        } else if (health > 2) {
            r = 255; g = 150; b = 255; // Różowy (Uszkodzony)
        } else {
            r = 255; g = 50; b = 200;  // Magenta (Krytyczny)
        }

        // --- ZMIENNE ANIMACJI ---
        float time = p.frameCount * 0.07f;

        // Płynna zmiana przezroczystości
        int alpha = (int) PApplet.map(p.sin(time * 1.5f), -1, 1, 80, 180); // Od 80 do 180 alpha

        // Delikatne falowanie kształtu
        float waveOffset = p.sin(time * 2.5f) * 3f;
        float waveScale = p.cos(time * 1.8f) * 0.05f; // Skalowanie kształtu

        // Pulsacja rdzenia
        float corePulse = p.sin(time * 4f) * 2f;


        // --- ZEWNĘTRZNA AURA / MIGOTANIE ---
        p.fill(r, g, b, alpha / 2); // Jeszcze bardziej przezroczysta aura
        p.ellipse(x, y, WIDTH * 1.8f + waveOffset, HEIGHT * 1.8f + waveOffset);

        // --- GŁÓWNY KSZTAŁT (przezroczysty, falujący) ---
        p.fill(r, g, b, alpha);
        p.ellipse(x, y, WIDTH * (1 + waveScale) + waveOffset, HEIGHT * (1 + waveScale) + waveOffset);

        // --- RDZEŃ ENERGETYCZNY (najmniej przezroczysty) ---
        p.fill(r, g, b, 200); // Rdzeń jest bardziej widoczny
        p.ellipse(x, y, WIDTH * 0.4f + corePulse, HEIGHT * 0.4f + corePulse);

        // --- CZĄSTECZKI ENERGETYCZNE (opcjonalny detal) ---
        // Generujemy kilka losowych cząsteczek wokół ducha
        for (int i = 0; i < 5; i++) {
            float particleX = x + p.random(-WIDTH * 0.7f, WIDTH * 0.7f);
            float particleY = y + p.random(-HEIGHT * 0.7f, HEIGHT * 0.7f);
            int particleAlpha = (int) p.random(50, 150);
            p.fill(r, g, b, particleAlpha);
            p.circle(particleX, particleY, p.random(2, 5));
        }

        p.popStyle();
    }
}

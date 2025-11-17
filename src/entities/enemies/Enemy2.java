package entities.enemies;

import entities.Enemy;
import processing.core.PApplet;

public class Enemy2 extends Enemy {


    public Enemy2(float x, float y, float speed, int scoreValue, int damage, int health, float verticalSpeed) {
        super(x, y, speed, scoreValue, damage, health, verticalSpeed);
    }

    @Override
    public void draw(PApplet p) {
        p.pushStyle();
        p.ellipseMode(PApplet.CENTER);
        p.noStroke();

        // --- KOLORY OD health ---
        int r, g, b;
        if (health > 6) {
            r = 100; g = 255; b = 100; // Zielony (Zdrowy)
        } else if (health > 3) {
            r = 255; g = 255; b = 100; // Żółty (Uszkodzony)
        } else {
            r = 255; g = 100; b = 100; // Czerwony (Krytyczny)
        }

        // --- ZMIENNE ANIMACJI ---
        float time = p.frameCount * 0.08f;
        float rotation = time * 2.5f; // Szybki obrót
        float pulse = p.sin(time * 3f) * 2f; // Szybkie pulsowanie

        // --- TŁO / AURA ---
        p.fill(r, g, b, 30);
        p.ellipse(x, y, WIDTH * 1.5f + pulse, HEIGHT * 1.5f + pulse);

        // --- GŁÓWNY KORPUS (obracający się) ---
        p.translate(x, y); // Przenosimy początek układu na pozycję przeciwnika
        p.rotate(rotation); // Obracamy cały kształt

        p.fill(30); // Ciemna obudowa
        p.ellipse(0, 0, WIDTH, HEIGHT); // Centralna elipsa

        // Cztery "ramiona" lub "łopaty"
        float armLength = WIDTH * 0.4f;
        float armThickness = 8;
        p.rectMode(PApplet.CENTER);
        p.fill(60);
        p.rect(0, armLength, armThickness, armLength * 1.5f, 5); // Dolne
        p.rect(0, -armLength, armThickness, armLength * 1.5f, 5); // Górne
        p.rect(armLength, 0, armLength * 1.5f, armThickness, 5); // Prawe
        p.rect(-armLength, 0, armLength * 1.5f, armThickness, 5); // Lewe

        // --- CENTRALNY RDZEŃ ENERGETYCZNY ---
        p.fill(r, g, b);
        p.ellipse(0, 0, WIDTH * 0.5f + pulse, HEIGHT * 0.5f + pulse);

        p.rotate(-rotation); // Przywracamy obrót
        p.translate(-x, -y); // Przywracamy początek układu

        p.popStyle();
    }
}

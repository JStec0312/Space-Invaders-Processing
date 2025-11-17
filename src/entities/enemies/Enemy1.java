package entities.enemies;

import entities.Enemy;
import processing.core.PApplet;

public class Enemy1 extends Enemy {

    public Enemy1(float x, float y, float speed, int scoreValue, int damage, int health, float verticalSpeed) {
        super(x, y, speed, scoreValue, damage, health, verticalSpeed);
    }

    public void draw(PApplet p) {
        p.pushStyle();
        p.ellipseMode(PApplet.CENTER); // Będziemy używać elips
        p.rectMode(PApplet.CENTER);
        p.noStroke();

        // --- KOLORY OD health (zachowane z oryginału) ---
        int r, g, b;
        if (health > 7) {
            r = 80; g = 230; b = 255;  // Zdrowy (Cyjan)
        } else if (health > 3) {
            r = 240; g = 200; b = 80;  // Uszkodzony (Żółty)
        } else {
            r = 255; g = 60; b = 80;   // Krytyczny (Czerwony)
        }

        // --- ZMIENNE ANIMACJI ---
        float time = p.frameCount * 0.05f;

        // Wolniejsze, głębsze pulsowanie dla "oka"
        float pulse = p.sin(time * 2.0f) * 3f;

        // Płynne unoszenie się dla bocznych stateczników
        float hover = p.sin(time) * 4f;

        // Powolne skanowanie "okiem" na boki
        float scan = p.cos(time * 0.5f) * 3f;


        // --- TŁO / AURA ---
        p.fill(r, g, b, 40);
        // Używamy elipsy dla bardziej miękkiej aury
        p.ellipse(x, y, WIDTH * 1.8f, HEIGHT * 1.8f);


        // --- BOCZNE STATECZNIKI / SILNIKI (NOWOŚĆ) ---
        // Unoszą się niezależnie (jeden w górę, drugi w dół)
        float finW = WIDTH * 0.25f;
        float finH = HEIGHT * 0.5f;
        float finOffsetX = WIDTH * 0.7f;

        p.fill(40); // Ciemny metal
        // Lewy statecznik
        p.rect(x - finOffsetX, y + hover, finW, finH, 5);
        // Prawy statecznik
        p.rect(x + finOffsetX, y - hover, finW, finH, 5);

        // Światełka na statecznikach
        p.fill(r, g, b, 200);
        p.circle(x - finOffsetX, y + hover, finW * 0.7f);
        p.circle(x + finOffsetX, y - hover, finW * 0.7f);


        // --- GŁÓWNY KORPUS / OBUDOWA ---
        // Używamy elipsy zamiast prostokąta dla gładszego kształtu
        p.fill(20); // Bardzo ciemna obudowa
        p.ellipse(x, y, WIDTH, HEIGHT);
        // Jaśniejszy panel
        p.fill(50);
        p.ellipse(x, y, WIDTH * 0.85f, HEIGHT * 0.85f);


        // --- CENTRALNE "OKO" (zamiast rdzenia) ---
        float eyeSize = WIDTH * 0.6f + pulse;

        // 1. "Źrenica" / Tło oka (przesuwa się - "skanuje")
        p.fill(10); // Czarna "głębia"
        p.ellipse(x + scan, y, eyeSize, eyeSize);

        // 2. Tęczówka (główny kolor zdrowia)
        p.fill(r, g, b);
        p.ellipse(x + scan, y, eyeSize * 0.9f, eyeSize * 0.9f);

        // 3. Blik / Odbicie światła
        p.fill(255, 255, 255, 200);
        p.circle(x + scan + eyeSize * 0.15f, y - eyeSize * 0.15f, eyeSize * 0.3f);


        // --- FEEDBACK OBRAŻEŃ (NOWOŚĆ) ---
        // Jeśli zdrowie jest niskie, narysuj "pęknięcie" na oku
        if (health <= 3) {
            p.stroke(r, g, b); // Użyj koloru krytycznego
            p.strokeWeight(2);
            p.noFill();
            // Narysuj prostą linię pęknięcia przez środek
            float crackY = HEIGHT * 0.2f;
            p.line(x + scan - eyeSize * 0.3f, y - crackY, x + scan + eyeSize * 0.3f, y + crackY);
        }

        p.popStyle();
    }

}

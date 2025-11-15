package entities;

import processing.core.PApplet;
import config.Settings;

public class Player {
    private float x, y;
    private float speed;
    private float width;
    private float height;
    private float engineWidth;
    private float engineHeight;
    private int lives;

    public Player(float x, float y, int lives) {
        this.x = x;
        this.y = y;
        this.speed = Settings.PLAYER_SPEED;
        this.width = Settings.PLAYER_WIDTH;
        this.height = Settings.PLAYER_HEIGHT;
        this.engineWidth = Settings.PLAYER_ENGINE_WIDTH;
        this.engineHeight = Settings.PLAYER_ENGINE_HEIGHT;
    }

    public void draw(PApplet p) {
        p.pushStyle();
        p.rectMode(PApplet.CENTER);

        // --- KOLOR BAZOWY OD HP ---
        // zamiast 3 sztywnych kolorów – przejście od zieleni do czerwieni w HSB
        p.colorMode(PApplet.HSB, 360, 100, 100, 100);
        float hpNorm = PApplet.constrain(lives / 10.0f, 0f, 1f); // zakładam max ~10 HP
        float hue = PApplet.lerp(0f, 120f, hpNorm); // 0 = czerwony, 120 = zielony
        int baseColor = p.color(hue, 80, 90);
        int borderColor = p.color(hue, 40, 60, 80);

        // --- GŁÓWNY PANCERZ ---
        float bodyW = width;
        float bodyH = height;

        // lekko przyciemnione tło ściany (płyta pancerna)
        p.noStroke();
        p.fill(hue, 40, 25);
        p.rect(x, y, bodyW * 1.05f, bodyH * 1.1f, 10);

        // wewnętrzny panel
        p.fill(baseColor);
        p.rect(x, y, bodyW * 0.9f, bodyH * 0.9f, 10);

        // --- SEGMENTY PANCERZA (podział na „kafelki”) ---
        p.stroke(borderColor);
        p.strokeWeight(2);
        p.noFill();

        // pionowa linia dzieląca
        p.line(x, y - bodyH * 0.45f, x, y + bodyH * 0.45f);

        // poziome linie
        p.line(x - bodyW * 0.45f, y, x + bodyW * 0.45f, y);
        p.line(x - bodyW * 0.45f, y - bodyH * 0.25f, x + bodyW * 0.45f, y - bodyH * 0.25f);
        p.line(x - bodyW * 0.45f, y + bodyH * 0.25f, x + bodyW * 0.45f, y + bodyH * 0.25f);

        // --- RDZEŃ ENERGETYCZNY W ŚRODKU ---
        float coreR = bodyH * 0.22f;
        float t = p.millis() * 0.003f;

        // otoczka
        p.noStroke();
        p.fill(hue, 60, 40, 70);
        p.ellipse(x, y, coreR * 1.6f, coreR * 1.6f);

        // pulsujący środek
        float pulse = 0.8f + 0.2f * PApplet.sin(t * 2f);
        p.fill(hue, 90, 100);
        p.ellipse(x, y, coreR * pulse, coreR * pulse);

        // małe „oko” w środku
        p.fill(0, 0, 100);
        p.ellipse(x, y, coreR * 0.35f, coreR * 0.35f);

        // --- GÓRNE „ANTENY / EMITERY” ---
        float topY = y - bodyH * 0.55f;
        float emitterW = bodyW * 0.18f;
        float emitterH = bodyH * 0.18f;

        for (int i = -1; i <= 1; i++) {
            float ex = x + i * bodyW * 0.3f;
            // podstawa
            p.fill(hue, 40, 40);
            p.rect(ex, topY, emitterW, emitterH * 0.6f, 4);

            // świecąca kulka
            p.fill(hue, 80, 100, 90);
            p.ellipse(ex, topY - emitterH * 0.3f, emitterH * 0.7f, emitterH * 0.7f);

            // pionowa smużka (jak wiązka)
            p.noStroke();
            p.fill(hue, 50, 80, 40);
            p.rect(ex, topY - emitterH, emitterW * 0.3f, emitterH * 1.5f, 4);
        }

        // --- DOLNE „KOTWICE” / NÓŻKI ---
        float baseY = y + bodyH * 0.55f;
        float legW = bodyW * 0.22f;
        float legH = bodyH * 0.18f;

        p.fill(hue, 20, 20);
        p.rect(x - bodyW * 0.35f, baseY, legW, legH, 4);
        p.rect(x + bodyW * 0.35f, baseY, legW, legH, 4);

        // małe jasne „śrubki”
        p.fill(0, 0, 100);
        float screwR = 3;
        p.ellipse(x - bodyW * 0.45f, y - bodyH * 0.45f, screwR, screwR);
        p.ellipse(x + bodyW * 0.45f, y - bodyH * 0.45f, screwR, screwR);
        p.ellipse(x - bodyW * 0.45f, y + bodyH * 0.45f, screwR, screwR);
        p.ellipse(x + bodyW * 0.45f, y + bodyH * 0.45f, screwR, screwR);

        p.popStyle();
    }


    public void update(boolean left, boolean right) {
        if (left)  this.x -= speed;
        if (right) this.x += speed;
        float halfW = width / 2f;
        this.x = Math.max(halfW, Math.min(x, Settings.WIDTH - halfW)); // czy nie wylatujemy za ekran
    }
    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
    public float getHeight(){
        return height;
    }
}

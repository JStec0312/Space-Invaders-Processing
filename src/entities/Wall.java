package entities;

import config.Settings;
import processing.core.PApplet;


//@Note: The wall design has been AI generated and IT IS NOT MY WORK.
// It has been done so, to improve game aesthetics quickly.
public class Wall {
    private  final float WIDTH = Settings.WALL_WIDTH;
    private  final float HEIGHT = Settings.WALL_HEIGHT;
    private int HP = Settings.WALL_HP;
    private final float x, y;
    public Wall(float x, float y){
        this.x = x;
        this.y = y;
    }
    public void decreaseHP(){
        HP--;
    }
    public int getHP(){
        return HP;
    }
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
    public void draw(PApplet p) {
        p.pushStyle();
        p.rectMode(PApplet.CENTER);
        p.ellipseMode(PApplet.CENTER);

        float cx = x;
        float cy = y;
        float bodyW = WIDTH;
        float bodyH = HEIGHT;
        float hw = bodyW / 2f;
        float hh = bodyH / 2f;

        // --- HP → kolor energii (płynne przejście od czerwieni do niebiesko-turkusowego) ---
        float t = PApplet.constrain(HP / 10.0f, 0f, 1f);  // zakładam max ~10 HP
        int colDamaged = p.color(255, 80, 60);            // red / orange
        int colHealthy = p.color(80, 220, 255);           // cyan / blue
        int bodyCol = p.lerpColor(colDamaged, colHealthy, t);

        // --- lekka poświata wokół ściany (aurora) ---


        // --- główna tarcza – heksagonalny „shield” ---
        p.noStroke();
        p.fill(15, 25, 50, 230);
        p.beginShape();
        p.vertex(cx - hw * 0.6f, cy - hh);
        p.vertex(cx + hw * 0.6f, cy - hh);
        p.vertex(cx + hw,        cy);
        p.vertex(cx + hw * 0.6f, cy + hh);
        p.vertex(cx - hw * 0.6f, cy + hh);
        p.vertex(cx - hw,        cy);
        p.endShape(PApplet.CLOSE);

        // obrys tarczy w kolorze energii
        p.noFill();
        p.stroke(bodyCol);
        p.strokeWeight(3f);
        p.beginShape();
        p.vertex(cx - hw * 0.55f, cy - hh * 0.9f);
        p.vertex(cx + hw * 0.55f, cy - hh * 0.9f);
        p.vertex(cx + hw * 0.9f,  cy);
        p.vertex(cx + hw * 0.55f, cy + hh * 0.9f);
        p.vertex(cx - hw * 0.55f, cy + hh * 0.9f);
        p.vertex(cx - hw * 0.9f,  cy);
        p.endShape(PApplet.CLOSE);

        // --- boczne „skrzydła” / panele energetyczne ---
        p.noStroke();
        p.fill(25, 40, 80, 220);
        // lewy panel
        p.beginShape();
        p.vertex(cx - hw * 1.05f, cy - hh * 0.5f);
        p.vertex(cx - hw * 0.75f, cy - hh * 0.9f);
        p.vertex(cx - hw * 0.75f, cy + hh * 0.9f);
        p.vertex(cx - hw * 1.05f, cy + hh * 0.5f);
        p.endShape(PApplet.CLOSE);
        // prawy panel
        p.beginShape();
        p.vertex(cx + hw * 1.05f, cy - hh * 0.5f);
        p.vertex(cx + hw * 0.75f, cy - hh * 0.9f);
        p.vertex(cx + hw * 0.75f, cy + hh * 0.9f);
        p.vertex(cx + hw * 1.05f, cy + hh * 0.5f);
        p.endShape(PApplet.CLOSE);

        // małe „diodki” na panelach
        p.fill(120, 220, 255);
        float ledR = bodyW * 0.04f;
        p.ellipse(cx - hw * 0.9f, cy - hh * 0.4f, ledR, ledR);
        p.ellipse(cx - hw * 0.9f, cy,              ledR, ledR);
        p.ellipse(cx - hw * 0.9f, cy + hh * 0.4f, ledR, ledR);

        p.ellipse(cx + hw * 0.9f, cy - hh * 0.4f, ledR, ledR);
        p.ellipse(cx + hw * 0.9f, cy,              ledR, ledR);
        p.ellipse(cx + hw * 0.9f, cy + hh * 0.4f, ledR, ledR);

        // --- górny „maszt” / antena ---
        p.noStroke();
        p.fill(20, 35, 70);
        float mastW = bodyW * 0.22f;
        float mastH = bodyH * 0.55f;
        p.rect(cx, cy - hh * 1.05f, mastW, mastH, 6);

        // kulka energii na szczycie
        p.fill(bodyCol);
        float orbR = bodyW * 0.24f;
        p.ellipse(cx, cy - hh * 1.38f, orbR, orbR);

        // wewnętrzny „rdzeń energii” w środku tarczy
        p.noStroke();
        p.fill(bodyCol, 220);
        float coreW = bodyW * 0.45f;
        float coreH = bodyH * 0.55f;
        p.rect(cx, cy, coreW, coreH, 10);

        // gradientowy „pasek ładowania” w rdzeniu
        p.fill(0, 0, 0, 120);
        p.rect(cx, cy, coreW * 0.85f, coreH * 0.18f, 8);
        p.fill(120, 255, 255);
        float charge = PApplet.map(HP, 0, 10, 0.1f, 1.0f);
        p.rect(cx - coreW * 0.4f + coreW * 0.8f * charge / 2f,
                cy,
                coreW * 0.8f * charge,
                coreH * 0.09f,
                6);

        // segmenty w rdzeniu (jak panele reaktora)
        p.stroke(10, 30, 60);
        p.strokeWeight(1.4f);
        p.noFill();
        for (int i = -1; i <= 1; i++) {
            float yy = cy + i * coreH * 0.18f;
            p.rect(cx, yy, coreW * 0.9f, coreH * 0.22f, 6);
        }

        // --- dolne „emittery” / nóżki ---
        p.noStroke();
        p.fill(25, 40, 80);
        float footW = bodyW * 0.28f;
        float footH = bodyH * 0.22f;
        p.rect(cx - hw * 0.4f, cy + hh * 1.02f, footW, footH, 6);
        p.rect(cx + hw * 0.4f, cy + hh * 1.02f, footW, footH, 6);

        p.fill(bodyCol);
        float emitterR = bodyW * 0.12f;
        p.ellipse(cx - hw * 0.4f, cy + hh * 1.18f, emitterR, emitterR * 0.6f);
        p.ellipse(cx + hw * 0.4f, cy + hh * 1.18f, emitterR, emitterR * 0.6f);

        // --- „pęknięcia” przy niskim HP ---
        if (HP <= 3) {
            p.stroke(255, 200, 200);
            p.strokeWeight(1.5f);
            p.noFill();

            p.beginShape();
            p.vertex(cx, cy - hh * 0.7f);
            p.vertex(cx + hw * 0.15f, cy - hh * 0.3f);
            p.vertex(cx - hw * 0.05f, cy);
            p.vertex(cx + hw * 0.2f, cy + hh * 0.35f);
            p.endShape();

            p.beginShape();
            p.vertex(cx - hw * 0.3f, cy - hh * 0.4f);
            p.vertex(cx - hw * 0.1f, cy - hh * 0.1f);
            p.vertex(cx - hw * 0.25f, cy + hh * 0.25f);
            p.endShape();
        }

        p.popStyle();
    }

}

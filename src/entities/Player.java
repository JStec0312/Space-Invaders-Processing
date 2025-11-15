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

    public Player(float x, float y) {
        this.x = x;
        this.y = y;
        this.speed = Settings.PLAYER_SPEED;
        this.width = Settings.PLAYER_WIDTH;
        this.height = Settings.PLAYER_HEIGHT;
        this.engineWidth = Settings.PLAYER_ENGINE_WIDTH;
        this.engineHeight = Settings.PLAYER_ENGINE_HEIGHT;
    }

    public void draw(PApplet p) {
        p.pushStyle(); //start rysowania
        p.noStroke();
        p.rectMode(PApplet.CENTER);


        float bodyTop    = y - height / 2f; //środek góra
        float bodyBottom = y + height / 2f; //środek dół

        // korpus
        p.fill(200); // jasnoszary kadłub
        p.rect(x, y, width, height);

        //czubek
        p.fill(220, 0, 60); // czerwony czubek
        p.triangle(
                x,bodyTop - height / 2f, // górny wierzchołek czubka
                x - width / 2f, bodyTop, //lewy dolny wierzchołek czubka
                x + width / 2f, bodyTop  //prawy dolny wierzchołek czubka
        );

        // skrzydła
        p.fill(180, 0, 40);
        // lewe skrzydło
        p.triangle(
                x - width / 2f, y,            // góra skrzydła
                x - width, bodyBottom,   // lewy dół skrzydła
                x - width / 2f, bodyBottom    // lewy dół przy kadłubie
        );
        // prawe skrzydło
        p.triangle(
                x + width / 2f, y, //góra skrzydła
                x + width,      bodyBottom, //prawy dół skrzydła
                x + width / 2f, bodyBottom // prawy przy kadłubie
        );

        // silnik
        float engineTop    = bodyBottom;
        float engineCenter = engineTop + engineHeight / 2f;
        float engineBottom = engineTop + engineHeight;

        p.fill(90); // ciemny szary
        p.rect(x, engineCenter, engineWidth, engineHeight);

        // Płomień
        p.fill(255, 180, 0); // pomarańczowy
        float rand = p.random(-4, 4); // drżenie silnika
        p.triangle(
                x,engineBottom + engineHeight + rand, // czubek płomienia
                x - engineWidth/2f, engineBottom,// lewy u podstawy
                x + engineWidth/2f, engineBottom // prawy u podstawy
        );

        p.popStyle(); //koniec rysowania
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

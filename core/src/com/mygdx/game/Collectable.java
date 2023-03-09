package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class Collectable {
    Texture landingAreaTexture;
    Texture goldCoinTexture;
    Array<Sprite> landing_areas;
    Array<Sprite> collectables;
    int score;

    public Collectable() {
        //Collectable und Collectables liste Initialisieren
        collectables = new Array<>();
        landing_areas = new Array<>();
        landingAreaTexture = new Texture("landing_area.png");
        goldCoinTexture = new Texture("goldCoin.png");
        score = 0;
    }

    public void addCollectable(float x, float y, float width, float height) {
        Sprite landing_area = new Sprite(landingAreaTexture);
        landing_area.setX(x);
        landing_area.setY(y);
        landing_area.setSize(width, height);
        landing_areas.add(landing_area);

        Sprite goldCoin = new Sprite(goldCoinTexture);
        goldCoin.setX(landing_area.getX() + 10);
        goldCoin.setY(landing_area.getY() + 10);
        goldCoin.setSize(50, 50);
        collectables.add(goldCoin);

    }
    /*
    public void test(){

    }

     */

    //Update Funktion prüft die "Spawnzeiten" und ob das Objekt eingesammelt wurde
    public void updateCollectable(Sprite rocket) {


        for (Iterator<Sprite> iter = landing_areas.iterator(); iter.hasNext(); ) {
            Rectangle landing_area = iter.next().getBoundingRectangle();
            if (landing_area.overlaps(rocket.getBoundingRectangle())) {

                if (Math.toRadians(rocket.getRotation()) > 0 && Math.toRadians(rocket.getRotation()) < 180) {
                    rocket.setRotation(rocket.getRotation() - 1f);
                } else {
                    rocket.setRotation(rocket.getRotation() + 1f);
                }
                score++;
                collectables.removeValue(iter.next(), true);
            }

        }
    }

    public Array<Sprite> getLandingAreas() {
        return landing_areas;
    }

    public Array<Sprite> getCollectables() {
        return collectables;
    }

    public int getScore() {
        return score;
    }

    public void dispose() {
        landingAreaTexture.dispose();
    }

}
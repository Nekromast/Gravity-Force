package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.landingAreasContainer;

import java.util.Iterator;

public class Collectable {
    Texture landingAreaTexture;
    Texture goldCoinTexture;
    Array<landingAreasContainer> landing_areas;
    Array<Sprite> collectables;
    Sound coin_sound;
    int score;
    Rocket rocket;
    TiledMap map;
    float volume;
    int coinCount = 0;


    public Collectable() {
        //Collectable und Collectables liste Initialisieren
        collectables = new Array<>();
        landing_areas = new Array<>();
        landingAreaTexture = new Texture("map/landing_area.png");
        goldCoinTexture = new Texture("map/goldCoin.png");
        score = 0;
        coin_sound = Gdx.audio.newSound(Gdx.files.internal("sounds/coin.mp3"));
        volume = 0.5f;
    }

    public void addCollectable(float x, float y, float width, float height) {
        Sprite landing_area = new Sprite(landingAreaTexture);
        landingAreasContainer landing_area_container = new landingAreasContainer(landing_area);
        landing_area.setX(x);
        landing_area.setY(y);
        landing_area.setSize(width, height);
        landing_areas.add(landing_area_container);

        Sprite goldCoin = new Sprite(goldCoinTexture);
        goldCoin.setX(landing_area.getX() + 10);
        goldCoin.setY(landing_area.getY() + 10);
        goldCoin.setSize(50, 50);
        collectables.add(goldCoin);

    }
    public void play(){
        coin_sound.play(volume);

    }


    //Update Funktion prüft die "Spawnzeiten" und ob das Objekt eingesammelt wurde
    public void checkLanding(Sprite rocket) {


        for (Iterator<landingAreasContainer> iter = landing_areas.iterator(); iter.hasNext(); ) {
            Rectangle landing_area = iter.next().getLandingArea().getBoundingRectangle();
            if (landing_area.overlaps(rocket.getBoundingRectangle())) {

                if (Math.toRadians(rocket.getRotation()) > 0 && Math.toRadians(rocket.getRotation()) < 180) {
                    rocket.setRotation(rocket.getRotation() - 1f);
                } else {
                    rocket.setRotation(rocket.getRotation() + 1f);
                }
                coinCount++;
                //score++;
                play();
                iter.next().switchWasLanded();
                collectables.removeValue(iter.next().getLandingArea(), true);
            }
        }
    }

    public void setRocket(Rocket rocket){
        this.rocket = rocket;

    }

    public void setMap(TiledMap map){
        this.map = map;
    }

    public Array<landingAreasContainer> getLandingAreas() {
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
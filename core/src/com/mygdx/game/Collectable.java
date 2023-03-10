package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class Collectable {
    Texture landingAreaTexture;
    Texture goldCoinTexture;
    Array<Sprite> landing_areas;
    Array<Sprite> collectables;
    Sound coin_sound;
    int score;
    Rocket rocket;
    TiledMap map;
    float volume;


    public Collectable() {
        //Collectable und Collectables liste Initialisieren
        collectables = new Array<>();
        landing_areas = new Array<>();
        landingAreaTexture = new Texture("map/landing_area.png");
        goldCoinTexture = new Texture("map/goldCoin.png");
        score = 0;
        coin_sound = Gdx.audio.newSound(Gdx.files.internal("coin.mp3"));
        volume = 0.5f;
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
    public void play(){
        coin_sound.play(volume);

    }

    public void coinCollision(){
        for (Iterator<Sprite> iter = collectables.iterator(); iter.hasNext(); ) {
            Rectangle coin = iter.next().getBoundingRectangle();
            if (coin.overlaps(rocket.getRocket().getBoundingRectangle())) {
                coin_sound.play();
                score++;
                collectables.removeValue(iter.next(), true);
            }
        }

    }

    //Update Funktion prüft die "Spawnzeiten" und ob das Objekt eingesammelt wurde
    public void checkLanding(Sprite rocket) {


        for (Iterator<Sprite> iter = landing_areas.iterator(); iter.hasNext(); ) {
            Rectangle landing_area = iter.next().getBoundingRectangle();
            if (landing_area.overlaps(rocket.getBoundingRectangle())) {

                if (Math.toRadians(rocket.getRotation()) > 0 && Math.toRadians(rocket.getRotation()) < 180) {
                    rocket.setRotation(rocket.getRotation() - 1f);
                } else {
                    rocket.setRotation(rocket.getRotation() + 1f);
                }
                score++;
                play();
                collectables.removeValue(iter.next(), true);
            }
        }
    }

    public void setRocket(Rocket rocket){
        this.rocket = rocket;

    }

    public void setMap(TiledMap map){
        this.map = map;
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
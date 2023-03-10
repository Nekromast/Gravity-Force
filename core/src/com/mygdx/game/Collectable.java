package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Collectable {
    Texture landingAreaTexture;
    Texture goldCoinTexture;
    Sprite landingArea;
    Sprite goldCoin;
    //Array<landingAreasContainer> landing_areas;
    Sound coin_sound;
    float volume;
    boolean wasLanded;


    public Collectable(float x, float y, float width, float height) {
        //landing_areas = new Array<>();
        landingAreaTexture = new Texture("map/landing_area.png");
        goldCoinTexture = new Texture("map/goldCoin.png");
        coin_sound = Gdx.audio.newSound(Gdx.files.internal("sounds/coin.mp3"));
        volume = 0.5f;

        landingArea = new Sprite(landingAreaTexture);
        landingArea.setX(x);
        landingArea.setY(y);
        landingArea.setSize(width, height);

        goldCoin = new Sprite(goldCoinTexture);
        goldCoin.setX(landingArea.getX() + 10);
        goldCoin.setY(landingArea.getY() + 10);
        goldCoin.setSize(50, 50);

        wasLanded = false;
    }

    public void addCollectable(float x, float y, float width, float height) {
        Sprite landing_area = new Sprite(landingAreaTexture);
        landing_area.setX(x);
        landing_area.setY(y);
        landing_area.setSize(width, height);

        Sprite goldCoin = new Sprite(goldCoinTexture);
        goldCoin.setX(landing_area.getX() + 10);
        goldCoin.setY(landing_area.getY() + 10);
        goldCoin.setSize(50, 50);

        //landingAreasContainer landing_area_container = new landingAreasContainer(landing_area, goldCoin);
        //landing_areas.add(landing_area_container);

    }
    public void play(){
        coin_sound.play(volume);

    }

    /*
    public Array<landingAreasContainer> getLandingAreas() {
        return landing_areas;
    }
     */
    public Sprite getLandingArea() {
        return landingArea;
    }

    public Sprite getGoldCoin() {
        return goldCoin;
    }

    public void dispose() {
        landingAreaTexture.dispose();
    }

    public void setWasLanded(boolean wasLanded) {
        this.wasLanded = wasLanded;
    }

    public boolean wasLanded() {
        return wasLanded;
    }

}
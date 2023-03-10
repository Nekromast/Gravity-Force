package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class landingAreasContainer {

    public Sprite landingArea;
    public Sprite goldCoin;
    public boolean wasLanded;
    Texture goldCoinTexture;


    public landingAreasContainer(Sprite landingArea, Sprite goldCoin) {
        this.landingArea = landingArea;
        this.goldCoin = goldCoin;
        wasLanded = false;
    }

    public Sprite getLandingArea() {
        return landingArea;
    }

    public Sprite getGoldCoin() {
        return goldCoin;
    }

    public void setWasLanded(boolean wasLanded) {
        this.wasLanded = wasLanded;
    }

    public boolean wasLanded() {
        return wasLanded;
    }

    public void dispose() {
        landingArea.getTexture().dispose();
    }

    public void switchWasLanded() {
        wasLanded = !wasLanded;
    }

}

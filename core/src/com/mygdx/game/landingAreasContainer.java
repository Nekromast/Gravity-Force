package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class landingAreasContainer {

    public Sprite landingArea;
    public boolean wasLanded;


    public landingAreasContainer() {
        landingArea = new Sprite();
        wasLanded = false;
    }
    public landingAreasContainer(Sprite landingArea){
        this.landingArea = landingArea;
        wasLanded = false;
    }

    public void setLandingArea(Sprite landingArea){
        this.landingArea = landingArea;
    }

    public Sprite getLandingArea(){
        return landingArea;
    }

    public void setWasLanded(boolean wasLanded){
        this.wasLanded = wasLanded;
    }

    public boolean getWasLanded(){
        return wasLanded;
    }

    public void dispose(){
        landingArea.getTexture().dispose();
    }

    public void switchWasLanded(){
        wasLanded = !wasLanded;
    }

}

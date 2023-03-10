package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class BaseStation {
    Texture landingArea;
    Sprite baseStation;

    public BaseStation(float x, float y, float width, float height){
        landingArea = new Texture("map/landing_area.png");
        baseStation = new Sprite(landingArea);
        baseStation.setPosition(x, y);
        baseStation.setSize(width, height);

    }
    public Sprite getBaseStation() {
        return baseStation;
    }
}

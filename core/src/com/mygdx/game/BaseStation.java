package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class BaseStation {
    Texture landingArea;
    Texture house;
    Sprite baseStation;
    Sprite houseSprite;

    public BaseStation(float x, float y, float width, float height){
        landingArea = new Texture("map/landing_area.png");
        house = new Texture("map/house.png");
        baseStation = new Sprite(landingArea);
        baseStation.setPosition(x, y-6);
        baseStation.setSize(width, height);
        houseSprite = new Sprite(house);
        houseSprite.setPosition(baseStation.getX() - 60 , baseStation.getY() - 4);
        houseSprite.setSize(62, 100);

    }
    public Sprite getBaseStation() {
        return baseStation;
    }
    public Sprite getHouseSprite() {
        return houseSprite;
    }

    public void dispose(){
        landingArea.dispose();
        house.dispose();
    }
}

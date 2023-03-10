package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class BaseStation {
    Texture landingArea;
    Texture house;
    Sprite baseStation;
    Sprite houseSprite;

    /**
     * @param x x-Koordinate für die Basisstation
     * @param y y-Koordinate für die Basisstation
     * @param width Breite der Basisstation
     * @param height Höhe der Basisstation
     */
    public BaseStation(float x, float y, float width, float height) {
        landingArea = new Texture("map/landing_area.png");
        house = new Texture("map/house.png");
        baseStation = new Sprite(landingArea);
        baseStation.setPosition(x, y - 6);
        baseStation.setSize(width, height);
        houseSprite = new Sprite(house);
        houseSprite.setPosition(baseStation.getX() - 60, baseStation.getY() - 4);
        houseSprite.setSize(62, 100);

    }

    public void dispose() {
        landingArea.dispose();
    }
    /**
     * @return gibt die Basisstation zurück
     */
    public Sprite getBaseStation() {
        return baseStation;
    }
    /**
     * @return gibt das Haus zurück
     */
    public Sprite getHouseSprite() {
        return houseSprite;
    }

    public void dispose(){
        landingArea.dispose();
        house.dispose();
    }
}

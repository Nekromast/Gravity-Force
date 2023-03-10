package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
//Hilfsklasse für die LandingAreas und die GoldCoins um boolean wasLanded zu speichern
public class landingAreasContainer {

    public Sprite landingArea;
    public Sprite goldCoin;
    public boolean wasLanded;
    Texture goldCoinTexture;

    /**
     * Konstruktor für die LandingAreas und die GoldCoins
     * @param landingArea LandingArea die auf der Map liegt
     * @param goldCoin GoldCoin die auf der LandingArea liegt
     */
    public landingAreasContainer(Sprite landingArea, Sprite goldCoin){
        this.landingArea = landingArea;
        this.goldCoin = goldCoin;
        wasLanded = false;
    }
    /**
     * Getter für die LandingArea
     * @return landingArea LandingArea die auf der Map liegt
     */
    public Sprite getLandingArea(){
        return landingArea;
    }
    /**
     * Getter für die GoldCoin
     * @return goldCoin GoldCoin die auf der LandingArea liegt
     */
    public Sprite getGoldCoin(){
        return goldCoin;
    }
    /**
     * Setter für wasLanded
     * @param wasLanded boolean Wert; gibt an ob die LandingArea schon betreten wurde
     */
    public void setWasLanded(boolean wasLanded){
        this.wasLanded = wasLanded;
    }
    /**
     * Getter für wasLanded
     * @return wasLanded boolean Wert; gibt an ob die LandingArea schon betreten wurde
     */
    public boolean wasLanded(){
        return wasLanded;
    }
    /**
     * Entfernt die LandingArea und die GoldCoin aus dem Speicher
     */
    public void dispose(){
        landingArea.getTexture().dispose();
    }
    /**
     * Wechselt den Wert von wasLanded
     */
    public void switchWasLanded(){
        wasLanded = !wasLanded;
    }

}

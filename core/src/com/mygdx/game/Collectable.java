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
    Sound coin_sound;
    float volume;
    boolean wasLanded;

    /**
     * @param x x-Koordinate für die ladingArea und goldCoin
     * @param y y-Koordinate für die ladingArea und goldCoin
     * @param width Breite der landingArea
     * @param height Höhe der landingArea
     */
    public Collectable(float x, float y, float width, float height) {
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
    /**
     * spielt den Sound Coin sound ab
     */
    public void play() {
        coin_sound.play(volume);

    }
    /**
     * @return gibt die Basisstation zurück
     */
    public Sprite getLandingArea() {
        return landingArea;
    }
    /**
     * @return gibt den GoldCoin zurück
     */
    public Sprite getGoldCoin() {
        return goldCoin;
    }
    /**
     * entfernt die Textur der Basisstation aus dem Speicher
     */
    public void dispose() {
        landingAreaTexture.dispose();
    }
    /**
     * @param wasLanded zeigt ob die landing Area schon betreten wurde
     * setzt den Wert von wasLanded
     */
    public void setWasLanded(boolean wasLanded) {
        this.wasLanded = wasLanded;
    }
    /**
     * @return gibt zurück ob die Basisstation schon betreten wurde
     */
    public boolean wasLanded() {
        return wasLanded;
    }

}
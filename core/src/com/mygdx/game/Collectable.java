package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class Collectable {
    Texture goldCoinTexture;
    Array<Sprite> collectables;
    int score;

    public Collectable(){
        //Collectable und Collectables liste Initialisieren
        collectables = new Array<>();
        goldCoinTexture = new Texture("goldCoin.png");
        score = 0;
    }

    public void addCollectable(float x, float y, float width, float height) {
        Sprite collectable = new Sprite(goldCoinTexture);
        collectable.setX(x);
        collectable.setY(y);
        collectable.setSize(width, height);
        collectables.add(collectable);
    }

    //Update Funktion prüft die "Spawnzeiten" und ob das Objekt eingesammelt wurde
    public void updateCollectable(Rectangle rocket){

        for (Iterator<Sprite> iter = collectables.iterator(); iter.hasNext(); ) {
            Rectangle collectable = iter.next().getBoundingRectangle();
            if(collectable.overlaps(rocket)) {
                score++;
                iter.remove();
            }
        }
    }

    public Array<Sprite> getCollectables(){
        return collectables;
    }
    public int getScore(){
        return score;
    }
    public void dispose(){
        goldCoinTexture.dispose();
    }

}
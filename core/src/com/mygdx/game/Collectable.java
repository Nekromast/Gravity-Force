package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Collectable {
    Texture collectableImage;
    public static Sprite collectable;
    static float COLLECTABLE_SCALE = 0.4f;



    public Collectable(){
        //Collectable und Collectables liste Initialisieren
        Rectangle collectable = new Rectangle();
        collectableImage = new Texture("Collectable.png");
        collectable = new Sprite(collectableImage);
        collectable.setScale(COLLECTABLE_SCALE);
        collectable.x = MathUtils.random(0, 800-64);
        collectable.y = MathUtils.random(0, 800-64);
        collectable.width = 64;
        collectable.height = 64;
        collectables.add(collectable);
    }

    //Update Funktion prüft die "Spawnzeiten" und ob das Objekt eingesammelt wurde
    public void update(){
        lastCollectTime = TimeUtils.nanoTime();
        if(TimeUtils.nanoTime() - lastCollectTime > 1000000000) spawnCollectable();

        for (Iterator<Rectangle> iter = collectables.iterator(); iter.hasNext(); ) {
                Rectangle collectable = iter.next();
                if(collectable.overlaps(rocket)) {
                  // collectableSound.play();
                   iter.remove();
                }
             }
          }
    }

    public Sprite getCollectable(){
        return collectable;
    }


    public void dispose(){
        collectableImage.dispose();
        collectable.dispose();
    }

}
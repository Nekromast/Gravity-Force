package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Rocket {
    Texture rocketImage;
    Texture rocketEngine;
    Rectangle hitbox;
    public static Sprite rocket;
    public static Sprite rocketEngineSprite;

    static float ROCKET_SCALE = 0.5f;
    static float ENGINE_SCALE = 0.6f;

    //Variablen für die Animation der Engine
    static Animation<TextureRegion> rocketAnimation;
    private static final int FRAME_COLS = 8, FRAME_ROWS = 1;
    private static final float FRAME_DURATION = 0.125f;
    float stateTime;

    //Health
    public static double health ;


    public Rocket(){
        //Rocket initialisieren
        rocketImage = new Texture("rocket/Nairan - Battlecruiser - Base.png");
        rocket = new Sprite(rocketImage);
        rocket.setScale(ROCKET_SCALE);
        rocket.setX(600);
        rocket.setY(4250);
        //rocket.setCenterX(rocket.getX() + rocket.getWidth());
        hitbox = rocket.getBoundingRectangle();
        //Rocket Engine initialisieren
        rocketEngine = new Texture("rocket/Nairan - Battlecruiser - Engine.png");
        TextureRegion[][] tmp = TextureRegion.split(rocketEngine, rocketEngine.getWidth() / FRAME_COLS, rocketEngine.getHeight() / FRAME_ROWS);
        TextureRegion[] rocketFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                rocketFrames[index++] = tmp[i][j];
            }
        }
        rocketAnimation = new Animation<>(FRAME_DURATION, rocketFrames);

        rocketEngineSprite = new Sprite(rocketFrames[0]);
        rocketEngineSprite.setScale(ENGINE_SCALE);
        stateTime = 0f;
    }

    //Rocket Engine an der Rakete ausrichten
    public void placeEngineAnimation(float delta){
        stateTime += delta;
        rocketEngineSprite.setRegion(rocketAnimation.getKeyFrame(stateTime, true));
        rocketEngineSprite.setPosition(rocket.getX(), rocket.getY());
    }

    public Sprite getRocket(){
        return rocket;
    }
    public Sprite getRocketEngine(){
        return rocketEngineSprite;
    }
    public Rectangle getHitbox(){
        hitbox.set(rocket.getBoundingRectangle());
        hitbox.setWidth(hitbox.getWidth() * 0.5f);
        hitbox.setHeight(hitbox.getHeight() * 0.8f);
        return hitbox;
    }
    public void setHitbox(float x, float y){
        hitbox = rocket.getBoundingRectangle();
        hitbox.setPosition(x, y);
    }

    public void setHealth(double newHealth){
        health = newHealth;
    }
    public double getHealth(){
        return health;
    }

    public void setRotation(float rotation){
        rocket.setRotation(rotation);
        rocketEngineSprite.setRotation(rotation);
    }
    public void translateX(float x){
        rocket.translateX(x);
        rocketEngineSprite.translateX(x);
    }
    public void translateY(float y){
        rocket.translateY(y);
        rocketEngineSprite.translateY(y);
    }

    public void dispose(){
        rocketImage.dispose();
        rocketEngine.dispose();
    }

}

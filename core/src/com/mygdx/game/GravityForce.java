package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class GravityForce implements Screen {

    Rocket rock;

    static Sprite rocket;
    Sprite rocketEngineSprite;
    SpriteBatch batch;
    SpriteBatch uiBatch;
    OrthographicCamera camera;
    OrthographicCamera uicamera;

    Texture rocketImage;
    Texture rocketEngine;
    Texture leftArrow;
    Texture rightArrow;
    Texture boostTexture;
    Texture background;


    //Die Rectangles für die Control Buttons
    Rectangle leftButton;
    Rectangle rightButton;
    Rectangle boostButton;

    //Variablen für die Bewegung der Rakete
    Integer CURRENT_VELOCITY = 0;
    Integer MAX_VELOCITY = 200;
    Integer GRAVITY = -50;
    Integer THRUST = 125;

    static boolean moving = false;

    //Kamera Variablen fürs Folgen der Rakete
    float lerp = 0.05f;
    Vector3 position;

    //Touchposition
    Vector3 touchPos;

    //Soundvariablen
    static Sound thrust_sound;
    long sound_id;
    float volume;
    boolean wasPlayed;
    float soundbuffer;

    public GravityForce(final Game game) {

        rock = new Rocket();

        batch = new SpriteBatch();
        uiBatch = new SpriteBatch();

        leftArrow = new Texture("leftArrow.png");
        rightArrow = new Texture("rightArrow.png");
        boostTexture = new Texture("boostTexture.png");
        background = new Texture("background.png");

        //Camera für die Ansicht
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        uicamera = new OrthographicCamera();
        uicamera.setToOrtho(false, 800, 480);

        //Rakete erstellen
        rocket = rock.getRocket();
        rocketEngineSprite = rock.getRocketEngine();

        //Die Rectangles für die Control Buttons
        leftButton = new Rectangle(0, 0, 80, 80);
        rightButton = new Rectangle(100, 0, 80, 80);
        boostButton = new Rectangle(700, 0, 100, 100);
        touchPos = new Vector3();

        //Soundeffekte
        thrust_sound = Gdx.audio.newSound(Gdx.files.internal("thrust.mp3"));
        volume = 0.5f;
        wasPlayed = false;
        soundbuffer = 0;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.GRAY);
        camera.update();
        //Kamera folgt der Rakete
        position = camera.position;
        position.x += (rocket.getX() - position.x) * lerp;
        position.y += (rocket.getY() - position.y) * lerp;
        camera.position.set(position.x, position.y, 0);

        //Animation der Engine
        rock.update(Gdx.graphics.getDeltaTime());

        // Steuerung der Rakete
        controlRocket();

        // Rakete im Screen behalten
        //keepRocketInScreen();

        playThrustSound();

        //Rakete und Background werden projiziert
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, -100, -100, 3840, 2160);
        rocket.draw(batch);
        if (moving) rocketEngineSprite.draw(batch);
        batch.end();

        //Das UI wird projiziert
        uiBatch.setProjectionMatrix(uicamera.combined);
        uiBatch.begin();
        uiBatch.draw(leftArrow, leftButton.x, leftButton.y, leftButton.width, leftButton.height);
        uiBatch.draw(rightArrow, rightButton.x, rightButton.y, rightButton.width, rightButton.height);
        uiBatch.draw(boostTexture, boostButton.x, boostButton.y, boostButton.width, boostButton.height);
        uiBatch.end();

        moving = false;
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    public void controlRocket() {
        // Schleife über alle Touchpoints (Finger 1 bis 5)
        for (int i = 0; i < 5; i++) {
            if (!Gdx.input.isTouched(i)) continue;
            // Koordinaten holen und mittels unproject korrekt umwandeln fürs Koordinatensystem
            uicamera.unproject(touchPos.set(Gdx.input.getX(i), Gdx.input.getY(i), 0));
            // Überprüfungen ob man auf die Buttons geklickt hat
            if (leftButton.contains(touchPos.x, touchPos.y)) {
                rocket.setRotation(rocket.getRotation() + THRUST * Gdx.graphics.getDeltaTime());
                rocketEngineSprite.setRotation(rocket.getRotation() + THRUST * Gdx.graphics.getDeltaTime());
            }
            if (rightButton.contains(touchPos.x, touchPos.y)) {
                rocket.setRotation(rocket.getRotation() - THRUST * Gdx.graphics.getDeltaTime());
                rocketEngineSprite.setRotation(rocket.getRotation() - THRUST * Gdx.graphics.getDeltaTime());
            }
            if (boostButton.contains(touchPos.x, touchPos.y)) {
                moving = true;
            }
        }
        velocity(moving);
    }

    public void velocity(boolean moving) {
        if (moving) {
            if (CURRENT_VELOCITY < MAX_VELOCITY) {
                CURRENT_VELOCITY += 10;
            }
        } else {
            if (CURRENT_VELOCITY > 0) {
                CURRENT_VELOCITY -= 5;
            }
        }
        // Bewegung der Rakete
        rocket.translateX((float) (Math.cos(Math.toRadians(rocket.getRotation() + 90)) * CURRENT_VELOCITY * Gdx.graphics.getDeltaTime()));
        rocket.translateY((float) (Math.sin(Math.toRadians(rocket.getRotation() + 90)) * CURRENT_VELOCITY * Gdx.graphics.getDeltaTime()));
        rocketEngineSprite.translateX((float) (Math.cos(Math.toRadians(rocket.getRotation() + 90)) * CURRENT_VELOCITY * Gdx.graphics.getDeltaTime()));
        rocketEngineSprite.translateY((float) (Math.sin(Math.toRadians(rocket.getRotation() + 90)) * CURRENT_VELOCITY * Gdx.graphics.getDeltaTime()));
        // Gravitation
        rocket.setY(rocket.getY() + GRAVITY * Gdx.graphics.getDeltaTime());


    }

    public void keepRocketInScreen() {
        // Linke Seite
        if (rocket.getX() < 0) {
            rocket.setX(0);
        }
        // Rechte Seite
        if (rocket.getX() > 800 - rocket.getWidth()) {
            rocket.setX(800 - rocket.getWidth());
        }
        // Boden
        if (rocket.getY() < -32) {
            rocket.setY(-32);
        }
        // Decke
        if (rocket.getY() > 480 - rocket.getHeight()) {
            rocket.setY(480 - rocket.getHeight());
        }
    }

    public void playThrustSound() {
        //Buffer weil der Sound noch geladen werden muss
        soundbuffer += Gdx.graphics.getDeltaTime();

        // Abspielen des Tons bei der ersten Bewegung & nach Bufferzeit
        if (!wasPlayed && soundbuffer > 2) {
            sound_id = thrust_sound.play(volume);
            thrust_sound.setLooping(sound_id, true);
            wasPlayed = true;
            System.out.println("Played :" + soundbuffer);
        }

        //Bei Bewegung wird der Sound lauter
        if (moving && volume + 0.75f * Gdx.graphics.getDeltaTime() < 1) {
            thrust_sound.setVolume(sound_id, volume += 0.75f * Gdx.graphics.getDeltaTime());
        }

        //Bei Stillstand wird der Sound schnell leiser
        if (volume - 2 * Gdx.graphics.getDeltaTime() > 0 && !moving)
            thrust_sound.setVolume(sound_id, volume -= 2 * Gdx.graphics.getDeltaTime());
        System.out.println(volume);
    }

    @Override
    public void dispose() {
        batch.dispose();
        uiBatch.dispose();
        rocketImage.dispose();
        rocketEngine.dispose();
        leftArrow.dispose();
        rightArrow.dispose();
        boostTexture.dispose();
        background.dispose();
        thrust_sound.dispose();

    }
}

package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class GravityForce implements Screen {

    SpriteBatch batch;
    SpriteBatch UiBatch;
    OrthographicCamera camera;
    OrthographicCamera uicamera;

    Texture rocketImage;
    Texture leftArrow;
    Texture rightArrow;
    Texture boostTexture;
    Texture background;

    //Die Rectangles für die Control Buttons
    Rectangle leftButton;
    Rectangle rightButton;
    Rectangle boostButton;

    static Sprite rocket;
    Vector3 touchPos;

    Integer MAX_VELOCITY = 200;
    Integer gravity = -50;
    Integer currentVelocity = 0;
    static boolean moving = false;


    //Kamera Variablen fürs Folgen der Rakete
    float lerp = 0.05f;
    Vector3 position;

    public GravityForce(final Game game) {

        batch = new SpriteBatch();
        UiBatch = new SpriteBatch();

        rocketImage = new Texture("rocket.png");
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
        rocket = new Sprite(rocketImage);
        rocket.setScale(0.5f);
        rocket.setX(400 - 32);
        rocket.setY(240 - 32);

        leftButton = new Rectangle(0, 0, 80, 80);
        rightButton = new Rectangle(100, 0, 80, 80);
        boostButton = new Rectangle(700, 0, 100, 100);
        touchPos = new Vector3();
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.GRAY);

        //Kamera folgt der Rakete
        position = camera.position;
        position.x += (rocket.getX() - position.x) * lerp;
        position.y += (rocket.getY() - position.y) * lerp;
        camera.position.set(position.x, position.y, 0);

        camera.update();

        //Rakete und Background werden projiziert
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, -100, -100, 3840, 2160);
        rocket.draw(batch);
        batch.end();

        //Das UI wird projiziert
        UiBatch.setProjectionMatrix(uicamera.combined);
        UiBatch.begin();
        UiBatch.draw(leftArrow, leftButton.x, leftButton.y, leftButton.width, leftButton.height);
        UiBatch.draw(rightArrow, rightButton.x, rightButton.y, rightButton.width, rightButton.height);
        UiBatch.draw(boostTexture, boostButton.x, boostButton.y, boostButton.width, boostButton.height);
        UiBatch.end();

        // Steuerung der Rakete
        controlRocket();

        // Rakete im Screen behalten
        //keepRocketInScreen();

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
                rocket.setRotation(rocket.getRotation() + 200 * Gdx.graphics.getDeltaTime());
            }
            if (rightButton.contains(touchPos.x, touchPos.y)) {
                rocket.setRotation(rocket.getRotation() - 200 * Gdx.graphics.getDeltaTime());
            }
            if (boostButton.contains(touchPos.x, touchPos.y)) {
                moving = true;
            }
        }

        velocity(moving);
        moving = false;
    }

    public void velocity(boolean moving) {
        if (moving) {
            if (currentVelocity < MAX_VELOCITY) {
                currentVelocity += 10;
            }
        } else {
            if (currentVelocity > 0) {
                currentVelocity -= 5;
            }
        }
        rocket.translateX((float) (Math.cos(Math.toRadians(rocket.getRotation() + 90)) * currentVelocity * Gdx.graphics.getDeltaTime()));
        rocket.translateY((float) (Math.sin(Math.toRadians(rocket.getRotation() + 90)) * currentVelocity * Gdx.graphics.getDeltaTime()));
        rocket.setY(rocket.getY() + gravity * Gdx.graphics.getDeltaTime());

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

    @Override
    public void dispose() {
        batch.dispose();
        rocketImage.dispose();
    }
}

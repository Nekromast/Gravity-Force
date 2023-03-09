package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.map.maploader;


public class GravityForce implements Screen {

    //Displayvariablen
    static final int DISPLAY_WIDTH = 800;
    static final int DISPLAY_HEIGHT = 480;

    Rocket rock;
    Collectable collectables;

    static Sprite rocket;
    Rectangle rocketrect;
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
    Texture goldCoinTexture;


    //Die Rectangles für die Control Buttons
    Rectangle leftButton;
    Rectangle rightButton;
    Rectangle boostButton;

    //Variablen für die Bewegung der Rakete
    Integer CURRENT_VELOCITY = 0;
    Integer MAX_VELOCITY = 200;
    Integer GRAVITY = -50;
    Integer THRUST = 125;

    double rocketVelocityX = 0;
    double rocketVelocityY = 0;

    static boolean isMoving = false;

    //Kamera Variablen fürs Folgen der Rakete
    float lerp = 0.05f;
    Vector3 position;

    //Touchposition
    Vector3 touchPos;

    //Soundvariablen
    static Sound thrust_sound;
    static Music background_music;
    long sound_id;
    float volume;
    boolean wasPlayed;
    float soundbuffer;

    //Map
    maploader gmap;
    TiledMap map;
    int MAPSCALE = 4;
    OrthogonalTiledMapRenderer tMapRend;
    boolean incomingCollision;

    //Collision
    float rocketOldX;
    float rocketOldY;
    Rectangle rocketNewRect;

    //Health
    Label healthLabel;

    //Score
    int score;
    Label scoreLabel;

    // Game Over
    boolean isGameOver;
    public GameOverListener gameOverListener;


    public GravityForce(final Game game) {
        isGameOver = false;

        rock = new Rocket();
        collectables = new Collectable();

        batch = new SpriteBatch();
        uiBatch = new SpriteBatch();

        leftArrow = new Texture("leftArrow.png");
        rightArrow = new Texture("rightArrow.png");
        boostTexture = new Texture("boostTexture.png");
        background = new Texture("background.png");
        goldCoinTexture = new Texture("goldCoin.png");

        //Camera für die Ansicht
        camera = new OrthographicCamera();
        camera.setToOrtho(false, DISPLAY_WIDTH, DISPLAY_HEIGHT);

        uicamera = new OrthographicCamera();
        uicamera.setToOrtho(false, DISPLAY_WIDTH, DISPLAY_HEIGHT);

        //Rakete erstellen
        rocket = rock.getRocket();
        rocketEngineSprite = rock.getRocketEngine();

        //Die Rectangles für die Control Buttons
        leftButton = new Rectangle(0, 0, 80, 80);
        rightButton = new Rectangle(100, 0, 80, 80);
        boostButton = new Rectangle(DISPLAY_WIDTH-100, 0, 100, 100);
        touchPos = new Vector3();

        //Health
        healthLabel = new Label("Health: " + rock.getHealth(), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        healthLabel.setBounds(DISPLAY_WIDTH / 2 - 100, 0, 100, 50);

        //Score
        scoreLabel = new Label("Score: " + score, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel.setBounds(DISPLAY_WIDTH / 2 + 100, 0, 100, 50);

        //Soundeffekte
        thrust_sound = Gdx.audio.newSound(Gdx.files.internal("thrust.mp3"));
        volume = 0.5f;
        wasPlayed = false;
        soundbuffer = 0;

        //Background Music
        background_music = Gdx.audio.newMusic(Gdx.files.internal("background_music.mp3"));

        //Karte
        gmap = new maploader();

        //Karte 2
        TmxMapLoader loader = new TmxMapLoader();
        map = loader.load("testmapc.tmx");

        for (MapObject object : map.getLayers().get("objects").getObjects()) {
            float x = object.getProperties().get("x", Float.class) * MAPSCALE;
            float y = object.getProperties().get("y", Float.class) * MAPSCALE;
            float width = object.getProperties().get("width", Float.class) * MAPSCALE;
            float height = object.getProperties().get("height", Float.class) * MAPSCALE;
            collectables.addCollectable(x, y, width, height);
        }

    }

    @Override
    public void show() {
        background_music.setLooping(true);
        background_music.setVolume(0.5f);
        background_music.play();
    }

    @Override
    public void render(float delta) {
        if (isGameOver) return;
        //map = gmap.getMap();
        tMapRend = new OrthogonalTiledMapRenderer(map, MAPSCALE);
        //ScreenUtils.clear(Color.GRAY);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();


        //Kamera folgt der Rakete
        position = camera.position;
        position.x += (rocket.getX() - position.x) * lerp;
        position.y += (rocket.getY() - position.y) * lerp;
        camera.position.set(position.x, position.y, 0);

        //Rechteck der Rakete für Kollisionen
        rocketrect = rocket.getBoundingRectangle();

        //Animation der Engine
        rock.update(Gdx.graphics.getDeltaTime());


        // Steuerung und Kollision der Rakete
        controlRocket();
        velocity(isMoving);
        //velocitySplit(isMoving);
        mapcollision();
        gravity();

        //map wird gerendert
        tMapRend.setView(camera);
        tMapRend.render();

        // Rakete im Screen behalten
        //keepRocketInScreen();

        playThrustSound();


        //Rakete und Background werden projiziert
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        //(background, -100, -100, 3840, 2160);
        rocket.draw(batch);
        if (isMoving) rocketEngineSprite.draw(batch);


        for (Sprite landing_area : collectables.getLandingAreas()) {
            landing_area.draw(batch);
        }
        for(Sprite goldCoin : collectables.getCollectables()) {
            goldCoin.draw(batch);
        }
        collectables.updateCollectable(rocket);

        batch.end();

        //Das UI wird projiziert
        uiBatch.setProjectionMatrix(uicamera.combined);
        uiBatch.begin();
        uiBatch.draw(leftArrow, leftButton.x, leftButton.y, leftButton.width, leftButton.height);
        uiBatch.draw(rightArrow, rightButton.x, rightButton.y, rightButton.width, rightButton.height);
        uiBatch.draw(boostTexture, boostButton.x, boostButton.y, boostButton.width, boostButton.height);
        healthLabel.setText("Health: " + (int) rock.getHealth());
        healthLabel.draw(uiBatch, 1);
        scoreLabel.setText("Score: " + score);
        scoreLabel.draw(uiBatch, 1);
        uiBatch.end();

        //Kollision und Bewegung, die ggf in deren Funktionen auf true gestellt werden
        incomingCollision = false;
        isMoving = false;

        score = collectables.getScore();
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
                isMoving = true;
            }
        }
    }

    public void velocity(boolean isMoving) {
        if (isMoving) {
            if (CURRENT_VELOCITY < MAX_VELOCITY) {
                CURRENT_VELOCITY += 10;
            }
        } else {
            if (CURRENT_VELOCITY > 0) {
                CURRENT_VELOCITY -= 5;
            }
            if (CURRENT_VELOCITY < 0) {
                CURRENT_VELOCITY = 10;
            }
        }

        // Bewegung der Rakete
        rocket.translateX((float) (Math.cos(Math.toRadians(rocket.getRotation() + 90)) * CURRENT_VELOCITY * Gdx.graphics.getDeltaTime()));
        rocket.translateY((float) (Math.sin(Math.toRadians(rocket.getRotation() + 90)) * CURRENT_VELOCITY * Gdx.graphics.getDeltaTime()));
        rocketEngineSprite.translateX((float) (Math.cos(Math.toRadians(rocket.getRotation() + 90)) * CURRENT_VELOCITY * Gdx.graphics.getDeltaTime()));
        rocketEngineSprite.translateY((float) (Math.sin(Math.toRadians(rocket.getRotation() + 90)) * CURRENT_VELOCITY * Gdx.graphics.getDeltaTime()));
    }

    public void velocitySplit(boolean isMoving) {
        if (isMoving && !incomingCollision) {
            if (rocketVelocityX < MAX_VELOCITY) {
                //rocketVelocityX += 10 * Math.cos(Math.toRadians(rocket.getRotation() + 90)) * Gdx.graphics.getDeltaTime();
                rocketVelocityX += Math.cos(Math.toRadians(rocket.getRotation() - 90)) * Gdx.graphics.getDeltaTime();
            }
            if (rocketVelocityY < MAX_VELOCITY) {
                //rocketVelocityY += 10 * Math.sin(Math.toRadians(rocket.getRotation() + 90)) * Gdx.graphics.getDeltaTime();
                rocketVelocityY += Math.sin(Math.toRadians(rocket.getRotation() - 90)) * 2 * Gdx.graphics.getDeltaTime();
            }
        } else {
            if (rocketVelocityX > 0) {
                rocketVelocityX -= 5;
            }
            if (rocketVelocityY > 0) {
                rocketVelocityY -= 5;
            }
            System.out.println("X: " + rocketVelocityX + " Y: " + rocketVelocityY);
            //rocket.translateX((float) (rocketVelocityX));
            rocket.translateY((float) (rocketVelocityY));
            rocketEngineSprite.translateX((float) rocketVelocityX);
            rocketEngineSprite.translateY((float) rocketVelocityY);
        }
    }


    public void gravity() {
        if (!incomingCollision) rocket.setY(rocket.getY() + GRAVITY * Gdx.graphics.getDeltaTime());
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
            setGameOver(true);
        }
    }

    public void playThrustSound() {
        //Buffer weil der Sound noch geladen werden muss
        soundbuffer += Gdx.graphics.getDeltaTime();

        // Abspielen des Tons bei der ersten Bewegung & nach Bufferzeit
        if (!wasPlayed && soundbuffer > 3) {
            sound_id = thrust_sound.play(volume);
            thrust_sound.setLooping(sound_id, true);
            wasPlayed = true;

        }

        //Bei Bewegung wird der Sound lauter
        if (isMoving && volume + 0.75f * Gdx.graphics.getDeltaTime() < 1) {
            thrust_sound.setVolume(sound_id, volume += 0.75f * Gdx.graphics.getDeltaTime());
        }

        //Bei Stillstand wird der Sound schnell leiser
        if (volume - 2 * Gdx.graphics.getDeltaTime() > 0 && !isMoving)
            thrust_sound.setVolume(sound_id, volume -= 2 * Gdx.graphics.getDeltaTime());
    }

    public void mapcollision() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        for (int x = 0; x < layer.getWidth(); x++) {
            for (int y = 0; y < layer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                if (cell == null) continue;
                TiledMapTile tile = cell.getTile();
                if (tile == null) continue;
                Rectangle tileBounds = new Rectangle(
                        x * tile.getTextureRegion().getRegionWidth() * MAPSCALE,
                        y * tile.getTextureRegion().getRegionHeight() * MAPSCALE,
                        tile.getTextureRegion().getRegionWidth() * MAPSCALE,
                        tile.getTextureRegion().getRegionHeight() * MAPSCALE);

                if (rocketrect.overlaps(tileBounds)) {
                    //hier was bei Kollision passieren soll
                    damage();
                    //incomingCollision = true;
                    return;
                }
            }
        }
    }

    public void damage() {
        rock.setHealth((rock.getHealth() - 30 * Gdx.graphics.getDeltaTime()));
        System.out.println(rock.getHealth());
        if (rock.getHealth() <= 0) {
            rock.setHealth(0);
            setGameOver(true);
        }
    }


    public void setListener(GameOverListener listener) {
        this.gameOverListener = listener;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
        if (gameOverListener != null) {
            gameOverListener.onGameOverChanged(gameOver);
        }
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
        background_music.dispose();
        thrust_sound.dispose();
        gmap.dispose();
        tMapRend.dispose();
        rock.dispose();

    }
}

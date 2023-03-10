package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.map.maploader;


public class GravityForce implements Screen {

    //Displayvariablen
    static final int DISPLAY_WIDTH = 800;
    static final int DISPLAY_HEIGHT = 480;
    static Sprite rocket;
    static boolean isMoving = false;
    //Soundvariablen
    static Music thrust_sound;
    static Music background_music;
    static Music gameOverSound;
    public GameOverListener gameOverListener;
    Rocket rock;
    Array<Collectable> collectables;
    BaseStation baseStation;
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
    //Die Rectangles für die Control Buttons
    Rectangle leftButton;
    Rectangle rightButton;
    Rectangle boostButton;
    //Variablen für die Bewegung der Rakete
    Integer CURRENT_VELOCITY = 0;
    Integer MAX_VELOCITY = 200;
    Integer GRAVITY = -50;
    Integer THRUST = 125;
    //Kamera Variablen fürs Folgen der Rakete
    float lerp = 0.05f;
    Vector3 position;
    //Touchposition
    Vector3 touchPos;
    float THRUSTSOUND_MAXVOLUME = 0.3f;
    boolean wasPlayed;
    //Map
    maploader gmap;
    TiledMap map;
    int MAPSCALE = 4;
    OrthogonalTiledMapRenderer tMapRend;
    //Landing
    boolean hasLandedOnLandingArea;
    boolean landingRotationFinished;
    //Score
    int coinCount;
    int score;
    Label coinCountLabel;
    Label scoreLabel;
    // Game Over
    boolean isGameOver;
    //Debug
    boolean debug = false;


    public GravityForce(final Game game) {
        //Batches erstellen
        batch = new SpriteBatch();
        uiBatch = new SpriteBatch();

        //Rakete erstellen
        rock = new Rocket();
        rocket = rock.getSprite();
        rocketEngineSprite = rock.getRocketEngine();

        //Collectables erstellen
        collectables = new Array<>();

        //Texturvariablen
        leftArrow = new Texture("ui/leftArrow.png");
        rightArrow = new Texture("ui/rightArrow.png");
        boostTexture = new Texture("ui/boostTexture.png");
        background = new Texture("map/background.png");

        //Camera für die Ansicht
        camera = new OrthographicCamera();
        camera.setToOrtho(false, DISPLAY_WIDTH, DISPLAY_HEIGHT);
        camera.position.set(rocket.getX(), rocket.getY(), 0);

        uicamera = new OrthographicCamera();
        uicamera.setToOrtho(false, DISPLAY_WIDTH, DISPLAY_HEIGHT);


        //Die Rectangles für die Control Buttons
        leftButton = new Rectangle(0, 0, 80, 80);
        rightButton = new Rectangle(100, 0, 80, 80);
        boostButton = new Rectangle(DISPLAY_WIDTH - 100, 0, 100, 100);
        touchPos = new Vector3();

        //Score
        coinCountLabel = new Label("Coins to submit: " + coinCount, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        coinCountLabel.setBounds(DISPLAY_WIDTH / 2 - 100, 0, 100, 50);
        scoreLabel = new Label("Score: " + score, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel.setBounds(DISPLAY_WIDTH / 2 + 100, 0, 100, 50);

        //Soundeffekte
        thrust_sound = Gdx.audio.newMusic(Gdx.files.internal("sounds/thrust.mp3"));
        gameOverSound = Gdx.audio.newMusic(Gdx.files.internal("sounds/gameOver.mp3"));
        wasPlayed = false;

        //Background Music
        background_music = Gdx.audio.newMusic(Gdx.files.internal("sounds/background_music.mp3"));

        //Karte (redundant)
        gmap = new maploader();

        //Karte 2
        TmxMapLoader loader = new TmxMapLoader();
        map = loader.load("map/testmapc.tmx");

        //Collectables
        collectables = new Array<>();
        for (MapObject object : map.getLayers().get("objects").getObjects()) {
            float x = object.getProperties().get("x", Float.class) * MAPSCALE;
            float y = object.getProperties().get("y", Float.class) * MAPSCALE;
            float width = object.getProperties().get("width", Float.class) * MAPSCALE;
            float height = object.getProperties().get("height", Float.class) * MAPSCALE;
            collectables.add(new Collectable(x, y, width, height));
        }


        //BaseStation
        MapObject baseStationObject = map.getLayers().get("station").getObjects().get(0);
        baseStation = new BaseStation(
                baseStationObject.getProperties().get("x", Float.class) * MAPSCALE,
                baseStationObject.getProperties().get("y", Float.class) * MAPSCALE,
                baseStationObject.getProperties().get("width", Float.class) * MAPSCALE,
                baseStationObject.getProperties().get("height", Float.class) * MAPSCALE);

        //Variablen laden
        isGameOver = false;
        landingRotationFinished = true;
    }

    @Override
    public void show() {
        background_music.setLooping(true);
        background_music.setVolume(0.1f);
        background_music.play();

        tMapRend = new OrthogonalTiledMapRenderer(map, MAPSCALE);
    }

    @Override
    public void render(float delta) {
        if (isGameOver) return;

        //Kamera und Map Setup beginnt


        ScreenUtils.clear(Color.DARK_GRAY);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        tMapRend.setView(camera);

        //Kamera folgt der Rakete
        position = camera.position;
        position.x += (rocket.getX() - position.x) * lerp;
        position.y += (rocket.getY() - position.y) * lerp;
        camera.position.set(position.x, position.y, 0);

        //Raketensetup beginnt

        //Rechteck der Rakete für Kollisionen
        rocketrect = rocket.getBoundingRectangle();

        //Animation der Engine
        rock.placeEngineAnimation(Gdx.graphics.getDeltaTime());


        // Steuerung und Kollision der Rakete
        controlRocket();
        checkLanding(); //Needs to be called before velocity
        checkBaseLanding();
        velocity();
        mapcollision();
        gravity();


        // Rakete im Screen behalten
        //keepRocketInScreen();

        playThrustSound();

        //Drawing beginnt

        //Rakete, Collectables und Background werden projiziert
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        //Draw background over the map
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                batch.draw(background, i * 800, j * 600);
            }
        }
        rocket.draw(batch);
        if (isMoving && landingRotationFinished) rocketEngineSprite.draw(batch);
        for (Collectable collectable : collectables) {
            Sprite landingAreaSprite = collectable.getLandingArea();
            landingAreaSprite.draw(batch);
            if (collectable.wasLanded()) landingAreaSprite.setColor(Color.GREEN);
            else collectable.getGoldCoin().draw(batch);
        }

        baseStation.getHouseSprite().draw(batch);
        baseStation.getBaseStation().draw(batch);
        tMapRend.render();
        batch.end();

        //Das UI wird projiziert
        uiBatch.setProjectionMatrix(uicamera.combined);
        uiBatch.begin();
        uiBatch.draw(leftArrow, leftButton.x, leftButton.y, leftButton.width, leftButton.height);
        uiBatch.draw(rightArrow, rightButton.x, rightButton.y, rightButton.width, rightButton.height);
        uiBatch.draw(boostTexture, boostButton.x, boostButton.y, boostButton.width, boostButton.height);
        coinCountLabel.setText("Coins to submit: " + coinCount);
        coinCountLabel.draw(uiBatch, 1);
        scoreLabel.setText("Score: " + score);
        scoreLabel.draw(uiBatch, 1);
        uiBatch.end();

        //Debugging
        if (debug) {
            debug();
        }

        //Kollision und Bewegung, die ggf in deren Funktionen auf true gestellt werden
        hasLandedOnLandingArea = false;
        isMoving = false;

    }

    public void controlRocket() {
        // Schleife über alle Touchpoints (Finger 1 bis 5)
        for (int i = 0; i < 5; i++) {
            if (!Gdx.input.isTouched(i)) continue;
            // Koordinaten holen und mittels unproject korrekt umwandeln fürs Koordinatensystem
            uicamera.unproject(touchPos.set(Gdx.input.getX(i), Gdx.input.getY(i), 0));
            // Überprüfungen ob man auf die Buttons geklickt hat
            if (leftButton.contains(touchPos.x, touchPos.y)) {
                rock.setRotation(rocket.getRotation() + THRUST * Gdx.graphics.getDeltaTime());
            }
            if (rightButton.contains(touchPos.x, touchPos.y)) {
                rock.setRotation(rocket.getRotation() - THRUST * Gdx.graphics.getDeltaTime());
            }
            if (boostButton.contains(touchPos.x, touchPos.y)) {
                isMoving = true;
            }
        }
    }

    public void velocity() {
        if (isMoving) {
            if (CURRENT_VELOCITY < MAX_VELOCITY) {
                CURRENT_VELOCITY += 10;
            }
        } else {
            if (CURRENT_VELOCITY > 0) {
                CURRENT_VELOCITY -= 5;
            }
            if (CURRENT_VELOCITY < 0) {
                CURRENT_VELOCITY = 0;
            }
        }
        // Falls die Rakete gelandet ist, soll sie nicht mehr beschleunigen können
        // bis sie richtig gedreht ist
        if ((hasLandedOnLandingArea && !landingRotationFinished)) {
            CURRENT_VELOCITY = 0;
        }

        // Bewegung der Rakete
        rock.translateX((float) (Math.cos(Math.toRadians(rocket.getRotation() + 90)) * CURRENT_VELOCITY * Gdx.graphics.getDeltaTime()));
        rock.translateY((float) (Math.sin(Math.toRadians(rocket.getRotation() + 90)) * CURRENT_VELOCITY * Gdx.graphics.getDeltaTime()));
    }


    public void gravity() {
        if (!hasLandedOnLandingArea)
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
            setGameOver(true);
        }
    }

    public void playThrustSound() {

        // Abspielen des Tons bei der ersten Bewegung & nach Bufferzeit
        if (!wasPlayed) {
            thrust_sound.play();
            thrust_sound.setLooping(true);
            wasPlayed = true;
        }
        //Bei Bewegung wird der Sound lauter
        if (isMoving && (thrust_sound.getVolume() < THRUSTSOUND_MAXVOLUME)) {
            thrust_sound.setVolume(thrust_sound.getVolume() + 1 * Gdx.graphics.getDeltaTime());
        }

        //Bei Stillstand wird der Sound schnell leiser
        if (thrust_sound.getVolume() > 0 && !isMoving)
            thrust_sound.setVolume(thrust_sound.getVolume() - 2 * Gdx.graphics.getDeltaTime());

        if (thrust_sound.getVolume() < 0) thrust_sound.setVolume(0);
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

                //Kollision mit Tilenur überprüfen, wenn es nicht auf einer Landefläche hält
                if (rock.getHitbox().overlaps(tileBounds) && !hasLandedOnLandingArea) {
                    //hier was bei Kollision passieren soll
                    //damage();
                    gameOverSound.play();
                    setGameOver(true);
                    return;
                }
            }
        }
    }

    public void checkLanding() {
        for (Collectable collectable : collectables) {
            Rectangle landing_area = collectable.getLandingArea().getBoundingRectangle();
            if (landing_area.overlaps(rock.getHitbox())) {
                hasLandedOnLandingArea = true;
                landingRotationFinished = false;
                //Rotation zwischen 0 und 360 Grad halten
                if (Math.toDegrees(Math.toRadians(rocket.getRotation())) > 360) {
                    rock.setRotation(rocket.getRotation() - 360);
                }
                //Rakete richtig drehen
                if (rocket.getRotation() > 0 && rocket.getRotation() < 180) {
                    rock.setRotation(rocket.getRotation() - 1f);
                } else {
                    rock.setRotation(rocket.getRotation() + 1f);
                }
                //Überprüfen ob die Rakete richtig gedreht ist
                if (rocket.getRotation() < 1 && rocket.getRotation() > -1) {
                    landingRotationFinished = true;
                }
                //Coin aufheben und Sound abspielen
                if (!collectable.wasLanded()) {
                    collectable.setWasLanded(true);
                    coinCount += 1;
                    collectable.play();
                }
            }
        }
    }

    public void checkBaseLanding() {
        Sprite base = baseStation.getBaseStation();
        Rectangle landing_area = base.getBoundingRectangle();
        if (landing_area.overlaps(rock.getHitbox())) {
            hasLandedOnLandingArea = true;
            landingRotationFinished = false;
            //Rotation zwischen 0 und 360 Grad halten
            if (Math.toDegrees(Math.toRadians(rocket.getRotation())) > 360) {
                rock.setRotation(rocket.getRotation() - 360);
            }
            //Rakete richtig drehen
            if (rocket.getRotation() > 0 && rocket.getRotation() < 180) {
                rock.setRotation(rocket.getRotation() - 1f);
            } else {
                rock.setRotation(rocket.getRotation() + 1f);
            }
            //Überprüfen ob die Rakete richtig gedreht ist
            if (rocket.getRotation() < 1 && rocket.getRotation() > -1) {
                landingRotationFinished = true;
            }
            score = score + coinCount;
            coinCount = 0;
        }
    }

    public void setListener(GameOverListener listener) {
        this.gameOverListener = listener;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
        pause();
        if (gameOverListener != null) {
            gameOverListener.onGameOverChanged(gameOver);
        }
    }

    public void debug() {
        //HitBox Shape
        ShapeRenderer shapeRenderer1 = new ShapeRenderer();
        shapeRenderer1.setProjectionMatrix(camera.combined);
        shapeRenderer1.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer1.setColor(Color.GREEN);
        shapeRenderer1.rect(rock.getHitbox().getX(), rock.getHitbox().getY(), rock.getHitbox().getWidth(), rock.getHitbox().getHeight());
        shapeRenderer1.end();
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
        //background.dispose();
        background_music.dispose();
        thrust_sound.dispose();
        gmap.dispose();
        tMapRend.dispose();
        rock.dispose();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
        background_music.pause();
        thrust_sound.pause();
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }
}

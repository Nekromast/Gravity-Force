package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class GravityForce extends ApplicationAdapter {
	SpriteBatch batch;
	OrthographicCamera camera;

	Texture rocketImage;
	Texture leftArrow;
	Texture rightArrow;
	Texture boostTexture;

	//Die Rectangles für die Control Buttons
	Rectangle leftButton;
	Rectangle rightButton;
	Rectangle boostButton;

	Sprite rocket;
	Vector3 touchPos;


	@Override
	public void create () {
		batch = new SpriteBatch();

		rocketImage = new Texture("rocket.png");
		leftArrow = new Texture("leftArrow.png");
		rightArrow = new Texture("rightArrow.png");
		boostTexture = new Texture("boostTexture.png");

		//Camera für die Ansicht
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		//Rakete erstellen
		rocket = new Sprite(rocketImage);
		rocket.setScale(0.5f);
		rocket.setX(400-32);
		rocket.setY(240-32);

		leftButton = new Rectangle(0, 0, 80, 80);
		rightButton = new Rectangle(100, 0, 80, 80);
		boostButton = new Rectangle(700, 0, 100, 100);
		touchPos = new Vector3();
	}

	@Override
	public void render () {
		ScreenUtils.clear(Color.GRAY);
		camera.update();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		rocket.draw(batch);
		batch.draw(leftArrow, leftButton.x, leftButton.y, leftButton.width, leftButton.height);
		batch.draw(rightArrow, rightButton.x, rightButton.y, rightButton.width, rightButton.height);
		batch.draw(boostTexture, boostButton.x, boostButton.y, boostButton.width, boostButton.height);
		batch.end();

		// Steuerung der Rakete
		controlRocket();

		// Rakete im Screen behalten
		if(rocket.getX() < 0) {
			rocket.setX(0);
		}
		if(rocket.getX() > 800 - rocket.getWidth()) {
			rocket.setX(800 - rocket.getWidth());
		}
		if(rocket.getY() < 0 - rocket.getHeight()/2) {
			rocket.setY(-32);
		}
		if(rocket.getY() > 480-rocket.getHeight()) {
			rocket.setY(480-rocket.getHeight());
		}
	}
	public void controlRocket() {
		// Schleife über alle Touchpoints (Finger 1 bis 5)
		for(int i=0; i<5;i++){
			if(!Gdx.input.isTouched(i)) continue;
			// Koordinaten holen und mittels unproject korrekt umwandeln fürs Koordinatensystem
			camera.unproject(touchPos.set(Gdx.input.getX(i), Gdx.input.getY(i), 0));

			// Überprüfungen ob man auf die Buttons geklickt hat
			if(leftButton.contains(touchPos.x, touchPos.y)) {
				rocket.setRotation(rocket.getRotation() + 200 * Gdx.graphics.getDeltaTime());
				System.out.println(rocket.getRotation());
			}
			if(rightButton.contains(touchPos.x, touchPos.y)) {
				rocket.setRotation(rocket.getRotation() - 200 * Gdx.graphics.getDeltaTime());
			}
			if(boostButton.contains(touchPos.x, touchPos.y)) {
				rocket.translateX((float) (Math.cos(Math.toRadians(rocket.getRotation()+90)) * 200 * Gdx.graphics.getDeltaTime()));
				rocket.translateY((float) (Math.sin(Math.toRadians(rocket.getRotation()+90)) * 200 * Gdx.graphics.getDeltaTime()));
			}
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		rocketImage.dispose();
	}
}

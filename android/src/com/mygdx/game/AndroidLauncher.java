package com.mygdx.game;

import static com.mygdx.game.R.*;

import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.GravityForce;

public class AndroidLauncher extends AndroidApplication {
	View gameView;
	Game game;
	boolean isGameOver = false;
	AndroidApplicationConfiguration config;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;
		// Set Mainmenu to fullscreen
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		if (Build.VERSION.SDK_INT < 16) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			View decorView = getWindow().getDecorView();
			int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
			decorView.setSystemUiVisibility(uiOptions);
		}
		//TODO: new Game() correctly
		game = new Game();
		gameView = initializeForView(game, config);

		//MainMenu per XML View
		setContentView(layout.mainmenu);

		Button startButton = findViewById(id.button_start);
		startButton.setOnClickListener(v -> {
			setContentView(gameView);
		});
		Button exitButton = findViewById(id.button_exit);
		exitButton.setOnClickListener(v -> {
			finish();
		});
		gameView.setOnTouchListener((v, event) -> {
			v.performClick();
			if (game.gravityForce.wasPlayed) {
				setContentView(layout.gameover);
				gameOver();
				return true;
			}
			return false;
		});
	}
	public void gameOver(){
		Button retryButton = findViewById(id.button_retry);
		retryButton.setOnClickListener(v -> {
			game.dispose();
			game = new Game();
			gameView = initializeForView(game, config);
			setContentView(gameView);
		});
		Button exitButton2 = findViewById(id.button_exit_gameover);
		exitButton2.setOnClickListener(v -> {
			finish();
		});
	}
}

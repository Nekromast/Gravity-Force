package com.mygdx.game;

import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.GravityForce;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
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

		//initialize(new Game(), config);
		initializeForView(new Game(), config);

		//MainMenu per XML View
		setContentView(R.layout.mainmenu);
		Button startButton = findViewById(R.id.button_start);
		startButton.setOnClickListener(v -> {
			initialize(new Game(), config);
		});
		Button exitButton = findViewById(R.id.button_exit);
		exitButton.setOnClickListener(v -> {
			finish();
		});

	}
}

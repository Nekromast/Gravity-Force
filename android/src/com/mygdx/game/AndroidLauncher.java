package com.mygdx.game;

import android.os.Bundle;
import android.view.View;
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

		//initialize(new Game(), config);

		initializeForView(new Game(), config);
		//MainMenu per XML View
		setContentView(R.layout.mainmenu);
		Button startButton = findViewById(R.id.button_start);
		startButton.setOnClickListener(v -> {
			AndroidApplicationConfiguration config1 = new AndroidApplicationConfiguration();
			config1.useAccelerometer = false;
			config1.useCompass = false;
			initialize(new Game(), config1);
		});

	}
}

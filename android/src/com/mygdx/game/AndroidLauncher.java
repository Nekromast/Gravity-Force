package com.mygdx.game;

import static com.mygdx.game.R.*;

import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.GravityForce;

public class AndroidLauncher extends AndroidApplication implements GameOverListener {
    View gameView;
    View gameOverView;
    RelativeLayout rootView;
    Game game;
    boolean isGameOver = false;
    AndroidApplicationConfiguration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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


        rootView = new RelativeLayout(this);
        game = new Game();
        game.setListener(this);
        gameView = initializeForView(game, config);
        gameOverView = View.inflate(this, layout.gameover, null);

        //MainMenu per XML View
        rootView.addView(gameView);
        rootView.addView(gameOverView, new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
                ));
        setContentView(layout.mainmenu);

        Button startButton = findViewById(id.button_start);
        startButton.setOnClickListener(v -> {
            //setContentView(gameView);
            gameOverView.setVisibility(View.INVISIBLE);
            setContentView(rootView);
        });
        Button exitButton = findViewById(id.button_exit);
        exitButton.setOnClickListener(v -> finish());

    }

    @Override
    public void onGameOverChanged(boolean isGameOver) {
        if (isGameOver) {
            runOnUiThread(() -> {
                gameOverView.setVisibility(View.VISIBLE);
                rootView.requestLayout();
                rootView.invalidate();
                gameOver();
            });
        }
    }

    public void gameOver() {
        Button retryButton = findViewById(id.button_retry);
        retryButton.setOnClickListener(v -> {
            game.dispose();
            game = new Game();
            game.setListener(this);
            gameView = initializeForView(game, config);

            rootView.removeViewAt(0);
            rootView.addView(gameView, 0);

            gameOverView.setVisibility(View.INVISIBLE);
            rootView.requestLayout();
            rootView.invalidate();

        });
        Button exitButton2 = findViewById(id.button_exit_gameover);
        exitButton2.setOnClickListener(v -> finish());
    }

}

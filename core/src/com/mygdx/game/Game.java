package com.mygdx.game;

public class Game extends com.badlogic.gdx.Game {
    GravityForce gravityForce;
    GameOverListener gameOverListener;
    public void setListener(GameOverListener listener) {
        gameOverListener = listener;
    }
    @Override
    public void create() {
        gravityForce = new GravityForce(this);
        gravityForce.setListener(gameOverListener);
        this.setScreen(gravityForce);
    }
    @Override
    public void render() {
        super.render();
    }
    @Override
    public void dispose() {
        super.dispose();
    }


}

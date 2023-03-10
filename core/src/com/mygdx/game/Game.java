package com.mygdx.game;

public class Game extends com.badlogic.gdx.Game {
    GravityForce gravityForce;
    GameOverListener gameOverListener;
    /**
     * @param listener Listener für das GameOver
     */
    public void setListener(GameOverListener listener) {
        gameOverListener = listener;
    }

    /**
     * Erstellt das Spiel
     */
    @Override
    public void create() {
        gravityForce = new GravityForce(this);
        gravityForce.setListener(gameOverListener);
        this.setScreen(gravityForce);
    }

    /**
     * render-Methode
     */
    @Override
    public void render() {
        super.render();
    }

    /**
     * dispose-Methode
     */
    @Override
    public void dispose() {
        super.dispose();
    }


}

package com.mygdx.game;


public interface GameOverListener {
    /**
     * @param isGameOver true wenn das Spiel vorbei ist
     */
    void onGameOverChanged(boolean isGameOver);
}

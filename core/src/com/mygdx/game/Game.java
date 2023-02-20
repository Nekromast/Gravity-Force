package com.mygdx.game;

public class Game extends com.badlogic.gdx.Game {
    @Override
    public void create() {
        this.setScreen(new MainMenuScreen(this));
    }
    @Override
    public void render() {
        super.render();
    }
    @Override
    public void dispose() {
    }

}

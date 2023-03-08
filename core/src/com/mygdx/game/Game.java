package com.mygdx.game;

public class Game extends com.badlogic.gdx.Game {
    GravityForce gravityForce;
    @Override
    public void create() {
        //this.setScreen(new MainMenuScreen(this));
        gravityForce = new GravityForce(this);
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

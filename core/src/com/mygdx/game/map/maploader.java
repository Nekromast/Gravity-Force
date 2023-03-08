package com.mygdx.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.sun.tools.javac.code.Attribute;

public class maploader {

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private AssetManager assetManager;



    public maploader() {

        assetManager = new AssetManager();
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        assetManager.load("testmapb.tmx",TiledMap.class);


    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public TiledMap getMap () {
        return assetManager.get("testmapb.tmx");
    }

    //public float getunitscale() {
    //    return unitscale;
    //}

    public void dispose() {
        assetManager.dispose();
    }
}




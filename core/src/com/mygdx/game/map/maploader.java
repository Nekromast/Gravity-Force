package com.mygdx.game.map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class maploader {

    private final AssetManager assetManager;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    /**
     * Constructor
     */
    public maploader() {

        assetManager = new AssetManager();
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        assetManager.load("testmapc.tmx", TiledMap.class);


    }
    /**
     * Getter für den AssetManager
     * @return assetManager
     * AssetManager der die Map lädt
     */
    public AssetManager getAssetManager() {
        return assetManager;
    }

    /**
     * @return gibt die Map zurück
     */
    public TiledMap getMap() {
        return assetManager.get("testmapc.tmx");
    }


    /**
     * entlädt alle Ressourcen des assetManager
     */
    /**
     * entfernt den  assetManager aus dem Speicher
     */
    public void dispose() {
        assetManager.dispose();
    }
}




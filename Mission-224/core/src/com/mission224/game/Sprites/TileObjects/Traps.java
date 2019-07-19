package com.mission224.game.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.mission224.game.Main;
import com.mission224.game.Screens.PlayScreen;

public class Traps extends InteractiveTileObject {

    private static TiledMapTileSet tileSet;
    //private final int BLANK_TRAP = null;

    public Traps(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        tileSet = map.getTileSets().getTileSet("spike A");
        fixture.setUserData(this);
        setCategoryFilter(Main.TRAP_BIT);
    }

    @Override
    public void onSideHit() {
        Gdx.app.log("Traps", "Collision");
        setCategoryFilter(Main.DESTROYED_BIT);
        getCell(5).setTile(null);
        Main.manager.get("Audio/SoundEffects/Hurt.wav", Sound.class).play();
    }
}

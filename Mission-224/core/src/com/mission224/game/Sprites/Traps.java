package com.mission224.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mission224.game.Main;

public class Traps extends InteractiveTileObject {
    public Traps(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        fixture.setUserData(this);
        setCategoryFilter(Main.TRAP_BIT);
    }

    @Override
    public void onSideHit() {
        Gdx.app.log("Traps", "Collision");
        setCategoryFilter(Main.DESTROYED_BIT);
    }
}

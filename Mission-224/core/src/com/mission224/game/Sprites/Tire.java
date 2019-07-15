package com.mission224.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.*;
import com.mission224.game.Main;

public class Tire extends InteractiveTileObject {
    public Tire(World world, TiledMap map, MapObject object) {
        super(world, map, object);
        fixture.setUserData(this);
        setCategoryFilter(Main.TIRE_BIT);
    }

    @Override
    public void onSideHit() {
        Gdx.app.log("Tire", "Collision");
    }
}

package com.mission224.game.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.mission224.game.Main;
import com.mission224.game.Screens.PlayScreen;
import com.mission224.game.Sprites.TileObjects.InteractiveTileObject;

public class Tire extends InteractiveTileObject {

    public Tire(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(Main.OBJECT_BIT);
    }

    @Override
    public void onSideHit() {
        Gdx.app.log("Tire", "Collision");
    }
}

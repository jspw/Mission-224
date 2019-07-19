package com.mission224.game.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.mission224.game.Main;
import com.mission224.game.Screens.PlayScreen;

public class WaterPump extends InteractiveTileObject {

    public WaterPump(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(Main.OBJECT_BIT);
    }

    @Override
    public void onSideHit() {
        Gdx.app.log("WaterPump", "Collision");
    }
}

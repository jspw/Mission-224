package com.mission224.game.sprites.tileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.mission224.game.Main;
import com.mission224.game.screens.PlayScreen;
import com.mission224.game.sprites.enemies.SmallFries1;

public class Tire extends InteractiveTileObject {

    public Tire(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(Main.OBJECT_BIT);
    }

    @Override
    public void onSideHit() {
        Gdx.app.log("Tire", "Collision");
        SmallFries1.playerEnemyCollision = false;
    }
}

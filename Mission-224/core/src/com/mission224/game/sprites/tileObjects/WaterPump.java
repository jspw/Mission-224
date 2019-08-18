package com.mission224.game.sprites.tileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.mission224.game.Main;
import com.mission224.game.screens.PlayScreen;
import com.mission224.game.sprites.enemies.SmallFries1;

public class WaterPump extends InteractiveTileObject {

    public WaterPump(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(Main.OBJECT_BIT);
    }

    @Override
    public void onSideHit() {
        Gdx.app.log("WaterPump", "Collision");
        SmallFries1.playerEnemyCollision = false;
    }
}

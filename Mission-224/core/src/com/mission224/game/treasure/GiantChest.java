package com.mission224.game.treasure;

import com.badlogic.gdx.math.Rectangle;
import com.mission224.game.Main;
import com.mission224.game.scenes.Hud;
import com.mission224.game.screens.PlayScreen;
import com.mission224.game.sprites.enemies.SmallFries1;
import com.mission224.game.sprites.tileObjects.InteractiveTileObject;

public class GiantChest extends InteractiveTileObject {

    public GiantChest(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(Main.TREASURE_BIT);
    }

    @Override
    public void onSideHit() {
        if(Hud.deadEnemies >= 7) {
            Hud.objectiveCleared = true;
            PlayScreen.playAgain = true;
        }
        SmallFries1.playerEnemyCollision = false;
    }
}

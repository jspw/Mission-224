package com.mission224.game.Sprites.TileObjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.mission224.game.Main;
import com.mission224.game.Screens.PlayScreen;
import com.mission224.game.Sprites.Enemies.SmallFries1;

public class Traps extends InteractiveTileObject {

    //private final int BLANK_TRAP = null;

    public Traps(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(Main.TRAP_BIT);
    }

    @Override
    public void onSideHit() {
        SmallFries1.playerEnemyCollision = false;
        setCategoryFilter(Main.DESTROYED_BIT);
        if(getCell(5).getTile() != null) Main.manager.get("Audio/SoundEffects/Hurt.wav", Sound.class).play();
        getCell(5).setTile(null);
    }
}

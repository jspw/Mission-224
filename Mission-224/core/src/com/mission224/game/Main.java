package com.mission224.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mission224.game.Screens.PlayScreen;

public class Main extends Game {
	public static final int V_WIDTH = 1000;
	public static final int V_HEIGHT = 600;
	public static final float PPM = 100;

	public static final short DEFAULT_BIT = 1;
	public static final short PLAYER_BIT = 2;
	public static final short WATER_PUMP_BIT = 4;
	public static final short TIRE_BIT = 8;
	public static final short TRAP_BIT = 16;
	public static final short DESTROYED_BIT = 32;

	public static SpriteBatch batch;

	// Create Main World
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	// Main Loop:
	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}

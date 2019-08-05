package com.mission224.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mission224.game.Scenes.Hud;
import com.mission224.game.Screens.Menu;
import com.mission224.game.Screens.PlayScreen;
import com.mission224.game.Sprites.Player;

public class Main extends Game {

	public static final int V_WIDTH = 1000;
	public static final int V_HEIGHT = 600;
	public static final float PPM = 100;

	// Box2D collision Bits
	public static final short GROUND_BIT = 1;
	public static final short PLAYER_BIT = 2;
	public static final short TRAP_BIT = 4;
	public static final short DESTROYED_BIT = 8;
	public static final short ENEMY_BIT = 16;
	public static final short OBJECT_BIT = 32;
	public static final short PLAYER_DETECTION_BIT = 64;
	public static final short BULLET_BIT = 128;
	public static final short ENEMY_BULLET_BIT = 256;

	public static SpriteBatch batch;
	public static AssetManager manager;

	// Create Main World
	@Override
	public void create () {

		batch = new SpriteBatch();

		// Load the Assets
		manager = new AssetManager();
		manager.load("Audio/Musics/Background_music_for_level_1.mp3", Music.class);
		manager.load("Audio/SoundEffects/Hurt.wav", Sound.class);
		manager.load("Audio/SoundEffects/gun.wav", Sound.class);
		manager.finishLoading();

		// Create Menu
		setScreen(new Menu(this));
	}

	// Main Loop:
	@Override
	public void render () {
		// Create Menu
		if(PlayScreen.playAgain || Hud.worldTimer <= 0){
			Hud.worldTimer = 300;
			Menu.play = false;
			Menu.help = false;
			Menu.mission = false;
			Menu.credit = false;
			PlayScreen.playAgain = false;
		}

		super.render();
	}

	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();
		manager.dispose();
	}
}

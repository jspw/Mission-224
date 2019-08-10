/*
First Release data of Mission-224: August 10, 2019
This game is made for improving our knowledge in Java or OOP programming. xD.....

Developers :
 Shahriar Elahi Dhruvo
 SUST, SWE-17
 F.B: https://www.facebook.com/ShahriarDhruvo
 Linked In: https://www.linkedin.com/in/shahriar-dhruvo-613641190/

 Mehedi Hasan Shifat
 SUST, SWE-17
 F.B: https://www.facebook.com/rio57mh
 Linked In: https://www.linkedin.com/in/mehedi-hasan-shifat-2b10a4172/
*/
package com.mission224.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mission224.game.screens.Menu;
import com.mission224.game.screens.PlayScreen;

public class Main extends Game {

	public static final String TITLE = "Mission-224";
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
	public static final short TREASURE_BIT = 512;

	public static SpriteBatch batch;
	public static AssetManager manager;

	// Create Main World
	@Override
	public void create () {

		batch = new SpriteBatch();

		// Load the Assets
		manager = new AssetManager();
		manager.load("Audio/Musics/Background_music_for_level_1.mp3", Music.class);
		manager.load("Audio/Musics/Background_music_for_Menu.mp3", Music.class);
		manager.load("Audio/SoundEffects/Hurt.wav", Sound.class);
		manager.load("Audio/SoundEffects/gun.wav", Sound.class);
        manager.load("Audio/SoundEffects/die.wav", Sound.class);
		manager.finishLoading();

		// Create Menu
		setScreen(new Menu(this));
	}

	// Main Loop:
	@Override
	public void render () {
		// Create Menu
		if(PlayScreen.playAgain){
			//Hud.worldTimer = 2;
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

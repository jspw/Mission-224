package com.mission224.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mission224.game.Main;

import static com.badlogic.gdx.math.MathUtils.random;

public class Menu implements Screen {

	private Main game;
	private Texture bg;
	private Texture p1;
	private Texture buttonTex;
	private Texture effect;
	private Texture pane ;
	private Texture fb;
	private Animation animation;

	public static boolean play;
	public static boolean mission;
	public static boolean help;
	public static boolean credit;
	private static boolean exit;
	private float elapsedTime;
	private int bal;
	private int x, y, timer;
	private static Music music;

	private Label label, label2, label3, label4, label5;
	private Label helpPanel, missionPanel, creditPanel;

	/*private TextureAtlas panelAtlas;
	private Animation panelAnimation;
	private float paneTime = 0f;
	int paneX=800;
	int paneY=80;*/

	public Menu(Main game) {

		// Initialization
		this.game = game;
		elapsedTime = 0;
		timer = 0;
		x = 600;
		y = 280;
		bal = 1;
		play = false;
		credit = false;
		exit = false;
		mission = false;
		help = false;

		bg = new Texture("Menu/background.png");
		p1 = new Texture("Menu/newLogo.png");
		effect = new Texture("Menu/Buttons/effect.png");
		pane = new Texture("Menu/Buttons/Panel.png");
		buttonTex = new Texture("Menu/Buttons/button.png");

		// Facebook Logo
		fb = new Texture("Menu/facebook.png");

		// Logo animation
		TextureAtlas atlas =  new TextureAtlas("Menu/logoA.atlas");
		animation = new Animation<TextureRegion>(1/6f, atlas.getRegions());

		/*// Panel animation
		panelAtlas = new TextureAtlas("Menu/PanelAnimation/animationPanel.atlas");
		panelAnimation = new Animation(1/60f,panelAtlas.getRegions());*/

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/Oswald-Regular.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 30;
		parameter.borderWidth = 1;
		parameter.color = Color.LIGHT_GRAY;
		parameter.shadowOffsetX = 3;
		parameter.shadowOffsetY = 3;
		parameter.shadowColor = new Color(0, 0f, 0, 1);
		BitmapFont font = generator.generateFont(parameter);
		generator.dispose();

		Label.LabelStyle labelStyle = new Label.LabelStyle();
		labelStyle.font = font;

		/*Label.LabelStyle labelStyle = new Label.LabelStyle();
		labelStyle.font = new BitmapFont(Gdx.files.internal("Fonts/oswald-32.fnt"));
		labelStyle.fontColor = Color.BLUE;*/

		label = new Label("PLAY", labelStyle);
		label2 = new Label("OBJECTIVE", labelStyle);
		label3 = new Label("HELP", labelStyle);
		label4 = new Label("CREDIT", labelStyle);
		label5 = new Label("EXIT", labelStyle);

		helpPanel = new Label("A or Left-Arrow  =  Move Left\nD or Right-Arrow  =  Movie Right\nSPACE or Up-Arrow  =  Jump\nLeft-Click  =  Shoot\nESC  =  Pause", labelStyle);
		missionPanel = new Label("Your score is determined by the multiplication\n" +
				"of TIME LEFT, YOUR HEALTH and THE NUMBER OF\n" +
				"ENEMIES YOU KILLED.\nYour objective is to loot the chest at the end of\n" +
				"the map and make a HIGH score.\nYou have to kill at least 7 ENEMIES to clear the\n" +
				"objective. (R.E. = Remaining Enemies.)", labelStyle);
		creditPanel = new Label("Project of SWE-224 By\nShahriar Elahi Dhruvo\nReg No: 2017831060\n\nMehedi Hasan Shifat\nReg No: 2017831017", labelStyle);

		label.setFontScale(1f,0.8f);
		label2.setFontScale(1f,0.8f);
		label3.setFontScale(1f,0.8f);
		label4.setFontScale(1f,0.8f);
		label5.setFontScale(1f, 0.8f);

		// Adding Background Music
		music = Main.manager.get("Audio/Musics/Background_music_for_Menu.mp3", Music.class);
		music.setLooping(true);
		music.play();
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		elapsedTime += Gdx.graphics.getDeltaTime();
		//paneTime+=Gdx.graphics.getDeltaTime();
		x += random()%50;
		y += random()%50;

		Main.batch.begin();

		Main.batch.draw(bg,0,0);

		bal++;
	    Main.batch.draw((TextureRegion) animation.getKeyFrame(elapsedTime,false), x, y);
	    if(bal >= 120){
	    	// Draw Logo
			Main.batch.draw(p1,300,100);

		// Draw Buttons:

            // Play Button
			Main.batch.draw(buttonTex,50,400);
			label.setPosition(95,402);
            label.draw(Main.batch,1);
			if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && Gdx.input.getX() >= 50 && Gdx.input.getX() <= 198 && Gdx.input.getY() >= 155 && Gdx.input.getY() <= 196){
                Main.batch.draw(effect,50,395);
				label.setPosition(95,400);
                label.draw(Main.batch,1);
				play = true;
				mission = false;
				help = false;
				exit = false;
				credit = false;

                // Create game screen
				game.setScreen(new PlayScreen(game));
				dispose();
			}

            // Missions button
			Main.batch.draw(buttonTex,50,320);
			label2.setPosition(62,321);
			label2.draw(Main.batch,1);
			if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && Gdx.input.getX() >= 50 && Gdx.input.getX() <= 198 && Gdx.input.getY() >= 235 && Gdx.input.getY() <= 283){
				Main.batch.draw(effect,50,315);
				label2.setPosition(62,319);
				label2.draw(Main.batch,1);
				mission = true;
				play = false;
				help = false;
				exit = false;
				credit = false;
				/*paneX -=30;
				if(paneX<300) paneX=800;
				game.getBatch().draw((TextureRegion) panelAnimation.getKeyFrame(paneTime,false),paneX,paneY);*/
			}

			// Help button
			Main.batch.draw(buttonTex,50,240);
			label3.setPosition(95,241);
			label3.draw(Main.batch,1);
			if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && Gdx.input.getX() >= 50 && Gdx.input.getX() <= 198 && Gdx.input.getY() >= 316 && Gdx.input.getY() <= 356){
				Main.batch.draw(effect,50,235);
				label3.setPosition(95,240);
				label3.draw(Main.batch,1);
				help = true;
				play = false;
				mission = false;
				credit = false;
				exit = false;
			}

			// Credit button
			Main.batch.draw(buttonTex,50,160);
			label4.setPosition(85,161);
			label4.draw(Main.batch,1);
			if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && Gdx.input.getX() >= 50 && Gdx.input.getX() <= 198 && Gdx.input.getY() >= 395 && Gdx.input.getY() <= 435){
				Main.batch.draw(effect,50,155);
				label4.setPosition(85,160);
				label4.draw(Main.batch,1);
				credit = true;
				play = false;
				mission = false;
				help = false;
				exit = false;
			}

			// Exit button
			Main.batch.draw(buttonTex,50,80);
			label5.setPosition(95,81);
			label5.draw(Main.batch,1);
			if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && Gdx.input.getX() >= 50 && Gdx.input.getX() <= 198 && Gdx.input.getY() >= 475 && Gdx.input.getY() <= 520){
				Main.batch.draw(effect,50,75);
				label5.setPosition(95,80);
				label5.draw(Main.batch,1);
				exit = true;
			}

			// Panel setup
			if(help){
				Main.batch.draw(pane,420,80);
				helpPanel.setFontScale(0.8f);
				helpPanel.setColor(Color.CYAN);
				helpPanel.setPosition(500,100);
				helpPanel.draw(Main.batch,1);
			}
			if(credit){
				Main.batch.draw(pane,420,80);
				creditPanel.setFontScale(.8f);
				creditPanel.setColor(Color.CYAN);
				creditPanel.setPosition(500,80);
				creditPanel.draw(Main.batch,1);

				Main.batch.draw(fb,720,205);
				Main.batch.draw(fb,720,110);
				if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && Gdx.input.getX()>=730 && Gdx.input.getX()<=760 && Gdx.input.getY()>=350 && Gdx.input.getY()<=386){
					timer++;
					if(timer > 10) {
						Gdx.net.openURI("https://www.facebook.com/ShahriarDhruvo");
						timer = 0;
					}
				}
				else if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && Gdx.input.getX()>=730 && Gdx.input.getX()<=760 && Gdx.input.getY()>=430 && Gdx.input.getY()<=480){
					timer++;
					if(timer > 10) {
						Gdx.net.openURI("https://www.facebook.com/rio57mh");
						timer = 0;
					}
				}
			}
			if(mission){
				Main.batch.draw(pane,420,80);
				missionPanel.setFontScale(.7f);
				missionPanel.setColor(Color.CYAN);
				missionPanel.setPosition(445,60);
				missionPanel.draw(Main.batch,1);
			}
        }

		if(exit) Gdx.app.exit();

		Main.batch.end();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		//System.out.println("I am disposing Menu");
		buttonTex.dispose();
		bg.dispose();
		p1.dispose();
		effect.dispose();
		pane.dispose();
		fb.dispose();
		music.stop();
	}
}

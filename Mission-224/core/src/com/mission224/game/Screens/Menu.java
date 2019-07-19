package com.mission224.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mission224.game.Main;

import static com.badlogic.gdx.math.MathUtils.random;
import static java.lang.System.exit;


public class Menu implements Screen {

	private Main game;

	private Texture bg;
	private Texture p1;
	private Texture buttonTex;
	private Texture effect;
	private Texture pane ;




	public static boolean play = false;
	public static boolean mission = false;
	public static boolean help = false;
	public static boolean credit = false;
	public static boolean exit = false;
	public static boolean pause=false;



	private TextureAtlas atlas;
	private Animation anim ;
	private float elaspedTime = 0f;
	int bal=1;
	int x=600,y=280;


	private TextureAtlas panelAtlas;
	private Animation panelAnimation;
	private float paneTime = 0f;
	int paneX=800;
	int paneY=80;

	private Label label,label2,label3,label4,label5;
	private  Label helpPanel,missionPanel,creditPanel;



	public Menu(Main game){

		this.game = game;

		bg = new Texture("Menu/background.jpg");
		p1 = new Texture("Menu/NewLogo.png");
		effect = new Texture("Menu/Buttons/effect.png");
		pane = new Texture("Menu/Buttons/Panel.png");
		buttonTex = new Texture("Menu/Buttons/button.png");


		//logo animation
		atlas =  new TextureAtlas("Menu/logoA.atlas");
		anim = new Animation(1/6f,atlas.getRegions());

		//panel animation
		panelAtlas = new TextureAtlas("Menu/PanelAnimation/animationPanel.atlas");
		panelAnimation = new Animation(1/60f,panelAtlas.getRegions());


		Label.LabelStyle labelStyle = new Label.LabelStyle();
		BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/oswald-32.fnt"));

		labelStyle.font = font;
		labelStyle.fontColor = Color.BLACK;

		label =new Label("PLAY", labelStyle);
		label2 =new Label("MISSION", labelStyle);
		label3 =new Label("HELP", labelStyle);
		label4 =new Label("CREDIT", labelStyle);
		label5 =new Label("EXIT", labelStyle);

		helpPanel = new Label("A = Move Left\nD = Movie Right\nSPACE = Jump\nLEFT MOUSE = Shoot",labelStyle);
		missionPanel = new Label("To be added",labelStyle);
		creditPanel = new Label("niggas",labelStyle);

		label.setFontScale(1f,0.8f);
		label2.setFontScale(1f,0.8f);
		label3.setFontScale(1f,0.8f);
		label4.setFontScale(1f,0.8f);

	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		elaspedTime += Gdx.graphics.getDeltaTime();
		paneTime+=Gdx.graphics.getDeltaTime();
		x+= random()%50;
		y+=random()%50;

		game.getBatch().begin();

		game.getBatch().draw(bg,0,0);

		bal++;
	//	System.out.println(bal);
	    game.getBatch().draw((TextureRegion) anim.getKeyFrame(elaspedTime,false),x,y);
	    if(bal >= 120){

	    	//Draw Logo
			game.getBatch().draw(p1,300,100);

			//Draw Buttons

            //Play Button
			game.getBatch().draw(buttonTex,50,400);
			label.setPosition(95,402);
            label.draw(game.getBatch(),1);

            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && Gdx.input.getX()>=50 && Gdx.input.getX()<=198 && Gdx.input.getY()>=155 && Gdx.input.getY()<=196){
                game.getBatch().draw(effect,50,395);
				label.setPosition(95,400);
                label.draw(game.getBatch(),1);
				play = true;
				mission=false;
				help=false;
				exit=false;
				credit=false;
				pause=false;

                //create game screen
				this.dispose();
				game.setScreen(new PlayScreen(game));

			}
            //Missions button
			game.getBatch().draw(buttonTex,50,320);
			label2.setPosition(73,321);
			label2.draw(game.getBatch(),1);
			if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && Gdx.input.getX()>=50 && Gdx.input.getX()<=198 && Gdx.input.getY()>=235 && Gdx.input.getY()<=283){
				game.getBatch().draw(effect,50,315);
				label2.setPosition(73,319);
				label2.draw(game.getBatch(),1);
				mission = true;
				play = false;
				help=false;
				exit=false;
				credit=false;
				paneX -=30;
				if(paneX<300) paneX=800;
				game.getBatch().draw((TextureRegion) panelAnimation.getKeyFrame(paneTime,false),paneX,paneY);
			}
			//Help Button
			game.getBatch().draw(buttonTex,50,240);
			label3.setPosition(95,241);
			label3.draw(game.getBatch(),1);
			if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && Gdx.input.getX()>=50 && Gdx.input.getX()<=198 && Gdx.input.getY()>=316 && Gdx.input.getY()<=356){
				game.getBatch().draw(effect,50,235);
				label3.setPosition(95,240);
				label3.draw(game.getBatch(),1);
				help = true;
				play = false;
				mission=false;
				credit=false;
				exit=false;
			}

			//Credit button
			game.getBatch().draw(buttonTex,50,160);
			label4.setPosition(85,161);
			label4.draw(game.getBatch(),1);
			if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && Gdx.input.getX()>=50 && Gdx.input.getX()<=198 && Gdx.input.getY()>=395 && Gdx.input.getY()<=435){
				game.getBatch().draw(effect,50,155);
				label4.setPosition(85,160);
				label4.draw(game.getBatch(),1);
				credit = true;
				play = false;
				mission=false;
				help=false;
				exit=false;
			}
			//Exit button
			game.getBatch().draw(buttonTex,50,80);
			label5.setPosition(95,81);
			label5.draw(game.getBatch(),1);
			if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && Gdx.input.getX()>=50 && Gdx.input.getX()<=198 && Gdx.input.getY()>=475 && Gdx.input.getY()<=520){
				game.getBatch().draw(effect,50,75);
				label5.setPosition(95,80);
				label5.draw(game.getBatch(),1);
				exit = true;
			}
			//panel setup
			if(help){
				game.getBatch().draw(pane,300,80);
				helpPanel.setColor(Color.BLUE);
				helpPanel.setFontScale(0.5f);
				helpPanel.setPosition(307,80);
				helpPanel.draw(game.getBatch(),1);
			}
			if(credit){

				game.getBatch().draw(pane,300,80);
				helpPanel.setColor(Color.BLUE);
				creditPanel.setPosition(300,80);
				creditPanel.draw(game.getBatch(),1);
			}
			if(mission){
				game.getBatch().draw(pane,300,80);
				helpPanel.setColor(Color.BLUE);
				missionPanel.setPosition(300,80);
			//	missionPanel.draw(game.getBatch(),1);
			}
        }


	    if(exit) exit(0);

		game.getBatch().end();

		if(Menu.pause) PlayScreen.music.stop();
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
		buttonTex.dispose();
		bg.dispose();
		p1.dispose();
		effect.dispose();
		pane.dispose();


	}
}

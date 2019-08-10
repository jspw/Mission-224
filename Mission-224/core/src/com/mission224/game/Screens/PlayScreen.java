package com.mission224.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mission224.game.Main;
import com.mission224.game.scenes.Hud;
import com.mission224.game.tools.Bullets;
import com.mission224.game.sprites.enemies.Enemy;
import com.mission224.game.sprites.Player;
import com.mission224.game.tools.B2WorldCreator;
import com.mission224.game.tools.WorldContactListener;

public class PlayScreen implements Screen {

    public static boolean playAgain;
    private float playAgainTimer;
    private float enemyDetectionDelay;

    // Screen Variables
    private OrthographicCamera gameCam;
    private Hud hud;
    private Viewport gamePort;
    private Stage stage;

    // Tiled map variables
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    // Box2D variables
    private World world;
    private Box2DDebugRenderer b2dr;
	private B2WorldCreator creator;

    // Player variables
    private Player player;
    private Main game;
    public static boolean canJump;
    private boolean pauseScreen;
    private float jumpDelay;

    // Music Variables
    private static Music music;
    private static Sound fireSound;

    public PlayScreen(Main game) {

        this.game = game;

        gameCam = new OrthographicCamera(Main.V_WIDTH, Main.V_HEIGHT);
        gamePort = new FitViewport(Main.V_WIDTH / Main.PPM, Main.V_HEIGHT / Main.PPM, gameCam);
        hud = new Hud(Main.batch);

        canJump = true;
        pauseScreen = false;
        enemyDetectionDelay = 0;
        playAgainTimer = 0;
        jumpDelay = 0;

        // Map Loader:
        TmxMapLoader mapLoader = new TmxMapLoader();
        //map = mapLoader.load(System.getProperty("user.dir") + "/" + "Maps/Level0.tmx");
        map = mapLoader.load("Maps/Level1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1/ Main.PPM);

        // GameCam centralized
        gameCam.position.set(gamePort.getWorldWidth()/2 + 0.25f, gamePort.getWorldHeight()/2, 0);

        // Box2D initialization
        world = new World(new Vector2(0, -30), true);
        b2dr =  new Box2DDebugRenderer();

        // Creating world
        creator = new B2WorldCreator(this);

        // Creating player in the game World
        player = new Player(this);

        // After effects of collision detection
        world.setContactListener(new WorldContactListener());

        // Adding Background Music
        music = Main.manager.get("Audio/Musics/Background_music_for_level_1.mp3", Music.class);
        music.setLooping(true);
        music.play();

        // Adding bullet sound effect
        fireSound = Main.manager.get("Audio/SoundEffects/gun.wav", Sound.class);

        // Pause Screen
        Viewport viewport = new FitViewport(Main.V_WIDTH, Main.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, Main.batch);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        // FreeType font generator
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/ALGER.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        parameter.borderWidth = 1;
        parameter.color = Color.RED;
        parameter.shadowOffsetX = 3;
        parameter.shadowOffsetY = 3;
        parameter.shadowColor = new Color(0, 0f, 0, 1);
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        Label pauseLabel = new Label("    Pause    ", labelStyle);

        table.add(pauseLabel).center().expand();
        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    // Handling User Inputs
    private void handleInput() {

        // Jump
        if((Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) && canJump && jumpDelay > 0.5f) {
            if(player.runningRight) player.b2body.applyLinearImpulse(new Vector2(0.5f ,8), player.b2body.getWorldCenter(), true);
            else player.b2body.applyLinearImpulse(new Vector2(-0.5f ,8), player.b2body.getWorldCenter(), true);
            canJump = false;
            jumpDelay = 0;
        }

        // Right
        if((Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) && player.b2body.getLinearVelocity().x >= -2){
            player.b2body.applyLinearImpulse(new Vector2(-1f ,0), player.b2body.getWorldCenter(), true);
        }

        // Left
        if((Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) && player.b2body.getLinearVelocity().x <= 2){
            player.b2body.applyLinearImpulse(new Vector2(1f ,0), player.b2body.getWorldCenter(), true);
        }

        // Bullet
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && player.getFiringDelay() > 0.5f) {
            float bulletX = player.b2body.getPosition().x;
            float bulletY = player.b2body.getPosition().y;

            if(player.runningRight) bulletX += .5f;
            else bulletX -= .3f;

            player.bullets.add(new Bullets(this, bulletX, bulletY, player.runningRight));
            fireSound.play();
        }
    }

    private void update(float dt) {

        // Pause screen
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && !pauseScreen) {
            pauseScreen = true;
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && pauseScreen) {
            pauseScreen = false;
        }

        if(!pauseScreen) {

            // To go back to menu after players's death
            updatePlayAgain(dt);

            // Handle user input
            handleInput();

            // Updating bodies to the world
            world.step(1 / 60f, 6, 2);

            // Update Player Position
            player.update(dt);


            for (Enemy enemy : creator.getSmallFries1Array()) enemy.update(dt);

            // Hud Update
            hud.update(dt, player.heathStatus());

            // Attach gameCam to the Player co-ordinate
            if (player.b2body.getPosition().x > 5f && player.b2body.getPosition().x < 74f)
                gameCam.position.x = player.b2body.getPosition().x;

            gameCam.update();
            mapRenderer.setView(gameCam);

            // Back to Menu
            if (playAgain) {
                game.setScreen(new DeadScreen(game));
                music.stop();
                fireSound.stop();
                dispose();
            }
            jumpDelay += dt;
            enemyDetectionDelay += dt;
        }
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);

        if(!pauseScreen) {
            Main.batch.setProjectionMatrix(hud.stage.getCamera().combined);

            // Map Render
            mapRenderer.render();

            // Box2DDebug Render
            //b2dr.render(world, gameCam.combined);

            Main.batch.setProjectionMatrix(gameCam.combined);
            Main.batch.begin();

            // Rendering Player
            player.draw(Main.batch);

            // Rendering Bullets
            for (Bullets bullet : player.bullets) {
                bullet.draw(Main.batch);
            }

            // Rendering Enemies
            for (Enemy enemy : creator.getSmallFries1Array()) {
                enemy.draw(Main.batch);
                //if(enemy.getX() < player.getX() + 800 / Main.PPM && !enemy.dead()) enemy.b2body.setActive(true);
                //if(enemy.getX() < player.getX() - 800 / Main.PPM && !enemy.dead()) enemy.b2body.setActive(false);
                if (!enemy.dead() && enemy.isDetect()) {
                    if ((player.getX() < enemy.getX()) && !enemy.isFlipX() && enemyDetectionDelay > 0.5f) {
                        enemy.reverseVelocity(true, false);
                        enemyDetectionDelay = 0;
                    }
                    if ((player.getX() > enemy.getX()) && enemy.isFlipX() && enemyDetectionDelay > 0.5f) {
                        enemy.reverseVelocity(true, false);
                        enemyDetectionDelay = 0;
                    }
                }
            }

            Main.batch.end();

            // Hud Render
            hud.stage.draw();
        }
        // Pause screen
        else stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    private void updatePlayAgain(float delta) {
        if(player.heathStatus() <= 0){
            playAgainTimer += delta;
            if(playAgainTimer > 1f)
                playAgain = true;
        }
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
        //System.out.println("I am disposing playScreen");
        hud.dispose();
        map.dispose();
        b2dr.dispose();
        world.dispose();
        //mapRenderer.dispose();
    }
}

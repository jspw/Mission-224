package com.mission224.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mission224.game.Main;
import com.mission224.game.Scenes.Hud;
import com.mission224.game.Sprites.Bullets;
import com.mission224.game.Sprites.Enemies.Enemy;
import com.mission224.game.Sprites.Player;
import com.mission224.game.Tools.B2WorldCreator;
import com.mission224.game.Tools.WorldContactListener;

public class PlayScreen implements Screen {

    // Screen Variables
    private Main game;
    private OrthographicCamera gameCam;
    private Hud hud;
    private Viewport gamePort;

    // Tiled map variables
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    // Box2D variables
    private World world;
    private Box2DDebugRenderer b2dr;
	private B2WorldCreator creator;

    // Player variables
    private Player player;
    private TextureAtlas atlas;
    public static boolean canJump;


    // Music Variables
    public static  Music music;

    // Project Path
    private String projectDir = System.getProperty("user.dir") + "/";


    public PlayScreen(Main game) {

        atlas = new TextureAtlas("CharactersFiles/Player.pack");

        this.game = game;
        gameCam = new OrthographicCamera(Main.V_WIDTH, Main.V_HEIGHT);
        gamePort = new FitViewport(Main.V_WIDTH / Main.PPM, Main.V_HEIGHT / Main.PPM, gameCam);
        hud = new Hud(game.batch);

        canJump = true;

        // Map Loader:
        mapLoader = new TmxMapLoader();
        map = mapLoader.load(projectDir + "Maps/Level1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1/ Main.PPM);

        // GameCam centralized
        gameCam.position.set(gamePort.getWorldWidth()/2 + 0.25f, gamePort.getWorldHeight()/2, 0);

        // Box2D initialization
        world = new World(new Vector2(0, -10), true);
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
        //music.play();
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {

    }

    // Handling User Inputs
    public void handleInput(float dt) {

        /*if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            gameCam.position.x += 100 * dt;

        }

        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            gameCam.position.x -= 100 * dt;

        }*/

        /*if(Gdx.input.isTouched()) {
            int x = Gdx.input.getX();
            int y = Gdx.input.getY();
            if(x <= 500)
                player.b2body.applyLinearImpulse(new Vector2(0f ,0.5f), player.b2body.getWorldCenter(), true);
            else
                player.b2body.applyLinearImpulse(new Vector2(0.1f ,0), player.b2body.getWorldCenter(), true);
        }*/

        // Jump
        if((Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) && canJump) {
            player.b2body.applyLinearImpulse(new Vector2(0 ,5f), player.b2body.getWorldCenter(), true);
            canJump = false;
        }

        // Right
        if((Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) && player.b2body.getLinearVelocity().x >= -2){
            player.b2body.applyLinearImpulse(new Vector2(-1f ,0), player.b2body.getWorldCenter(), true);
        }

        // Left
        if((Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) && player.b2body.getLinearVelocity().x <= 2){
            player.b2body.applyLinearImpulse(new Vector2(1f ,0), player.b2body.getWorldCenter(), true);
        }

        // Bullets
        if(Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            float bulletX = player.b2body.getPosition().x;
            float bulletY = player.b2body.getPosition().y;

            if(player.runningRight) bulletX += .5f;
            else bulletX -= .3f;

            player.bullets.add(new Bullets(this, bulletX, bulletY, player.runningRight));
        }
    }



    public void update(float dt) {

        // Handle user input
        handleInput(dt);

        // Updating bodies to the world
        world.step(1 / 60f, 6, 2);

        // Update Player Position
        player.update(dt);


        for(Enemy enemy:creator.getSmallFries1Array())
            enemy.update(dt);

        // Hud Update
        hud.update(dt);

        // Attach gameCam to the Player co-ordinate
        if(player.b2body.getPosition().x > 5f && player.b2body.getPosition().x < 74f)
            gameCam.position.x = player.b2body.getPosition().x;
        //System.out.println(player.b2body.getPosition().x);

        gameCam.update();
        mapRenderer.setView(gameCam);
    }

    @Override
    public void render(float delta) {

        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);

        // Map Render
        mapRenderer.render();

        // Box2DDebug Render
        b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();

	    // Rendering Player
        player.draw(game.batch);

        // Rendering Bullets
        for (Bullets bullet : player.bullets) {
            bullet.draw(game.batch);
        }

        // Rendering Enemies
        for(Enemy enemy : creator.getSmallFries1Array()) {
            enemy.draw(game.batch);
            if(enemy.getX() < player.getX() + 1600 / Main.PPM)
                enemy.b2body.setActive(true);
        }

        game.batch.end();

        // Hud Render
        hud.stage.draw();
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
        map.dispose();
        mapRenderer.dispose();
        b2dr.dispose();
        world.dispose();
        hud.stage.dispose();
        music.dispose();
    }
}

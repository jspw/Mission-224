package com.mission224.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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
import com.mission224.game.Sprites.Player;
import com.mission224.game.Tools.B2WorldCreator;
import com.mission224.game.Tools.WorldContactListener;


public class PlayScreen implements Screen {

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

    // Creating player
    private Player player;
    private TextureAtlas atlas;

    private String projectDir = System.getProperty("user.dir") + "/";

    public PlayScreen(Main game) {

        atlas = new TextureAtlas("PlayerFiles/Player.pack");

        this.game = game;
        gameCam = new OrthographicCamera(Main.V_WIDTH, Main.V_HEIGHT);
        gamePort = new FitViewport(Main.V_WIDTH / Main.PPM, Main.V_HEIGHT / Main.PPM, gameCam);
        hud = new Hud(game.batch);

        // Map Loader:
        mapLoader = new TmxMapLoader();
        map = mapLoader.load(projectDir + "Maps/Level1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1/ Main.PPM);

        // GameCam centralized
        gameCam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);

        // Box2D intialization
        world = new World(new Vector2(0, -10), true);
        b2dr =  new Box2DDebugRenderer();

        // Creating world
        new B2WorldCreator(world, map);

        // Creating player in the game World
        player = new Player(world, this);

        // After efects of collision ditaction
        world.setContactListener(new WorldContactListener());
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {

    }

    // Handeling user Inputs
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

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player.b2body.applyLinearImpulse(new Vector2(0 ,4f), player.b2body.getWorldCenter(), true);
        }

//        if(Gdx.input.isKeyPressed(Input.Keys.S)){
//            player.b2body.applyLinearImpulse(new Vector2(0 ,-10f),player.b2body.getWorldCenter(),true);
//        }
//
        if((Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) && player.b2body.getLinearVelocity().x >= -2){
            player.b2body.applyLinearImpulse(new Vector2(-1f ,0), player.b2body.getWorldCenter(), true);
        }

        if((Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) && player.b2body.getLinearVelocity().x <= 2){
            player.b2body.applyLinearImpulse(new Vector2(1f ,0), player.b2body.getWorldCenter(), true);
        }
    }

    public void update(float dt) {
        // Handle user input
        handleInput(dt);

        // Updating bodies to the world
        world.step(1/60f, 6, 2);

        // Update Player Position
        player.update(dt);

        // Attach gameCam to the Player co-ordinate
        gameCam.position.x = player.b2body.getPosition().x;

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

        // Player Render
        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();

        player.draw(game.batch);

        game.batch.end();

        // Hud Render
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
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
    }
}

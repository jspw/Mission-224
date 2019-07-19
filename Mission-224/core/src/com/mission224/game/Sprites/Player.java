package com.mission224.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mission224.game.Main;
import com.mission224.game.Screens.PlayScreen;

import java.util.ArrayList;

public class Player extends Sprite {
    public enum State { FALLING, JUMPING, STANDING, RUNNING, SHOOTING };
    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;
    //private TextureRegion playerStand;
    private Animation playerIdle;
    private Animation playerRun;
    private Animation playerJump;
    private Animation playerAttack;
    private TextureAtlas playerAttackAtlas;
    //private TextureRegion playerShooting;
    private float stateTimer;
    public boolean runningRight;

    // Main Player bullets
    public ArrayList <Bullets> bullets;

    // Player Png Info
    private static final int P_WIDTH = 50;
    private static final int PI_HEIGHT = 110;
    private static final int PJ_HEIGHT = 133;
    private static final int PR_HEIGHT = 93;

    public Player(PlayScreen screen) {

        super(screen.getAtlas().findRegion("1_terrorist_1_Idle"));
        this.world = screen.getWorld();

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        playerAttackAtlas = new TextureAtlas("CharactersFiles/Player_Attack.pack");

        // Player Idle/Standing Animation
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i=0; i<8; i++)
            frames.add(new TextureRegion(getTexture(), i*P_WIDTH, 23, P_WIDTH, PI_HEIGHT));
        playerIdle = new Animation(0.1f, frames);
        frames.clear();

        // Player Jump Animation
        for(int i=8; i<16; i++)
            frames.add(new TextureRegion(getTexture(), i*P_WIDTH, 0, P_WIDTH, PJ_HEIGHT));
        playerJump = new Animation(0.1f, frames);
        frames.clear();

        // Player Run Animation
        for(int i=16; i<22; i++)
            frames.add(new TextureRegion(getTexture(), i*P_WIDTH, 40, P_WIDTH, PR_HEIGHT));
        playerRun = new Animation(0.1f, frames);
        frames.clear();

        // Player Attack Animation
        for(int i=0; i<6; i++)
            frames.add(new TextureRegion(playerAttackAtlas.findRegion("1_terrorist_1_Attack1"), i*80, 0, 80, 116));
        playerAttack = new Animation(0.1f, frames);
        frames.clear();

        // Player Shooting
        //playerShooting = new TextureRegion(new Texture("CharactersFiles/Player_Attack.png"));

        // Player Stand
        //playerStand = new TextureRegion(getTexture(), 0, 23, P_WIDTH, PI_HEIGHT);

        definePlayer();
        setBounds(getX(), getY(), P_WIDTH / Main.PPM, PI_HEIGHT / Main.PPM);

        //setRegion(playerStand);

        // create bullets
        bullets = new ArrayList<Bullets>();
    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - 0.5f);
        setRegion(getFrame(dt));

       ///for bullets update
        ArrayList<Bullets>bulletToRemove = new ArrayList<Bullets>();
        for(Bullets bullet : bullets){
            bullet.update(dt);
            if(bullet.remove){
                bulletToRemove.add(bullet);
            }
        }
        bullets.removeAll(bulletToRemove);
    }

    private TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case JUMPING:
                region = (TextureRegion) playerJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = (TextureRegion) playerRun.getKeyFrame(stateTimer, true);
                //region = (TextureRegion) playerAttack.getKeyFrame(stateTimer);
                PlayScreen.canJump = true;
                break;
            case SHOOTING:
                //region = playerShooting;
                region = (TextureRegion) playerAttack.getKeyFrame(stateTimer);
                break;
            case FALLING:
            case STANDING:
            default:
                region = (TextureRegion) playerIdle.getKeyFrame(stateTimer, true);
                PlayScreen.canJump = true;
                //region = playerStand;
                break;
        }

        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        }

        if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    private State getState() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.P))
            return State.SHOOTING;
        if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if(b2body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if(b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;
    }

    public void definePlayer(){
        BodyDef  bdef = new BodyDef();
        bdef.position.set(224 / Main.PPM, 254 / Main.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();

        // Player Circle
        /*CircleShape shape = new CircleShape();
        shape.setRadius(10 / Main.PPM);*/
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(15 / Main.PPM,40 / Main.PPM);

        fdef.restitution = -3;
        fdef.filter.categoryBits = Main.PLAYER_BIT;

        // Which objects can collide with this object
        fdef.filter.maskBits = Main.GROUND_BIT | Main.OBJECT_BIT | Main.TRAP_BIT | Main.ENEMY_BIT | Main.PLAYER_DETECTION_BIT | Main.BULLET_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef);

        // Right Side Line to detect thing as sensor
        EdgeShape Rside = new EdgeShape();
        Rside.set(new Vector2(-25, 50).scl(1/Main.PPM), new Vector2(-25, -45).scl(1/Main.PPM));
        fdef.shape = Rside;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("Rside");

        // Left Side Line to detect thing as sensor
        EdgeShape Lside = new EdgeShape();
        Lside.set(new Vector2(25, 50).scl(1/Main.PPM), new Vector2(25, -45).scl(1/Main.PPM));
        fdef.shape = Lside;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("Lside");
    }
}

package com.mission224.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mission224.game.Main;
import com.mission224.game.Screens.PlayScreen;

public class Player extends Sprite {
    public enum State { FALLING, JUMPING, STANDING, RUNNING };
    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;
    private TextureRegion playerStand;
    private Animation playerRun;
    private Animation playerJump;
    private float stateTimer;
    private boolean runningRight;

    /*//Stand idle animation
    public static Animation idleAnimation;
    public static TextureAtlas idleAtlas;
    public static float elapseTime = 0f;*/

    //TEMP
    private static final int P_WIDTH = 50;
    private static final int PS_HEIGHT = 110;
    private static final int PJ_HEIGHT = 133;
    private static final int PR_HEIGHT = 93;

    public Player(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("1_terrorist_1_Idle"));
        this.world = world;

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        // Player Run Animation
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i=16; i<22; i++)
            frames.add(new TextureRegion(getTexture(), i*P_WIDTH, 40, P_WIDTH, PR_HEIGHT));
        playerRun = new Animation(0.1f, frames);
        frames.clear();

        // Player Jump Animation
        for(int i=8; i<16; i++)
            frames.add(new TextureRegion(getTexture(), i*P_WIDTH, 0, P_WIDTH, PJ_HEIGHT));
        playerJump = new Animation(0.1f, frames);

        // Player Stand
        playerStand = new TextureRegion(getTexture(), 0, 23, P_WIDTH, PS_HEIGHT);

        definePlayer();
        setBounds(0, 0, P_WIDTH / Main.PPM, PS_HEIGHT / Main.PPM);
        setRegion(playerStand);

        /*//idle animation
        idleAtlas = new TextureAtlas("idleAtlas/idle.atlas");
        idleAnimation = new Animation(1/30f,idleAtlas.getRegions());
        setBounds(0, 0, getRegionWidth() / Main.PPM, getRegionHeight() / Main.PPM);*/
    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - 0.25f);
        setRegion(getFrame(dt));
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
                break;
            case FALLING:
            case STANDING:
            default:
                region = playerStand;
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
        CircleShape shape = new CircleShape();
        shape.setRadius(10 / Main.PPM);

        fdef.filter.categoryBits = Main.PLAYER_BIT;
        fdef.filter.maskBits = Main.DEFAULT_BIT | Main.TIRE_BIT | Main.WATER_PUMP_BIT | Main.TRAP_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef);

        // Right Side Line to detect thing as sensor
        EdgeShape Rside = new EdgeShape();
        Rside.set(new Vector2(-25 / Main.PPM, 70 / Main.PPM), new Vector2(-25 / Main.PPM, -1 / Main.PPM));
        fdef.shape = Rside;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("Rside");

        // Left Side Line to detect thing as sensor
        EdgeShape Lside = new EdgeShape();
        Lside.set(new Vector2(25 / Main.PPM, 70 / Main.PPM), new Vector2(25 / Main.PPM, -1 / Main.PPM));
        fdef.shape = Lside;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("Lside");
    }
}

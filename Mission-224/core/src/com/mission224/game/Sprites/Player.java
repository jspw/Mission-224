package com.mission224.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mission224.game.Main;
import com.mission224.game.screens.PlayScreen;
import com.mission224.game.tools.Bullets;

import java.util.ArrayList;

public class Player extends Sprite {

    public enum State { FALLING, JUMPING, STANDING, RUNNING, SHOOTING, DYING }
    private State currentState;
    private State previousState;
    private World world;
    public Body b2body;
    private Animation playerIdle;
    private Animation playerRun;
    private Animation playerJump;
    private Animation playerAttack;
    private Animation dyingAnimation;
    private float stateTimer;
    private float attackAnimationDelay;
    public boolean runningRight;
    private boolean shooting;
    private float firingDelay;

    // Main Player bullets
    public ArrayList <Bullets> bullets;
    private int bulletHitCount;
    private boolean setToDestroy;
    private boolean destroyed;

    // Player Png Info
    private static final int PI_WIDTH = 50;
    private static final int PR_WIDTH = 60;
    private static final int PJ_WIDTH = 60;
    private static final int PA_WIDTH = 80;
    private static final int PD_WIDTH = 110;
    private static final int PI_HEIGHT = 110;
    private static final int PJ_HEIGHT = 150;
    private static final int PR_HEIGHT = 110;
    private static final int PA_HEIGHT = 110;
    private static final int PD_HEIGHT = 110;

    private static final int PLAYER_HEALTH = 20;

    public Player(PlayScreen screen) {

        this.world = screen.getWorld();

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        // Player Animations
        TextureAtlas attackAtlas = new TextureAtlas("CharactersFiles/Player_Attacking.pack");
        TextureAtlas jumpingAtlas = new TextureAtlas("CharactersFiles/Player_Jump.pack");
        TextureAtlas runningAtlas = new TextureAtlas("CharactersFiles/Player_Run.pack");
        TextureAtlas dyingAtlas = new TextureAtlas("CharactersFiles/Player_Dying.pack");
        TextureAtlas idleAtlas = new TextureAtlas("CharactersFiles/Player_Idle.pack");

        // Player Idle/Standing Animation
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i=0; i<8; i++)
            frames.add(new TextureRegion(idleAtlas.findRegion("1_terrorist_1_Idle"), i*PI_WIDTH, 0, PI_WIDTH, PI_HEIGHT));
        playerIdle = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        // Player Jump Animation
        for(int i=0; i<8; i++)
            frames.add(new TextureRegion(jumpingAtlas.findRegion("1_terrorist_1_Jump"), i*PJ_WIDTH, 0, PJ_WIDTH, PJ_HEIGHT));
        playerJump = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        // Player Run Animation
        for(int i=0; i<6; i++)
            frames.add(new TextureRegion(runningAtlas.findRegion("1_terrorist_1_Run"), i*PR_WIDTH, 0, PR_WIDTH, PR_HEIGHT));
        playerRun = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        // Player Attack Animation
        for(int i=0; i<6; i++)
            frames.add(new TextureRegion(attackAtlas.findRegion("1_terrorist_1_Attack1"), i*PA_WIDTH, 0, PA_WIDTH, PA_HEIGHT));
        playerAttack = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        // Player Dying Animation
        for(int i=0; i<9; i++)
            frames.add(new TextureRegion(dyingAtlas.findRegion("1_terrorist_1_Hurt"), i*PD_WIDTH, 0, PD_WIDTH, PD_HEIGHT));
        dyingAnimation = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        // Player Shooting
        //playerShooting = new TextureRegion(new Texture("CharactersFiles/Player_Attack.png"));

        definePlayer();
        setBounds(0, 0, PI_WIDTH / Main.PPM, PI_HEIGHT / Main.PPM);

        bulletHitCount = 0;
        firingDelay = 0;
        attackAnimationDelay = 1;
        setToDestroy = false;
        destroyed = false;
        shooting = false;

        // Create bullets
        bullets = new ArrayList<Bullets>();
    }

    public void update(float dt) {

        if (setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
            stateTimer = 0;
        }
        if (!destroyed)
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - 0.5f);

        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && firingDelay > 0.5f) {
            attackAnimationDelay = 0;
            firingDelay = 0;
            shooting = true;
        }
        if(shooting) attackAnimationDelay += dt;

        setRegion(getFrame(dt));

        // Bullets update
        ArrayList<Bullets> bulletToRemove = new ArrayList<Bullets>();
        for (Bullets bullet : bullets) {
            bullet.update(dt);
            if (bullet.remove) {
                bulletToRemove.add(bullet);
            }
        }

        bullets.removeAll(bulletToRemove);
        bulletToRemove.clear();
        firingDelay += dt;
    }

    private TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case JUMPING:
                region = (TextureRegion) playerJump.getKeyFrame(stateTimer);
                setBounds(getX(), getY(), PJ_WIDTH / Main.PPM, PJ_HEIGHT / Main.PPM);
                PlayScreen.canJump = false;
                break;
            case RUNNING:
                region = (TextureRegion) playerRun.getKeyFrame(stateTimer, true);
                setBounds(getX(), getY(), PR_WIDTH / Main.PPM, PR_HEIGHT / Main.PPM);
                /*region = (TextureRegion) playerAttack.getKeyFrame(stateTimer, true);
                setBounds(getX(), getY(), PA_WIDTH / Main.PPM, PA_HEIGHT / Main.PPM);*/
                PlayScreen.canJump = true;
                break;
            case SHOOTING:
                //region = playerShooting;
                region = (TextureRegion) playerAttack.getKeyFrame(stateTimer);
                setBounds(getX(), getY(), PA_WIDTH / Main.PPM, PA_HEIGHT / Main.PPM);
                PlayScreen.canJump = false;
                break;
            case DYING:
                region = (TextureRegion) dyingAnimation.getKeyFrame(stateTimer);
                setBounds(getX(), getY(), PD_WIDTH / Main.PPM, PD_HEIGHT / Main.PPM);
                PlayScreen.canJump = false;
                break;
            case FALLING:
            case STANDING:
            default:
                region = (TextureRegion) playerIdle.getKeyFrame(stateTimer, true);
                setBounds(getX(), getY(), PI_WIDTH / Main.PPM, PI_HEIGHT / Main.PPM);
                PlayScreen.canJump = true;
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
        //if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && firingDelay%41 == 0)
            //return State.SHOOTING;
        if(destroyed && attackAnimationDelay > 0.5f) {
            //attackAnimationDelay = 0;
            shooting = false;
            return State.DYING;
        }
        else if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING) && attackAnimationDelay > 0.5f) {
            //attackAnimationDelay = 0;
            shooting = false;
            return State.JUMPING;
        }
        else if(b2body.getLinearVelocity().y < 0 && attackAnimationDelay > 0.5f) {
            //attackAnimationDelay = 0;
            shooting = false;
            return State.FALLING;
        }
        else if(b2body.getLinearVelocity().x != 0 && attackAnimationDelay > 0.5f) {
            //attackAnimationDelay = 0;
            shooting = false;
            return State.RUNNING;
        }
        else if(attackAnimationDelay > 0.5f) {
            //attackAnimationDelay = 0;
            shooting = false;
            return State.STANDING;
        }
        else
            return State.SHOOTING;
    }

    private void definePlayer(){
        BodyDef  bdef = new BodyDef();
        bdef.position.set(224 / Main.PPM, 254 / Main.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        //FixtureDef sfdef = new FixtureDef();

        // Player Shape
        /*CircleShape shape = new CircleShape();
        shape.setRadius(10 / Main.PPM);*/
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(15 / Main.PPM,40 / Main.PPM);

        fdef.restitution = -3;
        fdef.filter.categoryBits = Main.PLAYER_BIT;

        // Which objects can collide with this object
        fdef.filter.maskBits = Main.GROUND_BIT | Main.OBJECT_BIT | Main.TRAP_BIT | Main.PLAYER_DETECTION_BIT | Main.ENEMY_BULLET_BIT | Main.ENEMY_BIT | Main.TREASURE_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        // Right Side Line to detect thing as sensor
        /*EdgeShape Rside = new EdgeShape();
        Rside.set(new Vector2(-25, 50).scl(1/Main.PPM), new Vector2(-25, -45).scl(1/Main.PPM));
        sfdef.shape = Rside;
        sfdef.isSensor = true;
        b2body.createFixture(sfdef).setUserData("Rside");

        // Left Side Line to detect thing as sensor
        EdgeShape Lside = new EdgeShape();
        Lside.set(new Vector2(25, 50).scl(1/Main.PPM), new Vector2(25, -45).scl(1/Main.PPM));
        sfdef.shape = Lside;
        sfdef.isSensor = true;
        b2body.createFixture(sfdef).setUserData("Lside");*/
    }

    @Override
    public void draw(Batch batch){
        if(!destroyed || stateTimer < 3)
            super.draw(batch);
    }

    public void playerBulletHit() {
        bulletHitCount++;
        Main.manager.get("Audio/SoundEffects/Hurt.wav", Sound.class).play();
        if(bulletHitCount >= PLAYER_HEALTH) {
            setToDestroy = true;
        }
    }

    public int heathStatus() {
        return PLAYER_HEALTH - bulletHitCount;
    }

    public float getFiringDelay() {
        return firingDelay;
    }
}

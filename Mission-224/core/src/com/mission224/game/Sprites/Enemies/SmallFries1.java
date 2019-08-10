package com.mission224.game.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mission224.game.Main;
import com.mission224.game.scenes.Hud;
import com.mission224.game.screens.PlayScreen;
import com.mission224.game.tools.EnemyBullets;

import java.util.ArrayList;

public class SmallFries1 extends Enemy {

    public enum State { WALKING, ATTACKING, DYING }
    public static boolean detect;
    public static boolean playerEnemyCollision;
    private State previousState;
    private float stateTime;
    private float resetDetect;
    private Animation walkAnimation;
    private Animation attackAnimation;
    private Animation dyingAnimation;
    private boolean facingRight;

    // Enemy Bullets
    private ArrayList<EnemyBullets> bullets;
    private int bulletHitCount;
    private boolean setToDestroy;
    private boolean destroyed;
    private int firingDelay;

    // Width, Height
    private static final short WALKING_WIDTH = 60;
    private static final short WALKING_HEIGHT = 104;
    private static final short ATTACKING_WIDTH = 86;
    private static final short ATTACKING_HEIGHT = 104;
    private static final short DYING_WIDTH = 104;
    private static final short DYING_HEIGHT = 115;

    public SmallFries1(PlayScreen screen, float x, float y) {

        super(screen, x, y);
        // Adding all animations
        TextureAtlas walkAtlas = new TextureAtlas("CharactersFiles/Enemy_Walk.pack");
        TextureAtlas attackAtlas = new TextureAtlas("CharactersFiles/Enemy_Attacking.pack");
        TextureAtlas dyingAtlas = new TextureAtlas("CharactersFiles/Enemy_Dying.pack");

        // Walk
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i=0; i<8; i++)
            frames.add(new TextureRegion(walkAtlas.findRegion("3_police_Walk"), i* WALKING_WIDTH, 0, WALKING_WIDTH, WALKING_HEIGHT));
        walkAnimation = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        // Attack
        for(int i=0; i<6; i++)
            frames.add(new TextureRegion(attackAtlas.findRegion("3_police_attack_Attack"), i*ATTACKING_WIDTH, 0, ATTACKING_WIDTH, ATTACKING_HEIGHT));
        attackAnimation = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        // Dying
        for(int i=0; i<9; i++)
            frames.add(new TextureRegion(dyingAtlas.findRegion("3_police_Hurt"), i*DYING_WIDTH, 0, DYING_WIDTH, DYING_HEIGHT));
        dyingAnimation = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        stateTime = 0;
        bulletHitCount = 0;
        firingDelay = 0;
        resetDetect = 0;
        facingRight = true;
        setToDestroy = false;
        destroyed = false;
        playerEnemyCollision = false;
        detect = false;

        setBounds(getX(), getY(), WALKING_WIDTH / Main.PPM, WALKING_HEIGHT / Main.PPM);

        // Create bullets
        bullets = new ArrayList<EnemyBullets>();
    }

    public void update(float dt) {

        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            stateTime = 0;
        }
        else if(!destroyed) {
            if(getState() == State.ATTACKING) {
                if (!facingRight)
                    setPosition(b2body.getPosition().x - (getWidth() / 2 + 0.25f), b2body.getPosition().y - 0.45f);
                else setPosition(b2body.getPosition().x - (getWidth() / 2 - 0.25f), b2body.getPosition().y - 0.45f);
            }
            else setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - 0.45f);
            b2body.setLinearVelocity(velocity);

            // For Bullets
            if(detect && firingDelay%127 == 0){
                float bulletX = b2body.getPosition().x ;
                float bulletY = b2body.getPosition().y;
                bullets.add(new EnemyBullets(screen, bulletX, bulletY, facingRight));
            }
        }
        setRegion(getFrame(dt));

        // Bullets update
        ArrayList<EnemyBullets> bulletToRemove = new ArrayList<EnemyBullets>();
        for(EnemyBullets bullet : bullets){
            bullet.update(dt);
            if(bullet.remove){
                bulletToRemove.add(bullet);
            }
        }
        bullets.removeAll(bulletToRemove);
        firingDelay++;

        // Resetting the detection of player
        if(detect && !playerEnemyCollision) {
            resetDetect += dt;
            if(resetDetect > 3) {
                resetDetect = 0;
                detect = false;
            }
        }
        else resetDetect = 0;
    }

    private State getState() {
        if(destroyed)
            return State.DYING;
        else if(detect)
            return State.ATTACKING;
        else
            return State.WALKING;
    }

    private TextureRegion getFrame(float dt){

        State currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case ATTACKING:
                region = (TextureRegion) attackAnimation.getKeyFrame(stateTime, true);
                setBounds(getX(), getY(), ATTACKING_WIDTH / Main.PPM, ATTACKING_HEIGHT / Main.PPM);
                break;
            case DYING:
                region = (TextureRegion) dyingAnimation.getKeyFrame(stateTime);
                setBounds(getX(), getY(), DYING_WIDTH / Main.PPM, DYING_HEIGHT / Main.PPM);
                break;
            default:
                region = (TextureRegion) walkAnimation.getKeyFrame(stateTime, true);
                setBounds(getX(), getY(), WALKING_WIDTH / Main.PPM, WALKING_HEIGHT / Main.PPM);
                break;
        }

        if((b2body.getLinearVelocity().x < 0 || !facingRight) && !region.isFlipX()) {
            region.flip(true, false);
            facingRight = false;
        }

        if((b2body.getLinearVelocity().x > 0 || facingRight) && region.isFlipX()) {
            region.flip(true, false);
            facingRight = true;
        }

        stateTime = currentState == previousState ? stateTime + dt : 0;
        previousState = currentState;
        return region;
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        FixtureDef sdef = new FixtureDef();

        // SmallFies shape
        /*CircleShape shape = new CircleShape();
        shape.setRadius(30 / Main.PPM);*/
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(10 / Main.PPM,40 / Main.PPM);

        fdef.restitution = -3;
        fdef.filter.categoryBits = Main.ENEMY_BIT;

        // Which objects can collide with this object
        fdef.filter.maskBits = Main.GROUND_BIT | Main.ENEMY_BIT | Main.OBJECT_BIT | Main.PLAYER_BIT | Main.BULLET_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        // Adding detection meter
        EdgeShape detectionLine = new EdgeShape();
        detectionLine.set(new Vector2(80, 25).scl(1/Main.PPM), new Vector2(-80, 25).scl(1/Main.PPM));
        sdef.shape = detectionLine;
        sdef.filter.categoryBits = Main.PLAYER_DETECTION_BIT;
        sdef.isSensor = true;
        b2body.createFixture(sdef).setUserData("EnemyDetectionLine");
    }

    @Override
    public boolean dead() {
        return destroyed;
    }

    @Override
    public boolean isDetect() {
        return detect;
    }

    public void draw(Batch batch) {
        if(!destroyed || stateTime < 3)
            super.draw(batch);

        // Bullet rendering
        for (EnemyBullets bullet : bullets){
            bullet.draw(batch);
        }
    }

    @Override
    public void enemyBulletHit() {
        bulletHitCount++;
        if(bulletHitCount > 1) {
            setToDestroy = true;
            Hud.deadEnemies++;
        }
    }
}

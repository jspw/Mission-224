package com.mission224.game.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mission224.game.Main;
import com.mission224.game.Screens.PlayScreen;

import java.awt.*;

public class SmallFries1 extends Enemy {

    public enum State { WALKING, ATTACKING, DYING };
    public State currentState;
    public State previousState;
    private float stateTime;
    private Animation walkAnimation;
    private Animation attackAnimation;
    private Animation dyingAnimation;
    private Array<TextureRegion> frames;
    private TextureAtlas walkAtlas;
    private TextureAtlas attackAtlas;
    private TextureAtlas dyingAtlas;
    private boolean facingRight;

    private int bulletHitCount;
    public boolean setToDestroy, destroyed;

    public static boolean ditect = false;

    private static final short WIDTH = 50;
    private static final short HEIGHT = 87;

    public SmallFries1(PlayScreen screen, float x, float y) {

        super(screen, x, y);
        // Adding all animations
        walkAtlas = new TextureAtlas("CharactersFiles/Enemy_Walk.pack");
        attackAtlas = new TextureAtlas("CharactersFiles/Enemy_Attacking.pack");
        dyingAtlas = new TextureAtlas("CharactersFiles/Enemy_Dying.pack");

        // Walk
        frames = new Array<TextureRegion>();
        for(int i=0; i<8; i++)
            frames.add(new TextureRegion(walkAtlas.findRegion("3_police_Walk"), i*WIDTH, 0, WIDTH, HEIGHT));
        walkAnimation = new Animation(0.1f, frames);
        frames.clear();

        // Attack
        for(int i=0; i<6; i++)
            frames.add(new TextureRegion(attackAtlas.findRegion("3_police_attack_Attack"), i*100, 0, 100, 121));
        attackAnimation = new Animation(0.1f, frames);
        frames.clear();

        // Dying
        for(int i=0; i<9; i++)
            frames.add(new TextureRegion(dyingAtlas.findRegion("3_police_Hurt"), i*200, 0, 200, 222));
        dyingAnimation = new Animation(0.1f, frames);
        frames.clear();

        stateTime = 0;
        bulletHitCount = 0;
        facingRight = true;
        setToDestroy = false;
        destroyed = false;

        setBounds(getX(), getY(), WIDTH / Main.PPM, HEIGHT / Main.PPM);
    }

    public void update(float dt) {

        if(!destroyed) stateTime += dt;
        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            stateTime = 0;
        }

        else if(!destroyed) {
            if(facingRight)
                setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - 0.5f);
            else
                setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - 0.5f);
            b2body.setLinearVelocity(velocity);
        }
        setRegion(getFrame(dt));
    }

    private State getState() {
        if(destroyed)
            return State.DYING;
        else if(ditect)
            return State.ATTACKING;
        else
            return State.WALKING;
    }

    private TextureRegion getFrame(float dt){

        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case ATTACKING:
                region = (TextureRegion) attackAnimation.getKeyFrame(stateTime, true);
                break;
            case DYING:
                region = (TextureRegion) dyingAnimation.getKeyFrame(stateTime);
                break;
            default:
                region = (TextureRegion) walkAnimation.getKeyFrame(stateTime, true);
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

        /*TextureRegion region = (TextureRegion) walkAnimation.getKeyFrame(stateTime, true);

        if((b2body.getLinearVelocity().x < 0 || !facingRight) && !region.isFlipX()) {
            region.flip(true, false);
            facingRight = false;
        }

        if((b2body.getLinearVelocity().x > 0 || facingRight) && region.isFlipX()) {
            region.flip(true, false);
            facingRight = true;
        }

        return region;*/
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();

        // Player Circle
        /*CircleShape shape = new CircleShape();
        shape.setRadius(30 / Main.PPM);*/
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(15 / Main.PPM,40 / Main.PPM);

        fdef.restitution = -3;
        fdef.filter.categoryBits = Main.ENEMY_BIT;

        // Which objects can collide with this object
        fdef.filter.maskBits = Main.GROUND_BIT | Main.ENEMY_BIT | Main.OBJECT_BIT | Main.PLAYER_BIT | Main.BULLET_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        /*// Adding detection meter
        EdgeShape detectionLine = new EdgeShape();
        detectionLine.set(new Vector2(25, 70).scl(1/Main.PPM), new Vector2(25, -15).scl(1/Main.PPM));
        fdef.shape = detectionLine;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("EnemyDetectionLine");*/
    }

    public void draw(Batch batch) {
        if(!destroyed || stateTime < 3)
            super.draw(batch);

        /*// bullet rendering
        for (EnemyBullet bullet : bullets){
            bullet.draw(batch);
        }*/
    }

    @Override
    public void enemyBulletHit() {
        bulletHitCount++;
        if(bulletHitCount > 5) {
            setToDestroy = true;
        }
    }
}

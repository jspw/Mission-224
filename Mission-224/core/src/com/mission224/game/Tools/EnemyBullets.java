package com.mission224.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.mission224.game.Main;
import com.mission224.game.screens.PlayScreen;

public class EnemyBullets  extends Sprite implements Disposable {

    private static Texture bulletTexture;
    private TextureRegion bulletRegion;
    private boolean setToDestroy;
    private boolean destroyed;
    private float stateTime;

    // Sprite looking at right or left
    private boolean right;

    private Body b2body;
    private World world;
    public float x, y;

    public boolean remove;

    public EnemyBullets (PlayScreen screen , float x, float y, boolean fireRight){

        this.x = x + 0.45f;
        this.y = y + 0.05f;
        right = fireRight;

        this.world = screen.getWorld();

        bulletTexture = new Texture("Extras/bullet.png");

        if(fireRight)
            bulletRegion = new TextureRegion(bulletTexture);
        else {
            bulletRegion = new TextureRegion(bulletTexture);
            bulletRegion.flip(true,false);
            this.x -= 0.60f;
        }

        stateTime = 0;

        defineBulletBody();

        setBounds(0,0,.4f,.3f);
        setRegion(bulletRegion);

        setToDestroy = false;
        destroyed = false;
        remove = false;
    }

    public void update(float dt){

        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            stateTime = 0;
        }
        else if(!destroyed){
            // Box2d Body for bullet update
            setPosition(b2body.getPosition().x - getWidth() / 1.6f, b2body.getPosition().y - getHeight() / 2.2f);
            setRegion(bulletRegion);

            if(right)
                b2body.applyLinearImpulse(new Vector2(0.3f, 0), b2body.getWorldCenter(), true);
            else
                b2body.applyLinearImpulse(new Vector2(-0.3f, 0), b2body.getWorldCenter(), true);
        }

        if(x > Gdx.graphics.getWidth() || x < 0)
            remove = true;
    }

    private void defineBulletBody(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(x, y);
        bdef.bullet = true;
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.gravityScale = 0;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(10 / Main.PPM);

        fdef.shape = shape;
        fdef.restitution = -3;
        fdef.friction = 0;

        fdef.filter.categoryBits = Main.ENEMY_BULLET_BIT;
        fdef.filter.maskBits = Main.PLAYER_BIT | Main.OBJECT_BIT;

        b2body.createFixture(fdef).setUserData(this);
    }

    public void draw(Batch batch){
        if(!destroyed || stateTime < 1)
            super.draw(batch);
    }

    public void hitObject(){
        setToDestroy = true;
    }

    public void dispose(){
        bulletTexture.dispose();
    }
}

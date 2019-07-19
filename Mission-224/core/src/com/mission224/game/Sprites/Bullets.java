package com.mission224.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mission224.game.Main;
import com.mission224.game.Screens.PlayScreen;

public class Bullets  extends Sprite {

    public static Texture bulletTexture;
    public TextureRegion bulleRegion;
    public boolean setToDestroy;
    public boolean destroyed;
    public float stateTime;

    // Sprite looking at right or left
    private boolean right;

    public Body b2body;
    public World world;
    //public PlayScreen screen;

    public float x,y;

    public boolean remove =  false;

    public Bullets (PlayScreen screen , float x, float y, boolean fireRight){
        this.x = x;
        this.y = y + .25f;
        right = fireRight;

        //this.screen = screen;
        this.world = screen.getWorld();



        bulletTexture = new Texture("fire_blue.png");

        if(fireRight)
            bulleRegion = new TextureRegion(bulletTexture);
        else {
            bulleRegion = new TextureRegion(bulletTexture);
            bulleRegion.flip(true,false);
        }

        stateTime = 0;

        defineBulletBody();
        
        setBounds(0,0,0.8f,0.5f);
        setRegion(bulleRegion);

        setToDestroy = false;
        destroyed = false;
    }

    public void update(float dt){


        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            //setRegion(new Texture("EmptyBullet.png"));
            stateTime = 0;
        }

        // ** Changed from if to else if **
         else if(!destroyed){

            ///box2d Body for bullet update
            setPosition(b2body.getPosition().x - getWidth() / 1.6f , b2body.getPosition().y - getHeight() / 2.2f);
            setRegion(bulleRegion);

            if(Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                if(right){
                    b2body.applyLinearImpulse(new Vector2(5, 0), b2body.getWorldCenter(), true);
                 //   System.out.println(b2body.getWorldCenter());
                }

                else
                    b2body.applyLinearImpulse(new Vector2(-5, 0), b2body.getWorldCenter(), true);
            }
        }

        if(x > Gdx.graphics.getWidth() || x < 0)
            remove = true;
    }


    /*public void render (SpriteBatch batch){
        batch.draw(bulletTexture,x,y,0.8f,0.5f);
    }*/

    public void defineBulletBody(){
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

        fdef.filter.categoryBits = Main.BULLET_BIT;
        fdef.filter.maskBits = Main.PLAYER_BIT | Main.OBJECT_BIT | Main.ENEMY_BIT;

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

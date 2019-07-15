package com.mission224.game.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mission224.game.Main;

public class Ground {

    protected TiledMap map;
    protected Body body;


    public Ground(World world, Rectangle bounds) {

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth()/2) / Main.PPM, (bounds.getY() + bounds.getHeight()/2) / Main.PPM);

        body = world.createBody(bdef);

        shape.setAsBox((bounds.getWidth()/2) / Main.PPM, (bounds.getHeight()/2) / Main.PPM);
        fdef.shape = shape;
        body.createFixture(fdef);
        shape.dispose();
    }
}

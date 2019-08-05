package com.mission224.game.Sprites.TileObjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mission224.game.Main;
import com.mission224.game.Screens.PlayScreen;

public class Ground {

    public Ground(PlayScreen screen, Rectangle bounds) {
        // Initializing
        World world = screen.getWorld();

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth()/2) / Main.PPM, (bounds.getY() + bounds.getHeight()/2) / Main.PPM);

        Body body = world.createBody(bdef);

        shape.setAsBox((bounds.getWidth()/2) / Main.PPM, (bounds.getHeight()/2) / Main.PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = Main.GROUND_BIT;
        body.createFixture(fdef);
        shape.dispose();
    }
}

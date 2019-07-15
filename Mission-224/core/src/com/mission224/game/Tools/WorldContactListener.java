package com.mission224.game.Tools;

import com.badlogic.gdx.physics.box2d.*;
import com.mission224.game.Sprites.InteractiveTileObject;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(fixA.getUserData() == "Rside" || fixA.getUserData() == "Lside" || fixB.getUserData() == "Rside" || fixB.getUserData() == "Lside") {
            Fixture side;
            if(fixA.getUserData() == "Rside"  || fixA.getUserData() == "Lside") side = fixA;
            else side = fixB;
            Fixture object = side == fixA ? fixB : fixA;

            if(object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())) {
                ((InteractiveTileObject) object.getUserData()).onSideHit();
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}

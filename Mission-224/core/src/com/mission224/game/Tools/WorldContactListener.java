package com.mission224.game.Tools;

import com.badlogic.gdx.physics.box2d.*;
import com.mission224.game.Sprites.Bullets;
import com.mission224.game.Sprites.Enemies.Enemy;
import com.mission224.game.Sprites.TileObjects.InteractiveTileObject;
import com.mission224.game.Main;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        if(fixA.getUserData() == "Rside" || fixA.getUserData() == "Lside" || fixB.getUserData() == "Rside" || fixB.getUserData() == "Lside") {
            Fixture side;
            if(fixA.getUserData() == "Rside"  || fixA.getUserData() == "Lside") side = fixA;
            else side = fixB;
            Fixture object = side == fixA ? fixB : fixA;

            if(object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())) {
                ((InteractiveTileObject) object.getUserData()).onSideHit();
            }
        }

        switch (cDef) {

            case Main.ENEMY_BIT | Main.OBJECT_BIT :

                if(fixA.getFilterData().categoryBits == Main.ENEMY_BIT)
                    ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
                break;

            case Main.ENEMY_BIT | Main.BULLET_BIT :

                if(fixA.getFilterData().categoryBits == Main.BULLET_BIT){
                    ((Bullets) fixA.getUserData()).hitObject();
                    ((Bullets) fixA.getUserData()).remove = true;
                    ((Enemy) fixB.getUserData()).enemyBulletHit();
                }
                else {
                    ((Bullets) fixB.getUserData()).hitObject();
                    ((Bullets) fixB.getUserData()).remove = true;
                    ((Enemy) fixA.getUserData()).enemyBulletHit();
                }
                break;

            case Main.OBJECT_BIT | Main.BULLET_BIT :

                if(fixA.getFilterData().categoryBits == Main.BULLET_BIT) {
                    ((Bullets) fixA.getUserData()).remove = true;
                    ((Bullets) fixA.getUserData()).hitObject();
                }
                else {
                    ((Bullets) fixB.getUserData()).remove = true;
                    ((Bullets) fixB.getUserData()).hitObject();
                }
                break;

            case Main.ENEMY_BIT :

                ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
                break;
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

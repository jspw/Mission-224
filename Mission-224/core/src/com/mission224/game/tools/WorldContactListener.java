package com.mission224.game.tools;

import com.badlogic.gdx.physics.box2d.*;
import com.mission224.game.sprites.enemies.Enemy;
import com.mission224.game.sprites.enemies.SmallFries1;
import com.mission224.game.sprites.Player;
import com.mission224.game.Main;
import com.mission224.game.sprites.tileObjects.Traps;
import com.mission224.game.treasure.GiantChest;

import static com.mission224.game.sprites.enemies.SmallFries1.detect;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        /*if(fixA.getUserData() == "Rside" || fixA.getUserData() == "Lside" || fixB.getUserData() == "Rside" || fixB.getUserData() == "Lside") {
            Fixture side;
            if(fixA.getUserData() == "Rside"  || fixA.getUserData() == "Lside") side = fixA;
            else side = fixB;
            Fixture object = side == fixA ? fixB : fixA;

            if(object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())) {
                ((InteractiveTileObject) object.getUserData()).onSideHit();
            }
        }*/

        switch (cDef) {

            case Main.ENEMY_BIT | Main.OBJECT_BIT :

                if(fixA.getFilterData().categoryBits == Main.ENEMY_BIT) {
                    if (!detect)
                        ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                }
                else {
                    if (!detect)
                        ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                }
                break;

            case Main.ENEMY_BIT | Main.BULLET_BIT :

                if(fixA.getFilterData().categoryBits == Main.BULLET_BIT){
                    ((Bullets) fixA.getUserData()).hitObject();
                    ((Bullets) fixA.getUserData()).remove = true;
                    ((SmallFries1) fixB.getUserData()).enemyBulletHit();
                }
                else {
                    ((Bullets) fixB.getUserData()).hitObject();
                    ((Bullets) fixB.getUserData()).remove = true;
                    ((SmallFries1) fixA.getUserData()).enemyBulletHit();
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

            case Main.OBJECT_BIT | Main.ENEMY_BULLET_BIT :

                if(fixA.getFilterData().categoryBits == Main.ENEMY_BULLET_BIT) {
                    ((EnemyBullets) fixA.getUserData()).remove = true;
                    ((EnemyBullets) fixA.getUserData()).hitObject();
                }
                else {
                    ((EnemyBullets) fixB.getUserData()).remove = true;
                    ((EnemyBullets) fixB.getUserData()).hitObject();
                }
                break;

            case Main.ENEMY_BULLET_BIT | Main.PLAYER_BIT :

                if(fixA.getFilterData().categoryBits == Main.ENEMY_BULLET_BIT) {
                    ((EnemyBullets) fixA.getUserData()).hitObject();
                    ((EnemyBullets) fixA.getUserData()).remove = true;
                    ((Player) fixB.getUserData()).playerBulletHit();
                }
                else {
                    ((EnemyBullets) fixB.getUserData()).hitObject();
                    ((EnemyBullets) fixB.getUserData()).remove = true;
                    ((Player) fixA.getUserData()).playerBulletHit();
                }
                break;

            case Main.PLAYER_BIT | Main.TRAP_BIT :

                if(fixA.getFilterData().categoryBits == Main.PLAYER_BIT) {
                    ((Player) fixA.getUserData()).playerBulletHit();
                    ((Traps) fixB.getUserData()).onSideHit();
                }
                else {
                    ((Player) fixB.getUserData()).playerBulletHit();
                    ((Traps) fixA.getUserData()).onSideHit();
                }
                SmallFries1.playerEnemyCollision = true;
                break;

            case Main.PLAYER_BIT | Main.PLAYER_DETECTION_BIT :

                SmallFries1.playerEnemyCollision = true;
                detect = true;
                break;

            case Main.PLAYER_BIT | Main.TREASURE_BIT :

                if(fixA.getFilterData().categoryBits == Main.TREASURE_BIT) {
                    ((GiantChest) fixA.getUserData()).onSideHit();
                }
                else {
                    ((GiantChest) fixB.getUserData()).onSideHit();
                }
                break;

            case Main.ENEMY_BIT :

                ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
                break;

            case Main.PLAYER_BIT | Main.GROUND_BIT :
            case Main.PLAYER_BIT | Main.OBJECT_BIT :
                SmallFries1.playerEnemyCollision = false;
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

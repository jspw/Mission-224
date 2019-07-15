package com.mission224.game.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mission224.game.Sprites.Ground;
import com.mission224.game.Sprites.Tire;
import com.mission224.game.Sprites.Traps;
import com.mission224.game.Sprites.WaterPump;

public class B2WorldCreator {

    public B2WorldCreator(World world, TiledMap map) {

        // Creating Traps
        for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Traps(world, map, rect);
        }

        // Creating Ground bodies & it's fixtures
        for(MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Ground(world, rect);
        }

        // Creating Wheels bodies & it's fixtures (Polygon)
        for(MapObject object : map.getLayers().get(8).getObjects().getByType(PolygonMapObject.class)) {
            new Tire(world, map, object);
        }

        // Creating WaterPump bodies & it's fixtures
        for(MapObject object : map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new WaterPump(world, map, rect);
        }
    }
}

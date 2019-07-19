package com.mission224.game.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mission224.game.Main;
import com.mission224.game.Screens.PlayScreen;
import com.mission224.game.Sprites.Enemies.SmallFries1;
import com.mission224.game.Sprites.TileObjects.*;

public class B2WorldCreator {

    // SmallFries Arrays
    private Array<SmallFries1> smallFries1Array;

    public Array<SmallFries1> getSmallFries1Array() {
        return smallFries1Array;
    }

    public B2WorldCreator(PlayScreen screen) {

        // Intializing
        TiledMap map = screen.getMap();

        // Creating Traps
        for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Traps(screen, rect);
        }

        // Creating Ground bodies & it's fixtures
        for(MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Ground(screen, rect);
        }

        // Creating Wheels bodies & it's fixtures (Polygon)
        for(MapObject object : map.getLayers().get(8).getObjects().getByType(PolygonMapObject.class)) {
            new Tire(screen, object);
        }

        // Creating WaterPump bodies & it's fixtures
        for(MapObject object : map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new WaterPump(screen, rect);
        }

        // Enemy detection Area
        for(MapObject object : map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new EnemyDitactionArea(screen, rect);
        }

        // Enemy Area added
        smallFries1Array = new Array<SmallFries1>();
        for(MapObject object : map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            smallFries1Array.add(new SmallFries1(screen, rect.getX() / Main.PPM, rect.getY() / Main.PPM));
        }
    }
}

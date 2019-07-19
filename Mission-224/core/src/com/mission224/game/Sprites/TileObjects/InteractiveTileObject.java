// For detecting rectangle shape in map for collision

package com.mission224.game.Sprites.TileObjects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mission224.game.Main;
import com.mission224.game.Screens.PlayScreen;

public abstract class InteractiveTileObject {

    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;

    private ChainShape shape;
    private BodyDef bdef;
    private FixtureDef fdef;

    public InteractiveTileObject(PlayScreen screen, Rectangle bounds) {
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = bounds;

        bdef = new BodyDef();
        fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth()/2) / Main.PPM, (bounds.getY() + bounds.getHeight()/2) / Main.PPM);

        body = world.createBody(bdef);

        shape.setAsBox((bounds.getWidth()/2) / Main.PPM, (bounds.getHeight()/2) / Main.PPM);
        fdef.shape = shape;
        fixture = body.createFixture(fdef);
        shape.dispose();
    }

    public InteractiveTileObject(PlayScreen screen, MapObject object) {
        // Intializing
        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        fdef = new FixtureDef();
        bdef = new BodyDef();

        bdef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bdef);

        shape = createPolyline((PolygonMapObject) object);
        fdef.shape = shape;
        fdef.restitution = 1;
        fixture = body.createFixture(fdef);
        shape.dispose();
    }

    private static ChainShape createPolyline(PolygonMapObject polyline) {
        float[] vertices = polyline.getPolygon().getTransformedVertices();
        Vector2[] worldvertices = new Vector2[vertices.length/2];
        for(int i=0; i<worldvertices.length; i++)
            worldvertices[i] = new Vector2(vertices[i*2] / Main.PPM, vertices[i*2+1] / Main.PPM);
        ChainShape chainShape = new ChainShape();
        chainShape.createChain(worldvertices);

        return chainShape;
    }

    public abstract void onSideHit();
    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell getCell(int layerPositionInMap) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerPositionInMap);
        return layer.getCell((int)(body.getPosition().x * Main.PPM / 16), (int)(body.getPosition().y * Main.PPM / 16));
    }

    public FixtureDef getFixtureDef() {
        return fdef;
    }
}

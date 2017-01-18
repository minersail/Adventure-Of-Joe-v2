package woohoo.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import woohoo.screens.PlayingScreen;
import woohoo.screens.PlayingScreen.WBodyType;

public class HexMapLoader
{
	PlayingScreen screen;
	public HexMapLoader(PlayingScreen scr)
	{
		screen = scr;
	}
	
	public MapLayers load(String filename, Texture tileset, Texture decorationTileset, World world)
	{		
		FileHandle mapHandle = Gdx.files.internal(filename);
		String map = mapHandle.readString();

		String[] rows = map.split("\n");
		int mapWidth = rows[0].length() / 9;
		int mapHeight = rows.length;		
		
		TiledMapTileLayer layer1 = new TiledMapTileLayer(mapWidth, mapHeight, 16, 16);
		TiledMapTileLayer layer2 = new TiledMapTileLayer(mapWidth, mapHeight, 16, 16);
        
		int i = 0;
		int j = 0;
		for (String row : rows)
		{
			String[] tiles = row.split(" ");
			for (String tile : tiles)
			{
                int decorRot = Integer.parseInt(tile.substring(1, 2), 16);
                int decorID = Integer.parseInt(tile.substring(2, 4), 16);
				int funcID = Integer.parseInt(tile.substring(4, 6), 16);
				int tileID = Integer.parseInt(tile.substring(6, 8), 16);
				int tileWidth = ((PlayingScreen)screen).T_TILE_WIDTH;
				int tileHeight = ((PlayingScreen)screen).T_TILE_HEIGHT;

				int columns = tileset.getWidth() / tileWidth;
				int tileX = (tileID % columns) * tileWidth;
				int tileY = (tileID / columns) * tileHeight;                
                
				int columns2 = decorationTileset.getWidth() / tileWidth;
				int tileX2 = (decorID % columns2) * tileWidth;
				int tileY2 = (decorID / columns2) * tileHeight;

				TextureRegion texture = new TextureRegion(tileset, tileX, tileY, 
														  tileWidth, tileHeight);
				texture.flip(false, true);
				
				StaticTiledMapTile t = new StaticTiledMapTile(texture);
				t.setId(Integer.parseInt(tile.substring(4, 8), 16));
				t.getProperties().put("isWall", funcID >= 4 && funcID <= 7); // funcIDs between 4 and 7 represent walls
                				
				if (t.getProperties().get("isWall", Boolean.class))
				{					
					BodyDef bodyDef = new BodyDef();
					bodyDef.type = BodyDef.BodyType.StaticBody;
					bodyDef.position.set(i + 0.5f, j + 0.5f);

					Body body = world.createBody(bodyDef);

					PolygonShape shape = new PolygonShape();
					shape.setAsBox(0.5f, 0.5f);

					FixtureDef fixtureDef = new FixtureDef();
					fixtureDef.shape = shape;
					fixtureDef.density = 1f;
					fixtureDef.friction = 0f;

					body.createFixture(fixtureDef);
					body.setUserData(WBodyType.Wall);
				}
					
				Cell cell = new Cell();
				cell.setTile(t);
				cell.setRotation(funcID % 4);
				layer1.setCell(i, j, cell);                
                
                if (decorID != 0)
                {
                    TextureRegion decoration = new TextureRegion(decorationTileset, tileX2, tileY2, 
                                                                 tileWidth, tileHeight);
                    decoration.flip(false, true);
                    
                    StaticTiledMapTile t2 = new StaticTiledMapTile(decoration);
                    t2.setId(Integer.parseInt(tile.substring(0, 4), 16));
                    
                    Cell cell2 = new Cell();
                    cell2.setTile(t2);
                    cell2.setRotation(decorRot % 4);
                    layer2.setCell(i, j, cell2);
                }
				
				i++;
			}
			j++;
			i = 0;
		}
		
		screen.mapWidth = mapWidth;
		screen.mapHeight = mapHeight;
        
        // Center screen        
        float extraX = (float)(screen.WORLD_WIDTH - mapWidth);
        float extraY = (float)(screen.WORLD_HEIGHT - mapHeight);
        screen.setCamera(screen.getCamera().viewportWidth / 2 - Math.max(0, extraX / 2), 
                         screen.getCamera().viewportHeight / 2 - Math.max(0, extraY / 2));
                
        MapLayers layers = new MapLayers();
		layer1.setName("Base");
		layer2.setName("Decorations");
        layers.add(layer1);
        layers.add(layer2);
		
		return layers;
	}
}
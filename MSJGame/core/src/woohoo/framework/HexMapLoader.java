package woohoo.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import woohoo.screens.PlayingScreen;

public class HexMapLoader
{
	Screen screen;
	public HexMapLoader(Screen scr)
	{
		screen = scr;
	}
	
	public TiledMapTileLayer load(String filename, Texture tileset)
	{		
		FileHandle mapHandle = Gdx.files.internal(filename);
		String map = mapHandle.readString();

		String[] rows = map.split("\n");
		int mapWidth = rows[0].length() / 5;
		int mapHeight = rows.length;		
		
		TiledMapTileLayer layer = new TiledMapTileLayer(mapWidth, mapHeight, 16, 16);
        
		int i = 0;
		int j = 0;
		for (String row : rows)
		{
			String[] tiles = row.split(" ");
			for (String tile : tiles)
			{
				int funcID = Integer.parseInt(tile.substring(0, 2), 16);
				int tileID = Integer.parseInt(tile.substring(2, 4), 16);
				int tileWidth = ((PlayingScreen)screen).T_TILE_WIDTH;
				int tileHeight = ((PlayingScreen)screen).T_TILE_HEIGHT;

				int columns = tileset.getWidth() / tileWidth;
				int tileX = (tileID % columns) * tileWidth;
				int tileY = (tileID / columns) * tileHeight;

				TextureRegion texture = new TextureRegion(tileset, tileX, tileY, 
														  tileWidth, tileHeight);
				texture.flip(false, true);
				
				StaticTiledMapTile t = new StaticTiledMapTile(texture);
				t.setId(Integer.parseInt(tile.substring(0, 4), 16));
				t.getProperties().put("isWall", funcID >= 4 && funcID <= 7); // funcIDs between 4 and 7 represent walls
					
				Cell cell = new Cell();
				cell.setTile(t);
				cell.setRotation(funcID % 4);
				layer.setCell(i, j, cell);
				
				i++;
			}
			j++;
			i = 0;
		}
		
		return layer;
	}
}
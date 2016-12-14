package woohoo.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import woohoo.msjgame.MSJGame;

public class HexMapLoader
{
	public TiledMapTileLayer load(String filename)
	{		
		FileHandle mapHandle = Gdx.files.internal(filename);
		String map = mapHandle.readString();

		String[] rows = map.split("\n");
		int mapWidth = rows[0].length() / 5;
		int mapHeight = rows.length;		
		
		TiledMapTileLayer layer = new TiledMapTileLayer(mapWidth, mapHeight, MSJGame.G_TILE_WIDTH, MSJGame.G_TILE_HEIGHT);

		int i = 0;
		int j = 0;
		for (String row : rows)
		{
			String[] tiles = row.split(" ");
			for (String tile : tiles)
			{
				int funcID = Integer.parseInt(tile.substring(0, 2), 16);
				int tileID = Integer.parseInt(tile.substring(2, 4), 16);

				int columns = AssetLoader.get("Tileset").getWidth() / MSJGame.T_TILE_WIDTH;
				int tileX = (tileID % columns) * MSJGame.T_TILE_WIDTH;
				int tileY = (tileID / columns) * MSJGame.T_TILE_HEIGHT;

				TextureRegion texture = new TextureRegion(AssetLoader.get("Tileset"), tileX, tileY, 
														  MSJGame.T_TILE_WIDTH, MSJGame.T_TILE_HEIGHT);
				texture.flip(false, true);
				
				StaticTiledMapTile t = new StaticTiledMapTile(texture);
				t.setId(Integer.parseInt(tile.substring(0, 4), 16));
				t.getProperties().put("isWall", funcID >= 4 && funcID <= 7); // funcIDs between 4 and 7 represent walls
				t.setOffsetX(MSJGame.G_TILE_WIDTH * i);
				t.setOffsetX(MSJGame.G_TILE_HEIGHT * j);
					
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
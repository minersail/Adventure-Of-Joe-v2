package woohoo.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import woohoo.framework.HexMapLoader;

/*
Anything that is drawn will be managed by this class
(Currently only the TileMap)
*/
public class GameRenderer
{    
    private static OrthographicCamera cam;
	private static FitViewport viewport;
    private static SpriteBatch batcher;
	
	private static HexMapLoader mapLoader;
	private static OrthogonalTiledMapRenderer renderer;
	private static TiledMap map;
	
	private static final int WORLD_WIDTH = 12; // Arbitrary unit; for this game 1 tile = 64 pixels = 1 meter
	private static final int WORLD_HEIGHT = 12; // Arbitrary unit; for this game 1 tile = 64 pixels = 1 meter
    
    public static void initialize()
    {
		float aspectRatio = (float)Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth();
		
        cam = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT * aspectRatio);
        cam.setToOrtho(true, WORLD_WIDTH, WORLD_HEIGHT * aspectRatio);
		cam.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT * aspectRatio / 2, 0);
		
		viewport = new FitViewport(120, 120 * aspectRatio, cam);
		viewport.apply();

        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);
		
		mapLoader = new HexMapLoader();		
		map = new TiledMap();
		map.getLayers().add(mapLoader.load("maps/test.txt"));
		
		renderer = new OrthogonalTiledMapRenderer(map);
    }
    
    public static void scrollCamera(float deltaX, float deltaY)
    {		
//        int leftBound = Gdx.graphics.getWidth() / 2;
//        int rightBound = TileMap.mapWidth * Tile.G_TILE_WIDTH - leftBound + 80;
//        int topBound = Gdx.graphics.getHeight() / 2;
//        int bottomBound = TileMap.mapHeight * Tile.G_TILE_HEIGHT - topBound + 80;
//
//        if (cam.position.x < rightBound  && deltaX > 0 ||		// Moving right
//                cam.position.x > leftBound   && deltaX < 0 ||		// Moving left
//                cam.position.y < bottomBound && deltaY > 0 ||		// Moving down
//                cam.position.y > topBound    && deltaY < 0)		// Moving up
//        {
//                cam.translate(deltaX, deltaY);
//                cam.update();
//                batcher.setProjectionMatrix(cam.combined);
//        }
        cam.translate(deltaX, deltaY);
        cam.update();
        batcher.setProjectionMatrix(cam.combined);
    }
    
    public static void setCamera(float deltaX, float deltaY)
    {
        cam.position.set(deltaX, deltaY, 0);
        cam.update();
        batcher.setProjectionMatrix(cam.combined);        
    }

    public static void render(float runTime)
    {
        cam.update();
        renderer.setView(cam);
        renderer.render();
//        TiledMapTileLayer layer = (TiledMapTileLayer)(map.getLayers().get(0));
//        
//        for (int i = 0; i < layer.getHeight(); i++)
//        {
//            for (int j = 0; j < layer.getWidth(); j++)
//                System.out.println(layer.getCell(i, j).getTile().toString());
//        }
    }
}

package woohoo.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import woohoo.framework.AssetLoader;
import woohoo.framework.CustomOrthogonalTiledMapRenderer;
import woohoo.framework.HexMapLoader;
import woohoo.gameobjects.Player;

/*
Anything that is drawn will be managed by this class
(Currently only the TileMap)
*/
public class GameRenderer
{    
    private static OrthographicCamera cam; // Manages aspect ratio, zoom, and position of camera
	private static FitViewport viewport; // Helper class for camera
    private static SpriteBatch batcher;
	
	private static HexMapLoader mapLoader;
	private static CustomOrthogonalTiledMapRenderer renderer;
	private static TiledMap map;
	
	private static final int WORLD_WIDTH = 16; // Arbitrary unit; how many tiles will fit width-wise on the screen
	private static final int WORLD_HEIGHT = 16; // Arbitrary unit; how many tiles will fit width-wise
    
    public static void initialize()
    {
		float aspectRatio = (float)Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth();
		
        cam = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT * aspectRatio);
        cam.setToOrtho(true, WORLD_WIDTH, WORLD_HEIGHT * aspectRatio);
		
		viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT * aspectRatio, cam);
		viewport.apply();
		cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);

        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);
		
		mapLoader = new HexMapLoader();		
		map = new TiledMap();
		map.getLayers().add(mapLoader.load("maps/test.txt"));
		
		Player player = new Player(new TextureRegion(AssetLoader.get("JoeFace")));
		MapLayer layer = new MapLayer();
		layer.getObjects().add(player);
		
		map.getLayers().add(layer);
		
		
		renderer = new CustomOrthogonalTiledMapRenderer(map, 1.0f / WORLD_WIDTH);
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
    
    public static void setCamera(float newX, float newY)
    {
        cam.position.set(newX, newY, 0);
        cam.update();
        batcher.setProjectionMatrix(cam.combined);        
    }

    public static void render(float runTime)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        cam.update();
        renderer.setView(cam);
        renderer.render();
    }
}

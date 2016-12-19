package woohoo.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import woohoo.framework.HexMapLoader;
import woohoo.framework.InputHandler;
import woohoo.gameobjects.Player;
import woohoo.gameobjects.components.MapObjectComponent;
import woohoo.gameworld.GameRenderer;
import woohoo.gameworld.GameWorld;

public class PlayingScreen implements Screen
{    
	/* Dimensions of tiles on the spritesheet */
    public final int T_TILE_WIDTH = 16;
    public final int T_TILE_HEIGHT = 16;
	
	private OrthographicCamera cam; // Manages aspect ratio, zoom, and position of camera
	private FitViewport viewport; // Helper class for camera
	
	private AssetManager assets;
	private InputHandler input;
	private HexMapLoader mapLoader;
	private GameRenderer renderer;
	private GameWorld world;
	private TiledMap map;
		
	private static final int WORLD_WIDTH = 16; // Arbitrary unit; how many tiles will fit width-wise on the screen
	private static final int WORLD_HEIGHT = 16; // Arbitrary unit; how many tiles will fit height-wise	
	
    private float runTime;

    // This is the constructor, not the class declaration
    public PlayingScreen(Game game)
    {
        float aspectRatio = (float)Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth();
		
        cam = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT * aspectRatio);
        cam.setToOrtho(true, WORLD_WIDTH, WORLD_HEIGHT * aspectRatio);
		
		viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT * aspectRatio, cam);
		viewport.apply();
		cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);
		
		loadAssets();
		input = new InputHandler(this);
		
		mapLoader = new HexMapLoader(this);
		map = new TiledMap();
		map.getLayers().add(mapLoader.load("maps/test.txt", (Texture)assets.get("images/tileset.png")));
		
		Player player = new Player((TextureAtlas)assets.get("images/oldman.pack"), 1, 1);
		MapLayer layer = new MapLayer();
		layer.getObjects().add(player.getComponent(MapObjectComponent.class));
		
		map.getLayers().add(layer);
		
		renderer = new GameRenderer(map, 1.0f / WORLD_WIDTH);
		world = new GameWorld(this);
		world.addEntity(player);

        Gdx.input.setInputProcessor(input);
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        cam.update();
        runTime += delta;
		
        world.update(delta);
        renderer.setView(cam);
        renderer.render();
    }
	
	public void loadAssets()
	{		
//		FileHandle data = new FileHandle("images/oldman.pack");
//		TextureAtlas atlas = new TextureAtlas(data, true);
		
		assets = new AssetManager();
		assets.load("images/oldman.pack", TextureAtlas.class);
		assets.load("images/tileset.png", Texture.class);
		assets.load("images/joeface.png", Texture.class);
		assets.finishLoading();
	}
	
	public void scrollCamera(float deltaX, float deltaY)
    {		
        cam.translate(deltaX, deltaY);
        cam.update();
    }
    
    public void setCamera(float newX, float newY)
    {
        cam.position.set(newX, newY, 0);
        cam.update();     
    }

    @Override
    public void resize(int width, int height)
    {
        System.out.println("GameScreen - resizing");
    }

    @Override
    public void show()
    {
        System.out.println("GameScreen - show called");
    }

    @Override
    public void hide()
    {
        System.out.println("GameScreen - hide called");
    }

    @Override
    public void pause()
    {
        System.out.println("GameScreen - pause called");
    }

    @Override
    public void resume()
    {
        System.out.println("GameScreen - resume called");
    }

    @Override
    public void dispose()
    {

    }
}

package woohoo.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader.TextureAtlasParameter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import woohoo.framework.HexMapLoader;
import woohoo.framework.InputHandler;
import woohoo.gameobjects.NPC;
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
	private Box2DDebugRenderer debugRenderer;
	
	private AssetManager assets;
	private InputHandler input;
	private HexMapLoader mapLoader;
	private GameRenderer renderer;
	private GameWorld engine;
	private TiledMap map;
	private World world;
		
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
		debugRenderer = new Box2DDebugRenderer();
		
		loadAssets();
		world = new World(new Vector2(0, 0), true);		
		
		mapLoader = new HexMapLoader(this);
		map = new TiledMap();
		MapLayers layers = mapLoader.load("maps/trees3.txt", (Texture)assets.get("images/tileset.png"), 
                                          (Texture)assets.get("images/tileset2.png"), world);
		
		renderer = new GameRenderer(map, 1.0f / WORLD_WIDTH);
		engine = new GameWorld(this, world);
        
		Player player = new Player((TextureAtlas)assets.get("images/oldman.pack"), 1, 1, world, engine);
        NPC npc = new NPC((Texture)assets.get("images/ginger.png"), 1, 1, world, engine);
        
		MapLayer objects = new MapLayer();
		objects.getObjects().add(player.getComponent(MapObjectComponent.class));
		objects.getObjects().add(npc.getComponent(MapObjectComponent.class));
		
        //map.getLayers().add(layers.get(0));
		map.getLayers().add(objects);
        //map.getLayers().add(layers.get(1));
		
		engine.addEntity(player);
        engine.addEntity(npc);

		input = new InputHandler(this, player);
        Gdx.input.setInputProcessor(input);
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        cam.update();
        runTime += delta;
		
		engine.update(delta);
		world.step(delta, 6, 2);
        renderer.setView(cam);
        renderer.render();
		debugRenderer.render(world, cam.combined);
    }
	
	public void loadAssets()
	{		
		TextureAtlasParameter atlasParam1 = new TextureAtlasParameter(true);
		BitmapFontParameter bitmapParam1 = new BitmapFontParameter();
		bitmapParam1.flip = true;
		
		assets = new AssetManager();
		assets.load("images/oldman.pack", TextureAtlas.class, atlasParam1);
		assets.load("images/tileset.png", Texture.class);
		assets.load("images/tileset2.png", Texture.class);
		assets.load("images/joeface.png", Texture.class);
        assets.load("images/ginger.png", Texture.class);
        assets.load("images/dialoguebox.png", Texture.class);
		assets.load("fonts/text.fnt", BitmapFont.class, bitmapParam1);
		assets.finishLoading();
	}
    
    public <T> T getAsset(String assetPath, Class<T> className)
    {
        return assets.get(assetPath, className);
    }
	
	public FitViewport getViewport()
	{
		return viewport;
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

package woohoo.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader.TextureAtlasParameter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import woohoo.framework.CharacterManager;
import woohoo.framework.DialogueManager;
import woohoo.framework.GateManager;
import woohoo.framework.HexMapLoader;
import woohoo.framework.InputHandler;
import woohoo.gameobjects.NPC;
import woohoo.gameobjects.Player;
import woohoo.gameobjects.components.MapObjectComponent;
import woohoo.gameworld.GameRenderer;
import woohoo.gameworld.GameWorld;

public class PlayingScreen implements Screen
{    
	public enum GameState
	{
		Playing, Dialogue
	}	
    
    public enum WBodyType
    {
        Player, Entity, Wall, Gate
    }
	
	/* Dimensions of tiles on the spritesheet */
    public final int T_TILE_WIDTH = 16;
    public final int T_TILE_HEIGHT = 16;
	
	private GameState state;
	
	private OrthographicCamera cam; // Manages aspect ratio, zoom, and position of camera
	private FitViewport viewport; // Helper class for camera
	private Box2DDebugRenderer debugRenderer;
	
    private GateManager gates;
	private CharacterManager characters;
    private DialogueManager dialogueManager;
	private AssetManager assets;
	private InputHandler input;
	private HexMapLoader mapLoader;
	private GameRenderer renderer;
	private GameWorld engine;
	private World world;
	private Stage ui;
		
	public int WORLD_WIDTH = 16; // Arbitrary unit; how many tiles will fit width-wise on the screen
	public int WORLD_HEIGHT = (int)(WORLD_WIDTH * (float)Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth()); // Arbitrary unit; how many tiles will fit height-wise
	
	public int mapWidth;
	public int mapHeight;
	
    private float runTime;

    // This is the constructor, not the class declaration
    public PlayingScreen(Game game)
    {
		// Set up camera
        float aspectRatio = (float)Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth();
		
        cam = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        cam.setToOrtho(true, WORLD_WIDTH, WORLD_HEIGHT);
		
		viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, cam);
		viewport.apply();
		cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);
		debugRenderer = new Box2DDebugRenderer();
		
		// Load assets
		assets = new AssetManager();
		loadAssets();
		
		// Define characters
		characters = new CharacterManager(assets);
		
		// Create physics
		world = new World(new Vector2(0, 0), true);	
        
        // Create gate sensors
        gates = new GateManager(this, world);
		
		// Create user interface
		ui = new Stage();
		
		// Create map
		mapLoader = new HexMapLoader(this);
		TiledMap map = new TiledMap();
		MapLayers layers = mapLoader.load("maps/0.txt", (Texture)assets.get("images/tileset.png"), 
                                          (Texture)assets.get("images/tileset2.png"), world);
		
		// Draw and update every frame
		renderer = new GameRenderer(map, 1.0f / WORLD_WIDTH);
		engine = new GameWorld(this, world);
		
		// Create dialogue
		dialogueManager = new DialogueManager(this, ui, (Skin)assets.get("ui/uiskin.json"));
        
		// Initialize objects
		Player player = new Player((TextureAtlas)assets.get("images/oldman.pack"), world);
        NPC npc = new NPC((Texture)assets.get("images/ginger.png"), world);
        
		MapLayer objects = new MapLayer();
		objects.setName("Objects");
		objects.getObjects().add(player.getComponent(MapObjectComponent.class));
		objects.getObjects().add(npc.getComponent(MapObjectComponent.class));
		
        map.getLayers().add(layers.get("Base"));
		map.getLayers().add(objects);
        map.getLayers().add(layers.get("Decorations"));
		
		engine.addEntity(player);
        engine.addEntity(npc);

		// Initialize input
		input = new InputHandler(this, player);
        Gdx.input.setInputProcessor(new InputMultiplexer(ui, input));
		
		state = GameState.Playing;
    }

    @Override
    public void render(float delta)
    {
		switch (state)
		{
			case Playing:				
				Gdx.gl.glClearColor(0, 0, 0, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				cam.update();
				runTime += delta;

				engine.update(delta);
				world.step(delta, 6, 2);
				ui.act();
				
				renderer.setView(cam);
				renderer.render();
				debugRenderer.render(world, cam.combined);
				ui.draw();
				break;
				
			case Dialogue:
				Gdx.gl.glClearColor(0, 0, 0, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				cam.update();
				runTime += delta;
				
				ui.act();

				renderer.setView(cam);
				renderer.render();
				debugRenderer.render(world, cam.combined);
				ui.draw();
				break;
		}
    }
	
	public void loadAssets()
	{		
		TextureAtlasParameter atlasParam1 = new TextureAtlasParameter(true);
		SkinParameter skinParam1 = new SkinParameter("ui/uiskin.atlas");
		
		assets.load("images/oldman.pack", TextureAtlas.class, atlasParam1);
		assets.load("images/tileset.png", Texture.class);
		assets.load("images/tileset2.png", Texture.class);
		assets.load("images/joeface.png", Texture.class);
        assets.load("images/ginger.png", Texture.class);		
        assets.load("ui/uiskin.atlas", TextureAtlas.class);
		assets.load("ui/uiskin.json", Skin.class, skinParam1);
		
		assets.load("images/faces/ginger.png", Texture.class);
		assets.load("images/faces/oldman.png", Texture.class);
		assets.finishLoading();
	}
	
	public void switchScreens(int areaID)
	{ 
        Array<Body> bodies = new Array<>();
		world.getBodies(bodies);
		
		for (Body body: bodies)
		{
			if (body.getUserData().equals("Wall"))
				world.destroyBody(body);
		}
		
		TiledMap map = new TiledMap();
		
		MapLayers layers = mapLoader.load("maps/" + areaID + ".txt", (Texture)assets.get("images/tileset.png"), 
                                          (Texture)assets.get("images/tileset2.png"), world);		
		
		MapLayer objects = renderer.getMap().getLayers().get("Objects");
		
        map.getLayers().add(layers.get(0));
		map.getLayers().add(objects);
        map.getLayers().add(layers.get(1));
		
		renderer.setMap(map);
	}
    
	public void setState(GameState s)
	{
		state = s;
	}
	
	public GameState getState()
	{
		return state;
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
    
    public OrthographicCamera getCamera()
    {
        return cam;
    }
	
	public GameWorld getEngine()
	{
		return engine;
	}
	
	public DialogueManager getDialogueManager()
	{
		return dialogueManager;
	}
	
	public CharacterManager getCharacters()
	{
		return characters;
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

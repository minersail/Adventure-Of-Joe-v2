package woohoo.screens;

import com.badlogic.ashley.core.Entity;
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
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import woohoo.framework.AIManager;
import woohoo.framework.IDManager;
import woohoo.framework.ContactManager;
import woohoo.framework.CutsceneManager;
import woohoo.framework.DialogueManager;
import woohoo.framework.EventManager;
import woohoo.framework.GateManager;
import woohoo.framework.HexMapLoader;
import woohoo.framework.InputHandler;
import woohoo.framework.InventoryManager;
import woohoo.gameobjects.Item;
import woohoo.gameobjects.NPC;
import woohoo.gameobjects.Character;
import woohoo.gameobjects.Enemy;
import woohoo.gameobjects.components.AIComponent;
import woohoo.gameobjects.components.CollisionComponent;
import woohoo.gameobjects.components.HealthBarComponent;
import woohoo.gameobjects.components.InventoryComponent;
import woohoo.gameobjects.components.MapObjectComponent;
import woohoo.gameobjects.components.SensorComponent;
import woohoo.gameworld.GameRenderer;
import woohoo.gameworld.GameWorld;

public class PlayingScreen implements Screen
{    
	public enum GameState
	{
		Playing, Dialogue, Inventory, Cutscene
	}	
    
    public enum WBodyType
    {
        Player, Entity, Wall, Gate, Item, NPC, Weapon, Enemy
    }
	
	/* Dimensions of tiles on the spritesheet */
    public final int T_TILE_WIDTH = 16;
    public final int T_TILE_HEIGHT = 16;
	
	private GameState state;
	
	final private OrthographicCamera cam; // Manages aspect ratio, zoom, and position of camera
	final private FitViewport viewport; // Helper class for camera
	final private Box2DDebugRenderer debugRenderer;
	
	final private AIManager aimanager;
    final private CutsceneManager cutscenes;
	final private EventManager events;
    final private InventoryManager inventoryManager;
	final private ContactManager contacts;
    final private GateManager gates;
	final private IDManager idManager;
    final private DialogueManager dialogueManager;
	final private AssetManager assets;
	final private InputHandler input;
	final private HexMapLoader mapLoader;
	final private GameRenderer renderer;
	final private GameWorld engine;
	final private World world;
	final private Stage ui;
		
	public int WORLD_WIDTH = 16; // Arbitrary unit; how many tiles will fit width-wise on the screen
	public int WORLD_HEIGHT = (int)(WORLD_WIDTH * (float)Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth()); // Arbitrary unit; how many tiles will fit height-wise
	
	public int mapWidth;
	public int mapHeight;
	
	private int currentArea = 2;
	
    private float runTime;

    // This is the constructor, not the class declaration
    public PlayingScreen(Game game)
    {
		// Set up camera
        cam = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        cam.setToOrtho(true, WORLD_WIDTH, WORLD_HEIGHT);
		
		viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, cam);
		viewport.apply();
		cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);
		debugRenderer = new Box2DDebugRenderer();
		
		// Load assets
		assets = new AssetManager();
		loadAssets();
		
		// Map ids
		idManager = new IDManager(assets);
		
		// Create physics
		world = new World(new Vector2(0, 0), true);	
		contacts = new ContactManager();
        
        // Create gate sensors
        gates = new GateManager(this);        
        gates.createGates(currentArea);
		
		// Create user interface
		ui = new Stage();
        inventoryManager = new InventoryManager(this, assets.get("ui/inventory.pack", TextureAtlas.class),
                                                assets.get("ui/uiskin.json", Skin.class));
		
		// Create map
		mapLoader = new HexMapLoader(this);
		TiledMap map = mapLoader.load(currentArea);
		
		// Draw and update every frame
		renderer = new GameRenderer(map, 1.0f / WORLD_WIDTH);
		engine = new GameWorld(this);
		
		// Create dialogue
		dialogueManager = new DialogueManager(this, ui, assets.get("ui/uiskin.json", Skin.class));
        cutscenes = new CutsceneManager(this);
        
		// Initialize objects
		engine.loadPlayer();
		engine.loadEntities(currentArea);
		
		aimanager = new AIManager(this);
		aimanager.initializePathfinding();
		
		// Create event manager
		events = new EventManager(this);
		events.createEvents(currentArea);
		
		// Initialize input
		input = new InputHandler(this);
        Gdx.input.setInputProcessor(new InputMultiplexer(ui, input));
		
		state = GameState.Playing;
    }

    @Override
    public void render(float delta)
    {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);	
		cam.update();
		runTime += delta;
		switch (state)
		{
			case Playing:			
				engine.update(delta);
				world.step(delta, 6, 2);
                gates.updateArea();
				ui.act();
				break;
				
			case Cutscene:		
				cutscenes.update(delta);
				world.step(delta, 6, 2);
				ui.act();
				break;
				
            case Dialogue:
			case Inventory:				
				ui.act();
				break;
		}
		
		renderer.setView(cam);
		renderer.render();
		//debugRenderer.render(world, cam.combined);
		ui.draw();
    }
	
	private void loadAssets()
	{			
		TextureAtlasParameter flipParam = new TextureAtlasParameter(true);
		SkinParameter skinParam1 = new SkinParameter("ui/uiskin.atlas");
		
		assets.load("images/entities/oldman.pack", TextureAtlas.class, flipParam);
		assets.load("images/entities/mother.pack", TextureAtlas.class, flipParam);
		assets.load("images/entities/youngjoe.pack", TextureAtlas.class, flipParam);
		assets.load("images/entities/joeface.png", Texture.class);
        assets.load("images/entities/ginger.png", Texture.class);
        assets.load("images/entities/robber.png", Texture.class);
        assets.load("images/entities/mother.png", Texture.class);	
        assets.load("images/entities/robert.png", Texture.class);	
        assets.load("images/entities/scav.png", Texture.class);	
        assets.load("images/entities/fitz.png", Texture.class);	
		assets.load("images/tilesets/tileset1.png", Texture.class);
		assets.load("images/tilesets/d_tileset1.png", Texture.class);
		assets.load("images/tilesets/tileset2.png", Texture.class);
		assets.load("images/tilesets/d_tileset2.png", Texture.class);
		assets.load("ui/inventory.pack", TextureAtlas.class, flipParam);
		assets.load("ui/healthbar.pack", TextureAtlas.class, flipParam);
        assets.load("ui/uiskin.atlas", TextureAtlas.class);
		assets.load("ui/uiskin.json", Skin.class, skinParam1);
		
		// Load faces
		assets.load("images/faces/000_youngjoe.png", Texture.class);
		assets.load("images/faces/001_mother.png", Texture.class);
		assets.load("images/faces/002_robert.png", Texture.class);
		assets.load("images/faces/003_robber.png", Texture.class);
        
        // Load items
        assets.load("images/items/000_Stick.png", Texture.class);
        assets.load("images/items/001_PurpleStick.png", Texture.class);
        assets.load("images/items/002_GreenStick.png", Texture.class);
        
		assets.finishLoading();
	}
	
	/*
	Add an entity into the game world
	
	Initializes everything necessary for an entity to function
	
	Can be used in conjuction with removeEntity() to repeatedly add and remove the same entity
	*/
	public void addEntity(Entity entity)
	{		
		MapObjects objects = renderer.getMap().getLayers().get("Objects").getObjects();
		
		if (entity instanceof Character)
		{
			entity.getComponent(MapObjectComponent.class).addTo(objects);
			entity.getComponent(HealthBarComponent.class).addTo(objects).initializeHealthBar(assets.get("ui/healthbar.pack", TextureAtlas.class));
			entity.getComponent(CollisionComponent.class).createMass(world);
			inventoryManager.loadInventory(entity.getComponent(InventoryComponent.class));
			
			if (entity instanceof Enemy)
			{
				entity.getComponent(SensorComponent.class).initializeCommand(contacts, world).createMass(world);
			}
			else if (entity instanceof NPC)
			{
				entity.getComponent(CollisionComponent.class).setImmovable(true);
			}
		}
		else if (entity instanceof Item)
		{
			entity.getComponent(MapObjectComponent.class).addTo(objects);
			entity.getComponent(SensorComponent.class).initializeCommand(contacts, world).createMass(world);
		}
		
		engine.addEntity(entity);
	}
	
	/*
	Remove an entity from the game world
	*/
	public void removeEntity(Entity entity)
	{	
		MapObjects objects = renderer.getMap().getLayers().get("Objects").getObjects();
		
		if (entity instanceof Character)
		{
			entity.getComponent(MapObjectComponent.class).removeFrom(objects);
			entity.getComponent(HealthBarComponent.class).removeFrom(objects);
			entity.getComponent(CollisionComponent.class).removeMass();
			
			if (entity instanceof Enemy)
			{
				entity.getComponent(SensorComponent.class).removeMass();
			}
		}
		else if (entity instanceof Item)
		{
			entity.getComponent(MapObjectComponent.class).removeFrom(objects);
			entity.getComponent(SensorComponent.class).removeMass();
		}
		
		engine.removeEntity(entity);
	}
    
	public void setState(GameState s)
	{
		state = s;
		if (state == GameState.Dialogue) engine.stopAll();
	}
	
	public void setArea(int area)
	{
		currentArea = area;
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
    
    public World getWorld()
    {
        return world;
    }
    
    public AssetManager getAssets()
    {
        return assets;
    }
    
    public HexMapLoader getMapLoader()
    {
        return mapLoader;
    }
    
    public GameRenderer getRenderer()
    {
        return renderer;
    }
    
    public Stage getUI()
    {
        return ui;
    }
	
	public DialogueManager getDialogueManager()
	{
		return dialogueManager;
	}
	
	public IDManager getIDManager()
	{
		return idManager;
	}
	
	public ContactManager getContactManager()
	{
		return contacts;
	}
    
    public InventoryManager getInventoryManager()
    {
        return inventoryManager;
    }
	
	public EventManager getEventManager()
	{
		return events;
	}
	
	public CutsceneManager getCutsceneManager()
	{
		return cutscenes;
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

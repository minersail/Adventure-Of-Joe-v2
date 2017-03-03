package woohoo.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader.TextureAtlasParameter;
import com.badlogic.gdx.files.FileHandle;
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
import woohoo.ai.AIDebugger;
import woohoo.framework.AIManager;
import woohoo.framework.AlertManager;
import woohoo.framework.IDManager;
import woohoo.framework.ContactManager;
import woohoo.framework.CutsceneManager;
import woohoo.framework.DialogueManager;
import woohoo.framework.EventManager;
import woohoo.framework.GateManager;
import woohoo.framework.HexMapLoader;
import woohoo.framework.InputHandler;
import woohoo.framework.InventoryManager;
import woohoo.framework.QuestManager;
import woohoo.gameobjects.Item;
import woohoo.gameobjects.NPC;
import woohoo.gameobjects.Character;
import woohoo.gameobjects.Enemy;
import woohoo.gameobjects.QuestIndicator;
import woohoo.gameobjects.components.CollisionComponent;
import woohoo.gameobjects.components.HealthBarComponent;
import woohoo.gameobjects.components.InventoryComponent;
import woohoo.gameobjects.components.MapObjectComponent;
import woohoo.gameobjects.components.SensorComponent;
import woohoo.gameworld.GameRenderer;
import woohoo.gameworld.GameWorld;
import woohoo.msjgame.MSJGame;

public class PlayingScreen implements Screen
{    
	public enum GameState
	{
		Playing, Dialogue, Inventory, Cutscene, Quests
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
	final private AIDebugger aiDebugger;
	
	final private AlertManager alerts;
	final private QuestManager quests;
	final private AIManager aiManager;
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
	
	private int currentArea = 0;
	
    private float runTime;

    // This is the constructor, not the class declaration
    public PlayingScreen(MSJGame game)
    {
		// Set up camera
        cam = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        cam.setToOrtho(true, WORLD_WIDTH, WORLD_HEIGHT);
		
		viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, cam);
		viewport.apply();
		cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);
		debugRenderer = new Box2DDebugRenderer();
		aiDebugger = new AIDebugger();
		
		// Load assets
		assets = new AssetManager();
		loadAssets("images/entities", "pack");
		loadAssets("images/faces", "png");
		loadAssets("images/items", "png");
		loadAssets("images/tilesets", "png");
		loadAssets("ui/alerts", "png");
		loadAssets("ui/quests", "png");
		loadAssets("ui", "pack");
		loadAssets("ui", "json");
		assets.finishLoading();
		
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
		dialogueManager = new DialogueManager(this, assets.get("ui/uiskin.json", Skin.class));
        cutscenes = new CutsceneManager(this);
		
		// Initialize quests
		quests = new QuestManager(this, assets.get("ui/uiskin.json", Skin.class));
        
		// Initialize objects
		engine.loadPlayer();
		engine.loadEntities(currentArea);
		
		aiManager = new AIManager(this);
		aiManager.initializePathfinding(currentArea);
		
		// Create event manager
		events = new EventManager(this);
		events.createEvents(currentArea);
		
		alerts = new AlertManager(this, assets.get("ui/uiskin.json", Skin.class));
		
		// Initialize input
		input = new InputHandler(this);
		
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
				engine.animate(delta);
				ui.act();
				break;
				
			case Quests:
			case Inventory:				
				ui.act();
				break;
		}
		
		alerts.act(delta);
		renderer.setView(cam);
		renderer.render();
		//aiDebugger.renderLineOfSight(engine.getEntity("player"), cam);
		//debugRenderer.render(world, cam.combined);
		ui.draw();
    }
	
	private void loadAssets(String directoryName, String extension)
	{			
		for (FileHandle handle : Gdx.files.internal(directoryName).list(extension))
		{
			loadAsset(handle);
		}        
	}
	
	private void loadAsset(FileHandle handle)
	{
		switch(handle.extension())
		{
			case "png":
				assets.load(handle.path(), Texture.class);
				break;
			case "pack":
			case "atlas":
				assets.load(handle.path(), TextureAtlas.class, new TextureAtlasParameter(true));
				break;
			case "json":
				assets.load(handle.path(), Skin.class, new SkinParameter(handle.pathWithoutExtension() + ".atlas"));
		}
	}
	
	/*
	Add an entity into the game world
	
	Initializes everything necessary for an entity to function
	
	Can be used in conjuction with removeEntity() to repeatedly add and remove the same entity
	*/
	public void addEntity(Entity entity)
	{		
		MapObjects entities = renderer.getMap().getLayers().get("Entities").getObjects();
		MapObjects items = renderer.getMap().getLayers().get("Items").getObjects();
		
		if (entity instanceof Character)
		{
			entity.getComponent(MapObjectComponent.class).addTo(entities);
			entity.getComponent(HealthBarComponent.class).addTo(entities).initializeHealthBar(assets.get("ui/healthbar.pack", TextureAtlas.class));
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
			entity.getComponent(MapObjectComponent.class).addTo(items);
			entity.getComponent(SensorComponent.class).initializeCommand(contacts, world).createMass(world);
		}
		else if (entity instanceof QuestIndicator)
		{
			entity.getComponent(MapObjectComponent.class).addTo(items);
		}
		
		engine.addEntity(entity);
	}
	
	/*
	Remove an entity from the game world
	*/
	public void removeEntity(Entity entity)
	{	
		MapObjects entities = renderer.getMap().getLayers().get("Entities").getObjects();
		MapObjects items = renderer.getMap().getLayers().get("Items").getObjects();
		
		if (entity instanceof Character)
		{
			entity.getComponent(MapObjectComponent.class).removeFrom(entities);
			entity.getComponent(HealthBarComponent.class).removeFrom(entities);
			entity.getComponent(CollisionComponent.class).removeMass();
			
			if (entity instanceof Enemy)
			{
				entity.getComponent(SensorComponent.class).removeMass();
			}
		}
		else if (entity instanceof Item)
		{
			entity.getComponent(MapObjectComponent.class).removeFrom(items);
			entity.getComponent(SensorComponent.class).removeMass();
		}
		else if (entity instanceof QuestIndicator)
		{
			entity.getComponent(MapObjectComponent.class).removeFrom(items);
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
    
    public InputHandler getInput()
    {
        return input;
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
	
	public AIManager getAIManager()
	{
		return aiManager;
	}
	
	public QuestManager getQuestManager()
	{
		return quests;
	}
	
	public AlertManager getAlertManager()
	{
		return alerts;
	}

    @Override
    public void resize(int width, int height)
    {
//        cam.viewportWidth = WORLD_WIDTH * (width / 800);
//        cam.viewportHeight = WORLD_HEIGHT * (height / 800);
//        cam.update();
        
        System.out.println("GameScreen - resize called");
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
	
	public void resetData()
	{
		FileHandle raw = Gdx.files.internal("raw/data");
		
		for (FileHandle handle : raw.list())
		{
			handle.copyTo(Gdx.files.local("data/" + handle.name()));
		}
	}
}

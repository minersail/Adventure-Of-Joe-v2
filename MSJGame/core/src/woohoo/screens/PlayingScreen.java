package woohoo.screens;

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
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import woohoo.ai.AIDebugger;
import woohoo.framework.*;
import woohoo.gameworld.*;
import woohoo.msjgame.MSJGame;

public class PlayingScreen implements Screen
{    
	public enum GameState
	{
		Playing, Dialogue, Inventory, Cutscene, Quests
	}	
	
	/* Dimensions of tiles on the spritesheet */
    public final int T_TILE_WIDTH = 16;
    public final int T_TILE_HEIGHT = 16;
	
	private GameState state;
	
	final private OrthographicCamera cam; // Manages aspect ratio, zoom, and position of camera
	final private FitViewport viewport; // Helper class for camera
	final private Box2DDebugRenderer debugRenderer;
	final private AIDebugger aiDebugger;
	
	final private EntityLoader entityLoader;
	final private AlertManager alerts;
	final private QuestManager quests;
    final private CutsceneManager cutscenes;
    final private InventoryManager inventoryManager;
	final private IDManager idManager;
    final private DialogueManager dialogueManager;
	final private AssetManager assets;
	final private HexMapLoader mapLoader;
	final private GameEngine engine;
	final private World world;
	final private Stage ui;
		
	public int WORLD_WIDTH = 16; // Arbitrary unit; how many tiles will fit width-wise on the screen
	public int WORLD_HEIGHT = (int)(WORLD_WIDTH * (float)Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth()); // Arbitrary unit; how many tiles will fit height-wise
	
	public int mapWidth;
	public int mapHeight;
	
	private int currentArea = 4;
	
    private float runTime;
	
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
		
		// Create user interface
		ui = new Stage();
        inventoryManager = new InventoryManager(this, assets.get("ui/inventory.pack", TextureAtlas.class),
                                                assets.get("ui/uiskin.json", Skin.class));
		
		// Create map
		mapLoader = new HexMapLoader(this);
		TiledMap map = mapLoader.load(currentArea);
		
		// Draw and update every frame
		engine = new GameEngine(this);
		entityLoader = new EntityLoader(this);
		
		// Create dialogue
		dialogueManager = new DialogueManager(this, assets.get("ui/uiskin.json", Skin.class));
        cutscenes = new CutsceneManager(this);
		
		// Initialize quests
		quests = new QuestManager(this, assets.get("ui/uiskin.json", Skin.class));
		alerts = new AlertManager(this, assets.get("ui/uiskin.json", Skin.class));
		
		//--------------------------------------------------------------------------
		AIStateSystem aiSystem = new AIStateSystem(this);
		AnimationSystem animationSystem = new AnimationSystem();
		ContactSystem contactSystem = new ContactSystem(world);
		DamageSystem damageSystem = new DamageSystem();
		EventSystem eventSystem = new EventSystem(this);
		GateSystem gateSystem = new GateSystem(this);
		InputSystem inputSystem = new InputSystem(this);
		ItemSystem itemSystem = new ItemSystem();
		LineOfSightSystem losSystem = new LineOfSightSystem();
		MovementSystem movementSystem = new MovementSystem();
		RenderSystem renderSystem = new RenderSystem(map, 1.0f / WORLD_WIDTH);
		renderSystem.getRenderer().setView(cam);
		WeaponSystem weaponSystem = new WeaponSystem();
		
		inputSystem.priority = 0;
		eventSystem.priority = 1;
		aiSystem.priority = 2;
		weaponSystem.priority = 3;
		damageSystem.priority = 4;
		losSystem.priority = 5;
		contactSystem.priority = 6;
		itemSystem.priority = 7;
		movementSystem.priority = 8;
		gateSystem.priority = 9;
		animationSystem.priority = 10;
		renderSystem.priority = 11;
		
		engine.addSystem(aiSystem);
		engine.addSystem(animationSystem);
		engine.addSystem(contactSystem);
		engine.addSystem(damageSystem);
		engine.addSystem(eventSystem);
		engine.addSystem(gateSystem);
		engine.addSystem(inputSystem);
		engine.addSystem(itemSystem);
		engine.addSystem(losSystem);
		engine.addSystem(movementSystem);
		engine.addSystem(renderSystem);
		engine.addSystem(weaponSystem);
		
		state = GameState.Playing;
	}

    public void initialize(int gameArea)
    {    
		entityLoader.loadPlayer();
		entityLoader.loadEntities(gameArea);
		engine.getSystem(GateSystem.class).initialize(gameArea);
		engine.getSystem(AIStateSystem.class).initialize(gameArea);
		engine.getSystem(EventSystem.class).initialize(gameArea);
		engine.getSystem(RenderSystem.class).initialize();
		engine.getSystem(MovementSystem.class).initialize();
    }

    @Override
    public void render(float delta)
    {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);	
		cam.update();
		runTime += delta;
		engine.update(delta);
		
		switch (state)
		{
			case Playing:			
				world.step(delta, 6, 2);
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
		//aiDebugger.renderLineOfSight(engine.getEntity("player"), cam);
		debugRenderer.render(world, cam.combined);
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
		    
	public void setState(GameState s)
	{
		state = s;
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
	
	public GameEngine getEngine()
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
        return engine.getSystem(RenderSystem.class).getRenderer();
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
    
    public InventoryManager getInventoryManager()
    {
        return inventoryManager;
    }
	
	public CutsceneManager getCutsceneManager()
	{
		return cutscenes;
	}
	
	public QuestManager getQuestManager()
	{
		return quests;
	}
	
	public AlertManager getAlertManager()
	{
		return alerts;
	}
	
	public EntityLoader getEntityLoader()
	{
		return entityLoader;
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

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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import woohoo.framework.IDManager;
import woohoo.framework.ContactManager;
import woohoo.framework.DialogueManager;
import woohoo.framework.GateManager;
import woohoo.framework.HexMapLoader;
import woohoo.framework.InputHandler;
import woohoo.framework.InventoryManager;
import woohoo.gameobjects.Item;
import woohoo.gameobjects.NPC;
import woohoo.gameobjects.Player;
import woohoo.gameobjects.Character;
import woohoo.gameobjects.Enemy;
import woohoo.gameobjects.components.AIComponent;
import woohoo.gameobjects.components.CollisionComponent;
import woohoo.gameobjects.components.HealthBarComponent;
import woohoo.gameobjects.components.MapObjectComponent;
import woohoo.gameobjects.components.SensorComponent;
import woohoo.gameworld.GameRenderer;
import woohoo.gameworld.GameWorld;

public class PlayingScreen implements Screen
{    
	public enum GameState
	{
		Playing, Dialogue, Inventory
	}	
    
    public enum WBodyType
    {
        Player, Entity, Wall, Gate, Item, NPC, Weapon, Enemy
    }
	
	/* Dimensions of tiles on the spritesheet */
    public final int T_TILE_WIDTH = 16;
    public final int T_TILE_HEIGHT = 16;
	
	private GameState state;
	
	private OrthographicCamera cam; // Manages aspect ratio, zoom, and position of camera
	private FitViewport viewport; // Helper class for camera
	private Box2DDebugRenderer debugRenderer;
	
    private InventoryManager inventoryManager;
	private ContactManager contacts;
    private GateManager gates;
	private IDManager idManager;
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
        gates.createGates(world, 0);
		
		// Create user interface
		ui = new Stage();
        inventoryManager = new InventoryManager(this, assets.get("images/itemframe.png", Texture.class),
                                                assets.get("images/blank_64.png", Texture.class),
                                                assets.get("ui/uiskin.json", Skin.class));
		
		// Create map
		mapLoader = new HexMapLoader(this);
		TiledMap map = mapLoader.load("maps/0.txt", assets.get("images/tileset.png", Texture.class), 
                                      assets.get("images/tileset2.png", Texture.class), world);
		
		// Draw and update every frame
		renderer = new GameRenderer(map, 1.0f / WORLD_WIDTH);
		engine = new GameWorld(this);
		
		// Create dialogue
		dialogueManager = new DialogueManager(this, ui, assets.get("ui/uiskin.json", Skin.class));
        
		// Initialize objects
		Player player = new Player(assets.get("images/oldman.pack", TextureAtlas.class));
        NPC npc = new NPC(assets.get("images/ginger.png", Texture.class));
		Item item = new Item(new TextureRegion(assets.get("images/items/000_Stick.png", Texture.class)));
		Enemy enemy = new Enemy(assets.get("images/ginger.png", Texture.class));
		
		addEntity(player);
		addEntity(npc);
		addEntity(item);
		addEntity(enemy);
		
		inventoryManager.fillInventory(player);
		
		// Initialize input
		input = new InputHandler(this, player);
        Gdx.input.setInputProcessor(new InputMultiplexer(ui, input));
		
		state = GameState.Playing;
    }

    @Override
    public void render(float delta)
    {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		switch (state)
		{
			case Playing:				
				cam.update();
				runTime += delta;

				engine.update(delta);
				world.step(delta, 6, 2);
                gates.updateArea();
				ui.act();
				
				renderer.setView(cam);
				renderer.render();
				debugRenderer.render(world, cam.combined);
				ui.draw();
				break;
				
			case Dialogue:
				cam.update();
				runTime += delta;
				
				ui.act();

				renderer.setView(cam);
				renderer.render();
				debugRenderer.render(world, cam.combined);
				ui.draw();
				break;
				
			case Inventory:
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
	
	private void loadAssets()
	{			
		TextureAtlasParameter atlasParam1 = new TextureAtlasParameter(true);
		SkinParameter skinParam1 = new SkinParameter("ui/uiskin.atlas");
		
		assets.load("images/oldman.pack", TextureAtlas.class, atlasParam1);
		assets.load("images/tileset.png", Texture.class);
		assets.load("images/tileset2.png", Texture.class);
		assets.load("images/joeface.png", Texture.class);
        assets.load("images/ginger.png", Texture.class);	
		assets.load("images/itemframe.png", Texture.class);
		assets.load("images/blank_64.png", Texture.class);
		assets.load("images/green.png", Texture.class);
		assets.load("images/red.png", Texture.class);
        assets.load("ui/uiskin.atlas", TextureAtlas.class);
		assets.load("ui/uiskin.json", Skin.class, skinParam1);
		
		// Load faces
		assets.load("images/faces/000_Ginger.png", Texture.class);
		assets.load("images/faces/001_OldMan.png", Texture.class);
        
        // Load items
        assets.load("images/items/000_Stick.png", Texture.class);
        assets.load("images/items/001_PurpleStick.png", Texture.class);
        assets.load("images/items/002_GreenStick.png", Texture.class);
        
		assets.finishLoading();
	}
	
	/*
	Add an entity into the game world
	*/
	public void addEntity(Entity entity)
	{		
		if (entity instanceof Character)
		{
			renderer.getMap().getLayers().get("Objects").getObjects().add(entity.getComponent(MapObjectComponent.class));
			renderer.getMap().getLayers().get("Objects").getObjects().add(entity.getComponent(HealthBarComponent.class));
			entity.getComponent(CollisionComponent.class).createMass(world);
			engine.addEntity(entity);
			inventoryManager.loadInventory((Character)entity);
            entity.getComponent(HealthBarComponent.class).initializeHealthBar(assets.get("images/green.png", Texture.class), 
                                                                              assets.get("images/red.png", Texture.class));
			
			if (entity instanceof Enemy)
			{
				entity.getComponent(SensorComponent.class).createMass(world);
				entity.getComponent(AIComponent.class).setPlayer(engine.getPlayer());
				contacts.addCommand(entity.getComponent(SensorComponent.class).getContactData(), world);
			}
		}
		else if (entity instanceof Item)
		{
			renderer.getMap().getLayers().get("Objects").getObjects().add(entity.getComponent(MapObjectComponent.class));
			entity.getComponent(SensorComponent.class).createMass(world);
			contacts.addCommand(entity.getComponent(SensorComponent.class).getContactData(), world);
			engine.addEntity(entity);
		}
	}
	
	/*
	Remove an entity from the game world
	*/
	public void removeEntity(Entity entity)
	{	
		if (entity instanceof Character)
		{
			renderer.getMap().getLayers().get("Objects").getObjects().remove(entity.getComponent(MapObjectComponent.class));
			entity.getComponent(CollisionComponent.class).removeMass();
			engine.removeEntity(entity);
		}
		else if (entity instanceof Item)
		{
			renderer.getMap().getLayers().get("Objects").getObjects().remove(entity.getComponent(MapObjectComponent.class));
			entity.getComponent(SensorComponent.class).removeMass();
			engine.removeEntity(entity);
		}
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

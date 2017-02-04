package woohoo.gameworld;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import java.util.ArrayList;
import woohoo.gameobjects.BaseEntity;
import woohoo.gameobjects.Enemy;
import woohoo.gameobjects.Item;
import woohoo.gameobjects.NPC;
import woohoo.gameobjects.Player;
import woohoo.gameobjects.components.DialogueComponent;
import woohoo.gameobjects.components.SensorComponent;
import woohoo.screens.PlayingScreen;

/*
All objects are data that will be drawn by the tiles

Their updates will be called in this class
*/
public class GameWorld extends Engine
{
	private PlayingScreen screen;
    
    public float runtime;
	
	public GameWorld(PlayingScreen scr)
	{
		screen = scr;
	}
    
	@Override
    public void update(float delta)
    {
		runtime += delta;

		for (Entity entity : getEntities())
		{
			((BaseEntity)entity).update(delta);
			
			if (entity instanceof Player)
			{
				adjustCamera((Player)entity);
			}
		}
    }
	
	@Override
	public void addEntity(Entity entity)
	{
		super.addEntity(entity);
	}
	
	public void loadEntities(int area)
	{
		FileHandle handle = Gdx.files.internal("data/entities.xml");
        
        XmlReader xml = new XmlReader();
        XmlReader.Element root = xml.parse(handle.readString());       
        XmlReader.Element entities = root.getChild(area);  
        
        for (XmlReader.Element entity : entities.getChildrenByName("entity"))
        {
			String eClass = entity.get("class");
			
			switch (eClass)
			{
				case "player":
					Player player = new Player(screen.getAssets().get("images/" + entity.get("texture"), TextureAtlas.class));
					screen.addEntity(player);
					player.setPosition(entity.getFloat("locX"), entity.getFloat("locY"));
					screen.getInventoryManager().fillInventory(player);
					break;
				case "npc":
					NPC npc = new NPC(screen.getAssets().get("images/" + entity.get("texture"), Texture.class), entity.getInt("id"));
					screen.addEntity(npc);
					npc.setPosition(entity.getFloat("locX"), entity.getFloat("locY"));
					break;
				case "item":
					Item item = new Item(screen.getIDManager().getItem(entity.getInt("id")).getItemTexture());
					screen.addEntity(item);
					item.setPosition(entity.getFloat("locX"), entity.getFloat("locY"));
					item.setType(entity.get("type"));
					item.flipImage();
					break;
				case "enemy":
					Enemy enemy = new Enemy(screen.getAssets().get("images/" + entity.get("texture"), Texture.class));
					screen.addEntity(enemy);
					enemy.setPosition(entity.getFloat("locX"), entity.getFloat("locY"));
					enemy.changeMaxHealth(entity.getFloat("health"));
					break;
				default:
					break;
			}
		}
	}
    
    public Player getPlayer()
    {
        for (Entity entity : getEntities())
        {
            if (entity instanceof Player)
                return (Player)entity;
        }
        
        Gdx.app.log("ERROR", "Player does not exist in GameWorld");
        return null;
    }
    
    public ArrayList<NPC> getNPCs()
    {
        ArrayList<NPC> npcs = new ArrayList<>();
        
        for (Entity entity : getEntities())
        {
            if (entity instanceof NPC)
                npcs.add((NPC)entity);
        }
        return npcs;
    }
	
	/*
	Check to see if the player is facing an NPC
	*/
	public boolean checkDialogue(Player player)
	{
		for (NPC npc : getNPCs())
        {
            if (player.isFacing(npc) && player.distanceTo(npc) < 1.1)
            {                
                screen.getDialogueManager().startDialogue(npc.getComponent(DialogueComponent.class));
				return true;
            }
        }
		
		return false;
	}
	
	public void checkItems(Player player)
	{
		for (Entity entity : getEntities())
		{
			if (entity instanceof Item)
			{
				Item item = (Item)entity;
				if (item.getComponent(SensorComponent.class).hasContact())
				{
					screen.getInventoryManager().addItem(player, item);					
					screen.removeEntity(item);					
				}
			}
		}
	}
	
	/*
	If map is smaller than screen, center it in screen. If it is larger, pan it based on player's location
	*/
	public void adjustCamera(Player player)
	{
		Vector2 p = player.getPosition();
		Vector2 newPos = new Vector2(p);
				
		if (screen.mapWidth > screen.WORLD_WIDTH)
		{
			newPos.x = Math.min(Math.max(newPos.x, screen.WORLD_WIDTH / 2), screen.mapWidth - screen.WORLD_WIDTH / 2);
		}
		else
		{
			float extraX = (float)(screen.WORLD_WIDTH - screen.mapWidth);
			newPos.x = screen.getCamera().viewportWidth / 2 - Math.max(0, extraX / 2);
		}
		
		// Move x and y camera coordinates independently
		if (screen.mapHeight > screen.WORLD_HEIGHT)
		{
			newPos.y = Math.min(Math.max(newPos.y, screen.WORLD_HEIGHT / 2), screen.mapHeight - screen.WORLD_HEIGHT / 2);
		}
		else
		{
			float extraY = (float)(screen.WORLD_HEIGHT - screen.mapHeight);
			newPos.y = screen.getCamera().viewportHeight / 2 - Math.max(0, extraY / 2);
		}
		
		screen.setCamera(newPos.x, newPos.y);
	}
}

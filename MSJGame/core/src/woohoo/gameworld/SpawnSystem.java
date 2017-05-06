package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import woohoo.framework.loading.EntityMold;
import woohoo.gameobjects.components.SpawnComponent;
import woohoo.screens.PlayingScreen;

public class SpawnSystem extends IteratingSystem
{
	PlayingScreen screen;
	
	public SpawnSystem(PlayingScreen scr)
	{
		super(Family.all(SpawnComponent.class).get());
		
		screen = scr;
	}
	
	public void initialize(int area)
	{		
		FileHandle handle = Gdx.files.local("data/entities.xml");
        
        XmlReader xml = new XmlReader();
        Element root = xml.parse(handle.readString());   
        Element ee = root.getChild(area);
		
		for (Entity entity : getEntities())
		{
			SpawnComponent spawn = Mappers.spawns.get(entity);
			
			for (Element e : ee.getChildrenByName("entity"))
			{
				if (spawn.moldName.equals(e.get("name")))
				{
					spawn.setMold(new EntityMold(e));
					break;
				}
			}
		}
	}
	
	/**
	 * Initializes pathfinding grid for a single entity. <br>
	 * Useful when SpawnComponents are added to an entity after the global initialization
	 * @param entity
	 * @param area
	 */
	public void initialize(Entity entity, int area)
    {
        FileHandle handle = Gdx.files.local("data/entities.xml");
        
        XmlReader xml = new XmlReader();
        Element root = xml.parse(handle.readString());    
		Element ee = root.getChild(area);
		
		SpawnComponent spawn = Mappers.spawns.get(entity);
			
		for (Element e : ee.getChildrenByName("entity"))
		{
			if (spawn.moldName.equals(e.get("name")))
			{
				spawn.setMold(new EntityMold(e));
				break;
			}
		}
    }

	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		SpawnComponent spawn = Mappers.spawns.get(entity);
		
		spawn.timer += deltaTime;
		
		if (spawn.timer >= spawn.interval)
		{
			Entity loaded = screen.getEntityLoader().loadEntity(spawn.getMold());			
			
			// Automate later
			// Initializes the entity in its respective systems
			if (Mappers.spawns.has(loaded))
			{
				screen.getEngine().getSystem(SpawnSystem.class).initialize(loaded, screen.currentArea);
			}
			
			spawn.timer = 0;
		}
	}
}

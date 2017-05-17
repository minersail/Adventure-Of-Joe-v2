package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import woohoo.gameworld.components.AIComponent;
import woohoo.gameworld.components.MovementComponent;
import woohoo.gameworld.components.PositionComponent;
import woohoo.screens.PlayingScreen;

public class AIStateSystem extends IteratingSystem
{
	PlayingScreen screen;
	
	public AIStateSystem(PlayingScreen scr)
	{
		super(Family.all(MovementComponent.class, PositionComponent.class, AIComponent.class).get());
		
		screen = scr;
	}
	    
	/**
	 * Initializes pathfinding grid for a single entity. <br>
	 * Useful when AIComponents are added to an entity after the global initialization
	 * @param entity
	 * @param area
	 */
	public void initialize(Entity entity, int area)
    {
        FileHandle handle = Gdx.files.local("data/pathfinding.xml");
        
        XmlReader xml = new XmlReader();
        Element root = xml.parse(handle.readString());    
		Element data = root.getChild(area);
		
		AIComponent brain = Mappers.ai.get(entity);
        brain.initializePathfinding(screen.getEngine().getSystem(RenderSystem.class).getRenderer().getMap(), screen.getWorld(), data);
		brain.getPattern().link(this);
    }

	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		AIComponent brain = Mappers.ai.get(entity);
		
//		brain.timer += deltaTime;
//		
//		if (brain.timer >= brain.timeStep) // Only recalculates pathfinding after a certain interval
//		{
//			movement.direction = brain.getState().getDirection(brain, position);
//			brain.timer = 0;
//		}

		brain.getPattern().runPattern(entity, deltaTime);
	}
	
	/**
	 * Allows the AIPatterns to link their entity ids to actual components by
	 * only exposing the engine method getEntity()
	 * @param str id of the entity's idcomponent
	 * @return the entity
	 */
	public Entity getEntity(String str)
	{
		return screen.getEngine().getEntity(str);
	}
}

package woohoo.framework.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import woohoo.gameobjects.components.PositionComponent;
import woohoo.gameworld.Mappers;

/** 
 * Implementation of EventTrigger based on movement to a location
 *
 * @author jordan
 */
public class MoveTrigger implements EventTrigger<Entity>
{
	private Vector2 position;
	private float distanceTo;
	
	/**
	 * Note: MoveEvents only work with entities inheriting off of {@link Character}
	 * @param pos Target location
	 * @param dist Distance from target location when event should be triggered
	 */
	public MoveTrigger(Vector2 pos, float dist)
	{
		// Switch from top-left corner to center
		position = new Vector2(pos.x + 0.5f, pos.y + 0.5f);
		distanceTo = dist;
	}
	
	/**
	 * Only works on subclasses of {@link Character}
	 * @param entity Entity calling the check
	 * @return Whether or not the entity is close enough to the activation position
	 */
	@Override
	public boolean check(Entity entity)
	{		
		return Mappers.positions.get(entity).position.dst(position) < distanceTo;
	}
}

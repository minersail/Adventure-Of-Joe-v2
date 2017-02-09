package woohoo.framework.events;

import com.badlogic.gdx.math.Vector2;
import woohoo.framework.EventManager;
import woohoo.gameobjects.BaseEntity;
import woohoo.gameobjects.Character;

/** 
 * Implementation of EventTrigger based on movement to a location
 *
 * @author jordan
 */
public class MoveTrigger implements EventTrigger
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
		position = pos;
		distanceTo = dist;
	}
	
	/**
	 * Only works on subclasses of {@link Character}
	 * @param entity entity calling the check
	 * @return Whether or not the entity is close enough to the activation position
	 */
	@Override
	public boolean check(BaseEntity entity)
	{		
		Character character = (Character) entity;

		return character.distanceTo(position) < distanceTo;
	}
}

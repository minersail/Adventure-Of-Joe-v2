package woohoo.framework.events;

import com.badlogic.gdx.math.Vector2;
import woohoo.framework.EventManager;
import woohoo.gameobjects.BaseEntity;
import woohoo.gameobjects.Character;

/** 
 * Implementation of Event with check() implemented. 
 * activate() must be implemented by an anonymous class or subclass.
 *
 * @author jordan
 */
public class MoveEvent implements Event
{
	EventManager manager;
	
	private Vector2 position;
	private float distanceTo;
	private int gameArea;
	
	/**
	 * Note: MoveEvents only work with entities inheriting off of {@link Character}
	 * @param pos Target location
	 * @param dist Distance from target location when event should be triggered
	 * @param area Game area that this listener is active in
	 * @param em Remove this eventually
	 */
	public MoveEvent(Vector2 pos, float dist, int area, EventManager em)
	{
		position = pos;
		distanceTo = dist;
		gameArea = area;
		manager = em;
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

		return character.distanceTo(position) < distanceTo && manager.getCurrentGameArea() == gameArea;
	}

	/**
	 * This should be overridden when implemented
	 */
	@Override
	public void activate()
	{
		System.out.println("Event happening");
	}
}

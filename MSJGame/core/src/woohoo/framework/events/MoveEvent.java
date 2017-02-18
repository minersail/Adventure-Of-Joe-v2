package woohoo.framework.events;

import com.badlogic.gdx.math.Vector2;
import woohoo.gameobjects.Character;

public class MoveEvent implements Event
{
	Character character;
	Vector2 position;
	
	/**
	 * Switches character's AIMode to MoveTo and moves to given location
	 * @param moveEntity Must be a character with an AIComponent
	 * @param pos Position entity will move to
	 */
	public MoveEvent(Character moveEntity, Vector2 pos)
	{
		character = moveEntity;
		position = pos;
	}
	
	@Override
	public void activate()
	{
		character.setTarget(position);
	}
}

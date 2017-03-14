package woohoo.framework.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import woohoo.gameobjects.components.AIComponent;
import woohoo.gameobjects.components.AIComponent.AIMode;
import woohoo.gameobjects.components.PositionComponent;

//// REWRITE SPLIT INTO AIFOLLOWEVENT, AIMOVEEVENT, ETC


public class AIEvent implements Event<Entity>
{
	private AIComponent aiChar;
	private PositionComponent followChar; // Will be null unless mode is AIMode.Follow
	private AIMode mode;
	private float x;
	private float y;
	
	public AIEvent(AIComponent character, String aimode, PositionComponent follow, float X, float Y)
	{
		aiChar = character;
		followChar = follow;
		x = X;
		y = Y;
		
		mode = AIMode.fromString(aimode);
	}
	
	@Override
	public void activate()
	{
		aiChar.mode = mode;
		
		if (mode == AIMode.Follow)
		{
			aiChar.setTarget(followChar);
		}
		else if (mode == AIMode.MoveTo)
		{
			aiChar.setTarget(new Vector2(x, y));
		}
	}	
}

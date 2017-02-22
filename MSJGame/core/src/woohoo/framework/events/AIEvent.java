package woohoo.framework.events;

import com.badlogic.gdx.math.Vector2;
import woohoo.gameobjects.Character;
import woohoo.gameobjects.components.AIComponent.AIMode;

public class AIEvent implements Event<Character>
{
	private Character aiChar;
	private Character followChar; // Will be null unless mode is AIMode.Follow
	private AIMode mode;
	private float x;
	private float y;
	
	public AIEvent(Character character, String aimode, Character follow, float X, float Y)
	{
		aiChar = character;
		followChar = follow;
		x = X;
		y = Y;
		
		switch(aimode.toLowerCase())
		{
			case "follow":
				mode = AIMode.Follow;
				break;
			case "moveto":
				mode = AIMode.MoveTo;
				break;
			case "input":
				mode = AIMode.Input;
				break;
			case "stay":
			default:
				mode = AIMode.Stay;
				break;
		}
	}
	
	@Override
	public void activate()
	{
		aiChar.setAIMode(mode);
		
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

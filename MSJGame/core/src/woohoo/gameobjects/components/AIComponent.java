package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import woohoo.gameobjects.Character;
import woohoo.gameobjects.components.MapObjectComponent.Direction;

public class AIComponent implements Component
{
	public enum AIMode
	{
		Follow, Stay, MoveTo, Input
	}
	
	// Reference to player, change later to reference of viable targets to follow
	private Character targetChar;
	private Vector2 targetPos;
	
	private Direction nextDirection;
    private boolean lockDirection;
	private float timer;
	private float timeStep; // How often the AI should switch directions
	
	private final float DEFAULT_TIMESTEP = 0.5f;
	
	private AIMode mode;
	
	public AIComponent()
	{		
		mode = AIMode.Stay;
		timeStep = DEFAULT_TIMESTEP;
	}
	
	public void update(float delta, Vector2 currentPos)
	{
		timer += delta;
		
		if (timer > timeStep)
		{
			timer = 0;
			lockDirection = false;
		}
		
		// If moving towards a certain spot
		if (mode == AIMode.MoveTo)
		{
			float dX = currentPos.x - targetPos.x;
			float dY = currentPos.y - targetPos.y;

			if (Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2)) < 0.25f)
				mode = AIMode.Stay;
		}
	}
	
	private Direction getDirection(Vector2 current, Vector2 target)
	{
		// If timer is not up yet, return previous direction
        if (lockDirection) return nextDirection;
        		
		float dX = current.x - target.x;
		float dY = current.y - target.y;
		
		if (Math.abs(dX) > Math.abs(dY))
		{
			nextDirection = dX > 0 ? Direction.Left : Direction.Right;
		}
		else
		{
			nextDirection = dY > 0 ? Direction.Up : Direction.Down;
		}
		
		lockDirection = true;
        return nextDirection;
	}
	
	public Direction calculateDirection(Vector2 current)
	{
		switch(mode)
		{
			case Follow:
				return getDirection(current, targetChar.getPosition());
			case MoveTo:								
				return getDirection(current, targetPos);
			case Stay:
				return null;
			default:
				return getDirection(current, targetChar.getPosition());				
		}
	}
	
	public void setTargetCharacter(Character character)
	{
		targetChar = character;
	}
	
	public AIMode getAIMode()
	{
		return mode;
	}
	
	public void setAIMode(AIMode aimode)
	{
		mode = aimode;
	}
	
	public void setTargetPosition(Vector2 position)
	{
		targetPos = position;
	}
	
	public void setTimeStep(float newStep)
	{
		timeStep = newStep;
	}
	
	public void resetTimeStep()
	{
		timeStep = DEFAULT_TIMESTEP;
	}
}

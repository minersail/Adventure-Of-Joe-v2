package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import woohoo.gameobjects.Player;
import woohoo.gameobjects.components.MapObjectComponent.Direction;

public class AIComponent implements Component
{
	public enum AIMode
	{
		Follow, Stay, MoveTo
	}
	
	// Reference to player, change later to reference of viable targets to follow
	private Player player;
	private Vector2 targetPos;
	private Direction nextDirection;
    private boolean lockDirection;
	private float timer;
	private AIMode mode;
	
	public AIComponent()
	{		
		mode = AIMode.Stay;
	}
	
	public void update(float delta)
	{
		timer += delta;
		
		if (timer > 0.5) // How often the AI should switch directions
		{
			timer = 0;
			lockDirection = false;
		}
	}
	
	private Direction getDirection(Vector2 current, Vector2 target)
	{
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
				return getDirection(current, player.getPosition());
			case MoveTo:				
				float dX = current.x - targetPos.x;
				float dY = current.y - targetPos.y;

				if (Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2)) < 0.25f)
					mode = AIMode.Stay;
				
				return getDirection(current, targetPos);
			case Stay:
				return null;
			default:
				return getDirection(current, player.getPosition());				
		}
	}
	
	public void setPlayer(Player player)
	{
		this.player = player;
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
}

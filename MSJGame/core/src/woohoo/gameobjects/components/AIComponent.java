package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import woohoo.gameobjects.Player;
import woohoo.gameobjects.components.MapObjectComponent.Direction;

public class AIComponent implements Component
{
	// Reference to player, change later to reference of viable targets to follow
	private Player player;
	private Direction nextDirection;
    private boolean lockDirection;
	private float timer;
	
	public AIComponent()
	{		
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
	
	public Direction calculateDirection(Vector2 pos)
	{
        if (lockDirection) return nextDirection;
        
		Vector2 playerPos = player.getPosition();
		
		float dX = pos.x - playerPos.x;
		float dY = pos.y - playerPos.y;
		
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
	
	public void setPlayer(Player player)
	{
		this.player = player;
	}
}
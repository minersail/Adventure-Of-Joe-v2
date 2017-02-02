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
	
	public AIComponent()
	{		
	}
	
	public void update(float delta)
	{
	}
	
	public Direction calculateDirection(Vector2 pos)
	{
		Vector2 playerPos = player.getPosition();
		
		float dX = pos.x - playerPos.x;
		float dY = pos.y - playerPos.y;
		
		if (Math.abs(dX) > Math.abs(dY))
		{
			return dX > 0 ? Direction.Left : Direction.Right;
		}
		else
		{
			return dY > 0 ? Direction.Up : Direction.Down;
		}
	}
	
	public void setPlayer(Player player)
	{
		this.player = player;
	}
}

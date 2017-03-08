package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class MovementComponent implements Component
{
	// Only really used for player movement
	public enum Movement
	{
		None, Horizontal, Vertical
	}
	
	private final Vector2 position;
	private final Vector2 velocity;
	private Movement movement;
	
	public MovementComponent()
	{
		position = new Vector2(0, 0);
		velocity = new Vector2(0, 0);
	}
	
	public boolean isStopped()
	{
		return velocity.isZero(0.5f);
	}
	
	public void setPosition(float x, float y)
	{
		position.x = x;
		position.y = y;
	}
	
	public Vector2 getPosition()
	{
		return position;
	}
	
	public void setVelocity(float x, float y)
	{
		velocity.x = x;
		velocity.y = y;
	}
	
	public Vector2 getVelocity()
	{
		return velocity;
	}
	
	public void setMovement(Movement move)
	{
		movement = move;
	}
	
	public Movement getMovement()
	{
		return movement;
	}
}

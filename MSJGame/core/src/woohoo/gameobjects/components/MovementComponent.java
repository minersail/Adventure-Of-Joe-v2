package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class MovementComponent implements Component
{	
	// Only really used for player movement
	public enum Movement
	{
		None, Horizontal, Vertical
	}
	
	public Vector2 velocity;
	public Movement movement;
	public float speed;
	
	public MovementComponent(World world)
	{
		movement = Movement.None;
		velocity = new Vector2(0, 0);
		speed = 1;
	}
	
	public boolean isStopped(float tolerance)
	{
		return velocity.isZero(tolerance);
	}
}

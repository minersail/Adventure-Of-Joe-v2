package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class MovementComponent implements Component
{
	private Vector2 velocity;
	
	public MovementComponent(float speedX, float speedY)
	{
		velocity = new Vector2(speedX, speedY);
	}
	
	public Vector2 getSpeed()
	{
		return velocity;
	}
}

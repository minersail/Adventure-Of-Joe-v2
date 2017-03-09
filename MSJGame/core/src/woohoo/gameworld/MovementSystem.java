package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import woohoo.gameobjects.components.MapObjectComponent;
import woohoo.gameobjects.components.MovementComponent;

public class MovementSystem extends IteratingSystem
{
	public MovementSystem()
	{
		super(Family.all(MapObjectComponent.class, MovementComponent.class).get());
	}
	
	@Override
	public void update(float delta)
	{
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) 
	{
		MapObjectComponent mapObject = Mappers.mapObjects.get(entity);
		MovementComponent movement = Mappers.movements.get(entity);

		if (movement.isStopped() && mapObject.getAnimationState() == MapObjectComponent.AnimationState.Walking)
		{
			mapObject.setAnimationState(MapObjectComponent.AnimationState.Idle);
		}

		if (!movement.isStopped()) 
		{
			if (movement.getMovement() == MovementComponent.Movement.Horizontal)
			{
				if (movement.getVelocity().x > 0) {
					mapObject.setDirection(MapObjectComponent.Direction.Right);
				} else {
					mapObject.setDirection(MapObjectComponent.Direction.Left);
				}
			}
			else if (movement.getMovement() == MovementComponent.Movement.Vertical)
			{
				if (movement.getVelocity().y > 0) 
				{
					mapObject.setDirection(MapObjectComponent.Direction.Down);
				} else {
					mapObject.setDirection(MapObjectComponent.Direction.Up);
				}
			}
		}
	}
}

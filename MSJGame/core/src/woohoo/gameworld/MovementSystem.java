package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import woohoo.gameobjects.components.HitboxComponent;
import woohoo.gameobjects.components.MovementComponent;
import woohoo.gameobjects.components.MovementComponent.Direction;
import woohoo.gameobjects.components.PositionComponent;

public class MovementSystem extends IteratingSystem
{
	public MovementSystem()
	{
		super(Family.all(MovementComponent.class, PositionComponent.class, HitboxComponent.class).get());
	}
	
	@Override
	public void update(float delta)
	{
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) 
	{		
		MovementComponent movement = Mappers.movements.get(entity);
		HitboxComponent hitbox = Mappers.hitboxes.get(entity);
		PositionComponent position = Mappers.positions.get(entity);
		
		switch (movement.direction)
		{
			case Up:
				movement.velocity.add(0, -movement.speed);
				break;
			case Down:
				movement.velocity.add(0, movement.speed);
				break;
			case Left:
				movement.velocity.add(-movement.speed, 0);
				break;
			case Right:
				movement.velocity.add(movement.speed, 0);
				break;
			case None:
				movement.velocity.setZero();
				break;
		}
		
		// Disallow diagonal movement
		if (Direction.isVertical(movement.direction))
		{
			hitbox.mass.setLinearVelocity(new Vector2(0, movement.velocity.y));	
		}
		else if (Direction.isHorizontal(movement.direction))
		{
			hitbox.mass.setLinearVelocity(new Vector2(movement.velocity.x, 0));	
		}
		else
		{
			hitbox.mass.setLinearVelocity(movement.velocity);
		}
		
		position.position = hitbox.mass.getPosition().cpy();
	}
	
	public void stopAll()
	{
		for (Entity entity : getEntities())
		{
			Mappers.movements.get(entity).velocity = Vector2.Zero;
		}
	}
}

package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import woohoo.gameobjects.components.HitboxComponent;
import woohoo.gameobjects.components.MovementComponent;
import woohoo.gameobjects.components.PositionComponent;

public class MovementSystem extends IteratingSystem
{
	public MovementSystem()
	{
		super(Family.all(MovementComponent.class, PositionComponent.class, HitboxComponent.class).get());
	}
	
	public void initialize()
	{
		for (Entity entity : getEntities())
		{
			Mappers.hitboxes.get(entity).mass.getTransform().setPosition(Mappers.positions.get(entity).position.cpy());
		}
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
				movement.velocity.set(0, -movement.speed);
				break;
			case Down:
				movement.velocity.set(0, movement.speed);
				break;
			case Left:
				movement.velocity.set(-movement.speed, 0);
				break;
			case Right:
				movement.velocity.set(movement.speed, 0);
				break;
			case None:
				movement.velocity.setZero();
				break;
		}
			
		hitbox.mass.setLinearVelocity(movement.velocity);
		
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

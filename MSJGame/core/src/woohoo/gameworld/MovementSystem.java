package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import woohoo.gameobjects.components.AnimMapObjectComponent;
import woohoo.gameobjects.components.AnimMapObjectComponent.AnimationState;
import woohoo.gameobjects.components.HitboxComponent;
import woohoo.gameobjects.components.MovementComponent;
import woohoo.gameobjects.components.MovementComponent.Movement;
import woohoo.gameobjects.components.MapObjectComponent.Direction;
import woohoo.gameobjects.components.PositionComponent;

public class MovementSystem extends IteratingSystem
{
	public MovementSystem()
	{
		super(Family.all(AnimMapObjectComponent.class, MovementComponent.class, PositionComponent.class, HitboxComponent.class).get());
	}
	
	@Override
	public void update(float delta)
	{
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) 
	{		
		moveEntity(entity);
		setEntityDirection(entity);
		setEntityAnimation(entity);
	}
	
	public void setEntityDirection(Entity entity)
	{
		AnimMapObjectComponent mapObject = Mappers.animMapObjects.get(entity);
		MovementComponent movement = Mappers.movements.get(entity);
		
		if (!movement.isStopped(0.25f)) 
		{
			if (movement.movement == Movement.Horizontal)
			{
				if (movement.velocity.x > 0) { mapObject.direction = Direction.Right;
				} else {					   mapObject.direction = Direction.Left; }
			}
			else if (movement.movement == Movement.Vertical)
			{
				if (movement.velocity.y > 0) { mapObject.direction = Direction.Down;
				} else { 					   mapObject.direction = Direction.Up; }
			}
		}
	}
	
	public void moveEntity(Entity entity)
	{
		AnimMapObjectComponent mapObject = Mappers.animMapObjects.get(entity);
		MovementComponent movement = Mappers.movements.get(entity);
		HitboxComponent hitbox = Mappers.hitboxes.get(entity);
		PositionComponent position = Mappers.positions.get(entity);
		
		if (mapObject.direction == null) return;
		
		switch (mapObject.direction)
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
		}
		
		mapObject.setAnimationState(AnimationState.Walking);
		
		switch (movement.movement)
		{
			case Vertical:
				hitbox.mass.setLinearVelocity(new Vector2(0, movement.velocity.y));				
				break;
			case Horizontal:
				hitbox.mass.setLinearVelocity(new Vector2(movement.velocity.x, 0));				
				break;
			case None:
				hitbox.mass.setLinearVelocity(movement.velocity);
				break;
		}
		
		position.position = hitbox.mass.getPosition().cpy();
	}
	
	public void setEntityAnimation(Entity entity)
	{
		AnimMapObjectComponent mapObject = Mappers.animMapObjects.get(entity);
		MovementComponent movement = Mappers.movements.get(entity);
		
		if (movement.isStopped(0.25f) && mapObject.getAnimationState() == AnimationState.Walking)
		{
			mapObject.setAnimationState(AnimationState.Idle);
		}
	}
	
	public boolean isFacing(Entity current, Entity target)
	{
		AnimMapObjectComponent mapObject = Mappers.animMapObjects.get(current);
		AnimMapObjectComponent tMapObject = Mappers.animMapObjects.get(target);
		PositionComponent movement = Mappers.positions.get(current);
		PositionComponent tMovement = Mappers.positions.get(target);
		
		switch (mapObject.direction)
		{
			case Up:
				if (movement.position.x > tMovement.position.x - tMapObject.size.x / 2 &&	// Center must be within left and right bounds of other
					movement.position.x < tMovement.position.x + tMapObject.size.x / 2 && // (Basically a check to see if *this* is below or above other)
					movement.position.y > tMovement.position.y)						    // Center must be below other's center
				{
					return true;
				}
				break;
			case Down:
				if (movement.position.x > tMovement.position.x - tMapObject.size.x / 2 &&	// Center must be within left and right bounds of other
					movement.position.x < tMovement.position.x + tMapObject.size.x / 2 && // (Basically a check to see if *this* is below or above other)
					movement.position.y < tMovement.position.y)						    // Center must be above other's center
				{
					return true;
				}				
				break;
			case Left:
				if (movement.position.y > tMovement.position.y - tMapObject.size.y / 2 &&	// Center must be within top and bottom bounds of other
					movement.position.y < tMovement.position.y + tMapObject.size.y / 2 && // (Basically a check to see if *this* is to left or right of other)
					movement.position.x > tMovement.position.x)						    // Center must be left of other's center
				{
					return true;
				}				
				break;
			case Right:
				if (movement.position.y > tMovement.position.y - tMapObject.size.y / 2 && // Center must be within top and bottom bounds of other
					movement.position.y < tMovement.position.y + tMapObject.size.y / 2 && // (Basically a check to see if *this* is to left or right of other)
					movement.position.x < tMovement.position.x)						    // Center must be right of other's center
				{
					return true;
				}						
				break;
		}
		return false;
	}
}

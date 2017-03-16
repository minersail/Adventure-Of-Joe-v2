package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import woohoo.gameobjects.components.AnimMapObjectComponent;
import woohoo.gameobjects.components.AnimMapObjectComponent.AnimationState;
import woohoo.gameobjects.components.MovementComponent;
import woohoo.gameobjects.components.MovementComponent.Direction;
import woohoo.gameobjects.components.PositionComponent;
import woohoo.gameobjects.components.PositionComponent.Orientation;

public class AnimationSystem extends IteratingSystem
{
	public AnimationSystem()
	{
		super(Family.all(AnimMapObjectComponent.class, MovementComponent.class, PositionComponent.class).get());
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		AnimMapObjectComponent animated = Mappers.animMapObjects.get(entity);
		MovementComponent movement = Mappers.movements.get(entity);
		PositionComponent position = Mappers.positions.get(entity);
        
        if (movement.direction != Direction.None)
            position.orientation = Orientation.fromString(movement.direction.text()); // The enums have built in strings for conversion
		
		if (movement.isStopped(0.25f))
		{
			animated.setAnimationState(AnimationState.Idle);
		}
		else
		{
			animated.setAnimationState(AnimationState.Walking);
		}
		
		if (animated.animState == AnimationState.Death)
		{
			animated.animString = "death";
		}
		else
		{
			animated.animString = position.orientation.text() + "_" + animated.animState.text();	
		}
		
		animated.animationTime += deltaTime;
	}
	
	public boolean isFacing(Entity current, Entity target)
	{
		AnimMapObjectComponent tMapObject = Mappers.animMapObjects.get(target);
		PositionComponent position = Mappers.positions.get(current);
		PositionComponent tPosition = Mappers.positions.get(target);
		
		switch (position.orientation)
		{
			case North:
				if (position.position.x > tPosition.position.x - tMapObject.size.x / 2 &&	// Center must be within left and right bounds of other
					position.position.x < tPosition.position.x + tMapObject.size.x / 2 && // (Basically a check to see if *this* is below or above other)
					position.position.y > tPosition.position.y)						    // Center must be below other's center
				{
					return true;
				}
				break;
			case South:
				if (position.position.x > tPosition.position.x - tMapObject.size.x / 2 &&	// Center must be within left and right bounds of other
					position.position.x < tPosition.position.x + tMapObject.size.x / 2 && // (Basically a check to see if *this* is below or above other)
					position.position.y < tPosition.position.y)						    // Center must be above other's center
				{
					return true;
				}				
				break;
			case West:
				if (position.position.y > tPosition.position.y - tMapObject.size.y / 2 &&	// Center must be within top and bottom bounds of other
					position.position.y < tPosition.position.y + tMapObject.size.y / 2 && // (Basically a check to see if *this* is to left or right of other)
					position.position.x > tPosition.position.x)						    // Center must be left of other's center
				{
					return true;
				}				
				break;
			case East:
				if (position.position.y > tPosition.position.y - tMapObject.size.y / 2 && // Center must be within top and bottom bounds of other
					position.position.y < tPosition.position.y + tMapObject.size.y / 2 && // (Basically a check to see if *this* is to left or right of other)
					position.position.x < tPosition.position.x)						    // Center must be right of other's center
				{
					return true;
				}						
				break;
		}
		return false;
	}
}

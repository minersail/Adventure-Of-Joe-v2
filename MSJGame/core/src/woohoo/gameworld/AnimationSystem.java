package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import woohoo.gameobjects.components.AnimMapObjectComponent;
import woohoo.gameobjects.components.MovementComponent;
import woohoo.gameobjects.components.PositionComponent;

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
        
		animated.getAnimationState().animate(deltaTime, entity); // Run animation code based on current animation state
		animated.animString = animated.getAnimationState().getAnimString(entity); // animString must be set here, where the entity is exposed
	}
}

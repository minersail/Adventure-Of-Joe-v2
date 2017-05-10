package woohoo.ai.aicommands;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.BodyDef;
import woohoo.gameobjects.components.HitboxComponent;
import woohoo.gameobjects.components.MovementComponent;
import woohoo.gameworld.Mappers;

public class AIIdleCommand implements AICommand
{
	@Override
	public boolean run(Entity entity)
	{
		return false; // literally do nothing, and continue to do nothing
	}

	@Override
	public void enter(Entity entity)
	{
		if (Mappers.hitboxes.has(entity))
		{
			HitboxComponent hitbox = Mappers.hitboxes.get(entity);			
			hitbox.mass.setType(BodyDef.BodyType.StaticBody); // Still entities will not be pushable
		}
		Mappers.movements.get(entity).direction = MovementComponent.Direction.None;
	}

	@Override
	public void exit(Entity entity)
	{
		if (Mappers.hitboxes.has(entity))
		{
			HitboxComponent hitbox = Mappers.hitboxes.get(entity);			
			hitbox.mass.setType(BodyDef.BodyType.DynamicBody); // Allow entity to move
		}
	}
}

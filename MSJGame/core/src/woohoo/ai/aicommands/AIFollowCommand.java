package woohoo.ai.aicommands;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.BodyDef;
import woohoo.gameobjects.components.AIComponent;
import woohoo.gameobjects.components.MovementComponent;
import woohoo.gameobjects.components.PositionComponent;
import woohoo.gameworld.Mappers;

public class AIFollowCommand implements AICommand
{
	PositionComponent target;
	
	public AIFollowCommand(PositionComponent other)
	{
		target = other;
	}
	
	@Override
	public boolean run(Entity entity)
	{		
		PositionComponent pos = Mappers.positions.get(entity);
		MovementComponent movement = Mappers.movements.get(entity);
		AIComponent ai = Mappers.ai.get(entity);
		
		movement.direction = ai.getDirectionFromPath(pos.position, target.position);
		
		return false; // Follow has no finish condition; must be exited out of externally
	}

	@Override
	public void enter(Entity entity)
	{
		if (Mappers.hitboxes.has(entity))
		{
			Mappers.hitboxes.get(entity).mass.setType(BodyDef.BodyType.DynamicBody);
		}
	}

	@Override
	public void exit(Entity entity)
	{
	}
}

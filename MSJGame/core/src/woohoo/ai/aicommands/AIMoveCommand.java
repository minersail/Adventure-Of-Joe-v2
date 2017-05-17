package woohoo.ai.aicommands;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import woohoo.gameworld.components.AIComponent;
import woohoo.gameworld.components.MovementComponent;
import woohoo.gameworld.components.PositionComponent;
import woohoo.gameworld.Mappers;

public class AIMoveCommand implements AICommand
{	
	private Vector2 target;
	private float threshold; // Distance to the target before move is considered complete
	
	public AIMoveCommand(Vector2 targetPos)
	{
		this(targetPos, 0.5f);
	}
	
	public AIMoveCommand(Vector2 targetPos, float thresholdDist)
	{
		target = targetPos;
		threshold = thresholdDist;
	}
	
	@Override
	public boolean run(Entity entity)
	{		
		PositionComponent pos = Mappers.positions.get(entity);
		MovementComponent movement = Mappers.movements.get(entity);
		AIComponent ai = Mappers.ai.get(entity);
		
		movement.direction = ai.getDirectionFromPath(pos.position, target);
		
		return pos.position.dst(target) < threshold; // Finished when within the threshold
	}

	@Override
	public void enter(Entity entity)
	{
	}

	@Override
	public void exit(Entity entity)
	{
	}
}

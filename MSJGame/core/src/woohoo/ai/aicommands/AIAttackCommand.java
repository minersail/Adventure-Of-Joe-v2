package woohoo.ai.aicommands;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import woohoo.gameobjects.components.MovementComponent.Direction;
import woohoo.gameobjects.components.PositionComponent.Orientation;
import woohoo.gameworld.Mappers;

public class AIAttackCommand implements AICommand
{	
	Orientation attackDirection;
	
	public AIAttackCommand(Orientation direction)
	{
		attackDirection = direction;
	}
	
	@Override
	public boolean run(Entity entity)
	{		
		if (Mappers.weapons.has(entity))
			Mappers.weapons.get(entity).spawnProjectile();
		else
			Gdx.app.error("ERROR", "Entity has no weapon!");
			
		return true; // Immediately finishes
	}

	@Override
	public void enter(Entity entity)
	{
		Mappers.positions.get(entity).orientation = attackDirection;
		Mappers.movements.get(entity).direction = Direction.None;
	}

	@Override
	public void exit(Entity entity)
	{
	}
}

package woohoo.ai.aipatterns;

import com.badlogic.ashley.core.Entity;
import woohoo.ai.aicommands.AIAttackCommand;
import woohoo.ai.aicommands.AIFollowCommand;
import woohoo.gameworld.components.PositionComponent;
import woohoo.gameworld.components.PositionComponent.Orientation;
import woohoo.gameworld.AIStateSystem;
import woohoo.gameworld.Mappers;

public class AttackChasePattern extends AIPattern
{
	private PositionComponent attacked;
	private String attackedStr;
	
	public AttackChasePattern(String attackedChar)
	{
		attackedStr = attackedChar;
	}
	
	@Override
	public void link(AIStateSystem system)
	{
		attacked = Mappers.positions.get(system.getEntity(attackedStr));
	}
	
	@Override
	public void initialize(Entity entity)
	{
		super.setCommand(new AIFollowCommand(attacked), entity);
	}
	
	@Override
	public void run(Entity entity, float deltaTime)
	{		
		if (getCommand().run(entity))
		{
			// Attack was successful
			super.setCommand(new AIFollowCommand(attacked), entity);
		}
		
		if (Mappers.positions.get(entity).position.dst(attacked.position) < 1.5f) // Change later to percentage of maximum range
		{
			Orientation direction = Orientation.fromVectors(Mappers.positions.get(entity).position, attacked.position);
			
			super.setCommand(new AIAttackCommand(direction), entity);
		}
	}
}

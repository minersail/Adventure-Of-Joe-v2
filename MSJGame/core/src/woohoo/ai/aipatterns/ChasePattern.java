package woohoo.ai.aipatterns;

import com.badlogic.ashley.core.Entity;
import woohoo.ai.aicommands.AIFollowCommand;
import woohoo.gameworld.components.PositionComponent;
import woohoo.gameworld.AIStateSystem;
import woohoo.gameworld.Mappers;

public class ChasePattern extends AIPattern
{	
	private PositionComponent target;
	private String targetStr;
	
	public ChasePattern(String position)
	{		
		targetStr = position;
	}
	
	public ChasePattern(PositionComponent targ)
	{		
		target = targ;
	}

	@Override
	public void link(AIStateSystem system)
	{	
		target = Mappers.positions.get(system.getEntity(targetStr));
	}

	@Override
	public void initialize(Entity entity)
	{
		super.setCommand(new AIFollowCommand(target), entity);
	}
	
	@Override
	public void run(Entity entity, float deltaTime)
	{		
		getCommand().run(entity);
	}
}

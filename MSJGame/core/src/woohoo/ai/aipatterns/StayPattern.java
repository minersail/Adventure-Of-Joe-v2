package woohoo.ai.aipatterns;

import com.badlogic.ashley.core.Entity;
import woohoo.ai.aicommands.AIIdleCommand;

public class StayPattern extends AIPattern
{	
	public StayPattern()
	{		
	}

	@Override
	public void initialize(Entity entity)
	{
		super.setCommand(new AIIdleCommand(), entity);
	}
	
	@Override
	public void run(Entity entity, float deltaTime)
	{		
		getCommand().run(entity);
	}
}

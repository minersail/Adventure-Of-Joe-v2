package woohoo.ai.aipatterns;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import woohoo.ai.aicommands.AIIdleCommand;
import woohoo.ai.aicommands.AIMoveCommand;

public class MovePattern extends AIPattern
{
	private Vector2 target;
	
	public MovePattern(Vector2 targetLoc)
	{		
		target = targetLoc;
	}

	@Override
	public void initialize(Entity entity)
	{
		super.setCommand(new AIMoveCommand(target), entity);
	}
	
	@Override
	public void run(Entity entity, float deltaTime)
	{		
		if (getCommand().run(entity))
		{
			super.setCommand(new AIIdleCommand(), entity);
		}
	}
}

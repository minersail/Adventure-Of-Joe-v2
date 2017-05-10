package woohoo.ai.aipatterns;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import woohoo.ai.Node;
import woohoo.ai.aicommands.AIIdleCommand;
import woohoo.ai.aicommands.AIMoveCommand;
import woohoo.gameobjects.components.AIComponent;
import woohoo.gameworld.Mappers;

public class WanderPattern extends AIPattern
{
	private float stillTimer; 
	private float stillMax; // How long the entity should stand still after reaching target location
	
	public WanderPattern()
	{
		this(5);
	}
	
	public WanderPattern(int max)
	{
		stillTimer = 0;
		stillMax = max;
	}

	@Override
	public void initialize(Entity entity)
	{
		super.setCommand(new AIMoveCommand(generateRandomLoc(Mappers.ai.get(entity))), entity);
	}

	@Override
	public void run(Entity entity, float deltaTime)
	{						
		if (getCommand().run(entity))
		{
			// Will only run on the tick that a MoveCommand finishes
			super.setCommand(new AIIdleCommand(), entity);
		}
		
		if (getCommand() instanceof AIIdleCommand) // Still timer has been started
		{
			stillTimer += deltaTime;	
			if (stillTimer >= stillMax) // Timer reaches max; generate new random location and reset timer
			{
				super.setCommand(new AIMoveCommand(generateRandomLoc(Mappers.ai.get(entity))), entity);
				stillTimer = 0;
			}
		}
	}
	
	private Vector2 generateRandomLoc(AIComponent ai)
	{
		Node node = ai.getAIMap().getRandomNode();
		
		return new Vector2(node.x, node.y);
	}
}

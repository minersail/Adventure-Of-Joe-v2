package woohoo.ai.aipatterns;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import woohoo.ai.aicommands.AIFollowCommand;
import woohoo.ai.aicommands.AIMoveCommand;
import woohoo.gameworld.components.LOSComponent;
import woohoo.gameworld.Mappers;

public class ChaseSentryPattern extends AIPattern
{
	private Array<Vector2> positions;
	private int index;
	private float timeEntityNotSeen;
	
	public ChaseSentryPattern(Array<Vector2> sentryLocations)
	{
		positions = sentryLocations;
		index = 0;		
	}

	@Override
	public void initialize(Entity entity)
	{
		super.setCommand(new AIMoveCommand(positions.get(index)), entity);
	}
	
	@Override
	public void run(Entity entity, float deltaTime)
	{		
		if (getCommand().run(entity))
		{ // Guaranteed to only return true if in MoveCommand state, as FollowCommand has no return true
			index++;
			if (index >= positions.size)
				index = 0;
			
			super.setCommand(new AIMoveCommand(positions.get(index)), entity);			
		}
		
		LOSComponent los = Mappers.sightLines.get(entity);		
		if (los.entitySeen)
		{
			super.setCommand(new AIFollowCommand(Mappers.positions.get(los.sightedEntity)), entity);
			timeEntityNotSeen = 0;
		}
		else
		{
			timeEntityNotSeen += deltaTime;
			if (timeEntityNotSeen > 5) // Lost sight for more than 5 secs
			{
				super.setCommand(new AIMoveCommand(positions.get(index)), entity);
			}
		}		
	}
}

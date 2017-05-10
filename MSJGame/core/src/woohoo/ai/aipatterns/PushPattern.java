package woohoo.ai.aipatterns;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
//import woohoo.ai.Node;
import woohoo.ai.aicommands.AIIdleCommand;
import woohoo.ai.aicommands.AIMoveCommand;
import woohoo.gameobjects.components.PositionComponent;
import woohoo.gameobjects.components.PositionComponent.Orientation;
import woohoo.gameworld.AIStateSystem;
import woohoo.gameworld.Mappers;

public class PushPattern extends AIPattern
{
	private String pushedStr;
	private PositionComponent pushed;
	private Vector2 target;
	
	//private Node removedNode; // Position of the pushed entity; Remove for pathfinding when moving in place to push entity
	
	public PushPattern(String pushedEntity, Vector2 targetLoc)
	{
		pushedStr = pushedEntity;
		target = targetLoc;
	}
	
	@Override
	public void link(AIStateSystem system)
	{
		pushed = Mappers.positions.get(system.getEntity(pushedStr));
	}

	@Override
	public void initialize(Entity entity)
	{
		moveToPush(entity);
	}
	
	@Override
	public void run(Entity entity, float deltaTime)
	{		
		if (getCommand().run(entity))
		{
			if (pushed.position.dst(target) < 0.5f)
			{
				super.setCommand(new AIIdleCommand(), entity);
			}
			else
			{
				super.setCommand(new AIMoveCommand(target), entity);
			}
			
			//if (removedNode != null) Mappers.ai.get(entity).getAIMap().addNode(removedNode);
		}
		if (Mappers.positions.get(entity).position.dst(pushed.position) > 1.5f)
		{
			moveToPush(entity);
		}
	}
	
	private void moveToPush(Entity entity)
	{
		PositionComponent pos = Mappers.positions.get(entity);
		Orientation orientation = Orientation.fromVectors(target, pos.position);
				
		//removedNode = Mappers.ai.get(entity).getAIMap().removeNode((int)pushed.position.x, (int)pushed.position.y);
		super.setCommand(new AIMoveCommand(pushed.position.cpy().add(orientation.getVector())), entity);
	}
}

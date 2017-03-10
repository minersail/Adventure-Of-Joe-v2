package woohoo.ai.aistates;

import woohoo.gameobjects.components.AIComponent;
import woohoo.gameobjects.components.MapObjectComponent;
import woohoo.gameobjects.components.PositionComponent;

public class StayState implements AIState
{
	public StayState()
	{
	}
	
	@Override
	public MapObjectComponent.Direction getDirection(AIComponent ai, PositionComponent pos)
	{
		return null;
	}
}

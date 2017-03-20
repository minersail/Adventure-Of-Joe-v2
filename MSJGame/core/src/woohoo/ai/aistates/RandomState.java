package woohoo.ai.aistates;

import woohoo.gameobjects.components.AIComponent;
import woohoo.gameobjects.components.MovementComponent.Direction;
import woohoo.gameobjects.components.PositionComponent;

public class RandomState implements AIState
{
	public RandomState()
	{
	}

	@Override
	public Direction getDirection(AIComponent ai, PositionComponent pos)
	{		
		int random = (int)Math.floor(Math.random() * Direction.values().length + 1);
		
		switch(random)
		{
			case 0:
				return Direction.Up;
			case 1:
				return Direction.Down;
			case 2:
				return Direction.Left;
			case 3:
				return Direction.Right;
			case 4:
			default:
				return Direction.None;
		}
	}
}

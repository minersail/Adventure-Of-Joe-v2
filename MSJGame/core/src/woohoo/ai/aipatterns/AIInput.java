package woohoo.ai.aipatterns;

import woohoo.gameobjects.components.MovementComponent.Direction;

public class AIInput
{
	private Direction direction;
	private boolean attack;
	
	public AIInput(Direction dir, boolean attacks)
	{
		direction = dir;
		attack = attacks;
	}
	
	public Direction getDirection()
	{
		return direction;
	}
	
	public boolean attacks()
	{
		return attack;
	}
}

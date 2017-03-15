package woohoo.framework.input;

import com.badlogic.ashley.core.Entity;
import woohoo.gameobjects.components.MovementComponent;
import woohoo.gameobjects.components.PositionComponent;
import woohoo.gameworld.Mappers;

public class MoveRightState implements InputState
{
	@Override
	public void execute(Entity entity)
	{
		if (!Mappers.movements.has(entity) || !Mappers.positions.has(entity)) return;

		Mappers.movements.get(entity).direction = MovementComponent.Direction.Right;
        Mappers.positions.get(entity).orientation = PositionComponent.Orientation.East;
	}
}

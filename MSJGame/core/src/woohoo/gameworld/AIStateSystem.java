package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import woohoo.gameobjects.components.AIComponent;
import woohoo.gameobjects.components.MapObjectComponent;
import woohoo.gameobjects.components.PositionComponent;

public class AIStateSystem extends IteratingSystem
{
	public AIStateSystem()
	{
		super(Family.all(MapObjectComponent.class, PositionComponent.class, AIComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		
	}
}

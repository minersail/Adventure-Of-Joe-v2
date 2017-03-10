package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import woohoo.gameobjects.components.AIComponent;
import woohoo.gameobjects.components.HitboxComponent;
import woohoo.gameobjects.components.MapObjectComponent;

public class AIStateSystem extends IteratingSystem
{
	public AIStateSystem()
	{
		super(Family.all(MapObjectComponent.class, HitboxComponent.class, AIComponent.class).get());
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		
	}
}

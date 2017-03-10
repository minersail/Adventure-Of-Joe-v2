package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import woohoo.gameobjects.components.GateComponent;
import woohoo.gameobjects.components.PositionComponent;

public class GateSystem extends IteratingSystem
{
	public GateSystem()
	{
		super(Family.all(GateComponent.class, PositionComponent.class).get());
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) 
	{
		
	}	
}

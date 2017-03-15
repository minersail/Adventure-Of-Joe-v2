package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import woohoo.gameobjects.components.LOSComponent;
import woohoo.gameobjects.components.PositionComponent;

public class LineOfSightSystem extends IteratingSystem
{
	public LineOfSightSystem()
	{
		super(Family.all(LOSComponent.class, PositionComponent.class).get());
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		LOSComponent los = Mappers.sightLines.get(entity);
		PositionComponent pos = Mappers.positions.get(entity);
		
		los.mass.getTransform().setPosition(pos.position.cpy());
		los.rotate(pos.orientation);
	}
}

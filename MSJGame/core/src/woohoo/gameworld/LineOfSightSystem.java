package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import woohoo.gameworld.components.LOSComponent;
import woohoo.gameworld.components.PositionComponent;
import woohoo.screens.PlayingScreen;

public class LineOfSightSystem extends IteratingSystem
{
	PlayingScreen screen;
	
	public LineOfSightSystem(PlayingScreen scr)
	{
		super(Family.all(LOSComponent.class, PositionComponent.class).get());
		
		screen = scr;
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		LOSComponent los = Mappers.sightLines.get(entity);
		PositionComponent pos = Mappers.positions.get(entity);
		
		// If hitbox exists use hitbox position
		if (Mappers.hitboxes.has(entity))
			los.mass.setTransform(Mappers.hitboxes.get(entity).mass.getPosition(), 0);
		else // Use default position
			los.mass.setTransform(pos.position.cpy(), 0);
		
		los.rotate(pos.orientation);
	}
}

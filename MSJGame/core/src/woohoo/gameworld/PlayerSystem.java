package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import woohoo.gameobjects.components.PlayerComponent;

public class PlayerSystem extends IteratingSystem
{
	public PlayerSystem()
	{
		super(Family.all(PlayerComponent.class).get());
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) 
	{
		Mappers.players.get(entity).touchedItems.clear();
	}	
}

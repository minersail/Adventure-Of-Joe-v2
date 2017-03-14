package woohoo.framework.input;

import com.badlogic.ashley.core.Entity;
import woohoo.gameworld.Mappers;

public class PickupItemCommand implements InputCommand
{
	@Override
	public void execute(Entity entity)
	{
		if (Mappers.players.get(entity) == null) return;
		
		for (Entity item : Mappers.players.get(entity).touchedItems)
		{
			// Remove item and stuff
		}
	}
}

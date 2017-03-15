package woohoo.framework.input;

import com.badlogic.ashley.core.Entity;
import woohoo.framework.InventoryManager;
import woohoo.gameworld.GameEngine;
import woohoo.gameworld.Mappers;

public class PickupItemCommand implements InputCommand
{
	InventoryManager manager;
	GameEngine engine;
	
	public PickupItemCommand(InventoryManager im, GameEngine ge)
	{
		manager = im;
		engine = ge;
	}
	
	@Override
	public void execute(Entity entity)
	{
		if (Mappers.players.get(entity) == null) return;
		
		for (Entity item : Mappers.players.get(entity).touchedItems)
		{
			manager.addItem(Mappers.inventories.get(entity), item);
			engine.removeEntity(item);
		}
	}
}

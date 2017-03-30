package woohoo.framework.input;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import woohoo.framework.InventoryManager;
import woohoo.gameobjects.components.DialogueComponent;
import woohoo.gameobjects.components.InventoryComponent;
import woohoo.gameobjects.components.PositionComponent;
import woohoo.gameworld.AnimationSystem;
import woohoo.gameworld.GameEngine;
import woohoo.gameworld.Mappers;

/**
 * Command to check for other entities with inventory component
 * If no entities are found, the inventory with only the player's will open
 * If entities are found, the dual screen will pop up with both player and other entity's inventories
 * @author jordan
 */
public class InventoryOpenCommand implements InputCommand
{
	private InventoryManager manager;
	private GameEngine engine;
	
	public InventoryOpenCommand(InventoryManager im, GameEngine ge)
	{
		manager = im;
		engine = ge;
	}
	
	@Override
	public void execute(Entity player)
	{
		for (Entity inventoryEntity : engine.getEntitiesFor(Family.all(InventoryComponent.class).get()))
		{
			if (inventoryEntity == player) continue; // Don't want player checking himself
			
			PositionComponent playerPos = player.getComponent(PositionComponent.class);
			PositionComponent invenPos = inventoryEntity.getComponent(PositionComponent.class);
			
			// Other entity detected; Show dual screen with both player and other's inventories
			if (engine.getSystem(AnimationSystem.class).isFacing(player, inventoryEntity) && playerPos.position.dst(invenPos.position) < 1.5f)
			{
				manager.showInventory(Mappers.inventories.get(inventoryEntity));
				return;
			}
		}
		
		// No other entities were found; Show only player's inventory
		manager.showInventory();
	}
}

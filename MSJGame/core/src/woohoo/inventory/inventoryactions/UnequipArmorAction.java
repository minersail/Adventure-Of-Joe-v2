package woohoo.inventory.inventoryactions;

import woohoo.framework.InventoryManager;

public class UnequipArmorAction implements InventoryAction
{
	@Override
	public void run(InventoryManager im)
	{
		im.unequipArmor();
	}
}

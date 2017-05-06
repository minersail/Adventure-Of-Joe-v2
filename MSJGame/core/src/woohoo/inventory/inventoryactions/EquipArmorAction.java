package woohoo.inventory.inventoryactions;

import com.badlogic.ashley.core.Entity;
import woohoo.framework.InventoryManager;

public class EquipArmorAction implements InventoryAction
{
	private Entity equipped;
	
	public EquipArmorAction(Entity equip)
	{
		equipped = equip;
	}
	
	@Override
	public void run(InventoryManager im)
	{
		im.equipArmor(equipped);
	}
}

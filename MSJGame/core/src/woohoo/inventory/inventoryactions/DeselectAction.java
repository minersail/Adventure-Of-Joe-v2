package woohoo.inventory.inventoryactions;

import com.badlogic.gdx.graphics.Color;
import woohoo.framework.InventoryManager;

public class DeselectAction implements InventoryAction
{
	@Override
	public void run(InventoryManager im)
	{
		im.getWeaponSlot().setColor(Color.WHITE);
	}
}

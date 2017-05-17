package woohoo.inventory.inventoryactions;

import com.badlogic.gdx.graphics.Color;
import woohoo.framework.InventoryManager;
import woohoo.gameworld.components.ItemDataComponent;
import woohoo.gameworld.Mappers;
import woohoo.inventory.InventorySlot;

public class SelectAction implements InventoryAction
{
	private InventorySlot selected;
	
	public SelectAction(InventorySlot select)
	{
		selected = select;
	}
	
	@Override
	public void run(InventoryManager im)
	{		
		if (selected.getItem() == null) return;
		
		if (Mappers.items.get(selected.getItem()).type != ItemDataComponent.ItemType.Weapon)// Grey out boxes this item can't be placed in
		{
			im.getWeaponSlot().setColor(Color.DARK_GRAY);
		}
		
		if (Mappers.items.get(selected.getItem()).type != ItemDataComponent.ItemType.Armor)// Grey out boxes this item can't be placed in
		{
			im.getArmorSlot().setColor(Color.DARK_GRAY);
		}
	}
}

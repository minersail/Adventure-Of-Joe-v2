package woohoo.inventory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.badlogic.gdx.utils.Array;
import woohoo.framework.InventoryManager;
import woohoo.inventory.inventoryactions.BuyAction;
import woohoo.inventory.inventoryactions.DeselectAction;
import woohoo.inventory.inventoryactions.InventoryAction;
import woohoo.inventory.inventoryactions.SellAction;
import woohoo.inventory.inventoryactions.UnequipWeaponAction;

public class InventoryTarget extends Target implements SlotListener
{
	protected Array<InventoryAction> actions;
	
	public InventoryTarget(InventorySlot slot)
	{
		super(slot);
		actions = new Array<>();
	}

	/*
	Called when a payload is moved over a target
	*/
	@Override
	public boolean drag(Source source, Payload payload, float x, float y, int pointer) 
	{
		return true;
	}

	/*
	Called when a payload moves out of a target's bounds
	*/
	@Override
	public void reset(Source source, Payload payload) {}

	/*
	Called when the payload is dropped on a target		
	*/
	@Override
	public void drop(Source source, Payload payload, float x, float y, int pointer) 
	{			
		// Switch the images, items, item counts, etc. of the two inventory slots
		InventorySlot sourceSlot = (InventorySlot)source.getActor();
		InventorySlot targetSlot = (InventorySlot)getActor();

		if (sourceSlot.getType() == InventoryManager.SlotType.Weapon) // If the item was dragged from the weapon slot
		{
			if (targetSlot.getItem() != null)
			{	// Prevent non-weapons from going into weapon slot if weapon switches with them
				sourceSlot.setDragged(false);
				return;
			}
			else
			{
				actions.add(new UnequipWeaponAction());
			}
		} // Dropped from player's inventory to the other's
		else if (sourceSlot.getType() == InventoryManager.SlotType.Player && targetSlot.getType() == InventoryManager.SlotType.Other)
		{
			Entity item = sourceSlot.getItem();
			actions.add(new SellAction(item));
		} // Dropped from other's inventory to the player's
		else if (sourceSlot.getType() == InventoryManager.SlotType.Other && targetSlot.getType() == InventoryManager.SlotType.Player)
		{
			Entity item = sourceSlot.getItem();
			actions.add(new BuyAction(item));
		}

		Image sourceImage = sourceSlot.getImage();
		Image targetImage = targetSlot.getImage();

		Entity sourceItem = sourceSlot.getItem();
		Entity targetItem = targetSlot.getItem();

		// Switch items
		sourceSlot.setImage(targetImage).setItem(targetItem).setCount(targetSlot.getCount()).setDragged(false);
		targetSlot.setImage(sourceImage).setItem(sourceItem).setCount(sourceSlot.getCount());

		actions.add(new DeselectAction(sourceItem));
	}

	@Override
	public Array<InventoryAction> getActions()
	{
		return actions;
	}
}
package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import java.util.ArrayList;
import woohoo.gameobjects.Item;

public class InventoryComponent implements Component
{
	private ArrayList<Item> items;
	
	public InventoryComponent()
	{
		items = new ArrayList<>();
	}
	
	public void update(float delta)
	{
	}
	
	public void addItem(Item item)
	{
		items.add(item);
	}
	
	public void removeItem(Item item)
	{
		items.remove(item);
	}
	
	public ArrayList<Item> getItems()
	{
		return items;
	}
}

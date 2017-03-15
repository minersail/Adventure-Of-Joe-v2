package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import java.util.ArrayList;

public class InventoryComponent implements Component
{
	private ArrayList<Entity> items;
	
	public InventoryComponent()
	{
		items = new ArrayList<>();
	}
	
	public void update(float delta)
	{
	}
	
	public void addItem(Entity item)
	{
		items.add(item);
	}
	
	public void removeItem(Entity item)
	{
		items.remove(item);
	}
	
	public ArrayList<Entity> getItems()
	{
		return items;
	}
}

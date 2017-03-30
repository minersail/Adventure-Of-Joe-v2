package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import java.util.ArrayList;

public class InventoryComponent implements Component
{
	private ArrayList<Entity> items;
	public int id; // Inventory id to link to an id in inventories.xml
	
	public InventoryComponent(int inventoryID)
	{
		items = new ArrayList<>();
		id = inventoryID;
		
		FileHandle handle = Gdx.files.local("data/inventories.xml");
        
        XmlReader xml = new XmlReader();
        XmlReader.Element root = xml.parse(handle.readString());
		XmlReader.Element inventory = root.getChild(id);
        
        for (XmlReader.Element e : inventory.getChildrenByName("item"))
        {	
            Entity item = new Entity();
			ItemDataComponent itemData = new ItemDataComponent(e.getChildByName("metadata").getAttributes());
			item.add(itemData);
			
            items.add(item);
        }
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

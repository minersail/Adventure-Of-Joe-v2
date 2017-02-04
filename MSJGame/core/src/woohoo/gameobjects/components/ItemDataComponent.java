package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.ObjectMap;

public class ItemDataComponent implements Component
{    
    public enum ItemType // Used in ItemMetaData
    {
        Item, Weapon
    }
    
    private ItemType type;
    private ObjectMap metaData;
    
    public ItemDataComponent(ObjectMap element)
    {
		if (element == null)
		{
			metaData = new ObjectMap();
			type = ItemType.Item;
		}
		else
		{
			metaData = element;
			setType((String)element.get("type"));
		}
    }
    
    public ItemType getType()
    {
        return type;
    }
    
    public void setType(ItemType type)
    {
        this.type = type;
		
		metaData.put("type", type);
    }
	
	public void setType(String str)
    {
        switch (str.toLowerCase())
		{
			case "weapon":
				setType(ItemType.Weapon);
				break;
			case "item":
			default:
				setType(ItemType.Item);
				break;
		}
    }
    
    public ObjectMap getMetaData()
    {
        return metaData;
    }            
}

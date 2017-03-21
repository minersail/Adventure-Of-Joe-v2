package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.ObjectMap;

public class ItemDataComponent implements Component
{    
    public enum ItemType // Used in ItemMetaData
    {
        Item("item"),
		Weapon("weapon");
		
		private String text;
		
		ItemType(String str)
		{
			text = str;
		}
		
		public String text()
		{
			return text;
		}
		
		public static ItemType fromString(String str) 
		{
			if (str == null) return Item;
			
			for (ItemType b : ItemType.values()) 
			{
				if (b.text.equalsIgnoreCase(str))
				{
					return b;
				}
			}
			throw new IllegalArgumentException("No ItemType with text " + str + " found.");
		}
    }
    
    public ItemType type;
    public ObjectMap metaData;
    
    public ItemDataComponent(ObjectMap element)
    {
		metaData = element;
		type = ItemType.fromString((String)element.get("type"));
    }          
}

package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.XmlReader.Element;

public class ItemDataComponent implements Component
{    
    public enum ItemType // Used in ItemMetaData
    {
        Item, Weapon
    }
    
    private ItemType type;
    private Element metaData;
    
    public ItemDataComponent()
    {
        metaData = null;
        type = ItemType.Item;
    }
    
    public ItemDataComponent(Element element)
    {
        metaData = element;
        switch (element.get("type"))
        {
            case "weapon":
                type = ItemType.Weapon;
                break;
            default:
                type = ItemType.Item;
                break;
        }
    }
    
    public ItemType getType()
    {
        return type;
    }
    
    public void setType(ItemType type)
    {
        this.type = type;
    }
    
    public Element getMetaData()
    {
        return metaData;
    }            
}

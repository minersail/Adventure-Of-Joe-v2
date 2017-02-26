package woohoo.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ObjectMap;
import woohoo.framework.contactcommands.SensorContact;
import woohoo.gameobjects.components.ItemDataComponent;
import woohoo.gameobjects.components.ItemDataComponent.ItemType;
import woohoo.gameobjects.components.MapObjectComponent;
import woohoo.gameobjects.components.SensorComponent;
import woohoo.screens.PlayingScreen.WBodyType;

public class Item extends BaseEntity
{    
	private SensorComponent sensor;
	private MapObjectComponent mapObject;
    private ItemDataComponent itemData;
	
    /*
    For items created with metadata
    */
	public Item(TextureRegion region, ObjectMap data)
	{		
		mapObject = new MapObjectComponent(region);
		sensor = new SensorComponent(WBodyType.Item);
        itemData = new ItemDataComponent(data);
		
		sensor.setContactData(new SensorContact(sensor, WBodyType.Player));
		
		super.add(mapObject);
        super.add(sensor);
        super.add(itemData);
	} 
	
	/*
    For items created without metadata
    */
    public Item(TextureRegion region)
	{		
		this(region, null);
	}
	
    public Item(Texture texture)
	{		
		this(new TextureRegion(texture));
	}
	
	public Item(Texture texture, ObjectMap data)
	{		
		this(new TextureRegion(texture), data);
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		
		sensor.update(delta);
		mapObject.update(delta, sensor.getPosition());
	}
	
	public void setPosition(float x, float y)
	{
		sensor.setPosition(x, y);
	}
	
	public void flipImage()
	{
		TextureRegion region = mapObject.getTextureRegion();
		region.flip(false, true);
		mapObject.setTextureRegion(region);
	}
    
    public boolean isWeapon()
    {
        return itemData.getType() == ItemType.Weapon;
    }
	
	public void setType(String str)
	{
		itemData.setType(str);
	}
    
    public ObjectMap getMetaData()
    {
        return itemData.getMetaData();
    }
}

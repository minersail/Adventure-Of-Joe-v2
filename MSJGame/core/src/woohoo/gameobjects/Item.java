package woohoo.gameobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.XmlReader.Element;
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
    For items created without metadata
    */
    public Item(TextureRegion region)
	{		
		mapObject = new MapObjectComponent(region);
		sensor = new SensorComponent(WBodyType.Item);
        itemData = new ItemDataComponent();
		
		sensor.setStartPosition(4, 4);
		sensor.setContactData(new SensorContact(sensor, WBodyType.Player));
		
		super.add(mapObject);
        super.add(sensor);
        super.add(itemData);
	}
	
    /*
    For items created with metadata
    */
	public Item(TextureRegion region, Element data)
	{		
		mapObject = new MapObjectComponent(region);
		sensor = new SensorComponent(WBodyType.Item);
        itemData = new ItemDataComponent(data);
		
		sensor.setStartPosition(4, 4);
		sensor.setContactData(new SensorContact(sensor, WBodyType.Player));
		
		super.add(mapObject);
        super.add(sensor);
        super.add(itemData);
	}
	
	@Override
	public void update(float delta)
	{
		sensor.update(delta);
		mapObject.update(delta, sensor.getPosition());
	}
	
	public void setPosition(float x, float y)
	{
		sensor.setPosition(x, y);
	}
	
	public void flipImage()
	{
		TextureRegion texture = mapObject.getTextureRegion();
		texture.flip(false, true);
		mapObject.setTextureRegion(texture);
	}
    
    public boolean isWeapon()
    {
        return itemData.getType() == ItemType.Weapon;
    }
    
    public Element getMetaData()
    {
        return itemData.getMetaData();
    }
}

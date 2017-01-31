package woohoo.gameobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import woohoo.framework.contactcommands.SensorContact;
import woohoo.gameobjects.components.MapObjectComponent;
import woohoo.gameobjects.components.SensorComponent;
import woohoo.screens.PlayingScreen.WBodyType;

public class Item extends BaseEntity
{
	private SensorComponent sensor;
	private MapObjectComponent mapObject;
    private boolean isWeapon;
	
	public Item(TextureRegion region)
	{		
		mapObject = new MapObjectComponent(region);
		sensor = new SensorComponent(WBodyType.Item);
		
		sensor.setStartPosition(4, 4);
		sensor.setContactData(new SensorContact(sensor, WBodyType.Player));
		
		super.add(mapObject);
        super.add(sensor);
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
    
    public void setWeapon(boolean weapon)
    {
        isWeapon = weapon;
    }
    
    public boolean isWeapon()
    {
        return isWeapon;
    }
}

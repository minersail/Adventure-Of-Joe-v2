package woohoo.gameobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import woohoo.gameobjects.components.MapObjectComponent;
import woohoo.gameobjects.components.SensorComponent;
import woohoo.screens.PlayingScreen.WBodyType;

public class Item extends BaseEntity
{
	SensorComponent sensor;
	MapObjectComponent mapObject;
	
	public Item(TextureRegion region, World world)
	{		
		mapObject = new MapObjectComponent(region);
		sensor = new SensorComponent(world, WBodyType.Item);
		
		sensor.setPosition(4, 4);
		
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
}

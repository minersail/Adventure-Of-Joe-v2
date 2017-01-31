package woohoo.gameobjects.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import woohoo.gameobjects.Item;
import woohoo.screens.PlayingScreen.WBodyType;

/*
WeaponComponent has two SensorComponents: one by inheritance and one by composition

The item's SensorComponent should be disabled while the weapon's is active, and vice versa
*/
public class WeaponComponent extends SensorComponent
{    
    // Every weapon can also be picked up, put in inventory, etc.
    private Item weaponItem;
    
    public WeaponComponent(Item item) 
    {
        super(WBodyType.Weapon);
        
        weaponItem = item;
    }    
    
    @Override
	public void createMass(World world)
	{
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		mass = world.createBody(bodyDef);

		PolygonShape shape = new PolygonShape();	
		shape.setAsBox(0.5f, 0.125f, new Vector2(0, 0.375f), (float)Math.PI / 2);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;			
		fixtureDef.isSensor = true;
		fixtureDef.density = 0.0001f;
			
        fixture = mass.createFixture(fixtureDef);
		mass.setUserData(type);
	}
    
    public Item getItem()
    {
        return weaponItem;
    }
}

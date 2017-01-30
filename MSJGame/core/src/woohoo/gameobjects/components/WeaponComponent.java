package woohoo.gameobjects.components;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import woohoo.gameobjects.Item;
import woohoo.screens.PlayingScreen.WBodyType;

public class WeaponComponent extends SensorComponent
{    
    // Every weapon can also be picked up, put in inventory, etc.
    private Item weaponItem;
    private Body weaponBody;
    
    public WeaponComponent(Item item) 
    {
        super(WBodyType.Weapon);
        
        weaponItem = item;
    }    
    
    @Override
	public void createMass(World world)
	{
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;
		weaponBody = world.createBody(bodyDef);

		CircleShape shape = new CircleShape();		
		shape.setRadius(0.49f);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;			
		fixtureDef.isSensor = true;
			
        mass.createFixture(fixtureDef);
		mass.setUserData(type);
		
		super.createMass(world);
	}
    
    public Item getItem()
    {
        return weaponItem;
    }
    
    public Body getWeaponMass()
    {
        return weaponBody;
    }
}

package woohoo.gameobjects.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import woohoo.gameobjects.Item;
import woohoo.gameobjects.components.MapObjectComponent.Direction;
import woohoo.screens.PlayingScreen.WBodyType;

/*
WeaponComponent has two SensorComponents: one by inheritance and one by composition

The item's SensorComponent should be disabled while the weapon's is active, and vice versa
*/
public class WeaponComponent extends SensorComponent
{    
    // Every weapon can also be picked up, put in inventory, etc.
    private Item weaponItem;
	private float weaponAngle; // Radians
    
    public WeaponComponent(Item item) 
    {
        super(WBodyType.Weapon);
        
        weaponItem = item;
    }    
	
	@Override
	public void update(float delta)
	{
		float rotation = mass.getTransform().getRotation();
		if (rotation < 0) rotation += 2 * (float)Math.PI;
		
		// Between 95% and 105% of the desired angle
		float upperBound = 0.95f * (weaponAngle + 2 * (float)Math.PI / 3);
		float lowerBound = 1.05f * (weaponAngle + 2 * (float)Math.PI / 3);
		
		if (lowerBound > 2 * (float)Math.PI) lowerBound -= 2 * (float)Math.PI;
		if (upperBound > 2 * (float)Math.PI) upperBound -= 2 * (float)Math.PI;
		
		if (rotation >= upperBound && rotation <= lowerBound)
		{
			isActive = false;
			mass.setTransform(mass.getTransform().getPosition(), weaponAngle);
			mass.setAngularVelocity(0);
			mass.setFixedRotation(true);
		}
	}
    
    @Override
	public void createMass(World world)
	{
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		mass = world.createBody(bodyDef);

		PolygonShape shape = new PolygonShape();	
		shape.setAsBox(0.5f, 0.125f, new Vector2(0.375f, 0), 0);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;			
		fixtureDef.isSensor = true;
		fixtureDef.density = 0.01f;
			
        fixture = mass.createFixture(fixtureDef);
		mass.setUserData(type);
		fixture.setUserData(this);
	}
    
    public Item getItem()
    {
        return weaponItem;
    }
	
	public void swing()
	{
		isActive = true;
		mass.setFixedRotation(false);
		mass.setTransform(mass.getTransform().getPosition(), weaponAngle);
		mass.setAngularVelocity(0);
		mass.applyTorque(0.8f, true);
	}
	
	public void setAngle(Direction dir)
	{
		switch (dir)
		{
			case Left:
				weaponAngle = 2 * (float)Math.PI / 3;
				break;
			case Up:
				weaponAngle = 7 * (float)Math.PI / 6;
				break;
			case Right:
				weaponAngle = 5 * (float)Math.PI / 3;
				break;
			case Down:
				weaponAngle = (float)Math.PI / 6;
				break;
		}
	}
}

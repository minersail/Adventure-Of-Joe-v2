package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import woohoo.framework.contactcommands.ContactData;
import woohoo.framework.fixturedata.HitboxData;

public class HitboxComponent implements Component
{
	public enum ContactType
	{
		Player(0x0),
		Wall(0x1),
		Gate(0x1 << 1),
		Item(0x1 << 2),
		NPC(0x1 << 3),
		Weapon(0x1 << 4),
		Enemy(0x1 << 5);
		
		private int category;
		
		ContactType(int categoryBit)
		{
			category = categoryBit;
		}
		
		public int bits()
		{
			return category;
		}
		
		public int mask()
		{
			// DO THIS LATER
//			if (this.equals(Player))
//			{
//				return 
//			}
			return 0;
		}
	}
	
	public Body mass;
	public Fixture fixture;
	
	public boolean isActive;
	
	public HitboxComponent(World world, boolean enableCollision)
	{		
        BodyDef bodyDef = new BodyDef();
		mass = world.createBody(bodyDef);
        mass.setType(BodyDef.BodyType.DynamicBody);
		
		CircleShape shape = new CircleShape();		
		shape.setRadius(0.49f);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		
        fixture = mass.createFixture(fixtureDef);
		fixture.setSensor(enableCollision);
		fixture.setUserData(new HitboxData(this));
        fixture.setDensity(100f);
        fixture.setFriction(0);
        fixture.setRestitution(0);
		
		isActive = true;
	}
	
	public void applyImpulse(float x, float y)
	{
		mass.applyLinearImpulse(new Vector2(x, y), mass.getLocalCenter(), true);
	}
	
	public ContactData getContactData()
	{
		return (ContactData)mass.getUserData();
	}
}

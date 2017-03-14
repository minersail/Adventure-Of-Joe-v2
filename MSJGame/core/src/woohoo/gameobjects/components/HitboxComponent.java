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

public class HitboxComponent implements Component
{	
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

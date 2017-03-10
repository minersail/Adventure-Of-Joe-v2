package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import woohoo.framework.ContactManager;
import woohoo.framework.contactcommands.ContactCommand;
import woohoo.framework.fixturedata.FixtureData;
import woohoo.framework.fixturedata.HitboxData;

public class HitboxComponent implements Component
{
	public Body mass;
	public Fixture fixture;
	
	public boolean isActive;
	public Fixture collidedFixture;
	public ContactCommand contactCode;
	
	public HitboxComponent(World world)
	{		
        BodyDef bodyDef = new BodyDef();
		mass = world.createBody(bodyDef);
        mass.setType(BodyDef.BodyType.StaticBody);
		
		CircleShape shape = new CircleShape();		
		shape.setRadius(0.49f);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;		
		
        fixture = mass.createFixture(fixtureDef);
		fixture.setSensor(true);
		fixture.setUserData(new HitboxData(this));
		
		isActive = true;
	}
	
	public HitboxComponent initializeCommand(ContactManager contacts, World world)
	{
		contacts.addCommand(contactCode, world);
		return this;
	}
	
	public void setActive(boolean active)
	{
		isActive = active;
	}
	
	public boolean isActive()
	{
		return isActive;
	}
	
	public boolean hasContact()
	{
		return collidedFixture != null;
	}
	
	public FixtureData getContact()
	{
		return (FixtureData)collidedFixture.getUserData();
	}
}

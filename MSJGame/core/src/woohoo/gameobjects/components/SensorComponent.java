package woohoo.gameobjects.components;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import woohoo.framework.contactcommands.SensorContact;
import woohoo.screens.PlayingScreen.WBodyType;

public class SensorComponent extends BodyComponent
{
	private Fixture fixture;
	private boolean hasContact;
	private SensorContact contactCode;
	
	public SensorComponent(World world, WBodyType type)
	{
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;

		mass = world.createBody(bodyDef);

		CircleShape shape = new CircleShape();		
		shape.setRadius(0.49f);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.isSensor = true;

		fixture = mass.createFixture(fixtureDef);
		mass.setUserData(type);
		
		contactCode = new SensorContact(this);
	}
	
	public void update(float delta)
	{
	}
	
	public boolean hasContact()
	{
		return hasContact;
	}
	
	public void hasContact(boolean contact)
	{
		hasContact = contact;
	}
	
	public Fixture getFixture()
	{
		return fixture;
	}
	
	public SensorContact getContactCode()
	{
		return contactCode;
	}
}

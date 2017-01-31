package woohoo.framework.contactcommands;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import woohoo.gameobjects.components.SensorComponent;
import woohoo.screens.PlayingScreen.WBodyType;

public class SensorContact implements ContactCommand
{
	// The SensorComponent that this ContactCommand will be detecting contact for
	private SensorComponent sensor;
	// What kind of body this sensor is testing for
	private WBodyType testType;
	
	public SensorContact(SensorComponent sc, WBodyType type)
	{
		sensor = sc;
		testType = type;
	}
	
	@Override
	public void startContact(Contact contact)
	{
		Fixture fA = contact.getFixtureA();
		Fixture fB = contact.getFixtureB();

		if (fA.equals(sensor.getFixture()) && fB.getBody().getUserData() == testType
			|| fB.equals(sensor.getFixture()) && fA.getBody().getUserData() == testType)
		{
			sensor.hasContact(true);
		}
	}

	@Override
	public void endContact(Contact contact)
	{
		Fixture fA = contact.getFixtureA();
		Fixture fB = contact.getFixtureB();

		if (fA.equals(sensor.getFixture()) && fB.getBody().getUserData() == testType
			|| fB.equals(sensor.getFixture()) && fA.getBody().getUserData() == testType)
		{
			sensor.hasContact(false);
		}
	}
}

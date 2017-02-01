package woohoo.framework.contactcommands;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import woohoo.framework.fixturedata.FixtureData;
import woohoo.framework.fixturedata.SensorData;
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
        
		// If this sensor is inactive, or if neither of the fixtures are this one
		if (!sensor.isActive() || (!fA.equals(sensor.getFixture()) && !fB.equals(sensor.getFixture()))) return;
        
        FixtureData AData = (FixtureData)fA.getUserData();
        FixtureData BData = (FixtureData)fB.getUserData();
		
        // If one of the fixtures is part of an inactive sensor
		if (AData instanceof SensorData && !((SensorData)AData).isActive()) return;
		if (BData instanceof SensorData && !((SensorData)BData).isActive()) return;

		if (fA.equals(sensor.getFixture()) && fB.getBody().getUserData() == testType
			|| fB.equals(sensor.getFixture()) && fA.getBody().getUserData() == testType)
		{
			sensor.setCollided(fA.equals(sensor.getFixture()) ? fB : fA);
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
			sensor.setCollided(null);
		}
	}
}

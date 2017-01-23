package woohoo.framework.contactcommands;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import woohoo.gameobjects.components.SensorComponent;
import woohoo.screens.PlayingScreen;

public class SensorContact implements ContactCommand
{
	// The SensorComponent that this ContactCommand will be detecting contact for
	SensorComponent sensor;
	
	public SensorContact(SensorComponent sc)
	{
		sensor = sc;
	}
	
	@Override
	public void startContact(Contact contact)
	{
		Fixture fA = contact.getFixtureA();
		Fixture fB = contact.getFixtureB();

		if (fA.equals(sensor.getFixture()) && fB.getBody().getUserData() == PlayingScreen.WBodyType.Player
			|| fB.equals(sensor.getFixture()) && fA.getBody().getUserData() == PlayingScreen.WBodyType.Player)
		{
			sensor.hasContact(true);
		}
	}

	@Override
	public void endContact(Contact contact)
	{
		Fixture fA = contact.getFixtureA();
		Fixture fB = contact.getFixtureB();

		if (fA.equals(sensor.getFixture()) && fB.getBody().getUserData() == PlayingScreen.WBodyType.Player
			|| fB.equals(sensor.getFixture()) && fA.getBody().getUserData() == PlayingScreen.WBodyType.Player)
		{
			sensor.hasContact(false);
		}
	}
}

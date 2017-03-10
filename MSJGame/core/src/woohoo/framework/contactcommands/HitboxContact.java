package woohoo.framework.contactcommands;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import woohoo.framework.fixturedata.FixtureData;
import woohoo.gameobjects.components.HitboxComponent;
import woohoo.screens.PlayingScreen.WBodyType;

public class HitboxContact implements ContactCheck
{
	// The SensorComponent that this ContactCheck will be detecting contact for
	private HitboxComponent hitbox;
	// What kind of body this sensor is testing for
	private WBodyType testType;
	
	public HitboxContact(HitboxComponent hb, WBodyType type)
	{
		hitbox = hb;
		testType = type;
	}
	
	@Override
	public boolean detectStart(Contact contact)
	{
		Fixture fA = contact.getFixtureA();
		Fixture fB = contact.getFixtureB();
        
		// If this sensor is inactive, or if neither of the fixtures are this one
		if (!hitbox.isActive() || (!fA.equals(hitbox.getFixture()) && !fB.equals(hitbox.getFixture()))) return false;
        
        FixtureData AData = (FixtureData)fA.getUserData();
        FixtureData BData = (FixtureData)fB.getUserData();
		
        // If one of the fixtures is part of an inactive sensor
		if (AData instanceof SensorData && !((SensorData)AData).isActive()) return false;
		if (BData instanceof SensorData && !((SensorData)BData).isActive()) return false;

		if (fA.equals(hitbox.getFixture()) && fB.getBody().getUserData() == testType
			|| fB.equals(hitbox.getFixture()) && fA.getBody().getUserData() == testType)
		{
			hitbox.setCollided(fA.equals(hitbox.getFixture()) ? fB : fA);
			return true;
		}
	}

	@Override
	public boolean detectEnd(Contact contact)
	{		
		Fixture fA = contact.getFixtureA();
		Fixture fB = contact.getFixtureB();

		if (fA.equals(hitbox.getFixture()) && fB.getBody().getUserData() == testType
			|| fB.equals(hitbox.getFixture()) && fA.getBody().getUserData() == testType)
		{
			hitbox.setCollided(null);
			return true;
		}
		
		return false;
	}

	@Override
	public void activateStart() 
	{
		
	}

	@Override
	public void activateEnd() 
	{
		
	}
}

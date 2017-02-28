package woohoo.gameobjects.components;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import woohoo.framework.ContactManager;
import woohoo.framework.fixturedata.FixtureData;
import woohoo.framework.fixturedata.SensorData;
import woohoo.screens.PlayingScreen.WBodyType;

public class SensorComponent extends BodyComponent
{
	protected boolean isActive = true;
	private Fixture collidedFixture;
	
	public SensorComponent(WBodyType bodyType)
	{		
		type = bodyType;
	}
	
	@Override
	public void createMass(World world)
	{
		super.createMass(world);
        
        mass.setType(BodyDef.BodyType.StaticBody);
		mass.setUserData(type);
			
		fixture.setSensor(true);
		fixture.setUserData(new SensorData(this));
	}
    
    @Override
    public Shape getShape()
    {
		CircleShape shape = new CircleShape();		
		shape.setRadius(0.49f);
        
        return shape;
    }
	
	@Override	
	public void update(float delta)
	{
	}
	
	public SensorComponent initializeCommand(ContactManager contacts, World world)
	{
		contacts.addCommand(contactData, world);
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
	
	public void setCollided(Fixture collided)
	{
		collidedFixture = collided;
	}
	
	public Fixture getFixture()
	{
		return fixture;
	}
	
	public FixtureData getContact()
	{
		return (FixtureData)collidedFixture.getUserData();
	}
}

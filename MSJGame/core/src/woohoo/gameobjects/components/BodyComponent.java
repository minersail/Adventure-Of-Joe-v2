package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import woohoo.framework.contactcommands.ContactCommand;
import woohoo.screens.PlayingScreen.WBodyType;

/*
Base component class for box2d-based components

Notes on user data:
Body user data is always WBodyType

Fixture user data can be:
	- Gate: GateData
	- BodyComponent: BodyComponent (or derived)
	- Maybe write some base class FixtureData
*/
public class BodyComponent implements Component
{
	protected Body mass;
	protected ContactCommand contactData;
	protected WBodyType type;
	
	private Vector2 startPosition = new Vector2(0, 0);
	
	/*
	Creates mass, overriden by subclasses
	*/
	public void createMass(World world)
	{
		mass.setTransform(startPosition, 0);
	}
	
	public void update(float delta)
	{
	}		
	
	public void removeMass()
	{
		mass.getWorld().destroyBody(mass);
	}
    
    public Body getMass()
    {
        return mass;
    }
	
	public void setStartPosition(float x, float y)
	{
		startPosition = new Vector2(x + 0.5f, y + 0.5f);
	}
	
	// Offsets because box2D has origins at center as opposed to top-left
	public void setPosition(float x, float y)
	{
		mass.setTransform(new Vector2(x + 0.5f, y + 0.5f), 0);
	}
	
	public Vector2 getPosition()
	{
		return new Vector2(mass.getPosition().x - 0.5f, mass.getPosition().y - 0.5f);
	}
    
    public void setContactData(ContactCommand command)
    {
        contactData = command;
    }
	
	public ContactCommand getContactData()
	{
		return contactData;
	}
}

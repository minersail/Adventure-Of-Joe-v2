package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import woohoo.framework.contactcommands.ContactCommand;
import woohoo.framework.fixturedata.HitData;
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
public abstract class BodyComponent implements Component
{
	protected Body mass;
	protected Fixture fixture;
	protected ContactCommand contactData;
	protected WBodyType type;
	
	/*
	Creates mass, overriden by subclasses
	*/
	public void createMass(World world)
    {
        BodyDef bodyDef = new BodyDef();
		mass = world.createBody(bodyDef);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = getShape();		
			
        fixture = mass.createFixture(fixtureDef);
    }
	
    public abstract Shape getShape();
	public abstract void update(float delta);
	
	public void removeMass()
	{
		mass.getWorld().destroyBody(mass);
	}
    
    public Body getMass()
    {
        return mass;
    }
	
	public void setImmovable(boolean move)
	{
		mass.setType(move ? BodyType.StaticBody : BodyType.DynamicBody);
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

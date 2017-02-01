package woohoo.gameobjects.components;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import woohoo.screens.PlayingScreen.WBodyType;

public class SensorComponent extends BodyComponent
{
	protected boolean isActive = true;
	protected Fixture fixture;
	private Fixture collidedFixture;
	
	public SensorComponent(WBodyType bodyType)
	{		
		type = bodyType;
	}
	
	@Override
	public void createMass(World world)
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
		fixture.setUserData(this);
		mass.setUserData(type);
		
		super.createMass(world);
	}
	
	@Override	
	public void update(float delta)
	{
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
}

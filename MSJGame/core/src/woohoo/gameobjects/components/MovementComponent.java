package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import woohoo.framework.fixturedata.FixtureData;

public class MovementComponent implements Component
{	
	// Only really used for player movement
	public enum Movement
	{
		None, Horizontal, Vertical
	}
	
	private Body mass;
	private Vector2 velocity = new Vector2(0, 0);
	private Movement movement;
	
	public MovementComponent(World world)
	{
		movement = Movement.None;
		
        BodyDef bodyDef = new BodyDef();
		mass = world.createBody(bodyDef);
        mass.setType(BodyDef.BodyType.DynamicBody);
		mass.setFixedRotation(true);
		mass.setLinearDamping(0.4f);	
		
		CircleShape shape = new CircleShape();		
		shape.setRadius(0.49f);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;		
			
        Fixture fixture = mass.createFixture(fixtureDef);      
		fixture.setUserData(new FixtureData());
        fixture.setDensity(100f);
        fixture.setFriction(0);
        fixture.setRestitution(0);	
	}
	
	public void addVelocity(float x, float y)
	{
		velocity.x += x;
		velocity.y += y;
	}
	
	public void setVelocity(float x, float y)
	{
		velocity.x = x;
		velocity.y = y;
	}
	
	public void move()
	{		
		switch (movement)
		{
			case Vertical:
				mass.setLinearVelocity(new Vector2(0, velocity.y));				
				break;
			case Horizontal:
				mass.setLinearVelocity(new Vector2(velocity.x, 0));				
				break;
			case None:
				mass.setLinearVelocity(velocity);
				break;
		}
	}
	
	public Vector2 getVelocity()
	{
		return velocity;
	}
	
	public void applyImpulse(float x, float y)
	{
		mass.applyLinearImpulse(new Vector2(x, y), mass.getLocalCenter(), true);
	}
	
	public boolean isStopped()
	{
		return Math.abs(mass.getLinearVelocity().x) < 0.5f && Math.abs(mass.getLinearVelocity().y) < 0.5f;
	}
	
	public void setMovement(Movement move)
	{
		movement = move;
	}
	
	public Movement getMovement()
	{
		return movement;
	}

	public Vector2 getPosition() 
	{
		return mass.getPosition();
	}
	
	public Body getMass()
	{
		return mass;
	}
}

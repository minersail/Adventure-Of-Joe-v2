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
	
	public Body mass;
	public Vector2 velocity;
	public Movement movement;
	public float speed;
	
	public MovementComponent(World world)
	{
		movement = Movement.None;
		velocity = new Vector2(0, 0);
		
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
	
	public void applyImpulse(float x, float y)
	{
		mass.applyLinearImpulse(new Vector2(x, y), mass.getLocalCenter(), true);
	}
	
	public boolean isStopped(float tolerance)
	{
		return mass.getLinearVelocity().isZero(tolerance);
	}
}

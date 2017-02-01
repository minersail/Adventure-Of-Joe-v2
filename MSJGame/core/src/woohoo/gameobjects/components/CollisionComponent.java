package woohoo.gameobjects.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import woohoo.framework.fixturedata.FixtureData;
import woohoo.screens.PlayingScreen.WBodyType;

public class CollisionComponent extends BodyComponent
{
	Vector2 velocity = new Vector2(0, 0);
	
	public CollisionComponent(WBodyType bodyType)
	{
		type = bodyType;
	}
	
	@Override
	public void createMass(World world)
	{
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		mass = world.createBody(bodyDef);

		CircleShape shape = new CircleShape();		
		shape.setRadius(0.49f);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 100f;
		fixtureDef.friction = 0f;
		fixtureDef.restitution = 0f;
		
		Fixture fixture = mass.createFixture(fixtureDef);
		mass.setUserData(type);
		mass.setFixedRotation(true);
		mass.setLinearDamping(0.4f);		
		fixture.setUserData(new FixtureData());
		
		super.createMass(world);
	}
	
	@Override
	public void update(float delta)
	{
		mass.setLinearVelocity(velocity);
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
	
	public Vector2 getVelocity()
	{
		return mass.getLinearVelocity();
	}
	
	public void addImpulse(float x, float y)
	{
		mass.applyLinearImpulse(new Vector2(x, y), mass.getLocalCenter(), true);
	}
	
	public boolean isStopped()
	{
		return Math.abs(mass.getLinearVelocity().x) < 0.5f && Math.abs(mass.getLinearVelocity().y) < 0.5f;
	}
}

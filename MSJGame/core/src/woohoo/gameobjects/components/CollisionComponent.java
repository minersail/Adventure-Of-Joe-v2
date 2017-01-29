package woohoo.gameobjects.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import woohoo.screens.PlayingScreen.WBodyType;

public class CollisionComponent extends BodyComponent
{
	Vector2 force = new Vector2(0, 0);
	
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
		fixtureDef.density = 1f;
		fixtureDef.friction = 0f;
		fixtureDef.restitution = 0f;
		
		mass.createFixture(fixtureDef);
		mass.setUserData(type);
		mass.setFixedRotation(true);
		mass.setLinearDamping(10f);
		
		super.createMass(world);
	}
	
	@Override
	public void update(float delta)
	{
		mass.applyForceToCenter(force.x * delta, force.y * delta, true);
	}
	
	public void addForce(float x, float y)
	{
		force.x += x;
		force.y += y;
	}
	
	public void setForce(float x, float y)
	{
		force.x = x;
		force.y = y;
	}
	
	public Vector2 getVelocity()
	{
		return mass.getLinearVelocity();
	}
	
	public boolean isStopped()
	{
		return Math.abs(mass.getLinearVelocity().x) < 1 && Math.abs(mass.getLinearVelocity().y) < 1;
	}
}

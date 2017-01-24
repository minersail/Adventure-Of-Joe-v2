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
	
	public CollisionComponent(World world, WBodyType type)
	{
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;

		mass = world.createBody(bodyDef);

		CircleShape shape = new CircleShape();		
		shape.setRadius(0.49f);
		
//		PolygonShape shape = new PolygonShape();
//		Vector2[] vertices = {
//			new Vector2(0, 0.05f), new Vector2(0.05f, 0),
//			new Vector2(0.95f, 0), new Vector2(1, 0.05f),
//			new Vector2(1, 0.95f), new Vector2(0.95f, 1),
//			new Vector2(0.05f, 1), new Vector2(0, 0.95f),
//		};
		//shape.set(vertices);
		//shape.setAsBox(0.49f, 0.49f);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1f;
		fixtureDef.friction = 0f;
		fixtureDef.restitution = 0f;

		mass.createFixture(fixtureDef);
		mass.setFixedRotation(true);
		mass.setLinearDamping(10f);
		mass.setUserData(type);
	}
	
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

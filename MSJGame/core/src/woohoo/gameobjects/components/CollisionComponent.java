package woohoo.gameobjects.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import woohoo.gameobjects.Player;

public class CollisionComponent
{
	Body mass;
	
	public CollisionComponent(Player player, World world)
	{
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(player.getPosition());

		mass = world.createBody(bodyDef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.5f, 0.5f);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1f;
		fixtureDef.friction = 0f;

		mass.createFixture(fixtureDef);
		mass.setFixedRotation(true);
	}
	
	public void addVelocity(float x, float y)
	{
		Vector2 v = mass.getLinearVelocity();
		mass.setLinearVelocity(v.x + x, v.y + y);
	}
	
	public void setVelocity(float x, float y)
	{
		mass.setLinearVelocity(new Vector2(x, y));
	}
	
	public Vector2 getVelocity()
	{
		return mass.getLinearVelocity();
	}
	
	public void setPosition(Vector2 newPosition)
	{
		mass.setTransform(newPosition, 0);
	}
	
	public Vector2 getPosition()
	{
		return mass.getPosition();
	}
}

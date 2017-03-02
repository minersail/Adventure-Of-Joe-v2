package woohoo.gameobjects.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import woohoo.framework.fixturedata.FixtureData;
import woohoo.screens.PlayingScreen.WBodyType;

public class CollisionComponent extends BodyComponent
{
	// Only really used for player movement
	public enum Movement
	{
		None, Horizontal, Vertical
	}
	
	private Vector2 velocity = new Vector2(0, 0);
	private Movement movement;
	
	public CollisionComponent(WBodyType bodyType)
	{
		type = bodyType;
		movement = Movement.None;
	}
	
	@Override
	public void createMass(World world)
	{
		super.createMass(world);
        
        mass.setType(BodyDef.BodyType.DynamicBody);
		
		mass.setUserData(type);
		mass.setFixedRotation(true);
		mass.setLinearDamping(0.4f);		
        
		fixture.setUserData(new FixtureData());
        fixture.setDensity(100f);
        fixture.setFriction(0);
        fixture.setRestitution(0);		
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
}

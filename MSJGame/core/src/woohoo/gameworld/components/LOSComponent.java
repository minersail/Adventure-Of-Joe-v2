package woohoo.gameworld.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import woohoo.gameworld.components.PositionComponent.Orientation;

public class LOSComponent implements Component
{
	public Body mass;
	public Fixture fixture;
	
	public boolean entitySeen; // Right now this would only be the player; CHANGE LATER TO ARRAY
	public Entity sightedEntity;
	
    public LOSComponent(World world) 
    {
		BodyDef bodyDef = new BodyDef();
		mass = world.createBody(bodyDef);
        mass.setType(BodyDef.BodyType.DynamicBody);
		
		PolygonShape shape = new PolygonShape();
		float LOSradius = 5;
		
		float SIN60 = (float)Math.sin(Math.PI / 3);
		float COS60 = (float)Math.cos(Math.PI / 3);
		float SIN70 = (float)Math.sin(1.22);
		float COS70 = (float)Math.cos(1.22);
		float SIN80 = (float)Math.sin(1.4);
		float COS80 = (float)Math.cos(1.4);
		
		// Cone shape
		Vector2[] vertices = {new Vector2(-0.1f, 0), new Vector2(LOSradius * SIN60, LOSradius * COS60), new Vector2(LOSradius * SIN70, LOSradius * COS70), 
							  new Vector2(LOSradius * SIN80, LOSradius * COS80), new Vector2(LOSradius * SIN60, LOSradius * -COS60),
							  new Vector2(LOSradius * SIN70, LOSradius * -COS70), new Vector2(LOSradius * SIN80, LOSradius * -COS80)};
		
		shape.set(vertices);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		
        fixture = mass.createFixture(fixtureDef);
		fixture.setSensor(true);
        fixture.setDensity(0.01f);
        fixture.setFriction(0);
        fixture.setRestitution(0);
    }
	
	// Maybe deprecate later in favor of Orientation.getAngle()
	public void rotate(Orientation direction)
	{
		switch(direction)
		{
			case North:
				mass.setTransform(mass.getPosition(), 3 * (float)Math.PI / 2);
				break;
			case South:
				mass.setTransform(mass.getPosition(), (float)Math.PI / 2);
				break;
			case West:
				mass.setTransform(mass.getPosition(), (float)Math.PI);
				break;
			case East:
				mass.setTransform(mass.getPosition(), 0);
				break;
		}
	}
}

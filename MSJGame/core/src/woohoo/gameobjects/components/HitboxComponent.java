package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import woohoo.framework.contactcommands.ContactData;
import woohoo.gameobjects.components.ContactComponent.ContactType;

public class HitboxComponent implements Component
{	
	public Body mass;
	public Fixture fixture;	
	public ContactType hitboxType;
	public float rotation; // Radians
	
	/**
	 * Circular hitbox collision mesh
	 * @param world reference to global world
	 * @param enableCollision Whether this hitbox will have collision
	 * @param startsStatic Whether or not this hitbox will start immovable (with no AI this setting is permanent)
	 * @param type ContactType for this hitbox (extracted later, automatically to add ContactData to this hitbox's mass)
	 * @param shape Shape of the hitbox
	 */
	public HitboxComponent(World world, boolean enableCollision, boolean startsStatic, ContactType type, String shape)
	{		
		hitboxType = type;
		
        BodyDef bodyDef = new BodyDef();
		mass = world.createBody(bodyDef);
        mass.setType(startsStatic ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = makeShape(shape);
		
        fixture = mass.createFixture(fixtureDef);
		fixture.setSensor(!enableCollision);
        fixture.setDensity(100f);
	}
	
	public ContactData getContactData()
	{
		return (ContactData)mass.getUserData();
	}
	
	private Shape makeShape(String str)
	{
		switch(str)
		{			
			case "melee":
			{
				PolygonShape shape = new PolygonShape(); // arrowhead shape
				float x = 0.2f; // width of the arrowhead
				float y = x / (float)Math.sqrt(2);
				Vector2[] vertices =
				{
					new Vector2(0, 0), new Vector2(0.5f, -0.5f), new Vector2(0.5f - y, -0.5f - y),
					new Vector2(- 2 * y, 0), new Vector2(0.5f - y, 0.5f + y), new Vector2(0.5f, 0.5f)
				};
				shape.set(vertices);
				return shape;
			}
			case "circle":
			default:
			{
				CircleShape shape = new CircleShape();
				shape.setRadius(0.49f);
				return shape;
			}
		}
	}
}

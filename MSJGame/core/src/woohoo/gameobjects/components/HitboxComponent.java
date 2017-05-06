package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import woohoo.framework.contactcommands.ContactData;
import woohoo.gameobjects.components.ContactComponent.ContactType;

public class HitboxComponent implements Component
{	
	public Body mass;
	public Fixture fixture;	
	public ContactType hitboxType;
	
	/**
	 * Circular hitbox collision mesh
	 * @param world reference to global world
	 * @param enableCollision Whether this hitbox will have collision
	 * @param startsStatic Whether or not this hitbox will start immovable (with no AI this setting is permanent)
	 * @param type ContactType for this hitbox (extracted later, automatically to add ContactData to this hitbox's mass)
	 */
	public HitboxComponent(World world, boolean enableCollision, boolean startsStatic, ContactType type)
	{		
		hitboxType = type;
		
        BodyDef bodyDef = new BodyDef();
		mass = world.createBody(bodyDef);
        mass.setType(startsStatic ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody);
		
		CircleShape shape = new CircleShape();		
		shape.setRadius(0.49f);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		
        fixture = mass.createFixture(fixtureDef);
		fixture.setSensor(!enableCollision);
        fixture.setDensity(100f);
	}
	
	public ContactData getContactData()
	{
		return (ContactData)mass.getUserData();
	}
}

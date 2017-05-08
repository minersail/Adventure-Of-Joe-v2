package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import woohoo.framework.contactcommands.ContactData;
import woohoo.framework.loading.HitboxMold;
import woohoo.gameobjects.components.ContactComponent.ContactType;
import woohoo.gameobjects.components.ContactComponent.Faction;

public class HitboxComponent implements Component
{	
	public Body mass;
	public Fixture fixture;	
	public ContactType hitboxType;
	public Faction faction;
	public float rotation; // Radians
	
	/**
	 * Circular hitbox collision mesh
	 * @param world reference to global world
	 * @param mold data container for hitbox initialization
	 */
	public HitboxComponent(World world, HitboxMold mold)
	{		
		hitboxType = mold.getType();
		faction = mold.getFaction();
		
        BodyDef bodyDef = new BodyDef();
		mass = world.createBody(bodyDef);
        mass.setType(mold.getStatic() ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = mold.getShape();
		
        fixture = mass.createFixture(fixtureDef);
		fixture.setSensor(!mold.getColliding());
        fixture.setDensity(100f);
	}
	
	public ContactData getContactData()
	{
		return (ContactData)mass.getUserData();
	}
}

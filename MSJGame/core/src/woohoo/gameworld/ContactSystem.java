package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import java.util.Iterator;
import woohoo.framework.contactcommands.ContactData;
import woohoo.gameobjects.components.ContactComponent;
import woohoo.gameobjects.components.HitboxComponent;
import woohoo.gameobjects.components.MovementComponent;
import woohoo.gameobjects.components.PositionComponent;

public class ContactSystem extends IteratingSystem
{
	public ContactSystem(World world)
	{
		super(Family.all(MovementComponent.class, PositionComponent.class, HitboxComponent.class, ContactComponent.class).get());
		
		world.setContactListener(new ContactListener() 
		{
			@Override
			public void beginContact(Contact contact)
			{
				ContactData data1 = (ContactData)contact.getFixtureA().getBody().getUserData();
				ContactData data2 = (ContactData)contact.getFixtureB().getBody().getUserData();
				
				data1.collisions.add(data2);
				data2.collisions.add(data1);
			}

			@Override
			public void endContact(Contact contact)
			{
				ContactData data1 = (ContactData)contact.getFixtureA().getBody().getUserData();
				ContactData data2 = (ContactData)contact.getFixtureB().getBody().getUserData();
				
				data1.collisions.removeValue(data2, true);
				data2.collisions.removeValue(data1, true);
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {}
		});
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) 
	{
		HitboxComponent hitbox = Mappers.hitboxes.get(entity);
		
		if (hitbox.getContactData().hasCollisions())
		{
			for (Iterator<ContactData> it = hitbox.getContactData().collisions.iterator(); it.hasNext();)
			{
				ContactData data = it.next();
				
				data.collisions.removeValue(hitbox.getContactData(), true); // Remove the corresponding collision on the other object
				it.remove();
			}
		}
	}
	
	private void processContact(Entity entity1, Entity entity2)
	{
		
	}
}

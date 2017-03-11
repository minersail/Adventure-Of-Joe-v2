package woohoo.framework.contactcommands;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import woohoo.gameobjects.components.HitboxComponent;

public class ContactData
{
	public HitboxComponent.ContactType type;
	public Entity owner;

	// Array of collisions
	public Array<ContactData> collisions;

	public ContactData()
	{
		collisions = null;
	}

	public boolean hasCollisions()
	{
		return collisions.size == 0;
	}
}

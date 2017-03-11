package woohoo.framework.contactcommands;

import woohoo.gameobjects.components.HitboxComponent;
import woohoo.gameobjects.components.ItemDataComponent;
import woohoo.gameworld.Mappers;

public class ItemContact implements ContactCommand
{
	public ItemContact()
	{
	}

	@Override
	public void activate(ContactData contactA, ContactData contactB)
	{
		if (contactA.type == HitboxComponent.ContactType.Item && contactB.type == HitboxComponent.ContactType.Player)
		{
			ItemDataComponent item = Mappers.items.get(contactA.owner);			
			item.playerTouched = true;
		}
		else if (contactB.type == HitboxComponent.ContactType.Item && contactA.type == HitboxComponent.ContactType.Player)
		{
			ItemDataComponent item = Mappers.items.get(contactB.owner);			
			item.playerTouched = true;
		}
	}
}

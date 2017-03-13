package woohoo.framework.contactcommands;

import woohoo.gameobjects.components.ContactComponent.ContactType;
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
		if (contactA.type == ContactType.Item && contactB.type == ContactType.Player)
		{
			ItemDataComponent item = Mappers.items.get(contactA.owner);			
			item.playerTouched = true;
		}
		else if (contactB.type == ContactType.Item && contactA.type == ContactType.Player)
		{
			ItemDataComponent item = Mappers.items.get(contactB.owner);			
			item.playerTouched = true;
		}
	}
}

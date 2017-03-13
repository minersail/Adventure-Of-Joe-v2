package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;
import woohoo.framework.contactcommands.ContactCommand;

/**
 * Contains contact data - 
 * @author jordan
 */
public class ContactComponent implements Component
{
	public enum ContactType
	{
		Player(0x0),
		Wall(0x1),
		Gate(0x1 << 1),
		Item(0x1 << 2),
		NPC(0x1 << 3),
		Weapon(0x1 << 4),
		Enemy(0x1 << 5);
		
		private int category;
		
		ContactType(int categoryBit)
		{
			category = categoryBit;
		}
		
		public int bits()
		{
			return category;
		}
		
		public int mask()
		{
			// DO THIS LATER
//			if (this.equals(Player))
//			{
//				return 
//			}
			return 0;
		}
	}
	
	Array<ContactCommand> commands;
}

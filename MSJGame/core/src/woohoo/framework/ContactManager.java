package woohoo.framework;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;
import woohoo.framework.contactcommands.ContactCommand;

public class ContactManager
{
	private ArrayList<ContactCommand> commands;
	
	public ContactManager()
	{
		commands = new ArrayList<>();
	}
	
	public void addCommand(ContactCommand command, World world)
	{
		commands.add(command);
		updateContactListener(world);
	}
	
	private void updateContactListener(World world)
	{
		world.setContactListener(
            new ContactListener() 
            {
                @Override
                public void beginContact(Contact contact)
				{
                    for (ContactCommand command : commands)
					{
						command.startContact(contact);
					}
                }

                @Override
                public void endContact(Contact contact)
				{
                    for (ContactCommand command : commands)
					{
						command.endContact(contact);
					}			
				}

                @Override
                public void preSolve(Contact contact, Manifold oldManifold) {}

                @Override
                public void postSolve(Contact contact, ContactImpulse impulse) {}
            }
        );
	}
}

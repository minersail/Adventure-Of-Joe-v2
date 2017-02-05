package woohoo.framework.contactcommands;

import com.badlogic.gdx.physics.box2d.Contact;

/**
 * Since only one ContactListener can be active at the same time,
 * all the code for contacts has to be placed in the same ContactListener
 * 
 * This is done using the OOP command pattern.
 * Since functions cannot be passed as parameters in Java, the command pattern
 * allows functions to be stored in interfaces and overridden in implementations.
 * 
 * @author jordan
 */
public interface ContactCommand
{
	public void startContact(Contact contact);
	public void endContact(Contact contact);
}

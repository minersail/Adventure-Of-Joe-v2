package woohoo.framework.contactcommands;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * Since only one ContactListener can be active at the same time,
 * all the code for contacts has to be placed in the same ContactListener
 * 
 * This is done using the OOP command pattern, which allows functions to 
 * be stored in interfaces and overridden in implementations.
 * 
 * @author jordan
 */
public interface ContactCommand
{
	public boolean detectStart(Contact contact);
	public void activateStart();
	public boolean detectEnd(Contact contact);
	public void activateEnd();
}



// NOTES
// 1. ContactCommand created, one per component
// 2. FixtureData created, one per fixture, and attached to Fixture through UserData
// 3. ContactCommand passed to ContactManager to DETECT contact
// 4. Contact is detected between two fixures, and if these two fixtures are relevant, store their data
// 5. ContactCommand will store data inside the FixtureData for the desired fixture, such as hasContact and the other Fixture
// Figure out
// 1-1 relationship?
// FixureData put into ContactCommand
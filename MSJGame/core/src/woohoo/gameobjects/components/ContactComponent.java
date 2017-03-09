package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.World;
import woohoo.framework.contactcommands.ContactCommand;

public interface ContactComponent extends Component
{
	public ContactCommand getContactData();
    public void setContactData(ContactCommand command);
	public void createMass(World world);
}

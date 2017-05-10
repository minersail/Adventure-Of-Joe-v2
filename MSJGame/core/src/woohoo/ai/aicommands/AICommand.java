package woohoo.ai.aicommands;

import com.badlogic.ashley.core.Entity;

/**
 * Class representing a simple AI action e.g. moveTo, attack, etc.
 * @author jordan
 */
public interface AICommand
{
	/**
	 * Will be run every tick as long as it returns false (or if externally exited)
	 * @param entity the entity that is this command is controlling
	 * @return true if the command is finished
	 */
	public boolean run(Entity entity); // Maybe change later so that returns an enumeration detailing Success/Failure/Waiting in order to be more verbose
	public void enter(Entity entity);
	public void exit(Entity entity);
}

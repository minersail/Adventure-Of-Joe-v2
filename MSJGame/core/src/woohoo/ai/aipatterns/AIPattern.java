package woohoo.ai.aipatterns;

import com.badlogic.ashley.core.Entity;
import woohoo.ai.aicommands.AICommand;
import woohoo.gameworld.AIStateSystem;

/**
 * This is a pseudo-FSM system, where: <br>
 * AICommands are analogous to a state, and <br>
 * AIPatterns are analogous to the machine. <br>
 * <br>
 * Actual modifications should happen in the states.
 * Patterns are for handling the business logic of switching between states.
 * 
 * Generally, reading from the entity happens in the pattern, and 
 * writing to the entity happens in the command.
 * @author jordan
 */
public abstract class AIPattern 
{
	private AICommand currentCommand;
	
	// Called every tick
	public final void runPattern(Entity entity, float deltaTime)
	{
		if (currentCommand == null)
		{
			initialize(entity);
		}
		
		run(entity, deltaTime);
	}
	
	// Run should only be called by runPattern and overridden by subclasses
	protected abstract void run(Entity entity, float deltaTime);
	
	/** 
	 * Called after constructor on first tick of run.
	 * Benefits of lazy initialization: <br>
	 *	- does not require entity to be injected into constructor <br>
	 *  - Certain things (such as pathfinding grid) may not yet be configured at time of constructor
	 * 
	 * @param entity
	 */
	protected abstract void initialize(Entity entity);
	
	/**
	 * Called after all entities have been loaded but before first game tick
	 * @param system System will be injected
	 */
	public void link(AIStateSystem system) {}
	
	public AICommand getCommand()
	{
		return currentCommand;
	}
	
	public void setCommand(AICommand newCommand, Entity entity)
	{
		if (currentCommand != null)
			currentCommand.exit(entity);
		
		currentCommand = newCommand;
		currentCommand.enter(entity);
	}
}

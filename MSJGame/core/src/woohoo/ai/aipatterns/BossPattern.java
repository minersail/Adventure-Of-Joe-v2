package woohoo.ai.aipatterns;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import woohoo.ai.Node;
import woohoo.ai.aicommands.AIAttackCommand;
import woohoo.ai.aicommands.AIMoveCommand;
import woohoo.ai.aicommands.AISwitchWeaponCommand;
import woohoo.gameobjects.components.AIComponent;
import woohoo.gameobjects.components.PositionComponent.Orientation;
import woohoo.gameworld.Mappers;

public class BossPattern extends AIPattern
{
	private final int WEAPON_ARROW = 8;
	private final int WEAPON_FIREBALL = 9;
	
	private float timer = 0;
	private Vector2 destination;
	private boolean fireballMode;
	
	@Override
	protected void initialize(Entity entity)
	{
		destination = generateRandomLoc(Mappers.ai.get(entity));
		super.setCommand(new AIMoveCommand(destination), entity);
	}
	
	@Override
	protected void run(Entity entity, float deltaTime)
	{
		// Recode later...bleugh
		if (getCommand().run(entity))
		{
			if (getCommand() instanceof AIMoveCommand)
			{
				destination = generateRandomLoc(Mappers.ai.get(entity));
				super.setCommand(new AISwitchWeaponCommand(WEAPON_FIREBALL), entity);
				fireballMode = true;
				timer = 0;
				return;
			}
			else if ((getCommand() instanceof AISwitchWeaponCommand || getCommand() instanceof AIAttackCommand) && fireballMode)
			{
				if (timer < 1)
				{
					super.setCommand(new AIAttackCommand(Orientation.getRandom()), entity);					
					timer += deltaTime;
				}
				else
				{
					super.setCommand(new AISwitchWeaponCommand(WEAPON_ARROW), entity);
					fireballMode = false;
					timer = 0;
				}
				return;
			}			
			super.setCommand(new AIMoveCommand(destination), entity);
		}
				
		timer += deltaTime;
		
		if (timer > 0.5)
		{
			super.setCommand(new AIAttackCommand(Orientation.getRandom()), entity);
			timer = 0;
		}
	}
	
	private Vector2 generateRandomLoc(AIComponent ai)
	{
		Node node = ai.getAIMap().getRandomNode();
		
		return new Vector2(node.x, node.y);
	}
}

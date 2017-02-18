package woohoo.framework;

import java.util.ArrayList;
import woohoo.gameobjects.BaseEntity;
import woohoo.gameobjects.Character;
import woohoo.gameobjects.Enemy;
import woohoo.gameobjects.components.AIComponent;
import woohoo.gameobjects.components.CollisionComponent;
import woohoo.screens.PlayingScreen;

public class AIManager
{
	PlayingScreen screen;
	
	public AIManager(PlayingScreen scr)
	{
		screen = scr;
	}
	
	public void initializePathfinding()
	{
		ArrayList<BaseEntity> entities = screen.getEngine().getDuplicateList();
		
		for (BaseEntity entity : entities)
		{
			if (entity instanceof Character)
			{
				entity.getComponent(AIComponent.class).initializePathfinding(screen.getRenderer().getMap(), screen.getWorld(), 
																			 entity.getComponent(CollisionComponent.class));
				
				if (entity instanceof Enemy)
				{
					entity.getComponent(AIComponent.class).setTargetCharacter(entity.getComponent(CollisionComponent.class).getPosition(), 
																			  screen.getEngine().getPlayer());
				}
			}
		}
	}
}
